package com.baiyi.opscloud.tencent.cloud.cvm.base;

import com.baiyi.opscloud.tencent.cloud.core.config.TencentCloudCoreConfig;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/20 5:25 下午
 * @Version 1.0
 */
@Slf4j
@Component
public abstract class BaseTencentCloudCVM implements InitializingBean {

    @Resource
    private TencentCloudCoreConfig tencentCloudCoreConfig;

    private static final String TENCENTCLOUD_DEFAULT_REGION = "ap-shanghai";

    protected static CvmClient cvmClient;

    protected static final long QUERY_PAGE_SIZE = 50;

    private TencentCloudCoreConfig.TencentCloudAccount getAccount() {
        return tencentCloudCoreConfig.getAccount();
    }

    protected List<String> getZones() {
        return getAccount().getZones();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            TencentCloudCoreConfig.TencentCloudAccount account = getAccount();
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey
            Credential cred = new Credential(account.getSecretId(), account.getSecretKey());

            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setReqMethod("GET"); // post请求(默认为post请求)
            httpProfile.setConnTimeout(30); // 请求连接超时时间，单位为秒(默认60秒)
            httpProfile.setEndpoint("cvm.ap-shanghai.tencentcloudapi.com"); // 指定接入地域域名(默认就近接入)

            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256"); // 指定签名算法(默认为HmacSHA256)
            clientProfile.setHttpProfile(httpProfile);

            // 实例化要请求产品(以cvm为例)的client对象,clientProfile是可选的
            String regionId = StringUtils.isEmpty(account.getRegionId()) ? TENCENTCLOUD_DEFAULT_REGION : account.getRegionId();
            BaseTencentCloudCVM.cvmClient = new CvmClient(cred, regionId, clientProfile);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("TencentCloud CvmClient创建失败!");
        }
    }

}
