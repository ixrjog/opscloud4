package com.baiyi.opscloud.vmware.vcsa.handler;

import com.baiyi.opscloud.vmware.vcsa.client.ClientSession;
import com.baiyi.opscloud.vmware.vcsa.config.VcsaConfig;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * @Author baiyi
 * @Date 2020/1/15 10:09 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class VcsaHandler {

    @Resource
    private VcsaConfig vcsaConfig;

    /**
     * 按 类型 查询
     *
     * @param type
     * @return
     */
    public ManagedEntity[] searchManagedEntities(String type) {
        ServiceInstance serviceInstance = getAPI();
        if (serviceInstance == null) return null;
        try {
            ManagedEntity[] mes = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntities(type);
            return mes;
        } catch (RemoteException e) {
            log.error("VCSA查询VM列表错误,{}", e);
            serviceInstance.getRootFolder().getServerConnection().logout();
        }
        return null;
    }

    /**
     * 按 类型+名称 查询
     *
     * @param type
     * @param name
     * @return
     */
    public ManagedEntity searchManagedEntity(String type, String name) {
        ServiceInstance serviceInstance = getAPI();
        if (serviceInstance == null) return null;
        try {
            ManagedEntity mes = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntity(type, name);
            return mes;
        } catch (RemoteException e) {
            log.error("VCSA 查询VM列表错误, {}", e);
            serviceInstance.getRootFolder().getServerConnection().logout();
        }
        return null;
    }

    public String getZone() {
        return vcsaConfig.getZone();
    }

    public ServiceInstance getAPI() {
        try {
            log.info("VCSA 登录 {}", vcsaConfig.getHost());
            ClientSession session = new ClientSession(vcsaConfig);
            URL url = getURL(session);
            log.info("VCSA Sever 登录成功");
            return new ServiceInstance(url, session.getUsername(), session.getPassword(), true);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("VCSA Sever 登陆失败 ！");
            return null;
        }
    }

    private URL getURL(ClientSession session)  throws MalformedURLException {
        if (session.getHost().indexOf(":") == -1) {
            return new URL("https", session.getHost(), "/sdk");
        } else {
            String[] s = session.getHost().split(":");
            return new URL("https", s[0], Integer.valueOf(s[1]),  "/sdk");
        }
    }

}
