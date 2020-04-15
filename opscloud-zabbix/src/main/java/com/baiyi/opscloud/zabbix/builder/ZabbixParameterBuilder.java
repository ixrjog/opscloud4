package com.baiyi.opscloud.zabbix.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/2 10:19 上午
 * @Version 1.0
 */
public class ZabbixParameterBuilder {

    /**
     * "interfaces": [
     *             {
     *                 "type": 1,
     *                 "main": 1,
     *                 "useip": 1,
     *                 "ip": "192.168.3.1",
     *                 "dns": "",
     *                 "port": "10050"
     *             }
     *         ],
     * @param parameterList
     * @return
     */
    public static List<LinkedHashMap<String, String>> buildLinkedParameter(ZabbixParameter ... parameterList){
        LinkedHashMap<String, String> linkedMap = Maps.newLinkedHashMap();
        for(ZabbixParameter parameter : parameterList)
            linkedMap.put(parameter.getName(),parameter.getValue());
        List<LinkedHashMap<String, String>> list = Lists.newArrayList();
        list.add(linkedMap);
        return list;
    }



}
