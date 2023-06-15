package com.baiyi.opscloud.common.helper;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.domain.model.WorkOrderApolloReleaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/6/13 18:04
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WorkOrderApolloReleaseHelper {

    /**
     * 有效期 2Hour
     */
    private static final long CACHE_MAX_TIME = NewTimeUtil.HOUR_TIME / 1000 * 2;

    private final RedisUtil redisUtil;

    private static final String KEY = "OC4:V0:WORKORDER:APOLLO:RELEASE:APPID:{}";

    public boolean hasKey(Integer applicationId) {
        if (applicationId == 0) {
            return false;
        }
        return redisUtil.hasKey(getKey(applicationId));
    }

    public WorkOrderApolloReleaseToken get(Integer applicationId) {
        return (WorkOrderApolloReleaseToken) redisUtil.get(getKey(applicationId));
    }

    public void set(WorkOrderApolloReleaseToken token) {
        redisUtil.set(getKey(token.getApplicationId()), token, CACHE_MAX_TIME);
    }

    private String getKey(Integer applicationId) {
        return MessageFormatter.format(KEY, applicationId).getMessage();
    }

}
