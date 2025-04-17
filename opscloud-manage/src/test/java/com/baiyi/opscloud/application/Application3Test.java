package com.baiyi.opscloud.application;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.application.ApplicationResourceParam;
import com.baiyi.opscloud.facade.application.ApplicationFacade;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/28 10:21
 * &#064;Version 1.0
 */
@Slf4j
public class Application3Test extends BaseUnit {

    @Resource
    private ApplicationService applicationService;
    @Resource
    private DsInstanceAssetService dsInstanceAssetService;
    @Resource
    private ApplicationFacade applicationFacade;

    @Test
    void test() {
        List<Application> applicationList = applicationService.queryAll();
        for (Application application : applicationList) {
            if (application.getName().contains("-h5")) {
                continue;
            }
            System.out.println("appName:" + application.getName());
            List<DatasourceInstanceAsset> assets = dsInstanceAssetService.queryByParam(DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name(), "daily", application.getName());
            // System.out.println(assets);
            for (DatasourceInstanceAsset asset : assets) {
                if (asset.getName().equals(application.getName()) || asset.getName().equals(application.getName() + "-daily")) {
                    bind(application, asset);
                    continue;
                }
                if (asset.getName().replaceAll(application.getName(), "").replaceAll("-daily", "").startsWith("-dc")) {
                    // 多环境
                    System.out.println(asset.getName());
                    bind(application, asset);
                }
            }
        }
    }

    void bind(Application application, DatasourceInstanceAsset asset) {
        try {
            ApplicationResourceParam.Resource resource = ApplicationResourceParam.Resource.builder()
                    .applicationId(application.getId())
                    .businessId(asset.getId())
                    .businessType(BusinessTypeEnum.ASSET.getType())
                    .name(asset.getAssetId())
                    .resourceType(DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name())
                    .comment(asset.getAssetId())
                    .build();
            applicationFacade.bindApplicationResource(resource);
        } catch (Exception e) {
            log.error("重复绑定:" + asset.getAssetId());
        }
    }

}
