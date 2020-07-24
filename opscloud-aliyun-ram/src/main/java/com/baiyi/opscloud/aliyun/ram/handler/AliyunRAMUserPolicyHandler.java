package com.baiyi.opscloud.aliyun.ram.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.AttachPolicyToUserRequest;
import com.aliyuncs.ram.model.v20150501.DetachPolicyFromUserRequest;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import com.baiyi.opscloud.aliyun.ram.base.BaseAliyunRAM;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamUser;
import com.baiyi.opscloud.domain.vo.cloud.AliyunRAMVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/6/11 2:52 下午
 * @Version 1.0
 */
@Component
public class AliyunRAMUserPolicyHandler extends BaseAliyunRAM {

    public BusinessWrapper<Boolean> attachPolicyToUser(AliyunCoreConfig.AliyunAccount aliyunAccount, OcAliyunRamUser ocAliyunRamUser, AliyunRAMVO.RAMPolicy ramPolicy) {
        AttachPolicyToUserRequest request = new AttachPolicyToUserRequest();
        request.setUserName(ocAliyunRamUser.getRamUsername());
        request.setPolicyName(ramPolicy.getPolicyName());
        request.setPolicyType(ramPolicy.getPolicyType());
        IAcsClient client = acqAcsClient(aliyunAccount);
        try {
            if (StringUtils.isEmpty(client.getAcsResponse(request).getRequestId()))
                return new BusinessWrapper(10000, "RequestId不存在！");
        } catch (ClientException e) {
            return new BusinessWrapper(10000, e.getMessage());
        }
        return BusinessWrapper.SUCCESS;
    }

    public BusinessWrapper<Boolean> detachPolicyFromUser(AliyunCoreConfig.AliyunAccount aliyunAccount, OcAliyunRamUser ocAliyunRamUser, AliyunRAMVO.RAMPolicy ramPolicy) {
        DetachPolicyFromUserRequest request = new DetachPolicyFromUserRequest();
        request.setUserName(ocAliyunRamUser.getRamUsername());
        request.setPolicyName(ramPolicy.getPolicyName());
        request.setPolicyType(ramPolicy.getPolicyType());
        IAcsClient client = acqAcsClient(aliyunAccount);
        try {
            if (StringUtils.isEmpty(client.getAcsResponse(request).getRequestId()))
                return new BusinessWrapper(10000, "RequestId不存在！");
        } catch (ClientException e) {
            return new BusinessWrapper(10000, e.getMessage());
        }
        return BusinessWrapper.SUCCESS;
    }

}
