package com.baiyi.opscloud.jumpserver.api.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/3/14 6:32 下午
 * @Version 1.0
 */
@Data
@Builder
public class Auth {
    private String username;
    private String password;
}
