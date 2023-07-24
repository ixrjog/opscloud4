package com.baiyi.opscloud.leo.holder;

import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.constants.HeartbeatTypeConstants;
import com.baiyi.opscloud.leo.domain.model.StopBuildFlag;
import com.baiyi.opscloud.leo.domain.model.StopDeployFlag;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/12/26 11:29
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LeoHeartbeatHolder {

    private final LeoDeployService leoDeployService;

    private final RedisUtil redisUtil;

    private final static String HEARTBEAT_KEY = "OC4:V0:LEO:HEARTBEAT:{}:ID:{}";

    private final static  String STOP_SIGNAL = "OC4:V0:LEO:{}:STOP:SIGNAL:ID:{}";

    public boolean isFinish(Integer leoDeployId) {
        LeoDeploy leoDeploy = leoDeployService.getById(leoDeployId);
        return leoDeploy.getIsFinish();
    }

    /**
     * 设置心跳
     *
     * @param heartbeatType
     * @param id
     */
    public void setHeartbeat(HeartbeatTypeConstants heartbeatType, Integer id) {
        redisUtil.set(StringFormatter.arrayFormat(HEARTBEAT_KEY, heartbeatType.name(), id), true, 20L);
    }

    /**
     * 任务是否存活
     *
     * @param heartbeatType
     * @param id
     * @return
     */
    public boolean isLive(HeartbeatTypeConstants heartbeatType, Integer id) {
        return redisUtil.hasKey(StringFormatter.arrayFormat(HEARTBEAT_KEY, heartbeatType.name(), id));
    }

    /**
     * 获取停止标记
     *
     * @param deployId
     * @return
     */
    public StopDeployFlag getStopDeployFlag(int deployId) {
        final String key = StringFormatter.arrayFormat(STOP_SIGNAL, HeartbeatTypeConstants.DEPLOY.name(), deployId);
        if (redisUtil.hasKey(key)) {
            return StopDeployFlag.builder()
                    .isStop(true)
                    .username((String) redisUtil.get(key))
                    .build();
        } else {
            return StopDeployFlag.NOT_STOP;
        }
    }

    public StopBuildFlag getStopBuildFlag(int buildId) {
        final String key = StringFormatter.arrayFormat(STOP_SIGNAL, HeartbeatTypeConstants.BUILD.name(), buildId);
        if (redisUtil.hasKey(key)) {
            return StopBuildFlag.builder()
                    .isStop(true)
                    .username((String) redisUtil.get(key))
                    .build();
        } else {
            return StopBuildFlag.NOT_STOP;
        }
    }

    /**
     * 设置停止信号量
     *
     * @param heartbeatType
     * @param id
     */
    public void setStopSignal(HeartbeatTypeConstants heartbeatType, Integer id) {
        final String key = StringFormatter.arrayFormat(STOP_SIGNAL, heartbeatType.name(), id);
        final String username = SessionHolder.getUsername();
        redisUtil.set(key, username, 600L);
    }

}