package com.baiyi.opscloud.tencent.exmail.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/10/12 3:40 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class TencentExmailUserHandler {

    public interface UserAPI {
        String CREATE = "/cgi-bin/user/create";
        String GET = "/cgi-bin/user/get";
        String UPDATE = "/cgi-bin/user/update";
        String DELETE = "/cgi-bin/user/delete";
    }

}
