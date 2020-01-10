package com.baiyi.opscloud.zabbix.server.impl;

import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.handler.ZabbixHandler;
import com.baiyi.opscloud.zabbix.http.ZabbixRequest;
import com.baiyi.opscloud.zabbix.http.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.mapper.ZabbixHostMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2019/12/31 5:56 下午
 * @Version 1.0
 */
@Slf4j
@Component("ZabbixServer")
public class ZabbixServerImpl implements com.baiyi.opscloud.zabbix.server.ZabbixServer {

    public static final String ZABBIX_RESULT = "result";







}
