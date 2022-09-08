package com.baiyi.opscloud.domain.param.message;

import com.baiyi.opscloud.domain.param.auth.IAuthPlatform;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
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
    public static class SendMessage implements IAuthPlatform {

        /**
         * VMS 语音
         * SMS 短信
         */
        @ApiModelProperty(value = "媒介类型 SMS , VMS")
        @NotBlank(message = "媒介不能为空")
        @ApiParam(required = true)
        @Pattern(regexp = "VMS|SMS", message = "媒介类型 SMS , VMS")
        private String media;

        @ApiModelProperty(value = "内容")
        @NotBlank(message = "内容不能为空")
        @ApiParam(required = true)
        @Length(max = 32, message = "内容长度不能大于32")
        private String content;

        @ApiModelProperty(value = "手机")
        @ApiParam(required = true)
        @Size(min = 1, message = "至少需要一个号码")
        private List<String> mobile;

        @NotBlank(message = "平台名称不能为空")
        @ApiParam(required = true)
        @ApiModelProperty(value = "平台名称(用于审计)")
        public String platform;

        @NotBlank(message = "平台令牌不能为空")
        @ApiParam(required = true)
        @ApiModelProperty(value = "平台令牌用于鉴权")
        public String platformToken;

    }
}
