package com.baiyi.opscloud.datasource.dingtalk.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.SingleTaskConstants;
import com.baiyi.opscloud.common.datasource.DingtalkConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.exception.DatasourceProviderException;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkDepartment;
import com.baiyi.opscloud.datasource.dingtalk.param.DingtalkDepartmentParam;
import com.baiyi.opscloud.datasource.dingtalk.provider.base.AbstractDingtalkAssetProvider;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/11/30 1:26 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class DingtalkDepartmentProvider extends AbstractDingtalkAssetProvider<DingtalkDepartment.Department> {

    @Resource
    private DingtalkDepartmentProvider dingtalkDepartmentProvider;

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.DINGTALK_DEPARTMENT.name();
    }

    private DingtalkConfig.Dingtalk buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, DingtalkConfig.class).getDingtalk();
    }

    @Override
    protected List<DingtalkDepartment.Department> listEntities(DsInstanceContext dsInstanceContext) {
        DingtalkConfig.Dingtalk dingtalk = buildConfig(dsInstanceContext.getDsConfig());
        try {
            Set<Long> deptIdSet = queryDeptSubIds(dsInstanceContext);
            List<DingtalkDepartment.Department> entities = Lists.newArrayList();
            deptIdSet.forEach(deptId -> {
                DingtalkDepartmentParam.GetDepartment getDepartment = DingtalkDepartmentParam.GetDepartment.builder()
                        .deptId(deptId)
                        .build();
                DingtalkDepartment.GetDepartmentResponse getDepartmentResponse = dingtalkDepartmentDriver.get(dingtalk, getDepartment);
                if (getDepartmentResponse.getResult() != null) {
                    entities.add(getDepartmentResponse.getResult());
                }
            });
            return entities;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DatasourceProviderException(e.getMessage());
        }
    }

    @Override
    @SingleTask(name = SingleTaskConstants.PULL_DINGTALK_DEPARTMENT, lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfName()
                .compareOfActive()
                .build();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(dingtalkDepartmentProvider);
    }

}