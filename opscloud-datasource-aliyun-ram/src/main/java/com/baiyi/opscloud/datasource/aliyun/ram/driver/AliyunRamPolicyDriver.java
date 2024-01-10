package com.baiyi.opscloud.datasource.aliyun.ram.driver;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.*;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamPolicy;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.datasource.aliyun.core.SimpleAliyunClient.Query.PAGE_SIZE;

/**
 * @Author baiyi
 * @Date 2021/12/10 10:45 AM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunRamPolicyDriver {

    private final AliyunClient aliyunClient;

    /**
     * 查询RAM所有策略
     *
     * @param regionId
     * @param aliyun
     * @return
     */
    public List<RamPolicy.Policy> listPolicies(String regionId, AliyunConfig.Aliyun aliyun) throws ClientException {
        List<ListPoliciesResponse.Policy> policies = Lists.newArrayList();
        String marker;
        ListPoliciesRequest request = new ListPoliciesRequest();
        request.setMaxItems(PAGE_SIZE);
        do {
            ListPoliciesResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            policies.addAll(response.getPolicies());
            marker = response.getMarker();
            request.setMarker(marker);
        } while (StringUtils.isNotBlank(marker));
        return BeanCopierUtil.copyListProperties(policies, RamPolicy.Policy.class);
    }

    public void attachPolicyToUser(String regionId, AliyunConfig.Aliyun aliyun, String ramUsername, RamPolicy.Policy policy) throws ClientException {
        AttachPolicyToUserRequest request = new AttachPolicyToUserRequest();
        request.setUserName(ramUsername);
        request.setPolicyName(policy.getPolicyName());
        request.setPolicyType(policy.getPolicyType());
        AttachPolicyToUserResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
    }

    public void detachPolicyFromUser(String regionId, AliyunConfig.Aliyun aliyun, String ramUsername, RamPolicy.Policy policy) throws ClientException {
        DetachPolicyFromUserRequest request = new DetachPolicyFromUserRequest();
        request.setUserName(ramUsername);
        request.setPolicyName(policy.getPolicyName());
        request.setPolicyType(policy.getPolicyType());
        DetachPolicyFromUserResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
    }

    public RamPolicy.Policy getPolicy(String regionId, AliyunConfig.Aliyun aliyun, RamPolicy.Policy policy) throws ClientException {
        GetPolicyRequest request = new GetPolicyRequest();
        request.setPolicyName(policy.getPolicyName());
        request.setPolicyType(policy.getPolicyType());
        GetPolicyResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        return BeanCopierUtil.copyProperties(response.getPolicy(), RamPolicy.Policy.class);
    }

}