package com.baiyi.opscloud.datasource.kubernetes;

import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetProperty;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import com.google.common.base.Joiner;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/9/29 19:47
 * @Version 1.0
 */
public class AppTest extends BaseKubernetesTest {

    @Resource
    private ApplicationService applicationService;

    @Resource
    private BusinessTagService businessTagService;

    @Resource
    private TagService tagService;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    @Test
    void aTest() {
        List<Application> applications = applicationService.queryAll();

        for (Application application : applications) {
            BaseBusiness.IBusiness query = SimpleBusiness.builder()
                    .businessType(BusinessTypeEnum.APPLICATION.getType())
                    .businessId(application.getId())
                    .build();


            DatasourceInstanceAsset queryAsset = DatasourceInstanceAsset.builder()
                    .instanceUuid("767faeaff68c4fdb9f9a847ed7ed0689")
                    .assetType(DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name())
                    .assetId("prod:" + application.getName())
                    .assetKey(application.getName())
                    .build();
            DatasourceInstanceAsset deployment = dsInstanceAssetService.getByUniqueKey(queryAsset);

            DatasourceInstanceAssetProperty property = null;

            String replicas = "-";
            if (deployment != null) {
                List<DatasourceInstanceAssetProperty> properties = dsInstanceAssetPropertyService.queryByAssetId(deployment.getId());
                Optional<DatasourceInstanceAssetProperty> optionalP = properties.stream().filter(e -> e.getName().equals("replicas")).findFirst();
                if(optionalP.isPresent())
                    replicas  = optionalP.get().getValue();
            }


            List<String> bizTags = businessTagService.queryByBusiness(query).stream()
                    .map(e -> tagService.getById(e.getTagId()).getTagKey()).collect(Collectors.toList());
            print(Joiner.on("\t").join(application.getName(), Joiner.on(",").join(bizTags),replicas));

        }

    }

}
