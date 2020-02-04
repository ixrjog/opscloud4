package com.baiyi.opscloud.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author feixue
 */
public class LogVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class LoginVO {

        private String uuid;
        private String token;
    }
}
