package com.sdg.cmdb.util.prometheus.params;

import com.sdg.cmdb.util.IPUtils;

/**
 * Created by liangjian on 16/10/18.
 */
public class IP {

    private String ip;

    public String getIp() {
        return ip;
    }

    private String netmask;
    private String getway;

    /**
     * 10.17.1.100
     * 10.17.1.0/24
     *
     * @param ip
     */
    public IP(String ip) {
        setIp(ip);
    }

    /**
     * 设置ip地址&掩码
     *
     * @param ip
     */
    private void setIp(String ip) {
        String[] ipValue = ip.split("\\/");
        switch (ipValue.length) {
            case 1:
                setIpAndCheck(ipValue[0]);
                break;
            case 2:
                setIpAndCheck(ipValue[0]);
                setNetmast(ipValue[1]);
                break;
            default:
                //地址不合法
                return;
        }
    }

    /**
     * 10.17.1.100
     *
     * @param ip
     */
    private void setIpAndCheck(String ip) {
        if (IPUtils.checkIp(ip)) {
            this.ip = ip;
        }
    }

    /**
     * 设置掩码
     * 24
     * 255.255.255.0
     *
     * @param netmask
     */
    private void setNetmast(String netmask) {
        String[] netmaskValue = netmask.split("\\.");
        switch (netmaskValue.length) {
            case 1:
                Integer q = Integer.valueOf(netmaskValue[0]);
                if (q <= 32) {
                    this.netmask = q.toString();
                }
                ;
                break;
            case 4:
                this.netmask = IPUtils.getNetMask(netmask) + "";
                break;
            default:
                return;
        }
    }

    public IP(String ip, String netmask) {
        setIpAndCheck(ip);
        setNetmast(netmask);
    }

    public IP(String ip, String netmask, String getway) {
        setIpAndCheck(ip);
        setNetmast(netmask);
        if (IPUtils.checkIp(getway)) {
            this.getway = getway;
        }
    }

    /**
     * 判断IP类型
     *
     * @return True 公网IP
     * False 私有IP
     */
    public Boolean isPublicIP() {
        return null;
    }

    public String toString() {
        return ip + " " + netmask + " " + getway;
    }

    /**
     * 获取IP段地址
     * @return
     * 10.17.1.0/24
     * 10.17.1.100
     */
    public String getIPSection(){
        if(ip==null) return null;
        if(netmask == null || netmask.equals("32")){
            return ip;
        } else{
            return ip+"/"+netmask;
        }
    }

}
