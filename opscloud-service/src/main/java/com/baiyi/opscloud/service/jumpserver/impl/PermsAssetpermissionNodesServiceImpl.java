package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.generator.jumpserver.PermsAssetpermissionNodes;
import com.baiyi.opscloud.mapper.jumpserver.PermsAssetpermissionNodesMapper;
import com.baiyi.opscloud.service.jumpserver.PermsAssetpermissionNodesService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/9 12:24 下午
 * @Version 1.0
 */
@Service
public class PermsAssetpermissionNodesServiceImpl implements PermsAssetpermissionNodesService {

    @Resource
    private PermsAssetpermissionNodesMapper permsAssetpermissionNodesMapper;

    @Override
    public PermsAssetpermissionNodes queryPermsAssetpermissionNodesByUniqueKey(PermsAssetpermissionNodes permsAssetpermissionNodes) {
        Example example = new Example(PermsAssetpermissionNodes.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetpermissionId", permsAssetpermissionNodes.getAssetpermissionId());
        criteria.andEqualTo("nodeId", permsAssetpermissionNodes.getNodeId());
        return permsAssetpermissionNodesMapper.selectOneByExample(example);
    }

    @Override
    public void addPermsAssetpermissionNodes(PermsAssetpermissionNodes permsAssetpermissionNodes) {
        permsAssetpermissionNodesMapper.insert(permsAssetpermissionNodes);
    }
}
