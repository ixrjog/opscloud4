package com.baiyi.opscloud.domain.vo.template;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModel
    public static class Template extends BaseVO implements EnvVO.IEnv {

        @ApiModelProperty(value = "业务模版数量", example = "1")
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
         * 模版key
         */

        private String templateKey;

        /**
         * 模版类型
         */

        private String templateType;


        /**
         * 模版变量
         */
        private String vars;

        /**
         * 模版内容
         */
        private String content;

        /**
         * 描述
         */
        private String comment;

    }

}
