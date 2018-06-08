package com.sdg.cmdb.util.prometheus;

/**
 * Created by liangjian on 16/10/17.
 */
public class ChgHostnameTool extends AbsTools {


    @Override
    protected String getBinPath(){
        return "bin/system/chg_hostname";
    }


    private String hostname;

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    private String ip;


    public ChgHostnameTool(String hostname) {
        super();
        setHostname(hostname);
    }

    public ChgHostnameTool(String hostname, String ip) {
        super();
        setHostname(hostname);
        setIp(ip);
    }

    @Override
    protected void makeParams() {
        putParam("-hostname=" + this.hostname);
        if (this.ip != null) {
            putParam("-ip=" + this.ip);
        }
    }

}
