package com.baiyi.opscloud.service.it.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetName;
import com.baiyi.opscloud.mapper.opscloud.OcItAssetNameMapper;
import com.baiyi.opscloud.service.it.OcItAssetNameService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/20 3:22 下午
 * @Since 1.0
 */

@Service
public class OcItAssetNameServiceImpl implements OcItAssetNameService {

    @Resource
    private OcItAssetNameMapper ocItAssetNameMapper;

    @Override
    public OcItAssetName queryOcItAssetNameById(Integer id) {
        return ocItAssetNameMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcItAssetName(OcItAssetName ocItAssetName) {
        ocItAssetNameMapper.insert(ocItAssetName);
    }

    @Override
    public List<OcItAssetName> queryOcItAssetNameByType(Integer assetTypeId) {
        Example example = new Example(OcItAssetName.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetTypeId", assetTypeId);
        return ocItAssetNameMapper.selectByExample(example);
    }

    @Override
    public OcItAssetName queryOcItAssetNameByName(String assetName) {
        Example example = new Example(OcItAssetName.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetName", assetName);
        return ocItAssetNameMapper.selectOneByExample(example);
    }

    @Override
    public List<OcItAssetName> queryOcItAssetNameAll() {
        return ocItAssetNameMapper.selectAll();
    }
}




