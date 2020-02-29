package com.baiyi.opscloud.aliyun.core;

import com.aliyuncs.IAcsClient;
import com.baiyi.opscloud.aliyun.core.config.AliyunAccount;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 6:52 下午
 * @Version 1.0
 */
public interface AliyunCore {

    List<String> getRegionIds();

    List<AliyunAccount> getAccounts();

    IAcsClient getAcsClient(String regionId);

    IAcsClient getAcsClient(String regionId, AliyunAccount aliyunAccount);

    AliyunAccount getAliyunAccountByUid(String uid);

}
