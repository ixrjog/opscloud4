package com.baiyi.opscloud.packer.user.am;

import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.user.AMVO;

/**
 * @Author baiyi
 * @Date 2022/2/8 1:33 PM
 * @Version 1.0
 */
public interface IToAMConverter {

    AMVO.XAM toAM(DsAssetVO.Asset asset);

    String getAMType();

}
