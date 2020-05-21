package com.baiyi.opscloud.domain.vo.keybox;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/5/21 10:45 上午
 * @Version 1.0
 */
public class KeyboxVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Keybox {

        private Integer id;
        private String systemUser;
        private String title;
        private String passphrase;
        private Integer keyType;
        private Integer defaultKey;
        private String fingerprint;
        private Date updateTime;
        private Date createTime;
        private String publicKey;
        private String privateKey;
    }



}
