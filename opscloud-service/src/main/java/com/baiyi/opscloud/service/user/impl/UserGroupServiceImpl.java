package com.baiyi.opscloud.service.user.impl;

import com.baiyi.opscloud.common.annotation.EventPublisher;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.param.user.UserGroupParam;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.EventActionTypeEnum;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.user.UserGroupVO;
import com.baiyi.opscloud.factory.business.base.AbstractBusinessService;
import com.baiyi.opscloud.mapper.UserGroupMapper;
import com.baiyi.opscloud.service.user.UserGroupService;
import com.baiyi.opscloud.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/16 3:16 下午
 * @Version 1.0
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@BusinessType(BusinessTypeEnum.USERGROUP)
@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl extends AbstractBusinessService<UserGroup> implements UserGroupService {

    private final UserGroupMapper userGroupMapper;

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.CREATE)
    public void add(UserGroup userGroup) {
        userGroup.setId(null);
        userGroupMapper.insert(userGroup);
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.UPDATE)
    public void update(UserGroup userGroup) {
        userGroupMapper.updateByPrimaryKey(userGroup);
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.DELETE)
    public void deleteById(Integer id) {
        userGroupMapper.deleteByPrimaryKey(id);
    }

    @Override
    public UserGroup getByName(String name) {
        Example example = new Example(UserGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        return userGroupMapper.selectOneByExample(example);
    }

    @Override
    public UserGroup getByKey(String key) {
        return getByName(key);
    }

    @Override
    public BusinessAssetRelationVO.IBusinessAssetRelation toBusinessAssetRelation(DsAssetVO.Asset asset) {
        UserGroup userGroup = getByKey(asset.getAssetKey());
        return BeanCopierUtil.copyProperties(userGroup, UserGroupVO.UserGroup.class);
    }

    @Override
    public UserGroup getById(Integer id) {
        return userGroupMapper.selectByPrimaryKey(id);
    }

    @Override
    public DataTable<UserGroup> queryPageByParam(UserGroupParam.UserGroupPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(UserGroup.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            String likeName = SQLUtil.toLike(pageQuery.getQueryName());
            criteria.andLike("name", likeName);
        }
        example.setOrderByClause("create_time");
        List<UserGroup> data = userGroupMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());

    }

    @Override
    public DataTable<UserGroup> queryPageByParam(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<UserGroup> data = userGroupMapper.queryUserPermissionGroupByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

}