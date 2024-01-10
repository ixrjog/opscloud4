package com.baiyi.opscloud.domain.vo.template;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2023/3/27 11:43
 * @Version 1.0
 */
public class MessageTemplateVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class MessageTemplate extends BaseVO {

        private Integer id;

        /**
         * 名称
         */
        private String name;

        /**
         * 消息关键字
         */
        private String msgKey;

        /**
         * 消息类型
         */
        private String msgType;

        /**
         * 消费者
         */
        private String consumer;

        /**
         * 标题
         */
        private String title;


        /**
         * 通知模板
         */
        private String msgTemplate;

        /**
         * 描述
         */
        private String comment;

    }

}