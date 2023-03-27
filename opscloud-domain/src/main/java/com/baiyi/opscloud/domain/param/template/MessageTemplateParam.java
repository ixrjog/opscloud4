package com.baiyi.opscloud.domain.param.template;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2023/3/27 11:46
 * @Version 1.0
 */
public class MessageTemplateParam {

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class MessageTemplatePageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "展开数据")
        private Boolean extend;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class UpdateMessageTemplate {

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
