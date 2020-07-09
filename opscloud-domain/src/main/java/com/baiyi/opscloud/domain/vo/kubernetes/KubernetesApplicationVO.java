package com.baiyi.opscloud.domain.vo.kubernetes;

import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/1 6:44 下午
 * @Version 1.0
 */
public class KubernetesApplicationVO {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Application {

        @ApiModelProperty(value = "实例详情")
        private List<Instance> instances;
        @ApiModelProperty(value = "服务器组")
        private ServerGroupVO.ServerGroup serverGroup;

        @ApiModelProperty(value = "授权")
        private Boolean authorized;

        private Integer id;
        private String name;
        private Integer serverGroupId;
        private Integer businessId;
        private Date createTime;
        private Date updateTime;
        private String comment;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Instance {

        @ApiModelProperty(value = "应用详情")
        private Application application;

        @ApiModelProperty(value = "无状态")
        private KubernetesDeploymentVO.Deployment deployment;

        @ApiModelProperty(value = "服务")
        private KubernetesServiceVO.Service service;

        private EnvVO.Env env;

        private Integer id;
        private Integer applicationId;
        private String instanceName;
        private Integer envType;
        private String envLabel;
        private Date createTime;
        private Date updateTime;
        private String templateVariable;
    }
}
