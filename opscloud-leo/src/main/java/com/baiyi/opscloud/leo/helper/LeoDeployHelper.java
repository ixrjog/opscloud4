package com.baiyi.opscloud.leo.helper;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
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
public class LeoDeployHelper {

    private final LeoDeployService leoDeployService;

    private final RedisUtil redisUtil;

    private final static String KEY = "leo#heartbeat#deploying#id=%s";

    public boolean isFinish(Integer leoDeployId) {
        LeoDeploy leoDeploy = leoDeployService.getById(leoDeployId);
        return leoDeploy.getIsFinish();
    }

    /**
     * 设置心跳
     * @param leoDeployId
     */
    public void setHeartbeat(Integer leoDeployId) {
        redisUtil.set(String.format(KEY, leoDeployId), true, 20L);
    }

    /**
     * 任务是否存活
     * @param leoDeployId
     * @return
     */
    public boolean isLive(Integer leoDeployId) {
        return redisUtil.hasKey(String.format(KEY, leoDeployId));
    }

}
