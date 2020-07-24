package com.baiyi.opscloud.aliyun.ram.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.ListPoliciesForUserRequest;
import com.aliyuncs.ram.model.v20150501.ListPoliciesForUserResponse;
import com.aliyuncs.ram.model.v20150501.ListPoliciesRequest;
import com.aliyuncs.ram.model.v20150501.ListPoliciesResponse;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import com.baiyi.opscloud.aliyun.ram.base.BaseAliyunRAM;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/10 10:15 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class AliyunRAMPolicyHandler extends BaseAliyunRAM {

    public static final String ALL_POLICIES = "";

    /**
     * 查询用户所有的授权策略
     *
     * @param aliyunAccount
     * @param username
     * @return
     */
    public List<ListPoliciesForUserResponse.Policy> listPoliciesForUser(AliyunCoreConfig.AliyunAccount aliyunAccount, String username) {
        ListPoliciesForUserRequest request = new ListPoliciesForUserRequest();
        request.setUserName(username);
        IAcsClient client = acqAcsClient(aliyunAccount);
        try {
            return client.getAcsResponse(request).getPolicies();
        } catch (ClientException e) {
        }
        return Collections.emptyList();
    }

    /**
     * 查询账户下所有RAM策略
     *
     * @param aliyunAccount
     * @return
     */
    public List<ListPoliciesResponse.Policy> getPolicies(AliyunCoreConfig.AliyunAccount aliyunAccount) {
        List<ListPoliciesResponse.Policy> policies = Lists.newArrayList();
        String marker = "";
        while (true) {
            ListPoliciesResponse responseMarker = listPolicies(acqAcsClient(aliyunAccount), ALL_POLICIES, marker);
            if (responseMarker.getPolicies() == null)
                return policies;
            policies.addAll(responseMarker.getPolicies());
            if (!responseMarker.getIsTruncated()) {
                return policies;
            } else {
                marker = responseMarker.getMarker();
            }
        }
    }

    private ListPoliciesResponse listPolicies(IAcsClient client, String policyType, String marker) {
        ListPoliciesRequest request = new ListPoliciesRequest();
        request.setMaxItems(MAX_ITEMS);
        if (!StringUtils.isEmpty(policyType))
            request.setPolicyType(policyType);
        if (!StringUtils.isEmpty(marker))
            request.setMarker(marker);
        try {
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }


}
