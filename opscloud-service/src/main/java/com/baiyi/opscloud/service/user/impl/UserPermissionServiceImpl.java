package com.baiyi.opscloud.service.user.impl;

import com.baiyi.opscloud.common.annotation.EventPublisher;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.EventActionTypeEnum;
import com.baiyi.opscloud.mapper.UserPermissionMapper;
import com.baiyi.opscloud.service.user.UserPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/26 5:39 下午
 * @Version 1.0
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@BusinessType(BusinessTypeEnum.USER_PERMISSION)
@Service
@RequiredArgsConstructor
public class UserPermissionServiceImpl implements UserPermissionService {

    private final UserPermissionMapper permissionMapper;

    @Override
    public UserPermission getById(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(UserPermission userPermission) {
        permissionMapper.updateByPrimaryKey(userPermission);
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.CREATE)
    public void add(UserPermission userPermission) {
        permissionMapper.insert(userPermission);
    }

    @Override
    public void deleteById(Integer id) {
        permissionMapper.deleteByPrimaryKey(id);
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.DELETE)
    public void delete(UserPermission userPermission) {
        permissionMapper.delete(userPermission);
    }

    @Override
    public UserPermission getByUserPermission(UserPermission userPermission) {
        return permissionMapper.selectOne(userPermission);
    }

    @Override
    public UserPermission getByUniqueKey(UserPermission userPermission) {
        Example example = new Example(UserPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userPermission.getUserId())
                .andEqualTo("businessType", userPermission.getBusinessType())
                .andEqualTo("businessId", userPermission.getBusinessId());
        return permissionMapper.selectOneByExample(example);
    }

    @Override
    public int countByBusiness(UserPermission userPermission) {
        Example example = new Example(UserPermission.class);
        Example.Criteria criteria = example.createCriteria();
        if (IdUtil.isNotEmpty(userPermission.getUserId())) {
            criteria.andEqualTo("userId", userPermission.getUserId());
        }
        criteria.andEqualTo("businessType", userPermission.getBusinessType())
                .andEqualTo("businessId", userPermission.getBusinessId());
        return permissionMapper.selectCountByExample(example);
    }

    /**
     * 查询授权不展示离职用户
     *
     * @param userPermission
     * @return
     */
    @Override
    public List<UserPermission> queryByBusiness(UserPermission userPermission) {
        Example example = new Example(UserPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", userPermission.getBusinessType())
                .andEqualTo("businessId", userPermission.getBusinessId());
        return permissionMapper.selectByExample(example);
    }

    @Override
    public List<UserPermission> queryByUserPermission(Integer userId, Integer businessType) {
        Example example = new Example(UserPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", businessType)
                .andEqualTo("userId", userId);
        return permissionMapper.selectByExample(example);
    }

    @Override
    public List<UserPermission> queryByUserId(Integer userId) {
        Example example = new Example(UserPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        return permissionMapper.selectByExample(example);
    }

    @Override
    public int statTotal(int businessType) {
        return permissionMapper.statTotal(businessType);
    }

}