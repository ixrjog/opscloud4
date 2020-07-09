package com.baiyi.opscloud.decorator.aliyun;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamPolicy;
import com.baiyi.opscloud.domain.vo.cloud.AliyunRAMVO;
import com.baiyi.opscloud.service.ram.OcAliyunRamPolicyService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/11 10:15 上午
 * @Version 1.0
 */
@Component
public class AliyunRAMDecorator {

    @Resource
    private OcAliyunRamPolicyService ocAliyunRamPolicyService;

    public AliyunRAMVO.RAMUser decorator(AliyunRAMVO.RAMUser ramUser, Integer extend) {
        if (extend == 0)
            return ramUser;
        List<OcAliyunRamPolicy> policies = ocAliyunRamPolicyService.queryOcAliyunRamPolicyByUserPermission(ramUser.getAccountUid(), ramUser.getId());
        ramUser.setPolicies(BeanCopierUtils.copyListProperties(policies, AliyunRAMVO.RAMPolicy.class));
        ramUser.setRamAccount(buildRamAccount(ramUser));
        ramUser.setRamAccountLoginUrl(buildRamAccountLoginUrl(ramUser));
        return ramUser;
    }

    private String buildRamAccount(AliyunRAMVO.RAMUser ramUser) {
        return Joiner.on("").join(ramUser.getRamUsername(), "@", ramUser.getAccountUid(), ".onaliyun.com");
    }

    private String buildRamAccountLoginUrl(AliyunRAMVO.RAMUser ramUser) {
        //   https://signin.aliyun.com/{ACCOUNT_UID}.onaliyun.com/login.htm
        return Joiner.on("/").join("https://signin.aliyun.com", ramUser.getAccountUid(), ".onaliyun.com", "login.htm");
    }
}
