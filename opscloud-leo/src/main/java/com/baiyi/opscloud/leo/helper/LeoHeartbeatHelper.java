package com.baiyi.opscloud.leo.helper;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.domain.model.DeployStop;
import com.baiyi.opscloud.leo.supervisor.DeployingSupervisor;
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

    public interface HeartbeatTypes {
        String DEPLOY = "deploy";
        String BUILD = "build";
    }

    private final LeoDeployService leoDeployService;

    private final RedisUtil redisUtil;

    private final static String HEARTBEAT_KEY = "leo#heartbeat#%s#id=%s";


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
    public void setHeartbeat(String heartbeatType, Integer id) {
        redisUtil.set(String.format(HEARTBEAT_KEY, heartbeatType, id), true, 20L);
    }

    /**
     * 任务是否存活
     *
     * @param heartbeatType
     * @param id
     * @return
     */
    public boolean isLive(String heartbeatType, Integer id) {
        return redisUtil.hasKey(String.format(HEARTBEAT_KEY, heartbeatType, id));
    }

    public DeployStop tryDeployStop(int deployId) {
        final String key = String.format(DeployingSupervisor.STOP_SIGNAL, deployId);
        if (redisUtil.hasKey(key)) {
            return DeployStop.builder()
                    .isStop(true)
                    .username((String) redisUtil.get(key))
                    .build();
        } else {
            return DeployStop.builder().build();
        }
    }

}
