package com.baiyi.opscloud.domain.param.cloud;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 11:25 上午
 * @Since 1.0
 */
public class AliyunSLBParam {

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "负载均衡状态")
        private String loadBalancerStatus;

        @ApiModelProperty(value = "负载均衡网络类型")
        private String addressType;
    }

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class SCPageQuery extends PageParam {

        @ApiModelProperty(value = "关键字查询")
        private String queryName;
    }

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class ACLPageQuery extends PageParam {

        @ApiModelProperty(value = "访问控制策略组名称")
        private String queryName;
    }

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class HttpsListenerQuery {

        @ApiModelProperty(value = "负载均衡实例的id")
        @NotNull
        private String loadBalancerId;

        @ApiModelProperty(value = "负载均衡实例http监听前端使用的端口")
        @NotNull
        private Integer httpsListenerPort;
    }

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class SetUpdateSC {

        @ApiModelProperty(value = "主键")
        @NotNull
        private Integer id;

        @ApiModelProperty(value = "更换的证书id")
        @NotBlank
        private String updateServerCertificateId;
    }

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class ReplaceSC {

        @ApiModelProperty(value = "负载均衡实例的id")
        private String loadBalancerId;

        @ApiModelProperty(value = "监听端口")
        private Integer listenerPort;

        @ApiModelProperty(value = "更换的证书id")
        @NotNull
        private String updateServerCertificateId;

        @ApiModelProperty(value = "服务器证书类型")
        @NotNull
        private Integer serverCertificateType;

        @Column(name = "域名扩展ID")
        private String domainExtensionId;
    }
}
