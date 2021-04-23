package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.jumpserver.AssetsNode;
import com.baiyi.opscloud.domain.param.jumpserver.assetsNode.AssetsNodePageParam;
import com.baiyi.opscloud.mapper.jumpserver.AssetsNodeMapper;
import com.baiyi.opscloud.service.jumpserver.AssetsNodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

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
    public DataTable<AssetsNode> queryAssetsNodePage(AssetsNodePageParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<AssetsNode> assetsNodeList = assetsNodeMapper.queryAssetsAssetPage(pageQuery);
        return new DataTable<>(assetsNodeList, page.getTotal());
    }

    @Override
    public AssetsNode queryAssetsNodeByValue(String value) {
        Example example = new Example(AssetsNode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("value", value);
        //criteria.andEqualTo("orgId", "");
        List<AssetsNode> list = assetsNodeMapper.selectByExample(example);
        //return assetsNodeMapper.selectOneByExample(example);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
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

    @Override
    public AssetsNode queryAssetsNodeRoot() {
        return assetsNodeMapper.queryAssetsNodeRoot();
    }
}
