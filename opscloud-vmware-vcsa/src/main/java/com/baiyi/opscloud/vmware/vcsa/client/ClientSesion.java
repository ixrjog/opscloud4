package com.baiyi.opscloud.vmware.vcsa.client;

import com.baiyi.opscloud.vmware.vcsa.config.VcsaConfig;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by liangjian on 2016/12/27.
 */
@Data
public class ClientSesion implements Serializable {
    private static final long serialVersionUID = 7860359014811417959L;
    private String host;//vcent url
    private String username;//vcent 用户名
    private String password;//vcent 密码

    //默认构造函数
    public ClientSesion() {}

    //构造函数
    public ClientSesion(VcsaConfig config) {
        this.host = config.getHost();
        this.username = config.getUser();
        this.password = config.getPassword();
    }

}
