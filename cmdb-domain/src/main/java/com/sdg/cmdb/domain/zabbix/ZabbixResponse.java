package com.sdg.cmdb.domain.zabbix;

import com.alibaba.fastjson.JSONArray;

/**
 * Created by liangjian on 2016/12/19.
 */
public class ZabbixResponse {

    private int id = 0;

    public int getId() {
        return id;
    }

    private JSONArray result;

    public JSONArray getResult() {
        return result;
    }

    private String idKey;

    public ZabbixResponse(JSONArray result, String idKey, int id) {
        if (result == null) return;
        this.result = result;
        this.id = id;
        this.idKey = idKey;
    }

    public ZabbixResponse(JSONArray result, String id) {
        if (result != null) this.result = result;
        if (id != null) this.id = Integer.parseInt(id);
    }


    public ZabbixResponse(JSONArray result) {
        if (result == null) return;
        this.result = result;
    }

    public String print() {
        return "result:" + this.result + "\n" + "idKey:" + idKey + "\n" + "id:" + id + "\n";
    }

}
