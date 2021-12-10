package com.baiyi.opscloud.datasource.aliyun.ram.drive;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.ListPoliciesRequest;
import com.aliyuncs.ram.model.v20150501.ListPoliciesResponse;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamPolicy;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
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
public class AliyunRamPolicyDrive {

    private final AliyunClient aliyunClient;

    /**
     * 查询RAM所有策略
     * @param regionId
     * @param aliyun
     * @return
     */
    public List<RamPolicy.Policy> listPolicies(String regionId, AliyunConfig.Aliyun aliyun) {
        List<ListPoliciesResponse.Policy> policies = Lists.newArrayList();
        String marker;
        try {
            ListPoliciesRequest request = new ListPoliciesRequest();
            request.setMaxItems(PAGE_SIZE);
            do {
                ListPoliciesResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
                policies.addAll(response.getPolicies());
                marker = response.getMarker();
                request.setMarker(marker);
            } while (Strings.isNotBlank(marker));
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return BeanCopierUtil.copyListProperties(policies ,RamPolicy.Policy.class);
    }

//    public void attachPolicyToUser(String regionId,AliyunConfig.Aliyun aliyun, String ramUsername, AliyunRAMVO.RAMPolicy ramPolicy) throws RuntimeException {
//        AttachPolicyToUserRequest request = new AttachPolicyToUserRequest();
//        request.setUserName(ramUsername);
//        request.setPolicyName(ramPolicy.getPolicyName());
//        request.setPolicyType(ramPolicy.getPolicyType());
//        try {
//            if (StringUtils.isEmpty(aliyunClient.getAcsResponse(regionId, aliyun, request).getRequestId()))
//                throw new CommonRuntimeException("API请求错误: requestId不存在 !");
//        } catch (ClientException e) {
//            throw new CommonRuntimeException( e.getMessage());
//        }
//    }
//
//    public BusinessWrapper<Boolean> detachPolicyFromUser(AliyunCoreConfig.AliyunAccount aliyunAccount, OcAliyunRamUser ocAliyunRamUser, AliyunRAMVO.RAMPolicy ramPolicy) {
//        DetachPolicyFromUserRequest request = new DetachPolicyFromUserRequest();
//        request.setUserName(ocAliyunRamUser.getRamUsername());
//        request.setPolicyName(ramPolicy.getPolicyName());
//        request.setPolicyType(ramPolicy.getPolicyType());
//        IAcsClient client = acqAcsClient(aliyunAccount);
//        try {
//            if (StringUtils.isEmpty(client.getAcsResponse(request).getRequestId()))
//                return new BusinessWrapper<>(10000, "RequestId不存在！");
//        } catch (ClientException e) {
//            return new BusinessWrapper<>(10000, e.getMessage());
//        }
//        return BusinessWrapper.SUCCESS;
//    }

}
