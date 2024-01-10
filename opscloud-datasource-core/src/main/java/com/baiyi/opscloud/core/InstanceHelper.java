package com.baiyi.opscloud.core;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.datasource.DsInstanceParam;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.tag.SimpleTagService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/2 3:31 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class InstanceHelper {

    protected static final int DATASOURCE_INSTANCE_TYPE = BusinessTypeEnum.DATASOURCE_INSTANCE.getType();

    private final DsInstanceService dsInstanceService;

    private final SimpleTagService simpleTagService;

    public DatasourceInstance getInstanceByUuid(String uuid) {
        return dsInstanceService.getByUuid(uuid);
    }

    public DatasourceInstance getInstanceByName(String name) {
        return dsInstanceService.getByInstanceName(name);
    }

    /**
     * 查询实例
     *
     * @param filterInstanceTypes 实例类型
     * @param tag                 符合的标签
     * @return
     */
    public List<DatasourceInstance> listInstance(DsTypeEnum[] filterInstanceTypes, String tag) {
        List<DatasourceInstance> instances = Lists.newArrayList();
        for (DsTypeEnum typeEnum : filterInstanceTypes) {
            DsInstanceParam.DsInstanceQuery query = DsInstanceParam.DsInstanceQuery.builder()
                    .instanceType(typeEnum.getName())
                    .build();
            // 过滤掉没有标签的实例
            instances.addAll(dsInstanceService.queryByParam(query).stream()
                    .filter(e -> simpleTagService.hasBusinessTag(tag, DATASOURCE_INSTANCE_TYPE, e.getId()))
                    .toList());
        }
        return instances;
    }

    /**
     * 实例是否有标签
     *
     * @param uuid
     * @param tag
     * @return
     */
    public boolean hasTagInInstance(String uuid, String tag) {
        DatasourceInstance instance = getInstanceByUuid(uuid);
        if (instance == null) {
            return false;
        }
        return simpleTagService.hasBusinessTag(tag, DATASOURCE_INSTANCE_TYPE, instance.getId());
    }

}