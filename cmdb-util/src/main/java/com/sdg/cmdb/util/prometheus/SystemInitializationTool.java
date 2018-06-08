package com.sdg.cmdb.util.prometheus;

/**
 * Created by liangjian on 16/10/18.
 */


import java.util.ArrayList;
import java.util.List;
/**
 * 系统初始化
 */
public class SystemInitializationTool {

    // 安装 prometheus
    public static final int system_install_prometheus = 0;

    //修改 DNS
    public static final int system_chg_dns = 1;

    // 系统配置优化
    public static final int system_sysconfig = 2;

    // 系统配置ssh
    public static final int system_sysconfig_ssh = 3;

    private int type;

    private String params;

    private List sts;

    private String prometheusHome = "/usr/local/prometheus";

    public SystemInitializationTool(int type, String params) {
        this.init();
        this.type = type;
        this.params = params;
    }

    private void init(){
        this.sts=new ArrayList<SystemTool>();
        sts.add(new SystemTool(0,"mkdir -p /usr/local/prometheus/bin && cd /usr/local/prometheus/bin && wget http://res.51xianqu.net/software/PROMETHEUS/bin/prometheus_update && chmod +x prometheus_update && ./prometheus_update -x=http://res.51xianqu.net/software/PROMETHEUS/etc/prometheus_update.conf -i && source /etc/profile"));
        sts.add(new SystemTool(1,prometheusHome+"/bin/system/chg_dns"));
        sts.add(new SystemTool(2,prometheusHome+"/bin/system/sysconfig"));
        sts.add(new SystemTool(3,prometheusHome+"/bin/system/sysconfig_ssh"));
    }

    protected String getCmdPath() {
        return null;
    }

    public class SystemTool{

        private int type;
        private String cmd;

        public SystemTool(int type,String cmd){
            this.type=type;
            this.cmd=cmd;
        }

        public String getCmdLine(String params){
            return null;
        }
    }
}
