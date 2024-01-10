package com.baiyi.opscloud.common.constants;

/**
 * @Author baiyi
 * @Date 2023/12/28 15:44
 * @Version 1.0
 */
public interface CacheKeyConstants {

    String OPSCLOUD_PREFIX = "OC4:";

    String SYSTEM_INFO_INSTANCE_IP = OPSCLOUD_PREFIX + "SYS:INFO:INSTANCE:IP:{}";

    String DINGTALK_TOKEN = OPSCLOUD_PREFIX + "V0:DINGTALK:TOKEN:{}";

    String LEO_HEARTBEAT_KEY = OPSCLOUD_PREFIX + "V0:LEO:HEARTBEAT:{}:ID:{}";

    String LEO_STOP_SIGNAL = OPSCLOUD_PREFIX + "V0:LEO:{}:STOP:SIGNAL:ID:{}";

    String SINGLE_TASK_ASPECT_KEY = OPSCLOUD_PREFIX + "V0:SINGLETASK:NAME:{}";

    String NACOS_AUTH_KEY = OPSCLOUD_PREFIX + "V0:NACOS:ACCESS:TOKEN:{}";

    String SEND_AUDIT_NOTICE_KEY = OPSCLOUD_PREFIX + "WORKORDER:TICKETID:{}:USERNAME:{}";

    String LEO_STAT_REPORT_KEY = OPSCLOUD_PREFIX + "LEO:STAT:REPORT";

    String LEO_PROD_STAT_REPORT_KEY = OPSCLOUD_PREFIX + "LEO:PROD:STAT:REPORT";

    String TERMINAL_REPORT_KEY = OPSCLOUD_PREFIX + "TERMINAL:REPORT";

    //  String JENKINS_PIPELINE_KEY = OPSCLOUD_PREFIX + "V0:LEO:BUILD:PIPELINE:BID:";

    String WORK_ORDER_LEO_DEPLOY_HOLDER_KEY = OPSCLOUD_PREFIX + "V0:LEO:DEPLOY:BID:{}";

    String ZABBIX_AUTH_KEY = "V0:ZABBIX:5:AUTH:URL:{}";

    // Zabbix
    //     @CacheEvict(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_host_ip_' + #ip")
    String Z = "V0:ZABBIX:5:URL:";

    String ZABBIX_IP = "IP:";

}