package com.baiyi.caesar.service.datasource.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.domain.param.datasource.DsAssetParam;
import com.baiyi.caesar.mapper.caesar.DatasourceInstanceAssetMapper;
import com.baiyi.caesar.service.datasource.DsInstanceAssetService;
import com.baiyi.caesar.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/17 1:41 下午
 * @Version 1.0
 */
@Service
public class DsInstanceAssetServiceImpl implements DsInstanceAssetService {

    @Resource
    private DatasourceInstanceAssetMapper dsInstanceAssetMapper;

    @Override
    public void add(DatasourceInstanceAsset asset) {
        dsInstanceAssetMapper.insert(asset);
    }

    @Override
    public void update(DatasourceInstanceAsset asset) {
        dsInstanceAssetMapper.updateByPrimaryKey(asset);
    }

    @Override
    public DatasourceInstanceAsset getByUniqueKey(DatasourceInstanceAsset asset) {
        Example example = new Example(DatasourceInstanceAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceUuid", asset.getInstanceUuid())
                .andEqualTo("assetId", asset.getAssetId())
                .andEqualTo("assetType", asset.getAssetType())
                .andEqualTo("assetKey", asset.getAssetKey());
        return dsInstanceAssetMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<DatasourceInstanceAsset> queryPageByParam(DsAssetParam.AssetPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(DatasourceInstanceAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceUuid", pageQuery.getInstanceUuid())
                .andEqualTo("assetType", pageQuery.getAssetType());

        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            String likeName = SQLUtil.toLike(pageQuery.getQueryName());
            criteria.andLike("assetId", likeName)
                    .orLike("kind", likeName)
                    .orLike("assetKey", likeName)
                    .orLike("assetKey2", likeName)
                    .orLike("description", likeName);
        }
        if (pageQuery.getIsActive() != null) {
            criteria.andEqualTo("isActive", pageQuery.getIsActive());
        }
        example.setOrderByClause("create_time");
        List<DatasourceInstanceAsset> data = dsInstanceAssetMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }
}
