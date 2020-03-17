package com.baiyi.opscloud.vmware.vcsa.esxi.impl;

import com.baiyi.opscloud.vmware.vcsa.esxi.VcsaESXi;
import com.baiyi.opscloud.vmware.vcsa.handler.VcsaHandler;
import com.baiyi.opscloud.vmware.vcsa.instance.ESXiInstance;
import com.google.common.collect.Lists;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.mo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/15 12:32 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class VcsaESXiImpl implements VcsaESXi {

    @Resource
    private VcsaHandler vcsaHandler;

    public static final String SEARCH_HOSTSYSTEM_TYPE = "HostSystem";

    @Override
    public List<ESXiInstance> getInstanceList() {
        try {
            ManagedEntity[] mes = vcsaHandler.searchManagedEntities(SEARCH_HOSTSYSTEM_TYPE);
            if (mes.length != 0) return convert(mes);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("VCSA SearchManagedEntities HostSystem Error");
        }
        return Collections.emptyList();
    }

    @Override
    public String getZone() {
        return vcsaHandler.getZone();
    }

    @Override
    public ESXiInstance getInstance(String serverName) {
        try {
            ManagedEntity me = vcsaHandler.searchManagedEntity(SEARCH_HOSTSYSTEM_TYPE, serverName);
            if (me != null) return convert(me);
        } catch (Exception e) {
            log.error("VCSA SearchManagedEntities HostSystem Error");
        }
        return null;
    }


    private List<ESXiInstance> convert(ManagedEntity[] mes) {
        List<ESXiInstance> esxiList = Lists.newArrayList();
        for (ManagedEntity me : mes)
            esxiList.add(convert(me));
        return esxiList;
    }

    private ESXiInstance convert(ManagedEntity me) {
        HostSystem hostSystem = (HostSystem) me;
        return ESXiInstance.builder()
                .hostHardwareInfo(hostSystem.getHardware())
                .hostSummary(hostSystem.getSummary())
                .datastoreSummaryList(getDatastoreSummaryList(hostSystem))
                .hostConfigInfo(hostSystem.getConfig())
                .build();
    }

    private List<DatastoreSummary> getDatastoreSummaryList(HostSystem hostSystem) {
        List<DatastoreSummary> datastoreSummaryList = Lists.newArrayList();
        try {
            Datastore[] datastores = hostSystem.getDatastores();
            for (Datastore datastore : datastores) {
                DatastoreSummary datastoreSummary = datastore.getSummary();
                datastoreSummaryList.add(datastoreSummary);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datastoreSummaryList;
    }
}
