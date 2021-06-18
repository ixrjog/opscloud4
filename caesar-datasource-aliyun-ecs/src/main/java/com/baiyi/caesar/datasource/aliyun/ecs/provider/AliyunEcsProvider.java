package com.baiyi.caesar.datasource.aliyun.ecs.provider;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baiyi.caesar.common.datasource.AliyunDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.AliyunDsConfig;
import com.baiyi.caesar.datasource.factory.DsFactory;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.vo.datasource.DsInstanceVO;
import com.baiyi.caesar.service.datasource.DsConfigService;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/17 4:47 下午
 * @Version 1.0
 */
@Resource
public class AliyunEcsProvider {

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsFactory dsFactory;

    public IAcsClient getAcsClient(DsInstanceVO.Instance dsInstance) {
        DatasourceConfig datasourceConfig = dsConfigService.getById(dsInstance.getConfigId());
        AliyunDsConfig.aliyun aliyun = dsFactory.build(datasourceConfig, AliyunDsInstanceConfig.class).getAliyun();
        IClientProfile profile = DefaultProfile.getProfile(aliyun.getRegionId(), aliyun.getAccount().getAccessKeyId(), aliyun.getAccount().getSecret());
        return new DefaultAcsClient(profile);
    }
}
