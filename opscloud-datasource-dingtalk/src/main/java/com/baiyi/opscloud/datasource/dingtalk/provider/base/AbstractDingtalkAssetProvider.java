package com.baiyi.opscloud.datasource.dingtalk.provider.base;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.DingtalkConfig;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetBusinessRelationProvider;
import com.baiyi.opscloud.datasource.dingtalk.driver.DingtalkDepartmentDriver;
import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkDepartment;
import com.baiyi.opscloud.datasource.dingtalk.param.DingtalkDepartmentParam;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import java.util.Optional;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/11/30 2:20 下午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractDingtalkAssetProvider<T> extends AbstractAssetBusinessRelationProvider<T> {

    @Resource
    protected DingtalkDepartmentDriver dingtalkDepartmentDriver;

    private static final long DEPT_ROOT_ID = 1L;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.DINGTALK_APP.name();
    }

    private DingtalkConfig.Dingtalk buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, DingtalkConfig.class).getDingtalk();
    }

    protected Set<Long> queryDeptSubIds(DsInstanceContext dsInstanceContext) {
        DingtalkConfig.Dingtalk dingtalk = buildConfig(dsInstanceContext.getDsConfig());
        // 遍历参数
        Set<Long> queryDeptIdSet = Optional.ofNullable(dingtalk)
                .map(DingtalkConfig.Dingtalk::getApp)
                .map(DingtalkConfig.App::getDepartment)
                .map(DingtalkConfig.Department::getDeptIds)
                .orElse(Sets.newHashSet(DEPT_ROOT_ID));
        // 结果集
        Set<Long> subIdSet = Sets.newHashSet(queryDeptIdSet);
        while (!queryDeptIdSet.isEmpty()) {
            final Long deptId = queryDeptIdSet.iterator().next();
            DingtalkDepartmentParam.ListSubDepartmentId listSubDepartmentId = DingtalkDepartmentParam.ListSubDepartmentId.builder()
                    .deptId(deptId)
                    .build();
            queryDeptIdSet.remove(deptId);
            DingtalkDepartment.DepartmentSubIdResponse departmentSubIdResponse = dingtalkDepartmentDriver.listSubId(dingtalk, listSubDepartmentId);
            if (!CollectionUtils.isEmpty(departmentSubIdResponse.getResult().getDeptIdList())) {
                subIdSet.addAll(departmentSubIdResponse.getResult().getDeptIdList());
                queryDeptIdSet.addAll(departmentSubIdResponse.getResult().getDeptIdList());
            }
        }
        log.info("子部门ID列表: size={}", subIdSet.size());
        return subIdSet;
    }

}