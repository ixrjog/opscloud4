package com.baiyi.opscloud.server.impl;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.server.IServer;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

import static com.baiyi.opscloud.common.base.Topic.TASK_ALIYUN_LOG_TOPIC;

/**
 * @Author baiyi
 * @Date 2020/6/15 4:39 下午
 * @Version 1.0
 */
@Slf4j
@Component("AliyunLogMessage")
public class AliyunLogMessage extends BaseServer implements IServer {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void sync() {
    }

    @Override
    public Boolean disable(OcServer ocServer) {
        return sendMessage(ocServer.getServerGroupId());
    }

    @Override
    public Boolean enable(OcServer ocServer) {
        return sendMessage(ocServer.getServerGroupId());
    }

    /**
     * 创建
     *
     * @param ocServer
     * @return
     */
    @Override
    public Boolean create(OcServer ocServer) {
        return sendMessage(ocServer.getServerGroupId());
    }

    /**
     * 移除
     *
     * @param ocServer
     * @return
     */
    @Override
    public Boolean remove(OcServer ocServer) {
        return sendMessage(ocServer.getServerGroupId());
    }

    /**
     * 更新
     *
     * @param ocServer
     * @return
     */
    @Override
    public Boolean update(OcServer ocServer) {
        return sendMessage(ocServer.getServerGroupId());
    }

    private Boolean sendMessage(Integer serverGroupId) {
        Set<Integer> keySet = getTopicKeySet();
        keySet.add(serverGroupId);
        redisUtil.set(TASK_ALIYUN_LOG_TOPIC, keySet, 30 * 60 * 60);
        return Boolean.TRUE;
    }

    private Set<Integer> getTopicKeySet() {
        if (redisUtil.hasKey(TASK_ALIYUN_LOG_TOPIC)) {
            return (Set<Integer>) redisUtil.get(TASK_ALIYUN_LOG_TOPIC);
        } else {
            return Sets.newHashSet();
        }
    }

}
