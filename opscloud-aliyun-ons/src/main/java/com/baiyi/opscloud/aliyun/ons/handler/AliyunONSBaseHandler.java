package com.baiyi.opscloud.aliyun.ons.handler;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/5 11:07 上午
 * @Since 1.0
 */

@Component
public class AliyunONSBaseHandler {

    @Resource
    private AliyunCore aliyunCore;

    @Resource
    private AliyunCoreConfig aliyunCoreConfig;

    private IAcsClient getInternetClient() {
        AliyunCoreConfig.AliyunAccount account = aliyunCore.getAccount();
        AliyunCoreConfig.Ons ons = aliyunCoreConfig.getOns();
        IClientProfile profile = DefaultProfile.getProfile(ons.getInternetRegionId(), account.getAccessKeyId(), account.getSecret());
        return new DefaultAcsClient(profile);
    }

    private IAcsClient getAcsClient(String regionId) {
        return aliyunCore.getAcsClient(regionId);
    }

    public IAcsClient getClient(String regionId) {
        AliyunCoreConfig.Ons ons = aliyunCoreConfig.getOns();
        return regionId.equals(ons.getInternetRegionId()) ? getInternetClient() : getAcsClient(regionId);
    }
}
