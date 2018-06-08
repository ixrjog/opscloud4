package com.sdg.cmdb.ws;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.dao.cmdb.AuthDao;
import com.sdg.cmdb.dao.cmdb.KeyboxDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.keybox.*;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.keybox.handler.ConnectionSession;
import com.sdg.cmdb.keybox.handler.RemoteInvokeHandler;
import com.sdg.cmdb.keybox.handler.SessionBridge;
import com.sdg.cmdb.plugin.chain.TaskCallback;
import com.sdg.cmdb.plugin.chain.TaskChain;
import com.sdg.cmdb.plugin.chain.TaskItem;
import com.sdg.cmdb.scheduler.task.ServerGroupSubTask;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.service.control.configurationfile.GetwayService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zxxiao on 2016/11/9.
 */
@Service
public class KeyboxWebSocketHandler extends TextWebSocketHandler implements InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger("cmdLogger");

    /**
     * user token & (web socket session id & web socket session)
     */
    private Map<String, Map<String, WebSocketSession>> userSession = new ConcurrentHashMap<>();

    private Map<String, ConnectionSession> connectionSessionMap = new ConcurrentHashMap<>();

    /**
     * 服务器组批量订阅独立分发关联关系
     */
    private Map<Long, List<String>> serverGroupSession = new ConcurrentHashMap<>();

    @Resource
    private KeyboxDao keyboxDao;

    @Resource
    private AuthDao authDao;

    @Resource
    private AuthService authService;

    @Resource
    private ServerService serverService;

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private GetwayService getwayService;

    @Resource
    private AnsibleTaskService ansibleTaskService;

    @Resource
    private ServerGroupSubTask groupSubTask;

    @Resource
    private ServerDao serverDao;

    @Resource
    private ServerGroupDao serverGropuDao;

    @Resource
    private ConfigServerGroupService configServerGroupService;

    private ExecutorService executors = Executors.newFixedThreadPool(10);

    private static final long TIME_ALIVE_INTERVAL = 40000l;

    private boolean keepRun = true;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.warn("do connection for:" + session.getUri().getQuery());

        String[] token = session.getUri().getQuery().split("=");
        if (token.length <= 1) {
            session.sendMessage(new TextMessage("do'nt add token for request! connection refused."));
            session.close();
        }

        String requestToken = token[1];
        Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
        sessionMap.put(session.getId(), session);
        userSession.put(requestToken, sessionMap);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.warn("connection for:" + session.getUri().getQuery() + " has closed!");
        String[] token = session.getUri().getQuery().split("=");
        if (token.length <= 1) {
            //避免被攻击
            for (Map<String, WebSocketSession> entry : userSession.values()) {
                if (!entry.containsKey(session.getId())) {
                    continue;
                } else {
                    entry.remove(session.getId());
                }
            }
            return;
        }

        String requestToken = token[1];
        String requestUsername = authService.getUserByToken(requestToken);

        //移除断开连接的会话
        userSession.get(requestToken).remove(session.getId());
        for (String key : connectionSessionMap.keySet()) {
            if (key.startsWith(requestUsername)) {
                ConnectionSession connectionSession = connectionSessionMap.get(key);
                if (connectionSession != null) {
                    connectionSession.unregisterRead(key);
                    connectionSession.close();
                }
            }
        }

        //移除服务器组订阅
        groupSubTask.unregisterSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        WebSocketContext socketContext = JSON.parseObject(message.getPayload().toString(), WebSocketContext.class);

        String requestType = socketContext.getRequestType();
        if (requestType.equals(WebSocketContext.RequestTypeEnum.init.getCode())) {
            doInitProcess(session, socketContext);
        } else if (requestType.equals(WebSocketContext.RequestTypeEnum.cmd.getCode())) {
            doCmdProcess(session, socketContext);
        } else if (requestType.equals(WebSocketContext.RequestTypeEnum.resize.getCode())) {
            doResizeProcess(session, socketContext);
        } else if (requestType.equals(WebSocketContext.RequestTypeEnum.close.getCode())) {
            doCloseProcess(session, socketContext);
        } else if (requestType.equals(WebSocketContext.RequestTypeEnum.subServerGroup.getCode())) {
            doServerGroupSub(session, socketContext);
        } else if (requestType.equals(WebSocketContext.RequestTypeEnum.subTaskChain.getCode())) {
            doTaskChain(session, socketContext);
        }
    }

    /**
     * 处理任务执行链路
     *
     * @param session
     * @param socketContext
     */
    private void doTaskChain(WebSocketSession session, WebSocketContext socketContext) {
        //Map map = JSON.parseObject(socketContext.getBody().toString(), Map.class);
        long serverId = Long.parseLong(socketContext.getBody().toString());

        ServerDO serverDO = serverDao.getServerInfoById(serverId);
        ServerGroupDO serverGroupDO = serverGropuDao.queryServerGroupById(serverDO.getServerGroupId());

        TaskCallback taskCallback = new TaskCallback() {
            @Override
            public void doNotify(Object notify) {
                BusinessWrapper wrapper = (BusinessWrapper) notify;
                WSResult wsResult = new WSResult();
                wsResult.setCode(wrapper.getCode());
                wsResult.setMsg(wrapper.getMsg());
                wsResult.setData(wrapper.getBody());

                try {
                    doWriteToSession(session, wsResult);
                } catch (IOException e) {
                    logger.error("write data to client error :" + e.getMessage(), e);
                }
            }
        };

       // TaskItem taskItem =  ansibleTaskService.taskInitSystem(serverDO, serverGroupDO, 1);
        TaskItem taskItem = null;
        TaskChain taskChain = new TaskChain("chainName", taskCallback, Arrays.asList(taskItem));
        taskChain.doInvoke();
        doCloseProcess(session, socketContext);
    }

    /**
     * 用户登出,销毁所有会话
     *
     * @param token
     */
    public void userLogout(String token) {
        if (userSession.containsKey(token)) {
            Map<String, WebSocketSession> sessionMap = userSession.get(token);
            for (WebSocketSession socketSession : sessionMap.values()) {
                try {
                    socketSession.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            sessionMap.clear();
            userSession.remove(token);
        }
    }

    /**
     * 订阅服务器组监控信息
     *
     * @param session
     * @param socketContext
     */
    private void doServerGroupSub(WebSocketSession session, WebSocketContext socketContext) throws IOException {
        long serverGroupId = Long.parseLong(socketContext.getBody().toString());
        ServerGroupDO serverGroupDO = serverGroupService.queryServerGroupById(serverGroupId);

        if (serverGroupDO == null) {
            logger.error("server group param error!");
            WSResult wsResult = new WSResult(ErrorCode.wsServerGroupParamError);
            doWriteToSession(session, wsResult);
            return;
        }

        List<String> sessionList = serverGroupSession.get(serverGroupDO.getId());
        if (sessionList == null) {
            sessionList = new ArrayList<>();
            serverGroupSession.put(serverGroupDO.getId(), sessionList);
        } else if (sessionList.contains(session.getId())) {
            logger.error("repeat sub server group for: " + serverGroupDO.getId() + " !");
            WSResult wsResult = new WSResult(ErrorCode.wsServerGroupRepeatSub);
            doWriteToSession(session, wsResult);
            return;
        }

        sessionList.add(session.getId());

        groupSubTask.unregisterSession(session);
        groupSubTask.register(serverGroupDO, session);
    }

    /**
     * 关闭ssh 会话
     *
     * @param session
     * @param socketContext
     */
    private void doCloseProcess(WebSocketSession session, WebSocketContext socketContext) {
        ConnectionSession connectionSession = connectionSessionMap.get(socketContext.getId());
        if (connectionSession != null) {
            connectionSession.unregisterRead(socketContext.getId());
            connectionSession.close();
        }
    }

    /**
     * 调整大小交互
     *
     * @param session
     * @param socketContext
     * @throws IOException
     */
    private void doResizeProcess(WebSocketSession session, WebSocketContext socketContext) throws IOException {
        Map<String, Object> sizeMap = JSON.parseObject(socketContext.getBody().toString(), Map.class);
        Double width = Double.parseDouble(sizeMap.get("width").toString());
        Double height = Double.parseDouble(sizeMap.get("height").toString());

        ConnectionSession connectionSession = connectionSessionMap.get(socketContext.getId());
        if (connectionSession != null) {
            connectionSession.setPtySize(width.intValue(), height.intValue());
        }
    }

    /**
     * 建立命令会话
     *
     * @param session
     * @param socketContext
     */
    private void doCmdProcess(WebSocketSession session, WebSocketContext socketContext) {
        String cmd = socketContext.getBody().toString();

        logger.warn(socketContext.getToken() + " exe cmd:" + cmd);

        ConnectionSession connectionSession = connectionSessionMap.get(socketContext.getId());
        if (connectionSession != null) {
            connectionSession.writeToRemote(cmd);
        }
    }

    /**
     * 建立初始化会话
     *
     * @param session
     * @param socketContext
     * @throws IOException'\b '
     */
    private void doInitProcess(WebSocketSession session, WebSocketContext socketContext) throws IOException {
        long serverId = Long.parseLong(socketContext.getBody().toString());

        ServerDO serverDO = serverService.getServerById(serverId);
        // 此处埋点统计堡垒机登陆的服务器
        try {

            String username = authDao.getUserByToken(socketContext.getToken());
            if (!StringUtils.isEmpty(username)) {
                KeyboxLoginStatusDO keyboxStatusDO = new KeyboxLoginStatusDO(serverDO, username);
                System.err.println(keyboxStatusDO);
                keyboxDao.addKeyboxStatus(keyboxStatusDO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        HostSystem hostSystem = new HostSystem();
        hostSystem.setInstanceId(socketContext.getId());
        hostSystem.setUser(serverDO.getLoginUser());
        hostSystem.setPort(22);
        configServerGroupService.invokeGetwayIp(serverDO);
        hostSystem.setHost(serverDO.getInsideIp());

        ApplicationKeyDO keyDO = keyboxDao.getApplicationKey();
        keyDO.setSessionId(socketContext.getId());

        ConnectionSession connectionSession = RemoteInvokeHandler.getSession(keyDO, hostSystem);

        if (connectionSession != null) {
            connectionSessionMap.put(socketContext.getId(), connectionSession);

            WSResult loginSuccess = new WSResult(socketContext.getId(), "", WSContentType.loginSuccss.getCode());
            doWriteToSession(session, loginSuccess);
            connectionSession.registerRead(socketContext.getId(), new SessionBridge() {
                @Override
                public void process(String value) {
                    try {
                        WSResult WSResult = new WSResult(socketContext.getId(), value, com.sdg.cmdb.domain.keybox.WSContentType.response.getCode());
                        doWriteToSession(session, WSResult);
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            });
        } else {
            logger.warn(socketContext.getId() + " do ssh connection for " + hostSystem.getHost() + " error with:" + hostSystem.getErrorMsg());

            WSResult WSResult = new WSResult(socketContext.getId(), hostSystem.getErrorMsg(), WSContentType.loginFailure.getCode());
            doWriteToSession(session, WSResult);
        }
    }

    /**
     * 给前端写数据
     *
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
        executors.submit(() -> {
            while (keepRun) {
                try {
                    for (Map<String, WebSocketSession> entry : userSession.values()) {
                        for (WebSocketSession session : entry.values()) {
                            if (session.isOpen()) {
                                byte[] bs = new byte[1];
                                bs[0] = 'i';
                                ByteBuffer byteBuffer = ByteBuffer.wrap(bs);
                                session.sendMessage(new PingMessage(byteBuffer));
                            }
                        }
                    }
                    Thread.sleep(TIME_ALIVE_INTERVAL);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
    }

    @Override
    public void destroy() throws Exception {
        keepRun = false;
    }
}
