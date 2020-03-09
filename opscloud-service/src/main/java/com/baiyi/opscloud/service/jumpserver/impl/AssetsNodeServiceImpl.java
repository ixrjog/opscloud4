package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.generator.jumpserver.AssetsNode;
import com.baiyi.opscloud.mapper.jumpserver.AssetsNodeMapper;
import com.baiyi.opscloud.service.jumpserver.AssetsNodeService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/9 9:26 上午
 * @Version 1.0
 */
@Service
public class AssetsNodeServiceImpl implements AssetsNodeService {

    @Resource
    private AssetsNodeMapper assetsNodeMapper;

    @Override
    public AssetsNode queryAssetsNodeByValue(String value) {
        Example example = new Example(AssetsNode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("value", value);
        return assetsNodeMapper.selectOneByExample(example);
    }

    @Override
    public AssetsNode queryAssetsNodeLastOne() {
        return assetsNodeMapper.queryAssetsNodeLastOne();
    }

    @Override
    public AssetsNode queryAssetsNodeByKey(String key) {
        Example example = new Example(AssetsNode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("key", key);
        return assetsNodeMapper.selectOneByExample(example);
    }

    @Override
    public void addAssetsNode(AssetsNode assetsNode) {
        assetsNodeMapper.insert(assetsNode);
    }
}
