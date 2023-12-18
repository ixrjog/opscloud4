package com.baiyi.opscloud.domain.vo.leo;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import io.swagger.v3.oas.annotations.media.Schema;
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

        @Schema(description = "业务标签")
        private List<TagVO.Tag> tags;

        @Schema(description = "数据源实例")
        private DsInstanceVO.Instance instance;

        @Override
        public String getInstanceUuid() {
            return this.jenkinsInstanceUuid;
        }

        private Integer quantityUsed;

        private Integer id;

        @Schema(description = "显示名称")
        private String name;

        @Schema(description = "模板版本")
        private String version;

        @Schema(description = "实例UUID")
        private String jenkinsInstanceUuid;

        @Schema(description = "模板名称")
        private String templateName;

        @Schema(description = "模板配置")
        private String templateConfig;

        @Schema(description = "模板参数")
        private String templateParameter;

        @Schema(description = "模板内容")
        private String templateContent;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "描述")
        private String comment;

        @Schema(description = "任务数量")
        private Integer jobSize;

    }

}