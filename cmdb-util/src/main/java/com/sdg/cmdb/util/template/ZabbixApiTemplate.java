package com.sdg.cmdb.util.template;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * Created by liangjian on 16/11/4.
 */
public class ZabbixApiTemplate {

    public static String authID;


    public static ZabbixApiTemplate builder(){
        ZabbixApiTemplate zat= new ZabbixApiTemplate();
        return zat;
    }

    private Map acqTemplate(String authID,String method,Map paramMap){
        Map<String, Object> map = new HashMap<>();
        map.put("jsonrpc", "2.0");
        map.put("method", method);
        //Map<String, Object> paramMap = new HashMap<>();
        //paramMap.put("output","extend");
        //paramMap.put("selectInterface","extend");
        //map.put("params", paramMap);
        map.put("auth",authID);
        map.put("id", 1);
        map.put("params", paramMap);
        return map;
    }


    /**
     * 登陆api的json模版
     * @param user
     * @param password
     * @return
     */
    public Map login(String user,String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("jsonrpc", "2.0");
        map.put("method", "user.login");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("user", user);
        paramMap.put("password", password);
        map.put("params", paramMap);
        map.put("id", 0);
        return map;
    }


    public Map getProxyServer(String authID) {
        Map<String, Object> paramMap  = new HashMap<>();
        paramMap.put("output", "extend");
        paramMap.put("selectInterface", "extend");
        return acqTemplate(authID,"proxy.get",paramMap);
    }

    public Map getHostByName(String authID,String hostName){
        Map<String, Object> paramMap  = new HashMap<>();
        paramMap.put("output", "extend");
        Map<String, Object> filter  = new HashMap<>();
        filter.put("host",hostName);
        paramMap.put("filter", filter);
        return acqTemplate(authID,"host.get",paramMap);
    }


}
