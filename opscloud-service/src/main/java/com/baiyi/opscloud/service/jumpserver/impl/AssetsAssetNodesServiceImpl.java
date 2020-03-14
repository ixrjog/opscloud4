package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.generator.jumpserver.AssetsAssetNodes;
import com.baiyi.opscloud.mapper.jumpserver.AssetsAssetNodesMapper;
import com.baiyi.opscloud.service.jumpserver.AssetsAssetNodesService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/9 5:14 下午
 * @Version 1.0
 */
@Service
public class AssetsAssetNodesServiceImpl implements AssetsAssetNodesService {

    @Resource
    private AssetsAssetNodesMapper assetsAssetNodesMapper;

    @Override
    public AssetsAssetNodes queryAssetsAssetNodesByUniqueKey(AssetsAssetNodes assetsAssetNodes) {
        Example example = new Example(AssetsAssetNodes.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetId", assetsAssetNodes.getAssetId());
        criteria.andEqualTo("nodeId", assetsAssetNodes.getNodeId());
        return assetsAssetNodesMapper.selectOneByExample(example);
    }

    @Override
    public AssetsAssetNodes queryAssetsAssetNodesByAssetId(String assetId) {
        Example example = new Example(AssetsAssetNodes.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetId", assetId);
        return assetsAssetNodesMapper.selectOneByExample(example);
    }

    @Override
    public void addAssetsAssetNodes(AssetsAssetNodes assetsAssetNodes) {
        assetsAssetNodesMapper.insert(assetsAssetNodes);
    }

    @Override
    public void delAssetsAssetNodes(int id) {
        assetsAssetNodesMapper.deleteByPrimaryKey(id);
    }

}
