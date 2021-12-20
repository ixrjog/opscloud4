package com.baiyi.opscloud.core.provider.base.asset;

import com.baiyi.opscloud.core.provider.base.param.UniqueAssetParam;

/**
 * @Author 修远
 * @Date 2021/7/1 4:43 下午
 * @Since 1.0
 */

@FunctionalInterface
public interface IAssetRelation {

    void buildAssetRelation(int dsInstanceId, UniqueAssetParam param);

}
