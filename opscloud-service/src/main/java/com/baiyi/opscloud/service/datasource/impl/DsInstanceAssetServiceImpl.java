package com.baiyi.opscloud.service.datasource.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.mapper.opscloud.DatasourceInstanceAssetMapper;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.util.SQLUtil;
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
    public void deleteById(Integer id) {
        dsInstanceAssetMapper.deleteByPrimaryKey(id);
    }

    @Override
    public DatasourceInstanceAsset getById(Integer id) {
        return dsInstanceAssetMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(DatasourceInstanceAsset asset) {
        dsInstanceAssetMapper.insert(asset);
    }

    @Override
    public void update(DatasourceInstanceAsset asset) {
        dsInstanceAssetMapper.updateByPrimaryKey(asset);
    }

    @Override
    public List<DatasourceInstanceAsset> listByInstanceAssetType(String instanceUuid, String assetType) {
        Example example = new Example(DatasourceInstanceAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceUuid", instanceUuid)
                .andEqualTo("assetType", assetType);
        return dsInstanceAssetMapper.selectByExample(example);
    }

    @Override
    public List<DatasourceInstanceAsset> listByUuidParentIdAssetAndType(String instanceUuid, Integer parentId, String assetType) {
        Example example = new Example(DatasourceInstanceAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceUuid", instanceUuid)
                .andEqualTo("parentId", parentId)
                .andEqualTo("assetType", assetType);
        return dsInstanceAssetMapper.selectByExample(example);
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
        if (pageQuery.getIsActive() != null) {
            criteria.andEqualTo("isActive", pageQuery.getIsActive());
        }
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            Example.Criteria criteria2 = example.createCriteria();
            String likeName = SQLUtil.toLike(pageQuery.getQueryName());
            criteria2.orLike("assetId", likeName)
                    .orLike("name", likeName)
                    .orLike("kind", likeName)
                    .orLike("assetKey", likeName)
                    .orLike("assetKey2", likeName)
                    .orLike("description", likeName);
            example.and(criteria2);
        }
        example.setOrderByClause("create_time");
        List<DatasourceInstanceAsset> data = dsInstanceAssetMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public DataTable<DatasourceInstanceAsset> queryPageByParam(DsAssetParam.UserPermissionAssetPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<DatasourceInstanceAsset> data = dsInstanceAssetMapper.queryUserPermissionAssetByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<DatasourceInstanceAsset> queryAssetByAssetParam(DatasourceInstanceAsset asset) {
        Example example = new Example(DatasourceInstanceAsset.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(asset.getInstanceUuid()))
            criteria.andEqualTo("instanceUuid", asset.getInstanceUuid());
        if (!StringUtils.isEmpty(asset.getAssetType()))
            criteria.andEqualTo("assetType", asset.getAssetType());
        if (asset.getIsActive() != null)
            criteria.andEqualTo("isActive", asset.getIsActive());
        if (!StringUtils.isEmpty(asset.getName()))
            criteria.andEqualTo("name", asset.getName());
        if (!StringUtils.isEmpty(asset.getAssetKey()))
            criteria.andLike("assetKey", SQLUtil.toLike(asset.getAssetKey()));
        if (!StringUtils.isEmpty(asset.getAssetKey2()))
            criteria.andEqualTo("assetKey2", asset.getAssetKey2());
        example.setOrderByClause("create_time");
        return dsInstanceAssetMapper.selectByExample(example);
    }

    @Override
    public List<String> queryInstanceAssetTypes(String instanceUuid) {
        return dsInstanceAssetMapper.queryInstanceAssetTypes(instanceUuid);
    }

    @Override
    public int countByInstanceAssetType(String instanceUuid, String assetType) {
        Example example = new Example(DatasourceInstanceAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceUuid", instanceUuid)
                .andEqualTo("assetType", assetType);
        return dsInstanceAssetMapper.selectCountByExample(example);
    }

    @Override
    public List<DatasourceInstanceAsset> listByParentId(Integer parentId) {
        Example example = new Example(DatasourceInstanceAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", parentId);
        return dsInstanceAssetMapper.selectByExample(example);
    }
}
