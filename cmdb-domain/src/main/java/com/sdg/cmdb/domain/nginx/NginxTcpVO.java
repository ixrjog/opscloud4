package com.sdg.cmdb.domain.nginx;


import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.kubernetes.KubernetesServiceCluster;
import com.sdg.cmdb.domain.server.EnvType;
import lombok.Data;

import java.io.Serializable;

@Data
public class NginxTcpVO extends NginxTcpDO implements Serializable {
    private static final long serialVersionUID = 3499869460286429981L;

    private String domain;
    private EnvType env;
    private int period = 60;

    public enum DomainEnum {
        prod(4, "ng-tcp.ops.yangege.cn"),
        test(5, "ng-tcp-test.ops.yangege.cn"),
        pre(7, "ng-tcp-pre.ops.yangege.cn");
        private int code;
        private String desc;

        DomainEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getDomainName(int code) {
            for (DomainEnum domainEnum : DomainEnum.values()) {
                if (domainEnum.getCode() == code) {
                    return domainEnum.getDesc();
                }
            }
            return "undefined";
        }
    }


    public static NginxTcpVO builder(KubernetesServiceCluster kubernetesServiceCluster, int envType, String portName) {
        NginxTcpVO nginxTcp = new NginxTcpVO();
        nginxTcp.setServiceName(kubernetesServiceCluster.getKubernetesServiceVO().getName());
        nginxTcp.setServerGroupId(kubernetesServiceCluster.getKubernetesServiceVO().getServerGroupId());
        nginxTcp.setServerGroupName(kubernetesServiceCluster.getKubernetesServiceVO().getServerGroupName());
        nginxTcp.setEnvType(envType);
        nginxTcp.setGmtExpired("2020-08-15 00:00:00");
        nginxTcp.setUserId(2407l);
        nginxTcp.setDisplayName("baiyi<白衣>");
        nginxTcp.setFinished(false);
        nginxTcp.setContent("系统默认映射策略");
        nginxTcp.setNginxPort(kubernetesServiceCluster.getNodePort());
        nginxTcp.setServerPort(kubernetesServiceCluster.getNodePort());
        nginxTcp.setPortName(portName);
        return nginxTcp;
    }

    public static NginxTcpVO builder(KubernetesServiceCluster kubernetesServiceCluster, int envType, String portName, UserDO userDO) {
        NginxTcpVO nginxTcp = new NginxTcpVO();
        nginxTcp.setServiceName(kubernetesServiceCluster.getKubernetesServiceVO().getName());
        nginxTcp.setServerGroupId(kubernetesServiceCluster.getKubernetesServiceVO().getServerGroupId());
        nginxTcp.setServerGroupName(kubernetesServiceCluster.getKubernetesServiceVO().getServerGroupName());
        nginxTcp.setEnvType(envType);
        nginxTcp.setUserId(userDO.getId());
        nginxTcp.setDisplayName(userDO.getUsername() + "<" + userDO.getDisplayName() + ">");
        nginxTcp.setFinished(false);
        nginxTcp.setNginxPort(kubernetesServiceCluster.getNodePort());
        nginxTcp.setServerPort(kubernetesServiceCluster.getNodePort());
        nginxTcp.setPortName(portName);
        return nginxTcp;
    }

    public NginxTcpVO() {
    }

    /**
     * 上帝限速
     *
     * @return
     */
    public int getPeriod() {
        if (this.period > 120)
            return 120;
        return this.period;
    }


}
