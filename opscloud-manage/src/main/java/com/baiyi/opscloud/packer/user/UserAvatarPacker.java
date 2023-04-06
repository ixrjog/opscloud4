package com.baiyi.opscloud.packer.user;

import com.baiyi.opscloud.common.util.ExtendUtil;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessAssetRelation;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetProperty;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/2/18 6:24 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserAvatarPacker implements IWrapper<UserVO.User> {

    private final DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    private final BusinessAssetRelationService bizAssetRelationService;

    /**
     * 从资产获取用户头像并插入
     *
     * @param user
     */
    @Override
    public void wrap(UserVO.User user, IExtend iExtend) {
        if (!ExtendUtil.isExtend(iExtend)) {
            return;
        }
        BaseBusiness.IBusiness iBusiness = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.USER.getType())
                .businessId(user.getId())
                .build();
        List<BusinessAssetRelation> relations = bizAssetRelationService.queryBusinessRelations(iBusiness, DsAssetTypeConstants.DINGTALK_USER.name());
        if (CollectionUtils.isEmpty(relations)) {
            return;
        }
        for (BusinessAssetRelation relation : relations) {
            DatasourceInstanceAssetProperty property =
                    dsInstanceAssetPropertyService.queryByAssetId(relation.getDatasourceInstanceAssetId()).stream()
                            .filter(p -> p.getName().equals("avatar"))
                            .findFirst()
                            .orElse(null);
            if (property != null) {
                user.setAvatar(property.getValue());
                return;
            }
        }
    }

}
