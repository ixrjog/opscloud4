package com.baiyi.opscloud.aliyun.core.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunAccount;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
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
        for (AliyunAccount aliyunAccount : aliyunCoreConfig.getAccounts())
            if(aliyunAccount.getMaster())
                return aliyunAccount.getRegionIds();
        return Collections.emptyList();
    }

    @Override
    public IAcsClient getAcsClient(String regionId) {
        for (AliyunAccount aliyunAccount : aliyunCoreConfig.getAccounts())
            if(aliyunAccount.getMaster()){
                String defRegionId;
                if (StringUtils.isEmpty(regionId)) {
                    defRegionId = aliyunAccount.getRegionId();
                } else {
                    defRegionId = regionId;
                }
                IClientProfile profile = DefaultProfile.getProfile(defRegionId, aliyunAccount.getAccessKeyId(), aliyunAccount.getSecret());
                IAcsClient client = new DefaultAcsClient(profile);
                return client;
            }
        return null;
    }


}
