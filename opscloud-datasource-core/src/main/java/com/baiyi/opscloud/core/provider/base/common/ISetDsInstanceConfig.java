package com.baiyi.opscloud.core.provider.base.common;

import com.baiyi.opscloud.domain.base.IInstanceType;

/**
 * @Author baiyi
 * @Date 2021/6/24 6:38 下午
 * @Version 1.0
 */
public interface ISetDsInstanceConfig extends IInstanceType {

    void setConfig(int instanceId);

}