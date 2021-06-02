package com.baiyi.caesar.domain.vo.datasource;

import com.baiyi.caesar.domain.vo.base.BaseVO;
import com.baiyi.caesar.domain.vo.sys.CredentialVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * @Author baiyi
 * @Date 2021/5/15 1:36 下午
 * @Version 1.0
 */
public class DatasourceConfigVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class DsConfig extends BaseVO implements CredentialVO.ICredential {

        @ApiModelProperty(value = "系统凭据")
        private CredentialVO.Credential credential;

        @ApiModelProperty(value = "此配置文件注册的实例数量")
        private Integer instanceSize;

        @ApiModelProperty(value = "已注册")
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
