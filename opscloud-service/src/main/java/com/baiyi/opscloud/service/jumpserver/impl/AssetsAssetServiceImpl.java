package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.jumpserver.AssetsAsset;
import com.baiyi.opscloud.domain.param.jumpserver.asset.AssetsAssetPageParam;
import com.baiyi.opscloud.mapper.jumpserver.AssetsAssetMapper;
import com.baiyi.opscloud.service.jumpserver.AssetsAssetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/9 1:29 下午
 * @Version 1.0
 */
@Service
public class AssetsAssetServiceImpl implements AssetsAssetService {

    @Resource
    private AssetsAssetMapper assetsAssetMapper;

    @Override
    public AssetsAsset queryAssetsAssetByIp(String ip) {
        Example example = new Example(AssetsAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ip", ip);
        return assetsAssetMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<AssetsAsset> fuzzyQueryAssetsAssetPage(AssetsAssetPageParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<AssetsAsset> assetsAssetList = assetsAssetMapper.fuzzyQueryAssetsAssetPage(pageQuery);
        return new DataTable<>(assetsAssetList, page.getTotal());
    }

    @Override
    public AssetsAsset queryAssetsAssetByHostname(String hostname) {
        Example example = new Example(AssetsAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("hostname", hostname);
        PageHelper.startPage(1, 1);
        return assetsAssetMapper.selectOneByExample(example);
    }

    @Override
    public List<AssetsAsset> queryAll() {
        return assetsAssetMapper.selectAll();
    }

    @Override
    public void updateAssetsAsset(AssetsAsset assetsAsset) {
        assetsAssetMapper.updateByPrimaryKey(assetsAsset);
    }

    @Override
    public void addAssetsAsset(AssetsAsset assetsAsset) {
        assetsAssetMapper.insert(assetsAsset);
    }

    @Override
    public void deleteAssetsAssetById(String id) {
        assetsAssetMapper.deleteByPrimaryKey(id);
    }
}
