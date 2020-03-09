package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.generator.jumpserver.PermsAssetpermissionUserGroups;
import com.baiyi.opscloud.mapper.jumpserver.PermsAssetpermissionUserGroupsMapper;
import com.baiyi.opscloud.service.jumpserver.PermsAssetpermissionUserGroupsServcie;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/9 12:12 下午
 * @Version 1.0
 */
@Service
public class PermsAssetpermissionUserGroupsServcieImpl implements PermsAssetpermissionUserGroupsServcie {

    @Resource
    private PermsAssetpermissionUserGroupsMapper permsAssetpermissionUserGroupsMapper;

    @Override
    public PermsAssetpermissionUserGroups queryPermsAssetpermissionUserGroupsByUniqueKey(PermsAssetpermissionUserGroups permsAssetpermissionUserGroups) {
        Example example = new Example(PermsAssetpermissionUserGroups.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetpermissionId", permsAssetpermissionUserGroups.getAssetpermissionId());
        criteria.andEqualTo("usergroupId", permsAssetpermissionUserGroups.getUsergroupId());
        return permsAssetpermissionUserGroupsMapper.selectOneByExample(example);
    }

    @Override
    public void addPermsAssetpermissionUserGroups(PermsAssetpermissionUserGroups permsAssetpermissionUserGroups) {
        permsAssetpermissionUserGroupsMapper.insert(permsAssetpermissionUserGroups);
    }
}
