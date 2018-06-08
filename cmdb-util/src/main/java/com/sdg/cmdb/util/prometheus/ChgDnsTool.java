package com.sdg.cmdb.util.prometheus;

import com.sdg.cmdb.util.prometheus.params.Dns;

/**
 * Created by liangjian on 16/10/19.
 */
public class ChgDnsTool extends AbsTools {


    @Override
    protected String getBinPath(){
        return "bin/system/chg_dns";
    }

    private Dns dns;

    public void setDns(Dns dns) {
        this.dns = dns;
    }

    public ChgDnsTool(Dns dns) {
        super();
        setDns(dns);
    }

    @Override
    protected void makeParams() {
        if(this.dns.getDnsMaster().getIp() != null){
            putParam(this.dns.getDnsMaster().getIp()) ;
        }
        if(this.dns.getDnsSlave() != null && this.dns.getDnsSlave().getIp() != null){
            putParam(this.dns.getDnsSlave().getIp()) ;
        }
    }
}
