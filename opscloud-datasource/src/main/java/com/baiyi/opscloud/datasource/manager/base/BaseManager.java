package com.baiyi.opscloud.datasource.manager.base;

import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.datasource.DsInstanceParam;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.tag.BaseTagService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/8/11 11:18 上午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseManager {

    @Resource
    private BaseTagService baseTagService;

    @Resource
    private DsInstanceService dsInstanceService;

    protected static final int DsInstanceBusinessType = BusinessTypeEnum.DATASOURCE_INSTANCE.getType();

    abstract protected DsTypeEnum[] getFilterInstanceTypes();

    abstract protected String getTag();

    /**
     * 查询实例
     *
     * @return
     */
    protected List<DatasourceInstance> listInstance() {
        List<DatasourceInstance> instances = Lists.newArrayList();
        for (DsTypeEnum typeEnum : getFilterInstanceTypes()) {
            DsInstanceParam.DsInstanceQuery query = DsInstanceParam.DsInstanceQuery.builder()
                    .instanceType(typeEnum.getName())
                    .build();
            // 过滤掉没有标签的实例
            instances.addAll(
                    dsInstanceService.queryByParam(query).stream().filter(e ->
                            baseTagService.hasBusinessTag(getTag(), DsInstanceBusinessType, e.getId())
                    ).collect(Collectors.toList())
            );
        }
        return instances;
    }

}
