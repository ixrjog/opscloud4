package com.sdg.cmdb.domain.server;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/1.
 */
public class ServerGroupDO implements Serializable {
    private static final long serialVersionUID = 552642567540413587L;

    private long id;

    private String name;

    private String content;

    private String ipNetwork;

    private int useType;

    private String gmtCreate;

    private String gmtModify;

    public ServerGroupDO() {
    }

    public ServerGroupDO(long id) {
        this.id = id;
    }

    public ServerGroupDO(String name,String content,int useType) {
        this.name = name;
        this.content = content;
        this.useType = useType;
    }

    public ServerGroupDO(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIpNetwork() {
        return ipNetwork;
    }

    public void setIpNetwork(String ipNetwork) {
        this.ipNetwork = ipNetwork;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    public String acqShortName() {
        return name.replace("group_", "");
    }

    @Override
    public String toString() {
        return "ServerGroupDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", ipNetwork='" + ipNetwork + '\'' +
                ", useType='" + useType + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }

    public enum UseTypeEnum {
        //0 保留
        zookeeper(1, "zookeeper"),
        webservice(2, "web-service"),
        mysql(3, "mysql"),
        other(4, "other"),
        webphp(5, "web-php"),
        publicserver(6, "public"),
        redis(7, "redis"),
        webserver(8, "web-server"),
        frontend(9, "front-end"),
        bi(10, "bi");
        private int code;
        private String desc;

        UseTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getUseTypeEnum(int code) {
            for (UseTypeEnum useTypeEnum : UseTypeEnum.values()) {
                if (useTypeEnum.getCode() == code) {
                    return useTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

}
