package com.baiyi.opscloud.service.user.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcUserPermission;
import com.baiyi.opscloud.mapper.opscloud.OcUserPermissionMapper;
import com.baiyi.opscloud.service.user.OcUserPermissionService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/25 5:14 下午
 * @Version 1.0
 */
@Service
public class OcUserPermissionServiceImpl implements OcUserPermissionService {

    @Resource
    private OcUserPermissionMapper ocUserPermissionMapper;

    @Override
    public void addOcUserPermission(OcUserPermission ocUserPermission) {
        ocUserPermissionMapper.insert(ocUserPermission);
    }

    @Override
    public void delOcUserPermissionById(int id) {
        ocUserPermissionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OcUserPermission queryOcUserPermissionByUniqueKey(OcUserPermission ocUserPermission) {
        Example example = new Example(OcUserPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", ocUserPermission.getUserId());
        criteria.andEqualTo("businessId", ocUserPermission.getBusinessId());
        criteria.andEqualTo("businessType", ocUserPermission.getBusinessType());
        // criteria.andEqualTo("content",ocUserPermission.getContent());
        //return ocUserPermissionMapper.queryOcUserPermissionByUniqueKey(ocUserPermission);
        return ocUserPermissionMapper.selectOneByExample(example);
    }

    @Override
    public List<OcUserPermission> queryUserBusinessPermissionByUserId(int userId, int businessId) {
        Example example = new Example(OcUserPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        return ocUserPermissionMapper.selectByExample(example);
    }

    @Override
    public List<OcUserPermission> queryUserBusinessPermissionByBusinessId(int businessType, int businessId) {
        Example example = new Example(OcUserPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", businessType);
        criteria.andEqualTo("businessId", businessId);
        return ocUserPermissionMapper.selectByExample(example);
    }
}
