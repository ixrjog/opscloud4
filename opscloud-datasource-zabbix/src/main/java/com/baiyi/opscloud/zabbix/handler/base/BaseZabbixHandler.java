package com.baiyi.opscloud.zabbix.handler.base;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.http.IZabbixRequest;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/12 9:37 上午
 * @Version 1.0
 */
public abstract class BaseZabbixHandler<T> {

    @Resource
    private ZabbixServer zabbixServer;

    protected JsonNode call(ZabbixConfig.Zabbix zabbix, IZabbixRequest request) {
        return zabbixServer.call(zabbix, request);
    }

    protected List<T> mapperList(JsonNode data, Class<T> tClass) {
        return ZabbixMapper.mapperList(data, tClass);
    }

    protected T mapperListGetOne(JsonNode data, Class<T> tClass) {
        List<T> list = mapperList(data, tClass);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    protected T mapperOne(JsonNode data, Class<T> tClass) {
       return ZabbixMapper.mapper(data, tClass);
    }

}
