package com.baiyi.opscloud.datasource.kubernetes.model;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import io.fabric8.kubernetes.api.model.ContainerState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2022/6/21 10:10
 * @Version 1.0
 */
public class ContainerBO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContainerStatus {

        @Builder.Default
        private String containerID = "-";
        @Builder.Default
        private String image = "-";
        @Builder.Default
        private String imageID = "-";
        @Builder.Default
        private ContainerState lastState = new ContainerState();
        @Builder.Default
        private String name = "-";
        @Builder.Default
        private Boolean ready = false;
        @Builder.Default
        private Integer restartCount = 0;
        @Builder.Default
        private Boolean started = false;
        @Builder.Default
        private ContainerState state = new ContainerState();

        public io.fabric8.kubernetes.api.model.ContainerStatus toContainerStatus() {
            return BeanCopierUtil.copyProperties(this, io.fabric8.kubernetes.api.model.ContainerStatus.class);
        }

    }

}