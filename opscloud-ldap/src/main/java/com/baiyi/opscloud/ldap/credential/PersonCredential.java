package com.baiyi.opscloud.ldap.credential;

import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2019/12/30 6:31 下午
 * @Version 1.0
 */
@Data
@Builder
public class PersonCredential {

    private String password;
    private String username;

    public boolean isEmpty() {
        return (StringUtils.isEmpty(username) || StringUtils.isEmpty(password));
    }

}
