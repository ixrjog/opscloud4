package com.baiyi.opscloud.domain.vo.user;

import com.baiyi.opscloud.domain.annotation.DesensitizedField;
import com.baiyi.opscloud.domain.constants.SensitiveTypeEnum;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.base.ShowTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/2/26 6:05 下午
 * @Version 1.0
 */
public class AccessTokenVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AccessToken extends BaseVO implements ShowTime.ILater {

        private String later;

        @ApiModelProperty(value = "主键")
        private Integer id;

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "是否有效")
        private Boolean valid;

        @ApiModelProperty(value = "令牌")
        @DesensitizedField(type = SensitiveTypeEnum.TOKEN)
        private String token;

        @ApiModelProperty(value = "令牌标识")
        private String tokenId;

        @ApiModelProperty(value = "过期时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull(message = "必须指定过期时间")
        private Date expiredTime;

        @ApiModelProperty(value = "描述")
        @NotNull(message = "必须指定描述")
        private String comment;

    }
}
