package com.baiyi.opscloud.facade.tencent.impl;

import com.baiyi.opscloud.common.util.RegexUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.tencent.ExmailParam;
import com.baiyi.opscloud.facade.tencent.TencentExmailUserFacade;
import com.baiyi.opscloud.tencent.exmail.TencentExmailUser;
import com.baiyi.opscloud.tencent.exmail.bo.TencentExmailUserBO;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/29 3:56 下午
 * @Since 1.0
 */

@Component("TencentExmailUserFacade")
public class TencentExmailUserFacadeImpl implements TencentExmailUserFacade {

    @Resource
    private TencentExmailUser tencentExmailUser;

    @Override
    public BusinessWrapper<Boolean> createUser(ExmailParam.User param) {
        if (!RegexUtils.isEmail(param.getUserid()))
            return new BusinessWrapper<>(ErrorEnum.USER_EMAIL_NON_COMPLIANCE_WITH_RULES);
        if (!RegexUtils.isPhone(param.getMobile()))
            return new BusinessWrapper<>(ErrorEnum.USER_PHONE_NON_COMPLIANCE_WITH_RULES);
        if (Strings.isEmpty(param.getName()))
            return new BusinessWrapper<>(ErrorEnum.TENCENT_EXMAIL_NAME_ISEMPTY);
        TencentExmailUserBO user = tencentExmailUser.getUser(param.getUserid());
        if (user != null)
            return new BusinessWrapper<>(ErrorEnum.TENCENT_EXMAIL_USER_EXIST);
        if (tencentExmailUser.createUser(param))
            return BusinessWrapper.SUCCESS;
        return new BusinessWrapper<>(ErrorEnum.TENCENT_EXMAIL_CREATE_FAIL);
    }

    @Override
    public BusinessWrapper<Boolean> checkUser(String userId) {
        if (!RegexUtils.isEmail(userId))
            return new BusinessWrapper<>(ErrorEnum.USER_EMAIL_NON_COMPLIANCE_WITH_RULES);
        TencentExmailUserBO user = tencentExmailUser.getUser(userId);
        if (user != null)
            return new BusinessWrapper<>(ErrorEnum.TENCENT_EXMAIL_USER_EXIST);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteUser(OcUser ocUser) {
        if (!Strings.isEmpty(ocUser.getEmail())) {
            TencentExmailUserBO user = tencentExmailUser.getUser(ocUser.getEmail());
            if (user != null) {
                if (tencentExmailUser.deleteUser(ocUser.getEmail()))
                    return BusinessWrapper.SUCCESS;
                return new BusinessWrapper<>(ErrorEnum.TENCENT_EXMAIL_DEL_FAIL);
            }
        }
        return BusinessWrapper.SUCCESS;
    }
}
