package com.baiyi.opscloud.aliyun.ram.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.AttachPolicyToUserRequest;
import com.aliyuncs.ram.model.v20150501.DetachPolicyFromUserRequest;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunAccount;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamPolicy;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/6/11 2:52 下午
 * @Version 1.0
 */
@Component
public class AliyunRAMUserPolicyHandler {

    @Resource
    private AliyunCore aliyunCore;

    public BusinessWrapper<Boolean> attachPolicyToUser(AliyunAccount aliyunAccount, OcAliyunRamUser ocAliyunRamUser, OcAliyunRamPolicy ocAliyunRamPolicy) {
        AttachPolicyToUserRequest request = new AttachPolicyToUserRequest();
        request.setUserName(ocAliyunRamUser.getRamUsername());
        request.setPolicyName(ocAliyunRamPolicy.getPolicyName());
        request.setPolicyType(ocAliyunRamPolicy.getPolicyType());
        IAcsClient client = acqAcsClient(aliyunAccount);
        try {
            if (StringUtils.isEmpty(client.getAcsResponse(request).getRequestId()))
                return new BusinessWrapper(10000, "RequestId不存在！");
        } catch (ClientException e) {
            return new BusinessWrapper(10000, e.getMessage());
        }
        return BusinessWrapper.SUCCESS;
    }

    public BusinessWrapper<Boolean> detachPolicyFromUser(AliyunAccount aliyunAccount, OcAliyunRamUser ocAliyunRamUser, OcAliyunRamPolicy ocAliyunRamPolicy) {
        DetachPolicyFromUserRequest request = new DetachPolicyFromUserRequest();
        request.setUserName(ocAliyunRamUser.getRamUsername());
        request.setPolicyName(ocAliyunRamPolicy.getPolicyName());
        request.setPolicyType(ocAliyunRamPolicy.getPolicyType());
        IAcsClient client = acqAcsClient(aliyunAccount);
        try {
            if (StringUtils.isEmpty(client.getAcsResponse(request).getRequestId()))
                return new BusinessWrapper(10000, "RequestId不存在！");
        } catch (ClientException e) {
            return new BusinessWrapper(10000, e.getMessage());
        }
        return BusinessWrapper.SUCCESS;
    }


    private IAcsClient acqAcsClient(AliyunAccount aliyunAccount) {
        return aliyunCore.getAcsClient(aliyunAccount.getRegionId(), aliyunAccount);
    }
}
