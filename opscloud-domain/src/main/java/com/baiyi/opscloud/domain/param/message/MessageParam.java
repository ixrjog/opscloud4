package com.baiyi.opscloud.domain.param.message;

import com.baiyi.opscloud.domain.param.auth.IAuthPlatform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
    @Schema
    public static class SendMessage implements IAuthPlatform {

        /**
         * VMS 语音
         * SMS 短信
         */
        @Schema(description = "媒介类型 SMS , VMS")
        @NotBlank(message = "媒介不能为空")
        @Pattern(regexp = "VMS|SMS", message = "媒介类型 SMS , VMS")
        private String media;

        @Schema(description = "内容")
        @NotBlank(message = "内容不能为空")
        @Length(max = 32, message = "内容长度不能大于32")
        private String content;

        @Schema(description = "手机")
        @Size(min = 1, message = "至少需要一个号码")
        private List<String> mobile;

        @NotBlank(message = "平台名称不能为空")
        @Schema(description = "平台名称(用于审计)")
        public String platform;

        @NotBlank(message = "平台令牌不能为空")
        @Schema(description = "平台令牌用于鉴权")
        public String platformToken;

    }

    @Data
    public static class GrafanaMessage {
        private String message;
    }

}