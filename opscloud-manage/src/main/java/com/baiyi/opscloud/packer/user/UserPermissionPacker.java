package com.baiyi.opscloud.packer.user;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.ExtendUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.vo.user.UserPermissionVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.facade.user.base.IUserBusinessPermissionPageQuery;
import com.baiyi.opscloud.facade.user.factory.UserBusinessPermissionFactory;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/5/27 4:12 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserPermissionPacker implements IWrapper<UserVO.User> {

    private final UserPermissionService permissionService;

    @Override
    public void wrap(UserVO.User user, IExtend iExtend) {
        if (!ExtendUtil.isExtend(iExtend)) {
            return;
        }
        Map<Integer, IUserBusinessPermissionPageQuery> context = UserBusinessPermissionFactory.getContext();
        UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery = UserBusinessPermissionParam.UserBusinessPermissionPageQuery
                .builder()
                .userId(user.getId())
                .page(1)
                .length(10000)
                .build();
        Map<String, List<UserVO.IUserPermission>> businessPermissions = Maps.newHashMap();
        context.keySet().forEach(k -> {
            DataTable<UserVO.IUserPermission> table = context.get(k).queryUserBusinessPermissionPage(pageQuery);
            BusinessTypeEnum businessTypeEnum = BusinessTypeEnum.getByType(k);
            if (businessTypeEnum != null && !CollectionUtils.isEmpty(table.getData())) {
                businessPermissions.put(businessTypeEnum.name(), table.getData());
            }
        });
        user.setBusinessPermissions(businessPermissions);
    }

    public void wrap(UserVO.IUserPermission iUserPermission) {
        UserPermission query = UserPermission.builder()
                .userId(iUserPermission.getUserId())
                .businessId(iUserPermission.getBusinessId())
                .businessType(iUserPermission.getBusinessType())
                .build();
        UserPermission userPermission = permissionService.getByUserPermission(query);
        if (userPermission != null) {
            iUserPermission.setUserPermission(BeanCopierUtil.copyProperties(userPermission, UserPermissionVO.UserPermission.class));
        }
    }

}
