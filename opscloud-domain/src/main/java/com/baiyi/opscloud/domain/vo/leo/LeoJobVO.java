package com.baiyi.opscloud.domain.vo.leo;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import io.swagger.v3.oas.annotations.media.Schema;
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
    public static class Job implements TagVO.ITags, EnvVO.IEnv, BaseBusiness.IBusiness {

        private final Integer businessType = BusinessTypeEnum.LEO_JOB.getType();

        @Schema(description = "最新构建信息")
        private List<LeoBuildVO.LatestBuildInfo> latestBuildInfos;

        @Override
        public Integer getBusinessId() {
            return this.id;
        }

        @Schema(description = "业务标签")
        private List<TagVO.Tag> tags;

        @Schema(description = "环境")
        private EnvVO.Env env;

        @Schema(description = "所属应用")
        private ApplicationVO.Application application;

        @Schema(description = "任务模板")
        private LeoTemplateVO.Template template;

        @Schema(description = "配置对象")
        private Object configDetails;

        private Integer id;

        @Schema(description = "关联任务ID")
        private Integer parentId;

        @Schema(description = "应用ID")
        private Integer applicationId;

        @Schema(description = "显示名称")
        private String name;

        @Schema(description = "任务Key(不可变名称)")
        private String jobKey;

        @Schema(description = "构建类型")
        private String buildType;

        @Schema(description = "模板版本")
        private String templateVersion;

        @Schema(description = "模板内容")
        private String templateContent;

        @Schema(description = "校验版本")
        private VerifyTemplateVersion verifyTemplateVersion;

        @Schema(description = "默认分支")
        private String branch;

        @Schema(description = "环境类型")
        private Integer envType;

        @Schema(description = "当前构建编号")
        private Integer buildNumber;

        @Schema(description = "隐藏任务")
        private Boolean hide;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "模板ID")
        private Integer templateId;

        @Schema(description = "任务超文本链接")
        private String href;

        @Schema(description = "任务参数配置")
        private String jobConfig;

        @Schema(description = "描述")
        private String comment;

        @Schema(description = "构建次数")
        private Integer buildSize;

        @Schema(description = "部署次数")
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