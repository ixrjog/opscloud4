package com.baiyi.opscloud.domain.vo.cloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAclListener;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbHttpsListener;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 3:15 下午
 * @Since 1.0
 */
public class AliyunSLBVO {

    @Data
    @ApiModel
    public static class LoadBalancer {

        private Integer id;

        @ApiModelProperty(value = "负载均衡实例ID")
        private String loadBalancerId;

        @ApiModelProperty(value = "负载均衡实例的名称")
        private String loadBalancerName;

        /**
         * 负载均衡实例状态：
         * inactive: 此状态的实例监听不会再转发流量。
         * active: 实例创建后，默认状态为active。
         * locked: 实例已经被锁定。
         */
        @ApiModelProperty(value = "负载均衡实例状态")
        private String loadBalancerStatus;

        @ApiModelProperty(value = "负载均衡实例服务地址")
        private String address;

        @ApiModelProperty(value = "负载均衡实例的网络类型")
        private String addressType;

        @ApiModelProperty(value = "regionId")
        private String regionId;

        @ApiModelProperty(value = "负载均衡实例的地域名称")
        private String regionIdAlias;

        @ApiModelProperty(value = "私网负载均衡实例的交换机ID")
        private String vSwitchId;

        @ApiModelProperty(value = "私网负载均衡实例的专有网络ID")
        private String vpcId;

        /**
         * 私网负载均衡实例的网络类型，取值：
         * vpc：专有网络实例。
         * classic：经典网络实例。
         */
        @ApiModelProperty(value = "私网负载均衡实例的网络类型")
        private String networkType;

        @ApiModelProperty(value = "实例的主可用区ID")
        private String masterZoneId;

        @ApiModelProperty(value = "实例的备可用区ID")
        private String slaveZoneId;

        /**
         * 公网实例的计费方式。取值：
         * 3：paybybandwidth，按带宽计费。
         * 4：paybytraffic，按流量计费（默认值）
         */
        @ApiModelProperty(value = "公网实例的计费方式")
        private String internetChargeType;

        @ApiModelProperty(value = "负载均衡实例创建时间")
        private String createTime;

        /**
         * 负载均衡实例付费类型，取值PayOnDemand或者PrePay
         */
        @ApiModelProperty(value = "负载均衡实例付费类型")
        private String payType;

        @ApiModelProperty(value = "resourceGroupId")
        private String resourceGroupId;

        @ApiModelProperty(value = "IP版本")
        private String addressIpVersion;

        @ApiModelProperty(value = "监听信息")
        private List<Listener> listenerList;

        @ApiModelProperty(value = "管理Nginx配置")
        private Integer isLinkNginx;

        private Map<Integer, List<BackendServer>> backendServers;
    }

    @Data
    @ApiModel
    public static class Listener {

        private Integer id;

        @ApiModelProperty(value = "负载均衡实例的id")
        private String loadBalancerId;

        @ApiModelProperty(value = "负载均衡实例前端使用的端口")
        private Integer listenerPort;

        @ApiModelProperty(value = "负载均衡实例前端使用的协议")
        private String listenerProtocol;

        @ApiModelProperty(value = "是否启用监听转发")
        private String listenerForward;

        @ApiModelProperty(value = "转发到的目的监听端口，必须是已经存在的HTTPS监听端口")
        private Integer forwardPort;

        @ApiModelProperty(value = "负载均衡监听端口和协议描述")
        private String listenDescription;

        @ApiModelProperty(value = "访问控制监听")
        private OcAliyunSlbAclListener accessControlListener;

        @ApiModelProperty(value = "https监听信息")
        private List<OcAliyunSlbHttpsListener> httpsListenerList;
    }

    @Data
    @ApiModel
    public static class AccessControl {

        private Integer id;

        @ApiModelProperty(value = "访问控制策略组id")
        private String slbAclId;

        @ApiModelProperty(value = "访问控制策略组名称")
        private String slbAclName;

        @ApiModelProperty(value = "关联的负载均衡实例的IP地址类型")
        private String addressIpVersion;

        @ApiModelProperty(value = "资源组id")
        private String resourceGroupId;

        @ApiModelProperty(value = "管理监听")
        private List<OcAliyunSlbAclListener> aclListenerList;
    }

    @Data
    @ApiModel
    public static class ServerCertificate {

        private Integer id;

        @ApiModelProperty(value = "服务器证书ID")
        private String serverCertificateId;

        @ApiModelProperty(value = "服务器证书名称")
        private String serverCertificateName;

        @ApiModelProperty(value = "服务器证书的地域")
        private String regionId;

        @ApiModelProperty(value = "服务器证书的指纹")
        private String fingerprint;

        @ApiModelProperty(value = "阿里云证书服务的服务器证书id")
        private String aliCloudCertificateId;

        @ApiModelProperty(value = "阿里云证书服务的服务器证书名称")
        private String aliCloudCertificateName;

        @ApiModelProperty(value = "域名")
        private String commonName;

        @ApiModelProperty(value = "过期时间")
        private String expireTime;

        @ApiModelProperty(value = "过期时间戳")
        private Long expireTimeStamp;

        @ApiModelProperty(value = "域名到期日和当前的时间的天数差值")
        private Integer expirationCurrDateDiff;

        @ApiModelProperty(value = "关联监听")
        private List<OcAliyunSlbHttpsListener> httpsListenerList;

        @ApiModelProperty(value = "更换的服务器证书id")
        private String updateServerCertificateId;

        @ApiModelProperty(value = "更换的服务器证书名")
        private String updateServerCertificateName;
    }

    @Data
    @ApiModel
    public static class AccessControlEntry {

        @ApiModelProperty(value = "访问控制条目ip")
        private String slbAclEntryIp;

        @ApiModelProperty(value = "访问控制条目备注")
        private String slbAclEntryComment;
    }

    @Data
    @ApiModel
    public static class BackendServer {

        @ApiModelProperty(value = "负载均衡实例前端使用的端口")
        private Integer listenerPort;

        @ApiModelProperty(value = "ECS实例id")
        private String serverId;

        @ApiModelProperty(value = "负载均衡实例后端使用的端口")
        private Integer port;

        @ApiModelProperty(value = "ECS实例的IP")
        private String serverIp;

        @ApiModelProperty(value = "负载均衡实例前端使用的协议")
        private String protocol;

        @ApiModelProperty(value = "服务器详情")
        private OcServer ocServer;

        @ApiModelProperty(value = "oc服务器名")
        private String serverName;

        @ApiModelProperty(value = "服务器组id")
        private Integer serverGroupId;
    }
}
