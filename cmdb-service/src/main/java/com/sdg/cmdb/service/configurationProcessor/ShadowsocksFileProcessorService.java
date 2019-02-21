package com.sdg.cmdb.service.configurationProcessor;

import com.alibaba.fastjson.JSONObject;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.util.EncryptionUtil;
import com.sdg.cmdb.domain.ss.SsVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShadowsocksFileProcessorService extends ConfigurationProcessorAbs {

    static public final String SS_METHOD = "aes-256-cfb";

    @Value("#{cmdb['ss.servers']}")
    private String ssServers;

    //String[] ssServers = {"hk-ss1.ops.yangege.cn", "us-w-ss2.ops.yangege.cn", "hz-ss3.ops.yangege.cn"};

    public String getConfig() {
        JSONObject shadowsocks_json = new JSONObject();
        shadowsocks_json.put("server", "0.0.0.0");
        shadowsocks_json.put("local_port", 1080);
        shadowsocks_json.put("timeout", 600);
        shadowsocks_json.put("method", SS_METHOD);
        shadowsocks_json.put("timeout", 300);
        this.addUser(shadowsocks_json);
        return shadowsocks_json.toString();
    }

    private void addUser(JSONObject shadowsocks_json) {
        List<UserDO> userDOList = userDao.getAllUser();
        Map<String, String> mapPort = new HashMap<>();
        HashMap<String, String> mapComment = new HashMap<>();
        for (UserDO user : userDOList) {
            // TODO 支持自定义端口
            String port= getSPort(user);
            mapPort.put(port, user.getPwd());
            mapComment.put(port, user.getUsername());
        }
        shadowsocks_json.put("port_password", mapPort);
        shadowsocks_json.put("_comment", mapComment);
    }

    public List<SsVO> getSsByUser(UserDO userDO) {
        // ss://bf-cfb-auth:test@192.168.100.1:8888
        List<SsVO> servers = new ArrayList<>();

        String[] serverList = ssServers.split(",");
        boolean isCheck = true;
        for (String server : serverList) {
            SsVO ssVO = new SsVO();
            ssVO.setServer(server);
            if (isCheck) {
                ssVO.setChecked(true);
                isCheck = false;
            }
            ssVO.setPort(Integer.valueOf(getSPort(userDO)));
            String key = SS_METHOD + ":" + userDO.getPwd() + "@" + ssVO.getServer() + ":" + ssVO.getPort();
            try {
                ssVO.setQrcode(EncryptionUtil.getSsQcode(key));
            } catch (Exception e) {
                e.printStackTrace();
            }
            servers.add(ssVO);
        }
        return servers;
    }

    private String getSPort(UserDO userDO){
        String port;
        if (userDO.getSPort() == 0) {
            port = String.valueOf(userDO.getId() + 20000);
        } else {
            port = userDO.getSPort() + "";
        }
        return port;
    }


}
