package com.sdg.cmdb.domain.auth;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zxxiao on 16/9/19.
 */
@Data
public class UserDO implements Serializable {
    private static final long serialVersionUID = 3444096336277588871L;

    private long id;

    private String mail;

    private String displayName;

    private String username;

    private String token;

    /*
    联系电话
     */
    private String mobile;

    /*
    登录密码
     */
    private String pwd;

    /*
    公钥
     */
    private String rsaKey;

    /*
    0：未授权；1：已授权
     */
    private int authed = 0;

    /*
    0：未授权；1：已授权
    */
    private int zabbixAuthed;

    /*
    是否无效。0：无效；1：有效
     */
    private int invalid;

    private List<ResourceDO> resourceDOList;

    private long roleId;

    /*
    机器数
     */
    private long servers;

    private int sPort;

    private String gmtCreate;

    private String gmtModify;

    public UserDO() {
    }

    public UserDO(String username) {
        this.username = username;
    }

    public UserDO(String mail, String displayName, String username, String token) {
        this.mail = mail;
        this.displayName = displayName;
        this.username = username;
        this.token = token;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


    public enum ZabbixAuthType {
        noAuth(0, "未授权"),
        authed(1, "已授权");
        private int code;
        private String msg;

        ZabbixAuthType(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }


    public enum AuthType {
        noAuth(0, "未授权"),
        authed(1, "已授权");
        private int code;
        private String msg;

        AuthType(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    public enum Invalid {
        invalid(0, "无效"),
        noInvalid(1, "有效");
        private int code;
        private String msg;

        Invalid(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
