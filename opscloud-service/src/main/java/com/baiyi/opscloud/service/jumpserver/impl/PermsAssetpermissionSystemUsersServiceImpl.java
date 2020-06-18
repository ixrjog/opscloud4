package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.generator.jumpserver.PermsAssetpermissionSystemUsers;
import com.baiyi.opscloud.mapper.jumpserver.PermsAssetpermissionSystemUsersMapper;
import com.baiyi.opscloud.service.jumpserver.PermsAssetpermissionSystemUsersService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/9 12:03 下午
 * @Version 1.0
 */
@Service
public class PermsAssetpermissionSystemUsersServiceImpl implements PermsAssetpermissionSystemUsersService {

    @Resource
    private PermsAssetpermissionSystemUsersMapper permsAssetpermissionSystemUsersMapper;

    @Override
    public PermsAssetpermissionSystemUsers queryPermsAssetpermissionSystemUsersByUniqueKey(PermsAssetpermissionSystemUsers permsAssetpermissionSystemUsers) {
        Example example = new Example(PermsAssetpermissionSystemUsers.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetpermissionId", permsAssetpermissionSystemUsers.getAssetpermissionId());
        criteria.andEqualTo("systemuserId", permsAssetpermissionSystemUsers.getSystemuserId());
        return permsAssetpermissionSystemUsersMapper.selectOneByExample(example);
    }

    @Override
    public void addPermsAssetpermissionSystemUsers(PermsAssetpermissionSystemUsers permsAssetpermissionSystemUsers) {
        permsAssetpermissionSystemUsersMapper.insert(permsAssetpermissionSystemUsers);
    }
}
