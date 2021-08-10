package com.baiyi.opscloud.facade.user.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.annotation.AssetBusinessUnbindRelation;
import com.baiyi.opscloud.domain.annotation.TagClear;
import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.param.user.UserGroupParam;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/16 3:20 下午
 * @Version 1.0
 */
@Service
public class UserGroupFacadeImpl implements UserGroupFacade, IUserBusinessPermissionPageQuery, InitializingBean {

    @Resource
    private UserGroupService userGroupService;

    @Resource
    private UserGroupPacker userGroupPacker;

    @Resource
    private UserPermissionPacker userPermissionPacker;

    @Resource
    private UserPermissionService userPermissionService;

    @Override
    public int getBusinessType() {
        return BusinessTypeEnum.USERGROUP.getType();
    }

    @Override
    public DataTable<UserGroupVO.UserGroup> queryUserGroupPage(UserGroupParam.UserGroupPageQuery pageQuery) {
        DataTable<UserGroup> table = userGroupService.queryPageByParam(pageQuery);
        return new DataTable<>(userGroupPacker.wrapVOList(table.getData(), pageQuery), table.getTotalNum());
    }

    @Override
    public void addUserGroup(UserGroupVO.UserGroup userGroup) {
        userGroupService.add(BeanCopierUtil.copyProperties(userGroup, UserGroup.class));
    }

    @Override
    public void updateUserGroup(UserGroupVO.UserGroup userGroup) {
        UserGroup pre = userGroupService.getById(userGroup.getId());
        if (pre == null) return;
        pre.setAllowWorkorder(userGroup.getAllowWorkorder());
        pre.setComment(userGroup.getComment());
        pre.setSource(userGroup.getSource());
        userGroupService.update(pre);
    }

    @Override
    @TagClear(type = BusinessTypeEnum.USERGROUP) // 清除标签
    @AssetBusinessUnbindRelation(type = BusinessTypeEnum.USERGROUP) // 解除资产绑定
    public void deleteUserGroupById(Integer id) {
        // 检查组成员
        UserPermission userPermission = UserPermission.builder()
                .businessId(id)
                .businessType(BusinessTypeEnum.USERGROUP.getType())
                .build();
        List<UserPermission> userPermissions = userPermissionService.queryByBusiness(userPermission);
        if (!CollectionUtils.isEmpty(userPermissions))
            throw new RuntimeException("必须删除用户组中的用户！");
        userGroupService.deleteById(id);
    }

    @Override
    public DataTable<UserVO.IUserPermission> queryUserBusinessPermissionPage(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery) {
        pageQuery.setBusinessType(getBusinessType());
        DataTable<UserGroup> table = userGroupService.queryPageByParam(pageQuery);
        List<UserGroupVO.UserGroup> data = userGroupPacker.wrapVOList(table.getData(), pageQuery);
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
