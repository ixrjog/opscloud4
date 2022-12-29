package com.baiyi.opscloud.facade.datasource.resource;

import com.baiyi.opscloud.domain.annotation.ApplicationResType;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.constants.ApplicationResTypeEnum;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/10/31 13:38
 * @Version 1.0
 */
@ApplicationResType(ApplicationResTypeEnum.GITLAB_PROJECT)
@BusinessType(BusinessTypeEnum.ASSET)
@Component
public class AppResQueryWithGitLabProjectAsset extends AbstractAppResQueryWithAsset {

    @Override
    protected String getResName(DsAssetVO.Asset asset) {
        //  git仓库
        return asset.getAssetKey();
    }

}
