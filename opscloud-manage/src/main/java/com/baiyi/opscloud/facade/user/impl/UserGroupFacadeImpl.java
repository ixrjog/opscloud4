package com.baiyi.opscloud.facade.user.impl;

import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.annotation.AssetBusinessRelation;
import com.baiyi.opscloud.domain.annotation.AssetBusinessUnbindRelation;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.annotation.TagClear;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.param.user.UserGroupParam;
import com.baiyi.opscloud.domain.vo.user.UserGroupVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.facade.user.UserGroupFacade;
import com.baiyi.opscloud.facade.user.base.IUserBusinessPermissionPageQuery;
import com.baiyi.opscloud.facade.user.factory.UserBusinessPermissionFactory;
import com.baiyi.opscloud.packer.user.UserGroupPacker;
import com.baiyi.opscloud.packer.user.UserPermissionPacker;
import com.baiyi.opscloud.service.user.UserGroupService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/16 3:20 下午
 * @Version 1.0
 */
@BusinessType(BusinessTypeEnum.USERGROUP)
@Service
@RequiredArgsConstructor
public class UserGroupFacadeImpl implements UserGroupFacade, IUserBusinessPermissionPageQuery, InitializingBean {

    private final UserGroupService userGroupService;

    private final UserGroupPacker userGroupPacker;

    private final UserPermissionPacker userPermissionPacker;

    private final UserPermissionService userPermissionService;

    @Override
    public Integer getBusinessType() {
        return BusinessTypeEnum.USERGROUP.getType();
    }

    @Override
    public DataTable<UserGroupVO.UserGroup> queryUserGroupPage(UserGroupParam.UserGroupPageQuery pageQuery) {
        DataTable<UserGroup> table = userGroupService.queryPageByParam(pageQuery);
        List<UserGroupVO.UserGroup> data = BeanCopierUtil.copyListProperties(table.getData(), UserGroupVO.UserGroup.class).stream()
                .peek(e -> userGroupPacker.wrap(e, pageQuery)).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    @AssetBusinessRelation
    public void addUserGroup(UserGroupParam.UserGroup userGroup) {
        userGroupService.add(BeanCopierUtil.copyProperties(userGroup, UserGroup.class));
    }

    @Override
    public void updateUserGroup(UserGroupParam.UserGroup userGroup) {
        UserGroup pre = userGroupService.getById(userGroup.getId());
        if (pre == null) {
            return;
        }
        pre.setAllowOrder(userGroup.getAllowOrder());
        pre.setComment(userGroup.getComment());
        pre.setSource(userGroup.getSource());
        userGroupService.update(pre);
    }

    @Override
    @TagClear
    @AssetBusinessUnbindRelation(type = BusinessTypeEnum.USERGROUP)
    public void deleteUserGroupById(Integer id) {
        // 检查组成员
        UserPermission userPermission = UserPermission.builder()
                .businessId(id)
                .businessType(BusinessTypeEnum.USERGROUP.getType())
                .build();
        List<UserPermission> userPermissions = userPermissionService.queryByBusiness(userPermission);
        if (!CollectionUtils.isEmpty(userPermissions)) {
            throw new OCException("必须删除用户组中的用户！");
        }
        userGroupService.deleteById(id);
    }

    @Override
    public DataTable<UserVO.IUserPermission> queryUserBusinessPermissionPage(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery) {
        pageQuery.setBusinessType(getBusinessType());
        DataTable<UserGroup> table = userGroupService.queryPageByParam(pageQuery);
        List<UserGroupVO.UserGroup> data = BeanCopierUtil.copyListProperties(table.getData(), UserGroupVO.UserGroup.class).stream()
                .peek(e -> userGroupPacker.wrap(e, pageQuery)).collect(Collectors.toList());
        if (pageQuery.getAuthorized()) {
            data.forEach(e -> {
                e.setUserId(pageQuery.getUserId());
                userPermissionPacker.wrap(e);
            });
        }
        return new DataTable<>(Lists.newArrayList(data), table.getTotalNum());
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        UserBusinessPermissionFactory.register(this);
    }
}
