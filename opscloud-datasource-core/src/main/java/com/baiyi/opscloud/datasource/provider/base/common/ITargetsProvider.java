package com.baiyi.opscloud.datasource.provider.base.common;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/6/21 4:08 下午
 * @Version 1.0
 */
public interface ITargetsProvider {

    Set<String> getTargetAssetKeys();
    
}
