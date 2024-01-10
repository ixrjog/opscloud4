package com.baiyi.opscloud.common.holder;

import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.model.WorkOrderToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/5/11 14:58
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WorkOrderLeoDeployHolder extends BaseWorkOrderToken<WorkOrderToken.LeoDeployToken> {

    /**
     * 有效期 1Day
     */
    private static final long CACHE_MAX_TIME = NewTimeUtil.DAY_TIME / 1000;

    private static final String KEY = "OC4:V0:WORKORDER:LEO:DEPLOY:BID:{}";

    @Override
    public WorkOrderToken.LeoDeployToken getToken(Integer applicationId) {
        return (WorkOrderToken.LeoDeployToken) redisUtil.get(getKey(applicationId));
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