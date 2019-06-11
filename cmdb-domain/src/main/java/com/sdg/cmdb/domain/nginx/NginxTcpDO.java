package com.sdg.cmdb.domain.nginx;

import com.alibaba.fastjson.JSON;

import lombok.Data;

import java.io.Serializable;

@Data
public class NginxTcpDO implements Serializable {
    private static final long serialVersionUID = -5614234418330164687L;

    private long id;
    private String serviceName;
    private String content;
    private long serverGroupId;
    private String serverGroupName;
    private int envType;

    private int serverPort; // 代理端口
    private int nginxPort; // 监听端口
    private String portName; // 端口名称 或 协议名称

    private String gmtExpired; // 过期时间
    private long userId;   // 操作人
    private String displayName;
    private boolean finished; // 是否完成，如果True则不计算过期时间

    private String gmtCreate;
    private String gmtModify;

    public NginxTcpDO() {
    }

    public NginxTcpDO(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
