package com.baiyi.opscloud.domain.vo.user;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/2/27 1:17 下午
 * @Version 1.0
 */
public class UserCredentialVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class CredentialDetails {
        private Map<String, List<Credential>> credentialMap;
    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Credential extends BaseVO {
        @ApiModelProperty(value = "主键")
        private Integer id;

        @ApiModelProperty(value = "用户id")
        private Integer userId;

        @ApiModelProperty(value = "标题")
        private String title;

        @ApiModelProperty(value = "凭据类型")
        private Integer credentialType;

        @ApiModelProperty(value = "凭据内容")
        @NotNull(message = "凭据不能为空")
        private String credential;

        @ApiModelProperty(value = "凭据指纹")
        private String fingerprint;

        @ApiModelProperty(value = "有效")
        private Boolean valid;

        @ApiModelProperty(value = "有效期")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expiredTime;

        private String comment;
    }

}
