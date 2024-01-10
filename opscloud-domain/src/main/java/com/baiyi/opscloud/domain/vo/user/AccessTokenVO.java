package com.baiyi.opscloud.domain.vo.user;

import com.baiyi.opscloud.domain.annotation.DesensitizedField;
import com.baiyi.opscloud.domain.constants.SensitiveTypeEnum;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.base.ReadableTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    @Schema
    public static class AccessToken extends BaseVO implements ReadableTime.ILater {

        private String later;

        @Schema(description = "主键")
        private Integer id;

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "是否有效")
        private Boolean valid;

        @Schema(description = "令牌")
        @DesensitizedField(type = SensitiveTypeEnum.TOKEN)
        private String token;

        @Schema(description = "令牌标识")
        private String tokenId;

        @Schema(description = "过期时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expiredTime;

        @Schema(description = "描述")
        private String comment;

    }

}