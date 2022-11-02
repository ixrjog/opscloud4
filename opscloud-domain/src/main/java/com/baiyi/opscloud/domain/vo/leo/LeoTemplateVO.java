package com.baiyi.opscloud.domain.vo.leo;

import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @Author baiyi
 * @Date 2022/11/1 18:07
 * @Version 1.0
 */
public class LeoTemplateVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Template implements DsInstanceVO.IDsInstance {

        private DsInstanceVO.Instance instance;

        @Override
        public String getInstanceUuid() {
            return this.jenkinsInstanceUuid;
        }

        private Integer id;

        @NotEmpty(message = "名称不能为空")
        @ApiModelProperty(value = "名称")
        private String name;

        @ApiModelProperty(value = "实例UUID")
        private String jenkinsInstanceUuid;

        @ApiModelProperty(value = "模板名称")
        private String templateName;

        @NotEmpty(message = "模板配置不能为空")
        @ApiModelProperty(value = "模板配置")
        private String templateConfig;

        @ApiModelProperty(value = "模板参数")
        private String templateParameter;

        @ApiModelProperty(value = "模板内容")
        private String templateContent;

        @ApiModelProperty(value = "描述")
        private String comment;



    }
}
