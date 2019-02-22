package com.sdg.cmdb.domain.server;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/6.
 */
@Data
public class ServerDO implements Serializable, Comparable<ServerDO> {
    private static final long serialVersionUID = -8455519494554601001L;

    private long id;
    private long serverGroupId;
    private int loginType;
    private String loginUser;
    private int envType;
    private String publicIp;
    private long publicIpId;
    private String insideIp;
    private long insideIpId;
    private int serverType;
    private String serverName;
    private String area;
    private int useType;
    private String serialNumber;
    private String ciGroup;
    private String content;
    private int zabbixStatus;
    private int zabbixMonitor;
    // 扩展字段 tomcatVersion
    private String extTomcatVersion;
    private String gmtModify;
    private String gmtCreate;

    public ServerDO() {
    }

    public ServerDO(ServerVO serverVO) {
        this.id = serverVO.getId();
        this.serverGroupId = serverVO.getServerGroupDO() == null ? 0l : serverVO.getServerGroupDO().getId();
        this.loginType = serverVO.getLoginType();
        this.loginUser = serverVO.getLoginUser();
        this.envType = serverVO.getEnvType();
        this.publicIp = serverVO.getPublicIP() == null ? "" : serverVO.getPublicIP().getIp();
        this.publicIpId = serverVO.getPublicIP() == null ? 0l : serverVO.getPublicIP().getId();
        this.insideIp = serverVO.getInsideIP() == null ? "" : serverVO.getInsideIP().getIp();
        this.insideIpId = serverVO.getInsideIP() == null ? 0l : serverVO.getInsideIP().getId();
        this.serverType = serverVO.getServerType();
        this.serverName = serverVO.getServerName();
        this.area = serverVO.getArea();
        this.useType = serverVO.getUseType();
        this.serialNumber = serverVO.getSerialNumber();
        this.ciGroup = serverVO.getCiGroup();
        this.content = serverVO.getContent();
        this.gmtCreate = serverVO.getGmtCreate();
        this.gmtModify = serverVO.getGmtModify();
    }


    /**
     * 带列号
     *
     * @return
     */
    public String acqServerName() {
        if (this.envType == ServerDO.EnvTypeEnum.prod.getCode()) {
            return serverName + "-" + serialNumber;
        } else {
            return serverName + "-" + ServerDO.EnvTypeEnum.getEnvTypeName(envType) + "-" + serialNumber;
        }
    }

//    @Override public boolean equals(Object o) {
//        if (o == this)
//            return true;
//        if (!(o instanceof ServerDO))
//            return false;
//        ServerDO s = (ServerDO)o;
//        return s.id == id
//                && s.serverName == serverName
//                && s.insideIp== s.insideIp
//                && s.serialNumber== s.serialNumber
//                && s.envType== s.envType;
//    }
//
//    @Override
//    public int hashCode() {
//        final int prime = 31;//为什么是31？因为这个数需要是质数 31是经验验证的一个能够很好地减小哈希碰撞的质数
//        int result =1;
//        result = prime * result + (int) id ;
//        result = prime * result + ((id == 0) ? 0 : serverName.hashCode());
//        return result;
//    }


    /**
     * 不带列号
     *
     * @return
     */
    public String acqHostname() {
        if (this.envType == ServerDO.EnvTypeEnum.prod.getCode()) {
            return serverName;
        } else {
            return serverName + "-" + ServerDO.EnvTypeEnum.getEnvTypeName(envType);
        }
    }

    @Override
    public String toString() {
     return JSON.toJSONString(this);
    }

    public enum EnvTypeEnum {
        //0 保留／在组中代表的是所有权限
        keep(0, "default"),
        dev(1, "dev"),
        daily(2, "daily"),
        gray(3, "gray"),
        prod(4, "prod"),
        test(5, "test"),
        back(6, "back");
        private int code;
        private String desc;

        EnvTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getEnvTypeName(int code) {
            for (EnvTypeEnum envTypeEnum : EnvTypeEnum.values()) {
                if (envTypeEnum.getCode() == code) {
                    return envTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }


    public enum ServerTypeEnum {
        //0 保留／在组中代表的是所有权限
        ps(0, "PS"),
        vm(1, "VM"),
        ecs(2, "ECS");
        private int code;
        private String desc;

        ServerTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getServerTypeName(int code) {
            for (ServerTypeEnum serverTypeEnum : ServerTypeEnum.values()) {
                if (serverTypeEnum.getCode() == code) {
                    return serverTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }


    @Override
    public int compareTo(ServerDO serverDO) {
        //自定义比较方法，如果认为此实体本身大则返回1，否则返回-1
        try {
            if (Integer.valueOf(this.serialNumber) == Integer.valueOf(serverDO.getSerialNumber()))
                return 0;
            if (Integer.valueOf(this.serialNumber) > Integer.valueOf(serverDO.getSerialNumber()))
                return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
