package com.sdg.cmdb.scheduler.task;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.keybox.WSResult;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.util.schedule.BaseJob;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import com.sdg.cmdb.zabbix.ZabbixHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zxxiao on 2017/4/12.
 */
@Service
public class ServerGroupSubTask implements BaseJob, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ServerGroupSubTask.class);

    private static final String taskCorn = "0/15 * * * * ?";

    /**
     * 订阅此服务器组的前端session集合
     */
    private Map<Long, List<WebSocketSession>> serverGroupSesionMap = new ConcurrentHashMap<>();

    private Map<Long, ServerGroupDO> serverGroupDOMap = new ConcurrentHashMap<>();

    /**
     * 单一session订阅的服务器组集合
     */
    private Map<WebSocketSession, List<Long>> sessionGroupIdMap = new ConcurrentHashMap<>();

    private ReentrantLock lock = new ReentrantLock();

    @Resource
    private SchedulerManager schedulerManager;

    @Resource
    private ZabbixHistoryService zabbixHistoryService;

    @Override
    public void execute() {
        for(ServerGroupDO serverGroupDO : serverGroupDOMap.values()) {
            List<Map<String, Object>> serverList = zabbixHistoryService.queryHistory(serverGroupDO);
            WSResult wsResult = new WSResult(serverList);

            for(WebSocketSession session : serverGroupSesionMap.get(serverGroupDO.getId())) {
                try {
                    doWriteToSession(session, wsResult);
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 注册session&服务器组的订阅关系
     * @param serverGroupDO
     * @param session
     */
    public void register(ServerGroupDO serverGroupDO, WebSocketSession session) {
        try {
            lock.lock();
            serverGroupDOMap.put(serverGroupDO.getId(), serverGroupDO);

            List<WebSocketSession> sessionList = serverGroupSesionMap.get(serverGroupDO.getId());

            if (sessionList == null) {
                sessionList = new ArrayList<>();
                serverGroupSesionMap.put(serverGroupDO.getId(), sessionList);
            }
            sessionList.add(session);
        } finally {
            lock.unlock();
        }

        List<Long> serverGroupIdList = sessionGroupIdMap.get(session);
        if (serverGroupIdList == null) {
            serverGroupIdList = new ArrayList<>();
            sessionGroupIdMap.put(session, serverGroupIdList);
        }
        serverGroupIdList.add(serverGroupDO.getId());
    }

    /**
     * 注销session&所有服务器组的订阅关系
     * @param session
     */
    public void unregisterSession(WebSocketSession session) {
        List<Long> serverGroupIdList = sessionGroupIdMap.get(session);
        if (serverGroupIdList == null || serverGroupIdList.isEmpty()) {
            return;
        } else {
            for(long serverGroupId : serverGroupIdList) {
                List<WebSocketSession> sessionList = serverGroupSesionMap.get(serverGroupId);
                if (sessionList != null) {
                    sessionList.remove(session);
                }
                try {
                    lock.lock();
                    //不存在订阅关系了，则不再获取数据
                    if (sessionList.isEmpty()) {
                        serverGroupSesionMap.remove(serverGroupId);
                        serverGroupDOMap.remove(serverGroupId);
                    }
                } finally {
                    lock.unlock();
                }
            }
        }

        sessionGroupIdMap.remove(session);
    }

    /**
     * 给前端写数据
     * @param session
     * @param wsResult
     * @throws IOException
     */
    private void doWriteToSession(WebSocketSession session, WSResult wsResult) throws IOException {
        synchronized (session) {
            session.sendMessage(new TextMessage(JSON.toJSONString(wsResult)));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        schedulerManager.registerJob(this, taskCorn, this.getClass().getName());
    }
}
