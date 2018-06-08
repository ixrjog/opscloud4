package com.sdg.cmdb.util.vmware.util;

/**
 * Created by liangjian on 2016/12/27.
 */
public class ClientSesion implements java.io.Serializable {
    private String host;//vcent url
    private String username;//vcent 用户名
    private String password;//vcent 密码

    // set 和  get
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //默认构造函数
    public ClientSesion() {
        super();
    }

    //构造函数
    public ClientSesion(String host, String username, String password) {
        super();
        this.host = host;
        this.username = username;
        this.password = password;
    }


}
