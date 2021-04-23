package com.baiyi.opscloud.tencent.exmail.impl;

import com.baiyi.opscloud.domain.param.tencent.ExmailParam;
import com.baiyi.opscloud.tencent.exmail.TencentExmailUser;
import com.baiyi.opscloud.tencent.exmail.bo.TencentExmailUserBO;
import com.baiyi.opscloud.tencent.exmail.handler.TencentExmailUserHandler;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/29 3:57 下午
 * @Since 1.0
 */

@Component
public class TencentExmailUserImpl implements TencentExmailUser {

    @Resource
    private TencentExmailUserHandler tencentExmailUserHandler;

    @Override
    public Boolean createUser(ExmailParam.User param) {
        return tencentExmailUserHandler.createUser(param);
    }

    @Override
    public TencentExmailUserBO getUser(String userId) {
        return tencentExmailUserHandler.getUser(userId);
    }

    @Override
    public Boolean updateUser(ExmailParam.User param) {
        return tencentExmailUserHandler.updateUser(param);
    }

    @Override
    @Retryable(value = RuntimeException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    public Boolean deleteUser(String userId) throws RuntimeException {
        Boolean result = tencentExmailUserHandler.deleteUser(userId);
        if (!result) {
            throw new RuntimeException("删除tencentExmail失败,retry");
        }
        return true;
    }
}
