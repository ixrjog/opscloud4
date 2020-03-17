package com.baiyi.opscloud.account.convert;

import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2020/1/7 9:43 上午
 * @Version 1.0
 */
public class ZabbixUserConvert {

    public static ZabbixUser convertOcUser(OcUser ocUser) {
        ZabbixUser zabbixUser = new ZabbixUser();
        zabbixUser.setAlias(ocUser.getUsername());
        if (!StringUtils.isEmpty(ocUser.getDisplayName())){
            zabbixUser.setName(ocUser.getDisplayName());
        }else{
            zabbixUser.setName(ocUser.getUsername());
        }
        return zabbixUser;
    }




}
