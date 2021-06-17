package com.baiyi.caesar.service.datasource.impl;

import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.mapper.caesar.DatasourceInstanceAssetMapper;
import com.baiyi.caesar.service.datasource.DsInstanceAssetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
