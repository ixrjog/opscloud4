package com.baiyi.caesar.service.datasource.impl;

import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAssetProperty;
import com.baiyi.caesar.mapper.caesar.DatasourceInstanceAssetPropertyMapper;
import com.baiyi.caesar.service.datasource.DsInstanceAssetPropertyService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/19 9:23 上午
 * @Version 1.0
 */
@Service
public class DsInstanceAssetPropertyServiceImpl implements DsInstanceAssetPropertyService {

    @Resource
    private DatasourceInstanceAssetPropertyMapper dsInstanceAssetPropertyMapper;

    @Override
    public void add(DatasourceInstanceAssetProperty property) {
        dsInstanceAssetPropertyMapper.insert(property);
    }

    @Override
    public void deleteById(Integer id) {
        dsInstanceAssetPropertyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<DatasourceInstanceAssetProperty> queryByAssetId(Integer assetId) {
        Example example = new Example(DatasourceInstanceAssetProperty.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("datasourceInstanceAssetId", assetId);
        return dsInstanceAssetPropertyMapper.selectByExample(example);
    }

    @Override
    public void saveAssetProperties(int assetId, Map<String, String> properties) {
        List<DatasourceInstanceAssetProperty> assetPropertyList = queryByAssetId(assetId);
        properties.forEach((k, v) -> {
            if (isEmptyProperty(assetPropertyList, k, v)) {
                DatasourceInstanceAssetProperty property = DatasourceInstanceAssetProperty.builder()
                        .datasourceInstanceAssetId(assetId)
                        .name(k)
                        .value(v)
                        .build();
                dsInstanceAssetPropertyMapper.insert(property);
            }
        });
        assetPropertyList.forEach(e -> deleteById(e.getId()));
    }

    private boolean isEmptyProperty(List<DatasourceInstanceAssetProperty> assetPropertyList, String name, String value) {
        Iterator<DatasourceInstanceAssetProperty> iter = assetPropertyList.iterator();
        while (iter.hasNext()) {
            DatasourceInstanceAssetProperty property = iter.next();
            if (property.getName().equals(name)) {
                if (property.getValue().equals(value)) {
                    iter.remove();
                } else {
                    property.setValue(value);
                    dsInstanceAssetPropertyMapper.updateByPrimaryKey(property);
                }
                return false;
            }
        }
        return true;
    }
}
