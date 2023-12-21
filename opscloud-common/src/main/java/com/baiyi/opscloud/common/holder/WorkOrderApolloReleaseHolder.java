package com.baiyi.opscloud.common.holder;

import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.model.WorkOrderToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/6/13 18:04
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WorkOrderApolloReleaseHolder extends BaseWorkOrderToken<WorkOrderToken.ApolloReleaseToken> {

    /**
     * 有效期 2Hour
     */
    private static final long CACHE_MAX_TIME = NewTimeUtil.HOUR_TIME / 1000 * 2;

    private static final String KEY = "OC4:V0:WORKORDER:APOLLO:RELEASE:APPID:{}";

    @Override
    public WorkOrderToken.ApolloReleaseToken getToken(Integer applicationId) {
        final String key = getKey(applicationId);
        if (redisUtil.hasKey(key)) {
            return (WorkOrderToken.ApolloReleaseToken) redisUtil.get(getKey(applicationId));
        } else {
            return null;
        }
    }

    @Override
    protected long getPeriodOfValidity() {
        return CACHE_MAX_TIME;
    }

    @Override
    protected String getKey(Integer id) {
        return StringFormatter.format(KEY, id);
    }

}