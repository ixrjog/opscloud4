package com.baiyi.opscloud.tencent.exmail;

import com.baiyi.opscloud.domain.param.tencent.ExmailParam;
import com.baiyi.opscloud.tencent.exmail.bo.TencentExmailUserBO;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/29 3:56 下午
 * @Since 1.0
 */
public interface TencentExmailUser {

    Boolean createUser(ExmailParam.User param);

    TencentExmailUserBO getUser(String userId);

    Boolean updateUser(ExmailParam.User param);

    Boolean deleteUser(String userId);
}
