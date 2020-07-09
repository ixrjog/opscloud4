package com.baiyi.opscloud.domain.vo.kubernetes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/24 4:45 下午
 * @Version 1.0
 */
public class KubernetesClusterVO {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Cluster {

        private List<KubernetesClusterNamespaceVO.Namespace> namespaces;

        @ApiModelProperty(value = "主键", example = "1")
        private Integer id;

        @ApiModelProperty(value = "集群名称")
        private String name;

        @ApiModelProperty(value = "管理url")
        private String masterUrl;

        @ApiModelProperty(value = "环境类型(此字段未使用)", example = "0")
        private Integer envType;

        @ApiModelProperty(value = "服务器组id", example = "1")
        private Integer serverGroupId;

        @ApiModelProperty(value = "服务器组名称")
        private String serverGroupName;

        private String comment;

        @ApiModelProperty(value = "配置文件")
        private String kubeconfig;

        private Date createTime;

        private Date updateTime;

    }

}
