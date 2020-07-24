package com.baiyi.opscloud.cloud.ram.builder;

import com.aliyuncs.ram.model.v20150501.ListPoliciesForUserResponse;
import com.aliyuncs.ram.model.v20150501.ListPoliciesResponse;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import com.baiyi.opscloud.cloud.ram.bo.AliyunRamPolicyBO;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamPolicy;

/**
 * @Author baiyi
 * @Date 2020/6/10 11:41 上午
 * @Version 1.0
 */
public class AliyunRamPolicyBuilder {

    public static OcAliyunRamPolicy build(AliyunCoreConfig.AliyunAccount aliyunAccount, ListPoliciesResponse.Policy policy) {
        AliyunRamPolicyBO aliyunRamPolicyBO = AliyunRamPolicyBO.builder()
                .accountUid(aliyunAccount.getUid())
                .policyName(policy.getPolicyName())
                .policyType(policy.getPolicyType())
                .description(policy.getDescription())
                .attachmentCount(policy.getAttachmentCount())
                .defaultVersion(policy.getDefaultVersion())
                .createDate(TimeUtils.acqGmtDate(policy.getCreateDate()))
                .updateDate(TimeUtils.acqGmtDate(policy.getUpdateDate()))
                .build();
        return convert(aliyunRamPolicyBO);
    }

    public static OcAliyunRamPolicy build(AliyunCoreConfig.AliyunAccount aliyunAccount, ListPoliciesForUserResponse.Policy policy) {
        AliyunRamPolicyBO aliyunRamPolicyBO = AliyunRamPolicyBO.builder()
                .accountUid(aliyunAccount.getUid())
                .policyName(policy.getPolicyName())
                .policyType(policy.getPolicyType())
                .description(policy.getDescription())
                .defaultVersion(policy.getDefaultVersion())
                .build();
        return convert(aliyunRamPolicyBO);
    }

    private static OcAliyunRamPolicy convert(AliyunRamPolicyBO aliyunRamPolicyBO) {
        return BeanCopierUtils.copyProperties(aliyunRamPolicyBO, OcAliyunRamPolicy.class);
    }
}
