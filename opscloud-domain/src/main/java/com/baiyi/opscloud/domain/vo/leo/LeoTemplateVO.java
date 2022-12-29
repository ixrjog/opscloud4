package com.baiyi.opscloud.domain.vo.leo;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    public static class Template implements DsInstanceVO.IDsInstance, TagVO.ITags {

        private final Integer businessType = BusinessTypeEnum.LEO_TEMPLATE.getType();

        @Override
        public Integer getBusinessId() {
            return this.id;
        }

        @ApiModelProperty(value = "业务标签")
        private List<TagVO.Tag> tags;

        @ApiModelProperty(value = "数据源实例")
        private DsInstanceVO.Instance instance;

        @Override
        public String getInstanceUuid() {
            return this.jenkinsInstanceUuid;
        }

        private Integer quantityUsed;

        private Integer id;

        @ApiModelProperty(value = "显示名称")
        private String name;

        @ApiModelProperty(value = "模板版本")
        private String version;

        @ApiModelProperty(value = "实例UUID")
        private String jenkinsInstanceUuid;

        @ApiModelProperty(value = "模板名称")
        private String templateName;

        @ApiModelProperty(value = "模板配置")
        private String templateConfig;

        @ApiModelProperty(value = "模板参数")
        private String templateParameter;

        @ApiModelProperty(value = "模板内容")
        private String templateContent;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        @ApiModelProperty(value = "描述")
        private String comment;

        @ApiModelProperty(value = "任务数量")
        private Integer jobSize;

    }

}
