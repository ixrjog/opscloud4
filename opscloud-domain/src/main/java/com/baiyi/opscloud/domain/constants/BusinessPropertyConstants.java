package com.baiyi.opscloud.domain.constants;

/**
 * @Author 修远
 * @Date 2021/7/22 5:25 下午
 * @Since 1.0
 */
public class BusinessPropertyConstants {

    public interface Zabbix {
        String BIDIRECTIONAL_SYNC = "zabbix_bidirectional_sync";
        String HOST_MACROS = "zabbix_host_macros";
        String TEMPLATES = "zabbix_templates";
        String PROXY = "zabbix_proxy";
    }

    public interface Ansible {
        String SUBGROUP = "ansible_subgroup";
    }

    public interface Prometheus {
        String ENABLE = "enable";
        String JOB_NAME = "job_name";
        String METRICS_PATH = "metrics_path";
        String CONFIG_PATH = "config_path";
    }

}