package com.baiyi.opscloud.domain.vo.template;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2021/12/6 10:39 AM
 * @Version 1.0
 */
public class TemplateVO {

    public interface ITemplate {

        Integer getTemplateId();

        void setTemplate(TemplateVO.Template template);

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Template extends BaseVO implements EnvVO.IEnv {

        @Schema(description = "业务模板数量", example = "1")
        private Integer bizTemplateSize;

        private EnvVO.Env env;

        private Integer id;

        /**
         * 名称
         */
        private String name;

        /**
         * 环境类型
         */
        private Integer envType;

        /**
         * 实例类型
         */
        private String instanceType;

        /**
         * 模板key
         */

        private String templateKey;

        /**
         * 模板类型
         */

        private String templateType;


        /**
         * 模板变量
         */
        private String vars;

        private String kind;

        /**
         * 模板内容
         */
        private String content;

        /**
         * 描述
         */
        private String comment;

    }

}