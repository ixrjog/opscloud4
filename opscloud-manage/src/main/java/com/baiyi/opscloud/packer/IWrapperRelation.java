package com.baiyi.opscloud.packer;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.IRelation;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;

/**
 * @Author baiyi
 * @Date 2022/2/18 2:55 PM
 * @Version 1.0
 */
public interface IWrapperRelation<T> {

    void wrap(T vo, IExtend iExtend, IRelation iRelation);

    void wrap(DsAssetVO.Asset asset, IExtend iExtend);

}
