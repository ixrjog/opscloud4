package com.baiyi.opscloud.sshcore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/14 5:56 下午
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KubernetesResource {

    private Integer applicationId;
    private Integer businessId;
    private Integer businessType;
    private String resourceType;
    private List<Pod> pods;
    private Integer lines;

    @Data
    public static class Pod {
        private String name;
        private String namespace;
        private String podIp;
        private List<Container> containers;
    }

    @Data
    public static class Container {
        private String name;
    }

}