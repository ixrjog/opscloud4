package com.baiyi.opscloud.datasource.aliyun.core;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author baiyi
 * @Date 2021/6/18 9:46 上午
 * @Version 1.0
 */
public class SimpleAliyunClient {

    public interface Query {
        int PAGE_SIZE = 50;
        int RDS_INSTANCE_PAGE_SIZE = 30;
    }

    public IAcsClient buildAcsClient(String regionId, AliyunConfig.Aliyun aliyun) {
        String defRegionId = StringUtils.isEmpty(aliyun.getRegionId()) ? aliyun.getRegionId() : regionId;
        IClientProfile profile = DefaultProfile.getProfile(defRegionId, aliyun.getAccount().getAccessKeyId(), aliyun.getAccount().getSecret());
        return new DefaultAcsClient(profile);
    }

}