package com.sdg.cmdb.domain.keybox;

import java.io.Serializable;

/**
 * Created by zxxiao on 2016/11/9.
 */
public class WebSocketContext<T> implements Serializable {
    private static final long serialVersionUID = 1871660891511518061L;

    /**
     * 唯一身份标识
     */
    private String id;

    /**
     * 请求身份
     */
    private String token;

    /**
     * 数据请求类型:
     * init
     * cmd
     */
    private String requestType;

    private T body;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "WebSocketContext{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", requestType='" + requestType + '\'' +
                ", body=" + body +
                '}';
    }

    public enum RequestTypeEnum {
        init("init", "会话初始建立"),
        keyCode("keyCode", "指令"),
        cmd("command", "交互命令"),
        resize("resize", "改变形体"),
        close("close", "关闭ssh会话"),
        subServerGroup("serverGroup", "基于服务器组订阅"),
        subTaskChain("taskChain", "订阅任务链表");
        private String code;
        private String desc;

        RequestTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public String getDescByCode(String code) {
            for(RequestTypeEnum typeEnum : RequestTypeEnum.values()) {
                if (typeEnum.getCode().equals(code)) {
                    return typeEnum.getDesc();
                }
            }
            return "未知类型";
        }
    }
}
