package com.baiyi.opscloud.cloud.ram.impl;

import com.aliyuncs.ram.model.v20150501.ListPoliciesResponse;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import com.baiyi.opscloud.aliyun.ram.handler.AliyunRAMPolicyHandler;
import com.baiyi.opscloud.cloud.ram.AliyunRAMPolicyCenter;
import com.baiyi.opscloud.cloud.ram.builder.AliyunRamPolicyBuilder;
import com.baiyi.opscloud.common.util.IDUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamPolicy;
import com.baiyi.opscloud.service.aliyun.ram.OcAliyunRamPolicyService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/6/10 6:04 下午
 * @Version 1.0
 */
@Component
public class AliyunRAMPolicyCenterImpl implements AliyunRAMPolicyCenter {

    @Resource
    private AliyunCore aliyunCore;

    @Resource
    private AliyunRAMPolicyHandler aliyunRAMPolicyHandler;

    @Resource
    private OcAliyunRamPolicyService ocAliyunRamPolicyService;

    @Override
    public List<ListPoliciesResponse.Policy> getPolicies(AliyunCoreConfig.AliyunAccount aliyunAccount) {
        return aliyunRAMPolicyHandler.getPolicies(aliyunAccount);
    }

    @Override
    public BusinessWrapper<Boolean> syncPolicies() {
        aliyunCore.getAccounts().forEach(e -> {
            Map<String, OcAliyunRamPolicy> ramPolicyMap = queryRamPolicyMap(e);
            List<ListPoliciesResponse.Policy> policies = getPolicies(e);
            syncPolicies(e, policies, ramPolicyMap);
            deletePolicies(ramPolicyMap);
        });
        return BusinessWrapper.SUCCESS;
    }

    private void syncPolicies(AliyunCoreConfig.AliyunAccount aliyunAccount, List<ListPoliciesResponse.Policy> policies, Map<String, OcAliyunRamPolicy> ramPolicyMap) {
        if (policies == null) return;
        policies.forEach(e -> {
            OcAliyunRamPolicy pre = AliyunRamPolicyBuilder.build(aliyunAccount, e);
            if (ramPolicyMap.containsKey(pre.getPolicyName())) {
                OcAliyunRamPolicy ocAliyunRamPolicy = ramPolicyMap.get(pre.getPolicyName());
                pre.setId(ocAliyunRamPolicy.getId());
                pre.setInWorkorder(ocAliyunRamPolicy.getInWorkorder());
                ramPolicyMap.remove(pre.getPolicyName());
            }
            saveRamPolicy(pre);
        });
    }

    private Map<String, OcAliyunRamPolicy> queryRamPolicyMap(AliyunCoreConfig.AliyunAccount aliyunAccount) {
        Map<String, OcAliyunRamPolicy> ramPolicyMap = Maps.newHashMap();
        List<OcAliyunRamPolicy> ramPolicies = ocAliyunRamPolicyService.queryOcAliyunRamPolicyByAccountUid(aliyunAccount.getUid());
        ramPolicies.forEach(e -> ramPolicyMap.put(e.getPolicyName(), e));
        return ramPolicyMap;
    }

    private void deletePolicies(Map<String, OcAliyunRamPolicy> ramPolicyMap) {
        ramPolicyMap.keySet().forEach(k -> {
            OcAliyunRamPolicy ocAliyunRamPolicy = ramPolicyMap.get(k);
            ocAliyunRamPolicyService.deleteOcAliyunRamPolicyById(ocAliyunRamPolicy.getId());
        });
    }

    private void saveRamPolicy(OcAliyunRamPolicy ocAliyunRamPolicy) {
        if (IDUtils.isEmpty(ocAliyunRamPolicy.getId())) {
            ocAliyunRamPolicyService.addOcAliyunRamPolicy(ocAliyunRamPolicy);
        } else {
            ocAliyunRamPolicyService.updateOcAliyunRamPolicy(ocAliyunRamPolicy);
        }
    }
}
