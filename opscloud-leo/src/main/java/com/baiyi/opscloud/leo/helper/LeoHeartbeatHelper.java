package com.baiyi.opscloud.leo.helper;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.constants.HeartbeatTypeConstants;
import com.baiyi.opscloud.leo.domain.model.DeployStopFlag;
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
public class LeoHeartbeatHelper {

    private final LeoDeployService leoDeployService;

    private final RedisUtil redisUtil;

    private final static String HEARTBEAT_KEY = "OC4:V0:LEO:HEARTBEAT:{}:ID:{}";

    public static final String STOP_SIGNAL = "OC4:V0:LEO:DEPLOY:STOP:SIGNAL:DID:{}";

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
     * @param deployId
     * @return
     */
    public DeployStopFlag getDeployStopFlag(int deployId) {
        final String key = StringFormatter.format(STOP_SIGNAL, deployId);
        if (redisUtil.hasKey(key)) {
            return DeployStopFlag.builder()
                    .isStop(true)
                    .username((String) redisUtil.get(key))
                    .build();
        } else {
            return DeployStopFlag.NOT_STOP;
        }
    }

}