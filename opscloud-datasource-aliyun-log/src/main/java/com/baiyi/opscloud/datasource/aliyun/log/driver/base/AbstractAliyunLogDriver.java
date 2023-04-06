package com.baiyi.opscloud.datasource.aliyun.log.driver.base;

/**
 * @Author baiyi
 * @Date 2021/9/16 4:51 下午
 * @Version 1.0
 */
public abstract class AbstractAliyunLogDriver {

    protected static final int QUERY_SIZE = 100;

    protected static final String ALIYUN_LOG_ENDPOINT = "${regionId}.log.aliyuncs.com";

    /**
     * 未配置 regionId 则使用杭州区
     *
     * @param aliyun
     * @return
     */
//    protected Client buildClient(AliyunConfig.Aliyun aliyun) {
//        String regionId = StringUtils.isEmpty(aliyun.getRegionId()) ? "cn-hangzhou" : aliyun.getRegionId();
//        String endpoint = ALIYUN_LOG_ENDPOINT.replace("${regionId}", regionId);
//        return new Client(endpoint, aliyun.getAccount().getAccessKeyId(), aliyun.getAccount().getSecret());
//    }
}
