package com.baiyi.opscloud.service.datasource.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetSubscription;
import com.baiyi.opscloud.domain.param.datasource.DsAssetSubscriptionParam;
import com.baiyi.opscloud.mapper.opscloud.DatasourceInstanceAssetSubscriptionMapper;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetSubscriptionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/27 2:50 下午
 * @Version 1.0
 */
@Service
public class DsInstanceAssetSubscriptionServiceImpl implements DsInstanceAssetSubscriptionService {

    @Resource
    private DatasourceInstanceAssetSubscriptionMapper dsInstanceAssetSubscriptionMapper;

    @Override
    public void add(DatasourceInstanceAssetSubscription datasourceInstanceAssetSubscription) {
        dsInstanceAssetSubscriptionMapper.insert(datasourceInstanceAssetSubscription);
    }

    @Override
    public void update(DatasourceInstanceAssetSubscription datasourceInstanceAssetSubscription) {
        dsInstanceAssetSubscriptionMapper.updateByPrimaryKey(datasourceInstanceAssetSubscription);
    }

    @Override
    public void deleteById(int id) {
        dsInstanceAssetSubscriptionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public DatasourceInstanceAssetSubscription getById(int id) {
        return dsInstanceAssetSubscriptionMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<DatasourceInstanceAssetSubscription> queryByAssetId(int assetId) {
        Example example = new Example(DatasourceInstanceAssetSubscription.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("datasourceInstanceAssetId", assetId);
        return dsInstanceAssetSubscriptionMapper.selectByExample(example);
    }

    @Override
    public DataTable<DatasourceInstanceAssetSubscription> queryPageByParam(DsAssetSubscriptionParam.AssetSubscriptionPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(DatasourceInstanceAssetSubscription.class);
        Example.Criteria criteria = example.createCriteria();
        if (pageQuery.getIsActive() != null) {
            criteria.andEqualTo("isActive", pageQuery.getIsActive());
        }
//        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
//            Example.Criteria criteria2 = example.createCriteria();
//            String likeName = SQLUtil.toLike(pageQuery.getQueryName());
//            criteria2.orLike("input", likeName)
//                    .orLike("inputFormatted", likeName);
//            example.and(criteria2);
//        }
        List<DatasourceInstanceAssetSubscription> data = dsInstanceAssetSubscriptionMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }


//    public List<DatasourceInstanceAssetSubscription> queryBy(int id){
//        Example example = new Example(DatasourceInstanceAssetSubscription.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("instanceUuid", asset.getInstanceUuid())
//                .andEqualTo("assetType", asset.getAssetType())
//                .andEqualTo("name", asset.getName())
//                .andEqualTo("isActive", true);
//        if (!StringUtils.isEmpty(asset.getInstanceUuid()))
//            criteria.andEqualTo("instanceUuid", asset.getInstanceUuid());
//        if (!StringUtils.isEmpty(asset.getName()))
//            criteria.andEqualTo("name", asset.getName());
//        if (!StringUtils.isEmpty(asset.getAssetKey()))
//            criteria.andLike("assetKey", SQLUtil.toLike(asset.getAssetKey()));
//        if (!StringUtils.isEmpty(asset.getAssetKey2()))
//            criteria.andEqualTo("assetKey2", asset.getAssetKey2());
//        example.setOrderByClause("create_time");
//        return dsInstanceAssetMapper.selectByExample(example);
//
//    }

}
