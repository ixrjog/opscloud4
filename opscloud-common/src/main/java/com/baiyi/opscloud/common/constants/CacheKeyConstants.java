package com.baiyi.opscloud.common.constants;

/**
 * @Author baiyi
 * @Date 2023/12/28 15_44
 * @Version 1.0
 */
public interface CacheKeyConstants {

    String OPSCLOUD_PREFIX = "'OC4_'";

    String SYSTEM_INFO_INSTANCE_IP = OPSCLOUD_PREFIX + "SYS_INFO_INSTANCE_IP_{}";

    String DINGTALK_TOKEN = OPSCLOUD_PREFIX + "V0_DINGTALK_TOKEN_{}";

    String LEO_HEARTBEAT_KEY = OPSCLOUD_PREFIX + "V0_LEO_HEARTBEAT_{}_ID_{}";

    String LEO_STOP_SIGNAL = OPSCLOUD_PREFIX + "V0_LEO_{}_STOP_SIGNAL_ID_{}";

    String SINGLE_TASK_ASPECT_KEY = OPSCLOUD_PREFIX + "V0_SINGLETASK_NAME_{}";

    String NACOS_AUTH_KEY = OPSCLOUD_PREFIX + "V0_NACOS_ACCESS_TOKEN_{}";

    String SEND_AUDIT_NOTICE_KEY = OPSCLOUD_PREFIX + "WORKORDER_TICKETID_{}_USERNAME_{}";

    String LEO_STAT_REPORT_KEY = OPSCLOUD_PREFIX + "'LEO_STAT_REPORT'";

    String LEO_PROD_STAT_REPORT_KEY = OPSCLOUD_PREFIX + "'LEO_PROD_STAT_REPORT'";

    String TERMINAL_REPORT_KEY = OPSCLOUD_PREFIX + "'TERMINAL_REPORT'";

    //  String JENKINS_PIPELINE_KEY = OPSCLOUD_PREFIX + "V0_LEO_BUILD_PIPELINE_BID_";

    String WORK_ORDER_LEO_DEPLOY_HOLDER_KEY = OPSCLOUD_PREFIX + "V0_LEO_DEPLOY_BID_{}";

    String ZABBIX_AUTH_KEY = "'V0_ZABBIX_5_AUTH_URL_{}'";

    // Zabbix
    //     @CacheEvict(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_host_ip_' + #ip")
    String Z = "V0_ZABBIX_5_URL_";

    String ZABBIX_IP = "IP_";

}