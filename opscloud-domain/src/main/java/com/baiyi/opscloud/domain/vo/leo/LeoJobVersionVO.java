package com.baiyi.opscloud.domain.vo.leo;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2023/2/22 21:00
 * @Version 1.0
 */
public class LeoJobVersionVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class JobVersion implements EnvVO.IEnv, Serializable {

        @Serial
        private static final long serialVersionUID = -2925847833549310319L;

        private String jobName;
        private Integer applicationId;
        private Integer envType;
        private EnvVO.Env env;
        private List<DeploymentVersion> deploymentVersions;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class DeploymentVersion implements TagVO.ITags, Serializable {

        @Serial
        private static final long serialVersionUID = 1901939181360045590L;

        public static final DeploymentVersion EMPTY = DeploymentVersion.builder().build();

        private List<TagVO.Tag> tags;

        @Override
        public Integer getBusinessId() {
            return this.assetId;
        }

        @Override
        public Integer getBusinessType() {
            return BusinessTypeEnum.ASSET.getType();
        }

        private DoDeployVersion doDeployVersion;

        private Integer jobId;

        /**
         * env:deploymentName
         */
        @Builder.Default
        private String name = "-";

        private String deploymentName;

        @Builder.Default
        private Integer replicas = 0;

        /**
         * 用于计算版本顺序
         */
        @Builder.Default
        private Integer buildId = -1;

        private String versionTypeDesc;

        /**
         * 资产ID
         */
        private Integer assetId;

        @Builder.Default
        private String image = "-";
        @Builder.Default
        private String versionName = "-";
        @Builder.Default
        private String versionDesc = "";

        private String versionType;

        @Builder.Default
        private Map<String, String> properties = Maps.newHashMap();

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class DoDeployVersion implements Serializable {

        @Serial
        private static final long serialVersionUID = -5057578961795862955L;

        public static final DoDeployVersion INVALID = DoDeployVersion.builder()
                .isActive(false)
                .build();

        @Schema(description = "有效")
        @Builder.Default
        private boolean isActive = true;

        @Schema(description = "构建ID")
        private Integer buildId;

    }

}