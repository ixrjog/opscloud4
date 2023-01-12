package com.baiyi.opscloud.domain.param.message;

import com.baiyi.opscloud.domain.param.auth.IAuthPlatform;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Author 修远
 * @Date 2022/9/7 7:29 PM
 * @Since 1.0
 */
public class MessageParam {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SendMessage implements IAuthPlatform {

        /**
         * VMS 语音
         * SMS 短信
         */
        @ApiModelProperty(value = "媒介类型 SMS , VMS", required = true)
        @NotBlank(message = "媒介不能为空")
        @Pattern(regexp = "VMS|SMS", message = "媒介类型 SMS , VMS")
        private String media;

        @ApiModelProperty(value = "内容", required = true)
        @NotBlank(message = "内容不能为空")
        @Length(max = 32, message = "内容长度不能大于32")
        private String content;

        @ApiModelProperty(value = "手机", required = true)
        @Size(min = 1, message = "至少需要一个号码")
        private List<String> mobile;

        @NotBlank(message = "平台名称不能为空")
        @ApiModelProperty(value = "平台名称(用于审计)", required = true)
        public String platform;

        @NotBlank(message = "平台令牌不能为空")
        @ApiModelProperty(value = "平台令牌用于鉴权", required = true)
        public String platformToken;

    }
}
