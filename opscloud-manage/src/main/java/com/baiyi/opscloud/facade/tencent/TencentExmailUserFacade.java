package com.baiyi.opscloud.facade.tencent;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.tencent.ExmailParam;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/29 3:55 下午
 * @Since 1.0
 */
public interface TencentExmailUserFacade {

    BusinessWrapper<Boolean> createUser(ExmailParam.User param);

    BusinessWrapper<Boolean> checkUser(String userId);

    BusinessWrapper<Boolean> deleteUser(OcUser ocUser);
}
