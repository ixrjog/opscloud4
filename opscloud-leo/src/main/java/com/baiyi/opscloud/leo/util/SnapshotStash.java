package com.baiyi.opscloud.leo.util;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.vo.leo.LeoDeployingVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/12/7 16:54
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class SnapshotStash {

    private final RedisUtil redisUtil;

    private static final long STASH_TIME = NewTimeUtil.DAY_TIME * 7 / 1000;

    public void save(Integer deployId, LeoDeployingVO.Deploying deploying) {
        redisUtil.set(getKey(deployId), deploying, STASH_TIME);
    }

    public Boolean isExist(Integer deployId) {
        return redisUtil.hasKey(getKey(deployId));
    }

    public LeoDeployingVO.Deploying get(Integer deployId) {
        return (LeoDeployingVO.Deploying) redisUtil.get(getKey(deployId));
    }

    private String getKey(Integer deployId) {
        return StringFormatter.format("v20221207#deploying#id={}", deployId);
    }

}