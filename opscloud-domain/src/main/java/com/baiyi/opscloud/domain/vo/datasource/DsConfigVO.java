package com.baiyi.opscloud.domain.vo.datasource;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.sys.CredentialVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * @Author baiyi
 * @Date 2021/5/15 1:36 下午
 * @Version 1.0
 */
public class DsConfigVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class DsConfig extends BaseVO implements CredentialVO.ICredential {

        @Schema(description = "系统凭据")
        private CredentialVO.Credential credential;

        @Schema(description = "此配置文件注册的实例数量")
        private Integer instanceSize;

        @Schema(description = "已注册")
        private Boolean isRegistered;

        private Integer id;
        private String name;
        private String uuid;
        private Integer dsType;
        private String version;
        private String kind;
        private Boolean isActive;
        private Integer credentialId;
        private String dsUrl;
        private String propsYml;
        private String comment;

    }

}