package com.baiyi.opscloud.service.datasource.impl;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetProperty;
import com.baiyi.opscloud.mapper.DatasourceInstanceAssetPropertyMapper;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/19 9:23 上午
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DsInstanceAssetPropertyServiceImpl implements DsInstanceAssetPropertyService {

    private final DatasourceInstanceAssetPropertyMapper dsInstanceAssetPropertyMapper;

    @Override
    public void add(DatasourceInstanceAssetProperty property) {
        dsInstanceAssetPropertyMapper.insert(property);
    }

    @Override
    public void deleteById(Integer id) {
        dsInstanceAssetPropertyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public DatasourceInstanceAssetProperty getByUniqueKey(Integer assetId, String name) {
        Example example = new Example(DatasourceInstanceAssetProperty.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("datasourceInstanceAssetId", assetId)
                .andEqualTo("name", name);
        return dsInstanceAssetPropertyMapper.selectOneByExample(example);
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
        List<DatasourceInstanceAssetProperty> assetProperties = queryByAssetId(assetId);
        properties.forEach((k, v) -> {
            DatasourceInstanceAssetProperty assetProperty = DatasourceInstanceAssetProperty.builder()
                    .datasourceInstanceAssetId(assetId)
                    .name(k)
                    .value(v)
                    .build();
            saveProperty(assetProperties, assetProperty);
        });
        assetProperties.forEach(e -> deleteById(e.getId()));
    }

    private void saveProperty(List<DatasourceInstanceAssetProperty> assetProperties, DatasourceInstanceAssetProperty assetProperty) {
        for (DatasourceInstanceAssetProperty property : assetProperties) {
            if (property.getName().equals(assetProperty.getName())) {
                if (!property.getValue().equals(assetProperty.getValue())) {
                    property.setValue(assetProperty.getValue());
                    try {
                        dsInstanceAssetPropertyMapper.updateByPrimaryKey(property);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        log.error(JSONUtil.writeValueAsString(property));
                    }
                }
                assetProperties.remove(property);
                return;
            }
        }
        dsInstanceAssetPropertyMapper.insert(assetProperty);
    }

}