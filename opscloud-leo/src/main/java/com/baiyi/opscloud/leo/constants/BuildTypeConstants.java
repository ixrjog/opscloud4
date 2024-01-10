package com.baiyi.opscloud.leo.constants;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @Author baiyi
 * @Date 2023/4/26 11:37
 * @Version 1.0
 */
public interface BuildTypeConstants {

    @Schema(description = "Maven2方包发布")
    String MAVEN_PUBLISH = "maven-publish";

    @Schema(description = "前端发布, 目前用K8S发布")
    String FRONT_END = "front-end";

    @Schema(description = "标准K8S发布")
    String KUBERNETES_IMAGE = "kubernetes-image";

}