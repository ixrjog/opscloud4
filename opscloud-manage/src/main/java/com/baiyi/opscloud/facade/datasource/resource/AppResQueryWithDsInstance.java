package com.baiyi.opscloud.facade.datasource.resource;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.annotation.ApplicationResType;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.constants.ApplicationResTypeEnum;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.application.ApplicationResourceParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.factory.resource.base.AbstractAppResQuery;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/10/31 14:40
 * @Version 1.0
 */
@ApplicationResType(ApplicationResTypeEnum.DATASOURCE_INSTANCE)
@BusinessType(BusinessTypeEnum.DATASOURCE_INSTANCE)
@RequiredArgsConstructor
@Component
public class AppResQueryWithDsInstance extends AbstractAppResQuery {

    private final DsInstanceService dsInstanceService;

    protected static ThreadLocal<ApplicationResourceParam.ResourcePageQuery> resourceQuery = new ThreadLocal<>();

    @Override
    public DataTable<ApplicationResourceVO.Resource> queryResourcePage(ApplicationResourceParam.ResourcePageQuery pageQuery) {
        resourceQuery.set(pageQuery);
        List<DatasourceInstance> instances = dsInstanceService.listByInstanceType(DsTypeEnum.JENKINS.getName());
        return new DataTable<>(instances.stream().map(this::toResource
        ).collect(Collectors.toList()), instances.size());
    }

    protected ApplicationResourceVO.Resource toResource(DatasourceInstance instance) {
        ApplicationResourceParam.ResourcePageQuery pageQuery = resourceQuery.get();
        return ApplicationResourceVO.Resource.builder()
                // 选择项名称
                .name(instance.getInstanceName())
                // 选择项说明
                .comment(instance.getComment())
                .applicationId(pageQuery.getApplicationId())
                .businessId(instance.getId())
                .businessType(BusinessTypeEnum.DATASOURCE_INSTANCE.getType())
                .resourceType(pageQuery.getAppResType())
                .build();
    }

}
