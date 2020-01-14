package com.baiyi.opscloud.aliyun.core.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 6:52 下午
 * @Version 1.0
 */
@Component("AliyunCore")
public class AliyunCoreImpl implements AliyunCore {

    @Resource
    private AliyunCoreConfig aliyunCoreConfig;

    @Override
    public List<String> getRegionIds() {
        return aliyunCoreConfig.getRegionIds();
    }

    @Override
    public IAcsClient getAcsClient(String regionId) {
        String defRegionId;
        if (StringUtils.isEmpty(regionId)) {
            defRegionId = aliyunCoreConfig.getRegionId();
        } else {
            defRegionId = regionId;
        }
        IClientProfile profile = DefaultProfile.getProfile(defRegionId, aliyunCoreConfig.getAccessKeyId(), aliyunCoreConfig.getSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        return client;
    }


}
