package com.baiyi.opscloud.server.utils;

import com.alibaba.fastjson.JSONArray;
import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.server.bo.ZabbixInterfaceBO;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostgroup;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/2 3:12 下午
 * @Version 1.0
 */
public class ZabbixUtils {

    public static JSONArray buildMacrosParameter(Map<String, String> serverAttributeMap) {
        if (!serverAttributeMap.containsKey(Global.SERVER_ATTRIBUTE_ZABBIX_HOST_MACROS))
            return new JSONArray();
        String macrosOpt = serverAttributeMap.get(Global.SERVER_ATTRIBUTE_ZABBIX_HOST_MACROS);
        if (!StringUtils.isEmpty(macrosOpt)) {
            try {
                JSONArray macros = JSONArray.parseArray(macrosOpt);
                if (macros.size() != 0)
                    return macros;
            } catch (Exception e) {
            }
        }
        return new JSONArray();
    }

    public static List<Object> buildInterfacesParameter(String manageIp) {
        ZabbixInterfaceBO interfaceBO = ZabbixInterfaceBO.builder()
                .ip(manageIp)
                .build();
        List<Object> list = Lists.newArrayList();
        list.add(interfaceBO);
        return list;
    }

    public static List<Map<String, Object>> buildParameter(String paramName, Object param) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(paramName, param);
        List<Map<String, Object>> params = Lists.newArrayList();
        params.add(map);
        return params;
    }


    /**
     * groups:
     * [{"groupid": "50"}]
     *
     * @param zabbixHostgroup
     */
    public static List<Map<String, Object>> buildGroupsParameter(ZabbixHostgroup zabbixHostgroup) {
        return buildParameter("groupid", zabbixHostgroup.getGroupid());
    }
}
