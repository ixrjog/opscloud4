package com.baiyi.opscloud.vmware.vcsa.esxi;

import com.baiyi.opscloud.vmware.vcsa.instance.ESXiInstance;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/15 12:32 下午
 * @Version 1.0
 */
public interface VcsaESXi  {
    List<ESXiInstance> getInstanceList();

    String getZone();

    ESXiInstance getInstance(String serverName);

}
