package com.baiyi.opscloud.zabbix.builder;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostgroup;


/**
 * @Author baiyi
 * @Date 2019/11/27 4:30 PM
 * @Version 1.0
 */
public class ZabbixHostgroupBuilder {

    /**
     *
     * @param zabbixHostgroupBO
     * @return
     */
    public static ZabbixHostgroup buildOcCloudserver(ZabbixHostgroupBO zabbixHostgroupBO) {

        return BeanCopierUtils.copyProperties(zabbixHostgroupBO, ZabbixHostgroup.class);
    }

}
