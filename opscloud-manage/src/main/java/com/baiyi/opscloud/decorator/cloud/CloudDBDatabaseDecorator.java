package com.baiyi.opscloud.decorator.cloud;

import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDb;
import com.baiyi.opscloud.domain.generator.opscloud.OcEnv;
import com.baiyi.opscloud.domain.param.tag.TagParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudDBDatabaseVO;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.facade.TagFacade;
import com.baiyi.opscloud.service.cloud.OcCloudDBService;
import com.baiyi.opscloud.service.env.OcEnvService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/2 12:54 下午
 * @Version 1.0
 */
@Component
public class CloudDBDatabaseDecorator {

    @Resource
    private OcEnvService ocEnvService;

    @Resource
    private OcCloudDBService ocCloudDBService;

    @Resource
    private TagFacade tagFacade;

    public CloudDBDatabaseVO.CloudDBDatabase decorator(CloudDBDatabaseVO.CloudDBDatabase cloudDBDatabase, Integer extend) {

        // 装饰 环境信息
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(cloudDBDatabase.getEnvType());
        if (ocEnv != null) {
            EnvVO.Env env = BeanCopierUtils.copyProperties(ocEnv, EnvVO.Env.class);
            cloudDBDatabase.setEnv(env);
        }

        // 装饰 标签
        TagParam.BusinessQuery businessQuery = new TagParam.BusinessQuery();
        businessQuery.setBusinessType(BusinessType.CLOUD_DATABASE.getType());
        businessQuery.setBusinessId(cloudDBDatabase.getId());
        cloudDBDatabase.setTags(tagFacade.queryBusinessTag(businessQuery));

        if (extend != null && extend == 1) {
            OcCloudDb ocCloudDb = ocCloudDBService.queryOcCloudDbById(cloudDBDatabase.getCloudDbId());
            cloudDBDatabase.setCloudDbType(ocCloudDb.getCloudDbType());
        }
        return cloudDBDatabase;
    }
}
