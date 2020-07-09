package com.baiyi.opscloud.domain.vo.kubernetes;

import com.baiyi.opscloud.domain.vo.server.ServerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/4 3:20 下午
 * @Version 1.0
 */
public class KubernetesPodVO {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Pod {

        @ApiModelProperty(value = "实例名称")
        private String instanceName;

        @ApiModelProperty(value = "名称")
        private String name;

        @ApiModelProperty(value = "容器详情")
        private List<Container> containers;

        @ApiModelProperty(value = "宿主服务器详情")
        private ServerVO.Server server;

        private String hostIP;

        private String podIP;

        @ApiModelProperty(value = "运行阶段",example = "Runnind")
        private String phase;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Container {

        @ApiModelProperty(value = "容器镜像",example ="registry.example.com/opscloud/beauty-service:daily-beauty-service_daily200214_fd3eb799" )
        private String image;

        @ApiModelProperty(value = "容器镜像",example ="docker-pullable://registry.example.com/opscloud/beauty-service@sha256:9307ceb4fa4da6615c1a1558eb8767c7e7c076d8cc466ccfc36a8bec6a44f616" )
        private String imageID;

        private String name;

        private Boolean ready;

        private Boolean started;

        @ApiModelProperty(value = "容器ID",example ="docker://9061167c28227408ff38d76703c5562da88a73a12bc33f01acf4834e66577852" )
        private String containerID;

        @ApiModelProperty(value = "容器12位ID",example ="9061167c2822" )
        private String id;

    }
}
