package com.baiyi.opscloud.leo.helper;

import com.baiyi.opscloud.common.helper.WorkOrderLeoDeployHelper;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 部署通行证
 *
 * @Author baiyi
 * @Date 2023/5/16 11:14
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LeoDeployPassCheck {

    private final WorkOrderLeoDeployHelper workOrderLeoDeployHelper;

    private final RedisUtil redisUtil;

    /**
     * 缓存 2Hour
     */
    private static final long CACHE_MAX_TIME = NewTimeUtil.HOUR_TIME / 1000 * 2;

    /**
     * 历史部署信息
     */
    private static final String KEY = "OC4:V0:LEO:DEPLOY:BID:%s";

    public boolean checkPass(Integer buildId) {
        if (buildId == 0) {
            return false;
        }
        // 通过工单申请发布
        if (workOrderLeoDeployHelper.hasKey(buildId)) {
            return true;
        }
        // 查询通行证
        return redisUtil.hasKey(getKey(buildId));
    }

    /**
     * 颁发通行证
     *
     * @param buildId
     */
    public void issuePass(Integer buildId) {
        redisUtil.set(getKey(buildId), "PASS", CACHE_MAX_TIME);
    }

    private String getKey(Integer buildId) {
        return String.format(KEY, buildId);
    }


}
