package com.sdg.cmdb.service.control.configurationfile;

import com.alibaba.fastjson.JSONObject;
import com.sdg.cmdb.domain.auth.UserDO;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liangjian on 2016/12/15.
 */
@Service
public class ShadowsocksService extends ConfigurationFileControlAbs {


    public String acqConfig() {
        JSONObject shadowsocks_json = new JSONObject();
        shadowsocks_json.put("server", "0.0.0.0");
        shadowsocks_json.put("local_port", 1080);
        shadowsocks_json.put("timeout", 600);
        shadowsocks_json.put("method", "aes-256-cfb");
        shadowsocks_json.put("timeout", 300);
        this.addUser(shadowsocks_json);
        return shadowsocks_json.toString();
    }


    private void addUser(JSONObject shadowsocks_json) {
        List<UserDO> userDOList = userDao.getAllUser();
        Map<String, String> mapPort = new HashMap<>();
        HashMap<String, String> mapComment = new HashMap<>();
        for (UserDO user : userDOList) {
            String port = String.valueOf(user.getId() + 20000);
            mapPort.put(port, user.getPwd());
            mapComment.put(port, user.getUsername());
        }
        shadowsocks_json.put("port_password", mapPort);
        shadowsocks_json.put("_comment", mapComment);
    }


}
