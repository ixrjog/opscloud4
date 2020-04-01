package com.baiyi.opscloud.server.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.server.IServer;
import com.baiyi.opscloud.zabbix.server.ZabbixHostServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/1 7:19 下午
 * @Version 1.0
 */
@Slf4j
@Component("ZabbixHost")
public class ZabbixHost extends BaseServer implements IServer {

    @Resource
    private ZabbixHostServer zabbixHostServer;

    @Override
    public boolean sync() {
        return true;
    }


    @Override
    public boolean create(OcServer ocServer) {
        return true;
    }

    @Override
    public boolean disable(OcServer ocServer) {
        return true;
    }

    @Override
    public boolean enable(OcServer ocServer) {
        return true;
    }

    @Override
    public boolean remove(OcServer ocServer) {
        return true;
    }

    @Override
    public boolean update(OcServer ocServer) {
        return true;
    }


}
