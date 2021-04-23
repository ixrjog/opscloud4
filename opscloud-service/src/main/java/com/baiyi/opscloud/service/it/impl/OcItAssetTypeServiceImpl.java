package com.baiyi.opscloud.service.it.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetType;
import com.baiyi.opscloud.mapper.opscloud.OcItAssetTypeMapper;
import com.baiyi.opscloud.service.it.OcItAssetTypeService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/20 3:23 下午
 * @Since 1.0
 */

@Service
public class OcItAssetTypeServiceImpl implements OcItAssetTypeService {

    @Resource
    private OcItAssetTypeMapper ocItAssetTypeMapper;


    @Override
    public OcItAssetType queryOcItAssetTypeById(Integer id) {
        return ocItAssetTypeMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcItAssetType(OcItAssetType ocItAssetType) {
        ocItAssetTypeMapper.insert(ocItAssetType);
    }

    @Override
    public List<OcItAssetType> queryOcItAssetTypeAll() {
        return ocItAssetTypeMapper.selectAll();
    }

    @Override
    public OcItAssetType queryOcItAssetTypeByType(String assetType) {
        Example example = new Example(OcItAssetType.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetType", assetType);
        return ocItAssetTypeMapper.selectOneByExample(example);
    }
}
