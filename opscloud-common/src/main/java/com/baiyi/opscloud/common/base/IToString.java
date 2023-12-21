package com.baiyi.opscloud.common.base;

import com.baiyi.opscloud.common.util.JSONUtil;

/**
 * @Author baiyi
 * @Date 2022/7/26 09:43
 * @Version 1.0
 */
public abstract class IToString {

    @Override
    public String toString() {
        return JSONUtil.writeValueAsString(this);
    }

}