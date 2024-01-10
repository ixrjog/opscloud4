package com.baiyi.opscloud.core.asset.impl;

import com.baiyi.opscloud.core.asset.impl.base.AbstractAssetToBO;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.user.UserGroupVO;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/6 11:26 上午
 * @Version 1.0
 */
@Component
public class LdapGroupAssetToUserGroup extends AbstractAssetToBO {

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.GROUP.name();
    }

    protected BusinessAssetRelationVO.IBusinessAssetRelation toBO(DsAssetVO.Asset asset, BusinessTypeEnum businessTypeEnum) {
        return UserGroupVO.UserGroup.builder()
                .name(asset.getAssetKey())
                // 允许工单申请
                .allowOrder(true)
                .source("LDAP")
                .build();
    }

    @Override
    public List<BusinessTypeEnum> getBusinessTypes() {
        return Lists.newArrayList(BusinessTypeEnum.USERGROUP);
    }

}