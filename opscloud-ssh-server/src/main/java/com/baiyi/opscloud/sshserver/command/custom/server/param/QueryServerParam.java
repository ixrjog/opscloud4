package com.baiyi.opscloud.sshserver.command.custom.server.param;

import com.baiyi.opscloud.domain.param.server.ServerParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/6/11 3:01 下午
 * @Version 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryServerParam {

    private String sessionId;
    private String username;

    private ServerParam.UserPermissionServerPageQuery queryParam;

}