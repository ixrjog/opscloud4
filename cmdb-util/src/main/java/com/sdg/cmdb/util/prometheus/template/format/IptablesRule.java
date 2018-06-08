package com.sdg.cmdb.util.prometheus.template.format;

import com.sdg.cmdb.util.prometheus.params.IP;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjian on 16/11/2.
 */
public class IptablesRule {

    //端口
    private String port;

    //规则注释
    private String desc;

    public String getDesc() {
        if (desc == null || desc.equals("")) return "";
        return "# " + desc + "\n";
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String ruleHead;

    private String getRuleHead(){
        if (ruleHead==null ) {
            ruleHead="";
        }
        return ruleHead;
    }

    private String ruleBody="";

    private int ruleType;
    //规则类型 允许ip访问固定端口
    public final static int ruleTypeAllowIP4Port = 0;
    //规则类型 开放端口
    public final static int ruleTypeOpenPort = 1;
    //规则类型 允许ip通过
    public final static int ruleTypeAllowIP = 2;

    private List<IP> ipList;


    public IptablesRule(String port, List<IP> ips, int ruleType, String desc) {
        if (port != null) this.port = port;
        if (ips != null) this.ipList = ips;
        this.ruleType = ruleType;
        if (desc != null) setDesc(desc);
    }

    //规则类型 允许ip访问固定端口
    public IptablesRule(String port, List<IP> ips, String desc) {
        if (port != null) this.port = port;
        if (ips != null) this.ipList = ips;
        this.ruleType = 0;
        if (desc != null) setDesc(desc);
    }

    /**
     * //规则类型 开放端口
     *
     * @param port
     * @param desc
     */
    public IptablesRule(String port, String desc) {
        if (port != null) this.port = port;
        this.ruleType = 1;
        if (desc != null) setDesc(desc);
    }

    //规则类型 允许ip通过
    public IptablesRule(List<IP> ips, String desc) {
        if (ips != null) this.ipList = ips;
        this.ruleType = 2;
        if (desc != null) setDesc(desc);
    }

    private void buildRuleByType() {
        switch (ruleType) {
            case ruleTypeAllowIP4Port:
                buildRuleTypeAllowIP4Port();
                break;
            case ruleTypeOpenPort:
                buildRuleTypeOpenPort();
                break;
            case ruleTypeAllowIP:
                buildRuleTypeAllowIP();
                break;
        }
    }

    /**
     * 提取完整的规则
     *
     * @return
     */
    public String getRule() {
        buildRuleByType();
        return getDesc() + getRuleHead() + ruleBody;
    }

    /**
     * 允许ip访问固定端口
     */
    private void buildRuleTypeAllowIP4Port() {
        ruleHead = "-I INPUT -p tcp --dport " + port + " -j DROP\n";
        for (IP ip : ipList) {
            ruleBody = ruleBody + "-I INPUT -s " + ip.getIPSection() + " -p tcp -m multiport --dports " + port + "-j ACCEPT\n";
        }
    }

    /**
     * 开放端口
     */
    private void buildRuleTypeOpenPort() {
        if (port != null && !port.equals("0") && !port.equals("")) {
            ruleBody = ruleBody + "-A INPUT -m state --state NEW -m tcp -p tcp --dport " + port + " -j ACCEPT\n";
        }
    }

    /**
     * 开放IP
     */
    private void buildRuleTypeAllowIP() {
        //-A INPUT -s 10.0.0.0/8 -j ACCEPT
        for (IP ip : ipList) {
            ruleBody = ruleBody + "-A INPUT -s " + ip.getIPSection() + " -j ACCEPT\n";
        }

    }

}