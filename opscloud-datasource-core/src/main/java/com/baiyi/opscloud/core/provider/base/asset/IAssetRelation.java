package com.baiyi.opscloud.core.provider.base.asset;

import com.baiyi.opscloud.core.provider.base.param.UniqueAssetParam;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/1 4:43 下午
 * @Since 1.0
 */

@FunctionalInterface
public interface IAssetRelation {

    void buildAssetRelation(int dsInstanceId, UniqueAssetParam param);

}
