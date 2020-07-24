package com.baiyi.opscloud.cloud.ram;

import com.aliyuncs.ram.model.v20150501.ListPoliciesResponse;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import com.baiyi.opscloud.domain.BusinessWrapper;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/10 6:03 下午
 * @Version 1.0
 */
public interface AliyunRAMPolicyCenter {

    List<ListPoliciesResponse.Policy> getPolicies(AliyunCoreConfig.AliyunAccount aliyunAccount);

    BusinessWrapper<Boolean> syncPolicies();

//    void deleteUserPolicies();

}
