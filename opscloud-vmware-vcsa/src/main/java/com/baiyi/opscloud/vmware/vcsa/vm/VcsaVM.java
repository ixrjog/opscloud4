package com.baiyi.opscloud.vmware.vcsa.vm;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.vmware.vcsa.instance.VMInstance;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/15 10:43 上午
 * @Version 1.0
 */

public interface VcsaVM {

    List<VMInstance> getInstanceList();

    VMInstance getInstance(String serverName);

    String getZone();

    BusinessWrapper<Boolean> power(String instanceName, Boolean action);

}
