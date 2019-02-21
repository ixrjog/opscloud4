package com.sdg.cmdb.domain.server;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/1.
 */
@Data
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

    public String acqShortName() {
        return name.replace("group_", "");
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
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
