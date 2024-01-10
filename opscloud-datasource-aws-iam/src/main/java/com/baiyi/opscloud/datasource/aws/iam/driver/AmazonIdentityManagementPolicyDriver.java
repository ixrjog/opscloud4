package com.baiyi.opscloud.datasource.aws.iam.driver;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.*;
import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamPolicy;
import com.baiyi.opscloud.datasource.aws.iam.service.AmazonIdentityManagementService;
import com.google.common.collect.Lists;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/1/21 2:13 PM
 * @Version 1.0
 */
@Component
public class AmazonIdentityManagementPolicyDriver {

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1H, key = "'account_id_'+ #config.account.id + '_policies'", unless = "#result == null")
    public Map<String, IamPolicy.Policy> getPolicyMap(AwsConfig.Aws config) {
        List<IamPolicy.Policy> policies = listPolicies(config);
        return policies.stream().collect(Collectors.toMap(IamPolicy.Policy::getPolicyName, a -> a, (k1, k2) -> k1));
    }

    /**
     * https://docs.aws.amazon.com/IAM/latest/APIReference/API_AttachUserPolicy.html
     * @param config
     * @param userName
     * @param policy
     */
    public void attachUserPolicy(AwsConfig.Aws config, String userName, IamPolicy.Policy policy) {
        AttachUserPolicyRequest request = new AttachUserPolicyRequest();
        request.setUserName(userName);
        request.setPolicyArn(policy.getArn());
        buildAmazonIdentityManagement(config).attachUserPolicy(request);
    }

    /**
     * https://docs.aws.amazon.com/IAM/latest/APIReference/API_DetachUserPolicy.html
     * @param config
     * @param userName
     * @param policy
     */
    public void detachUserPolicy(AwsConfig.Aws config, String userName, IamPolicy.Policy policy){
        DetachUserPolicyRequest request = new DetachUserPolicyRequest();
        request.setUserName(userName);
        request.setPolicyArn(policy.getArn());
        buildAmazonIdentityManagement(config).detachUserPolicy(request);
    }

    public List<IamPolicy.Policy> listPolicies(AwsConfig.Aws config) {
        ListPoliciesRequest request = new ListPoliciesRequest();
        List<Policy> policies = Lists.newArrayList();
        while (true) {
            ListPoliciesResult result = buildAmazonIdentityManagement(config).listPolicies(request);
            policies.addAll(result.getPolicies());
            if (result.getIsTruncated()) {
                request.setMarker(result.getMarker());
            } else {
                break;
            }
        }
        return BeanCopierUtil.copyListProperties(policies, IamPolicy.Policy.class);
    }

    /**
     * @param aws
     * @param policyArn arn:aws:iam::aws:policy/AWSDirectConnectReadOnlyAccess
     * @return
     */
    public IamPolicy.Policy getPolicy(AwsConfig.Aws aws, String policyArn) {
        GetPolicyRequest request = new GetPolicyRequest();
        request.setPolicyArn(policyArn);
        GetPolicyResult result = buildAmazonIdentityManagement(aws).getPolicy(request);
        return BeanCopierUtil.copyProperties(result.getPolicy(), IamPolicy.Policy.class);
    }

    public List<IamPolicy.Policy> listUserPolicies(AwsConfig.Aws config, String userName) {
        ListAttachedUserPoliciesRequest request = new ListAttachedUserPoliciesRequest();
        request.setUserName(userName);
        List<AttachedPolicy> attachedPolicies = Lists.newArrayList();
        while (true) {
            ListAttachedUserPoliciesResult result = buildAmazonIdentityManagement(config).listAttachedUserPolicies(request);
            attachedPolicies.addAll(result.getAttachedPolicies());
            if (result.getIsTruncated()) {
                request.setMarker(result.getMarker());
            } else {
                break;
            }
        }
        return attachedPolicies.stream().map(e -> getPolicy(config, e.getPolicyArn())).collect(Collectors.toList());
    }

    private AmazonIdentityManagement buildAmazonIdentityManagement(AwsConfig.Aws aws) {
        return AmazonIdentityManagementService.buildAmazonIdentityManagement(aws);
    }

}
