package com.baiyi.opscloud.domain.vo.leo;

import com.baiyi.opscloud.domain.vo.env.EnvVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

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
    @ApiModel
    public static class JobVersion implements EnvVO.IEnv, Serializable {

        private static final long serialVersionUID = -2925847833549310319L;

        private String jobName;
        private EnvVO.Env env;
        private Integer envType;
        private List<DeploymentVersion> deploymentVersions;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class DeploymentVersion implements Serializable {

        private static final long serialVersionUID = 1901939181360045590L;

        public static final DeploymentVersion EMPTY = DeploymentVersion.builder().build();

        /**
         * env:deploymentName
         */
        @Builder.Default
        private String name = "-";
        private Integer replicas = 0;

        /**
         * 用于计算版本顺序
         */
        private Integer buildId = -1;

        @Builder.Default
        private String image = "-";
        @Builder.Default
        private String versionName = "-";
        @Builder.Default
        private String versionDesc = "";

    }

}
