package com.baiyi.opscloud.cloud.ram.handler;

import com.aliyuncs.ram.model.v20150501.ListPoliciesForUserResponse;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import com.baiyi.opscloud.aliyun.ram.handler.AliyunRAMPolicyHandler;
import com.baiyi.opscloud.cloud.ram.builder.AliyunRamPolicyBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamPermission;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamPolicy;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamUser;
import com.baiyi.opscloud.service.aliyun.ram.OcAliyunRamPermissionService;
import com.baiyi.opscloud.service.aliyun.ram.OcAliyunRamPolicyService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/6/10 6:12 下午
 * @Version 1.0
 */
@Component
public class AliyunRAMUserPolicyPermissionHandler {

    @Resource
    private AliyunRAMPolicyHandler aliyunRAMPolicyHandler;

    @Resource
    private OcAliyunRamPolicyService ocAliyunRamPolicyService;

    @Resource
    private OcAliyunRamPermissionService ocAliyunRamPermissionService;

    /**
     * 同步用户策略授权信息
     *
     * @param aliyunAccount
     * @param ocAliyunRamUser
     */
    public void syncUserPolicyPermission(AliyunCoreConfig.AliyunAccount aliyunAccount, OcAliyunRamUser ocAliyunRamUser) {
        List<ListPoliciesForUserResponse.Policy> policies = aliyunRAMPolicyHandler.listPoliciesForUser(aliyunAccount, ocAliyunRamUser.getRamUsername());
        Map<String, OcAliyunRamPolicy> userPolicyMap = getUserPolicyMap(ocAliyunRamUser);
        policies.forEach(e -> {
            if (userPolicyMap.containsKey(e.getPolicyName())) {
                userPolicyMap.remove(e.getPolicyName());
            } else {
                OcAliyunRamPolicy ocAliyunRamPolicy = getOcAliyunRamPolicy(aliyunAccount, e);
                OcAliyunRamPermission ocAliyunRamPermission = new OcAliyunRamPermission();
                ocAliyunRamPermission.setAccountUid(aliyunAccount.getUid());
                ocAliyunRamPermission.setUserId(ocAliyunRamUser.getId());
                ocAliyunRamPermission.setPolicyId(ocAliyunRamPolicy.getId());
                ocAliyunRamPermissionService.addOcAliyunRamPermission(ocAliyunRamPermission);
            }
        });
        deleteUserPolicyByMap(ocAliyunRamUser, userPolicyMap);
    }

    private void deleteUserPolicyByMap(OcAliyunRamUser ocAliyunRamUser, Map<String, OcAliyunRamPolicy> userPolicyMap) {
        userPolicyMap.keySet().forEach(e -> {
            OcAliyunRamPolicy ocAliyunRamPolicy = userPolicyMap.get(e);
            OcAliyunRamPermission queryParam = new OcAliyunRamPermission();
            queryParam.setPolicyId(ocAliyunRamPolicy.getId());
            queryParam.setUserId(ocAliyunRamUser.getId());
            queryParam.setAccountUid(ocAliyunRamUser.getAccountUid());
            OcAliyunRamPermission ocAliyunRamPermission = ocAliyunRamPermissionService.queryOcAliyunRamPermissionByUniqueKey(queryParam);
            ocAliyunRamPermissionService.deleteOcAliyunRamPermissionById(ocAliyunRamPermission.getId());
        });
    }

    private OcAliyunRamPolicy getOcAliyunRamPolicy(AliyunCoreConfig.AliyunAccount aliyunAccount, ListPoliciesForUserResponse.Policy policy) {
        OcAliyunRamPolicy ocAliyunRamPolicy = ocAliyunRamPolicyService.queryOcAliyunRamPolicyByUniqueKey(aliyunAccount.getUid(), policy.getPolicyName());
        if (ocAliyunRamPolicy != null) return ocAliyunRamPolicy;
        ocAliyunRamPolicy = AliyunRamPolicyBuilder.build(aliyunAccount, policy);
        ocAliyunRamPolicyService.addOcAliyunRamPolicy(ocAliyunRamPolicy);
        return ocAliyunRamPolicy;
    }

    private Map<String, OcAliyunRamPolicy> getUserPolicyMap(OcAliyunRamUser ocAliyunRamUser) {
        List<OcAliyunRamPolicy> userPolicies = ocAliyunRamPolicyService.queryOcAliyunRamPolicyByUserPermission(ocAliyunRamUser.getAccountUid(), ocAliyunRamUser.getId());
        return userPolicies.stream().collect(Collectors.toMap(OcAliyunRamPolicy::getPolicyName, a -> a, (k1, k2) -> k1));
    }

    /**
     * 删除用户账户的的所有授权
     * @param ocAliyunRamUser
     */
    public void deleteOcAliyunRamPermissionByOcAliyunRamUser(OcAliyunRamUser ocAliyunRamUser) {
        ocAliyunRamPermissionService.queryOcAliyunRamPermissionByOcAliyunRamUser(ocAliyunRamUser).forEach(e-> ocAliyunRamPermissionService.deleteOcAliyunRamPermissionById(e.getId()));
    }

}
