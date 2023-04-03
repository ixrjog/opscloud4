package com.baiyi.opscloud.domain.vo.leo;

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
    public static class Job implements TagVO.ITags, EnvVO.IEnv {

        private final Integer businessType = BusinessTypeEnum.LEO_JOB.getType();

        @Schema(name = "最新构建信息")
        private List<LeoBuildVO.LatestBuildInfo> latestBuildInfos;

        @Override
        public Integer getBusinessId() {
            return this.id;
        }

        @Schema(name = "业务标签")
        private List<TagVO.Tag> tags;

        @Schema(name = "环境")
        private EnvVO.Env env;

        @Schema(name = "所属应用")
        private ApplicationVO.Application application;

        @Schema(name = "任务模板")
        private LeoTemplateVO.Template template;

        @Schema(name = "配置对象")
        private Object configDetails;

        private Integer id;

        @Schema(name = "关联任务ID")
        private Integer parentId;

        @Schema(name = "应用ID")
        private Integer applicationId;

        @Schema(name = "显示名称")
        private String name;

        @Schema(name = "任务Key(不可变名称)")
        private String jobKey;

        @Schema(name = "模板版本")
        private String templateVersion;

        @Schema(name = "模板内容")
        private String templateContent;

        @Schema(name = "校验版本")
        private VerifyTemplateVersion verifyTemplateVersion;

        @Schema(name = "默认分支")
        private String branch;

        @Schema(name = "环境类型")
        private Integer envType;

        @Schema(name = "当前构建编号")
        private Integer buildNumber;

        @Schema(name = "隐藏任务")
        private Boolean hide;

        @Schema(name = "有效")
        private Boolean isActive;

        @Schema(name = "模板ID")
        private Integer templateId;

        @Schema(name = "任务超文本链接")
        private String href;

        @Schema(name = "任务参数配置")
        private String jobConfig;

        @Schema(name = "描述")
        private String comment;

        @Schema(name = "构建次数")
        private Integer buildSize;

        @Schema(name = "部署次数")
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
