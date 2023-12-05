package com.baiyi.opscloud.application;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.ApplicationResource;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.user.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/12/5 16:11
 * @Version 1.0
 */
public class ApplicationTest2 extends BaseUnit {

    @Resource
    private ApplicationService applicationService;

    @Resource
    private UserService userService;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private ApplicationResourceService applicationResourceService;

    @Test
    void test() {
        DatasourceInstanceAsset queryAsset = DatasourceInstanceAsset.builder()
                .instanceUuid("81bfb61730e14b069541331676ccfc3e")
                .assetType("KUBERNETES_DEPLOYMENT")
                .build();
        List<DatasourceInstanceAsset> assets = dsInstanceAssetService.queryAssetByAssetParam(queryAsset);

        Map<String, DatasourceInstanceAsset> assetMap = assets.stream().collect(Collectors.toMap(DatasourceInstanceAsset::getName, a -> a, (k1, k2) -> k1));

        ApplicationParam.ApplicationPageQuery pageQuery = ApplicationParam.ApplicationPageQuery.builder()
                .page(1)
                .length(500)
                .build();

        DataTable<Application> table = applicationService.queryPageByParam(pageQuery);

        for (Application application : table.getData()) {
            if (application.getName().endsWith("-h5")) {
                continue;
            }

            String deploymentName = application.getName();

            if (assetMap.containsKey(deploymentName)) {

                DatasourceInstanceAsset asset = assetMap.get(deploymentName);

                ApplicationResource applicationResource = ApplicationResource.builder()
                        .applicationId(application.getId())
                        .name(asset.getAssetId())
                        .virtualResource(false)
                        .resourceType("KUBERNETES_DEPLOYMENT")
                        .businessId(asset.getId())
                        .businessType(5)
                        .comment(asset.getAssetId())
                        .build();
                try {
                    applicationResourceService.add(applicationResource);
                    print("新增成功：" + application.getName());
                } catch (Exception e) {
                    print("新增失败：" + application.getName());
                }

            }


        }
    }

}
