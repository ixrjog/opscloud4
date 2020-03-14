package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.generator.jumpserver.PermsAssetpermission;
import com.baiyi.opscloud.mapper.jumpserver.PermsAssetpermissionMapper;
import com.baiyi.opscloud.service.jumpserver.PermsAssetpermissionService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/9 11:18 上午
 * @Version 1.0
 */
@Service
public class PermsAssetpermissionServiceImpl implements PermsAssetpermissionService {

    @Resource
    private PermsAssetpermissionMapper permsAssetpermissionMapper;

    @Override
    public PermsAssetpermission queryPermsAssetpermissionByName(String name) {
        Example example = new Example(PermsAssetpermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        List<PermsAssetpermission> list = permsAssetpermissionMapper.selectByExample(example);
        if (list != null && list.size()> 0)
            return list.get(0);
        return null;
    }

    @Override
    public void addPermsAssetpermission(PermsAssetpermission permsAssetpermission) {
        permsAssetpermissionMapper.insert(permsAssetpermission);
    }


}
