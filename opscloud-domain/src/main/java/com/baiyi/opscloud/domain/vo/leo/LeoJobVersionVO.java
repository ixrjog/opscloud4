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

    /**
     * # 灰色（副本0）
     * background-color: #9d9fa3
     * <p>
     * # 蓝
     * background-color: #128ca8
     * <p>
     * # 绿
     * background-color: #2bbe32
     * <p>
     * # 其他版本
     * background-color: #e56c0d
     */
    public interface VersionColors {
        String BLUE = "#128ca8";
        String GREEN = "#2bbe32";
        String OTHER = "#e56c0d";
        String OFFLINE = "#9d9fa3";
    }


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

        private String versionTypeDesc;

        /**
         * background-color
         */
        private String versionColor = VersionColors.OTHER;

        @Builder.Default
        private String image = "-";
        @Builder.Default
        private String versionName = "-";
        @Builder.Default
        private String versionDesc = "";

        private String versionType;

    }

}
