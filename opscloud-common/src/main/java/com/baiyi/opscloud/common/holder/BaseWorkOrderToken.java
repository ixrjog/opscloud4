package com.baiyi.opscloud.common.holder;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.domain.model.WorkOrderToken;
import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2023/6/16 11:30
 * @Version 1.0
 */
public abstract class BaseWorkOrderToken<T extends WorkOrderToken.IWorkOrderToken> {

    @Resource
    protected RedisUtil redisUtil;

    public boolean hasKey(Integer id) {
        if (id == 0) {
            return false;
        }
        return redisUtil.hasKey(getKey(id));
    }

    abstract protected String getKey(Integer id);

    abstract public T getToken(Integer id);

    public void setToken(T token) {
        redisUtil.set(getKey(token.getKey()), token, getPeriodOfValidity());
    }

    /**
     * 有效期
     * @return
     */
    abstract protected long getPeriodOfValidity();

}