package com.baiyi.opscloud.service.user.impl;

import com.baiyi.opscloud.common.annotation.ArkIntercept;
import com.baiyi.opscloud.common.annotation.EventPublisher;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.annotation.Encrypt;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.constants.EventActionTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.factory.business.base.AbstractBusinessService;
import com.baiyi.opscloud.mapper.UserMapper;
import com.baiyi.opscloud.service.user.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
 * @Author baiyi
 * @Date 2021/5/14 10:27 上午
 * @Version 1.0
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@BusinessType(BusinessTypeEnum.USER)
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends AbstractBusinessService<User> implements UserService {

    private final UserMapper userMapper;

    @Override
    public DataTable<User> queryPageByParam(UserParam.UserPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<User> data = userMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public DataTable<User> queryPageByParam(UserBusinessPermissionParam.BusinessPermissionUserPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<User> data = userMapper.queryBusinessPermissionUserPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    @ArkIntercept
    public User getById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public BusinessAssetRelationVO.IBusinessAssetRelation toBusinessAssetRelation(DsAssetVO.Asset asset) {
        // 处理Dingtalk
        if (asset.getAssetType().equals(DsAssetTypeConstants.DINGTALK_USER.name())) {
            if (asset.getProperties().containsKey("username")) {
                log.info("同步钉钉查询本地用户: name={}, username={}", asset.getName(), asset.getProperties().get("username"));
                User user = getByKey(asset.getProperties().get("username"));
                if (user != null) {
                    return toUserVO(user);
                }
                if (asset.getProperties().containsKey("mobile")) {
                    List<User> users = listByPhone(asset.getProperties().get("mobile"));
                    if (!CollectionUtils.isEmpty(users) && users.size() == 1) {
                        log.info("同步钉钉查询本地用户: name={}, mobile={}", asset.getName(), asset.getProperties().get("mobile"));
                        return toUserVO(users.getFirst());
                    }
                }
            }
            return null;
        }
        return toUserVO(getByKey(asset.getAssetKey()));
    }

    private UserVO.User toUserVO(User user) {
        return BeanCopierUtil.copyProperties(user, UserVO.User.class);
    }

    @Override
    public List<User> queryAll() {
        return userMapper.selectAll();
    }

    @Override
    @ArkIntercept
    public User getByUsername(String username) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        return userMapper.selectOneByExample(example);
    }

    @Override
    public User getByKey(String key) {
        return getByUsername(key);
    }

    @Override
    @Encrypt
    @EventPublisher(eventAction = EventActionTypeEnum.CREATE)
    public void add(User user) {
        user.setId(null);
        userMapper.insert(user);
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.UPDATE)
    public void update(User user) {
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public void updateLogin(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.DELETE)
    public void delete(User user) {
        user.setIsActive(false);
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    @Encrypt
    @EventPublisher(eventAction = EventActionTypeEnum.CREATE)
    public void setActive(User user) {
        user.setIsActive(true);
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    @Encrypt
    @EventPublisher(eventAction = EventActionTypeEnum.DELETE)
    public void setInactive(User user) {
        user.setIsActive(false);
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    @Encrypt
    @EventPublisher(eventAction = EventActionTypeEnum.UPDATE)
    public void updateBySelective(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void updateMfa(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<User> listByIsActive(boolean isActive) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isActive", isActive);
        return userMapper.selectByExample(example);
    }

    @Override
    public List<User> listByPhone(String phone) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("phone", phone)
                .andEqualTo("isActive", true);
        return userMapper.selectByExample(example);
    }

    @Override
    public List<User> queryByTagKeys(List<String> tagKeys) {
        return userMapper.queryByTagKeys(tagKeys);
    }

    @Override
    public DataTable<User> queryPageByParam(UserParam.EmployeeResignPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<User> data = userMapper.queryEmployeeResignPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

}