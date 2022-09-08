package com.baiyi.opscloud.domain.param.message;

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
    public static class SendMessage {

        /**
         * VMS 语音
         * SMS 短信
         */
        @NotBlank(message = "媒介不能为空")
        @Pattern(regexp = "VMS|SMS", message = "媒介类型 SMS , VMS")
        private String media;

        /**
         * 内容
         */
        @NotBlank(message = "内容不能为空")
        @Length(max = 32, message = "内容长度不能大于32")
        private String content;

        /**
         * 手机
         */
        @Size(min = 1, message = "至少需要一个号码")
        private List<String> mobile;



    }
}
