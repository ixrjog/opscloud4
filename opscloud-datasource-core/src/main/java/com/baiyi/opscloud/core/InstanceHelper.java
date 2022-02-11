package com.baiyi.opscloud.core;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.datasource.DsInstanceParam;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.tag.SimpleTagService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 查询实例
     *
     * @return
     */
    public List<DatasourceInstance> listInstance(DsTypeEnum[] filterInstanceTypes, String tag) {
        List<DatasourceInstance> instances = Lists.newArrayList();
        for (DsTypeEnum typeEnum : filterInstanceTypes) {
            DsInstanceParam.DsInstanceQuery query =
                    DsInstanceParam.DsInstanceQuery.builder().instanceType(typeEnum.getName()).build();
            // 过滤掉没有标签的实例
            instances.addAll(dsInstanceService.queryByParam(query).stream().filter(e -> simpleTagService.hasBusinessTag(tag, DATASOURCE_INSTANCE_TYPE, e.getId())).collect(Collectors.toList()));
        }
        return instances;
    }

}
