package com.baiyi.opscloud.domain.vo.leo;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/4 14:47
 * @Version 1.0
 */
public class LeoJobVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Job implements TagVO.ITags, EnvVO.IEnv {

        private final Integer businessType = BusinessTypeEnum.LEO_JOB.getType();

        @Override
        public Integer getBusinessId() {
            return this.id;
        }

        @ApiModelProperty(value = "业务标签")
        private List<TagVO.Tag> tags;

        @ApiModelProperty(value = "环境")
        private EnvVO.Env env;

        @ApiModelProperty(value = "所属应用")
        private ApplicationVO.Application application;

        @ApiModelProperty(value = "任务模板")
        private LeoTemplateVO.Template template;

        @ApiModelProperty(value = "配置对象")
        private Object configDetails;

        private Integer id;

        @ApiModelProperty(value = "关联任务ID")
        private Integer parentId;

        @ApiModelProperty(value = "应用ID")
        private Integer applicationId;

        @ApiModelProperty(value = "显示名称")
        private String name;

        @ApiModelProperty(value = "任务Key(不可变名称)")
        private String jobKey;

        @ApiModelProperty(value = "模板版本")
        private String templateVersion;

        @ApiModelProperty(value = "模板内容")
        private String templateContent;

        @ApiModelProperty(value = "校验版本")
        private VerifyTemplateVersion verifyTemplateVersion;

        @ApiModelProperty(value = "默认分支")
        private String branch;

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        @ApiModelProperty(value = "当前构建编号")
        private Integer buildNumber;

        @ApiModelProperty(value = "隐藏任务")
        private Boolean hide;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        @ApiModelProperty(value = "模版ID")
        private Integer templateId;

        @ApiModelProperty(value = "任务超文本链接")
        private String href;

        @ApiModelProperty(value = "任务参数配置")
        private String jobConfig;

        @ApiModelProperty(value = "描述")
        private String comment;

        @ApiModelProperty(value = "构建次数")
        private Integer buildSize;

        @ApiModelProperty(value = "部署次数")
        private Integer deploySize;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerifyTemplateVersion {

        @Builder.Default
        private String type = "success";

        @Builder.Default
        private Boolean isIdentical = true;

        private String displayVersion;

    }

}
