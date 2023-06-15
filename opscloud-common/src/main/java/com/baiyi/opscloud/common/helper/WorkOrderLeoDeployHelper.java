package com.baiyi.opscloud.common.helper;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.domain.model.WorkOrderLeoDeployToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/5/11 14:58
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WorkOrderLeoDeployHelper {

    /**
     * 有效期 1Day
     */
    private static final long CACHE_MAX_TIME = NewTimeUtil.DAY_TIME / 1000;

    private final RedisUtil redisUtil;

    private static final String KEY = "OC4:V0:WORKORDER:LEO:DEPLOY:BID:{}";

    public boolean hasKey(Integer buildId) {
        if (buildId == 0) {
            return false;
        }
        return redisUtil.hasKey(getKey(buildId));
    }

    public WorkOrderLeoDeployToken get(Integer buildId) {
        return (WorkOrderLeoDeployToken) redisUtil.get(getKey(buildId));
    }

    public void set(WorkOrderLeoDeployToken token) {
        redisUtil.set(getKey(token.getBuildId()), token, CACHE_MAX_TIME);
    }

    private String getKey(Integer buildId) {
        return MessageFormatter.format(KEY, buildId).getMessage();
    }

}
