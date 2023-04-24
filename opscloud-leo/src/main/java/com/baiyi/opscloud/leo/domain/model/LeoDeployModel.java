package com.baiyi.opscloud.leo.domain.model;

import com.baiyi.opscloud.common.util.YamlUtil;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.vo.leo.LeoDeployingVO;
import com.baiyi.opscloud.leo.domain.model.base.YamlDump;
import com.baiyi.opscloud.leo.exception.LeoJobException;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/12/5 17:38
 * @Version 1.0
 */
public class LeoDeployModel {

    public static DeployConfig load(LeoDeploy leoDeploy) {
        return load(leoDeploy.getDeployConfig());
    }

    /**
     * 从配置加载
     *
     * @param config
     * @return
     */
    public static DeployConfig load(String config) {
        if (StringUtils.isEmpty(config)) {
            return DeployConfig.EMPTY_DEPLOY;
        }
        try {
            return YamlUtil.loadAs(config, DeployConfig.class);
        } catch (Exception e) {
            throw new LeoJobException("转换配置文件错误: {}", e.getMessage());
        }

    }

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeployConfig extends YamlDump {
        private static final DeployConfig EMPTY_DEPLOY = DeployConfig.builder().build();
        private Deploy deploy;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Deploy {
        private String comment;
        // Kubernetes
        private LeoBaseModel.Kubernetes kubernetes;
        @Schema(description = "字典")
        private Map<String, String> dict;
        @Schema(description = "通知")
        private LeoBaseModel.Notify notify;
        @Schema(description = "部署类型")
        private String deployType;
        @Schema(description = "部署版本1")
        private DeployVersion deployVersion1;
        @Schema(description = "部署版本2")
        private DeployVersion deployVersion2;
        @Schema(description = "构建标签")
        private List<String> tags;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeployVersion {

        @Schema(description = "未知的版本")
        public static final DeployVersion UNKNOWN = DeployVersion.builder().build();

        // 构建ID
        @Builder.Default
        private Integer buildId = 0;
        // 版本名称
        @Builder.Default
        private String versionName = "unknown";

        @Builder.Default
        private String versionDesc = "";
        // 镜像
        private String image;

        @Builder.Default
        private List<LeoDeployingVO.PodDetails> pods = Lists.newArrayList();

        private String comment;

    }

}