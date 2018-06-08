package com.sdg.cmdb.util.prometheus.params;

/**
 * Created by liangjian on 16/10/18.
 */
public class Dns {

    //主dns
    private IP dnsMaster;
    //从dns
    private IP dnsSlave;

    public IP getDnsSlave() {
        return dnsSlave;
    }

    public IP getDnsMaster() {
        return dnsMaster;
    }

    public void setDnsSlave(IP dnsSlave) {
        this.dnsSlave = dnsSlave;
    }

    public void setDnsMaster(IP dnsMaster) {
        this.dnsMaster = dnsMaster;
    }

    public Dns(IP dnsMaster){
        this.dnsMaster=dnsMaster;
    }

    public Dns(IP dnsMaster,IP dnsSlave){
        this.dnsMaster=dnsMaster;
        this.dnsSlave=dnsSlave;
    }

}
