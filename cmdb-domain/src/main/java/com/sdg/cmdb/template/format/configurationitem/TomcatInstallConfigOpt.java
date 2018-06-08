package com.sdg.cmdb.template.format.configurationitem;

import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjian on 16/10/13.
 */

public class TomcatInstallConfigOpt {

    private List<TomcatInstallConfigOpt> tics ;


    private String name;

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    private String jdkVersion = "7";

    public void setJdkVersiong(String jdkVersion) {
        if (jdkVersion != null) {
            this.jdkVersion = jdkVersion;
        }
    }

    private String tomcatVersion = "7";

    public void setTomcatVersion(String tomcatVersion) {
        if (tomcatVersion != null) {
            this.tomcatVersion = tomcatVersion;
        }
    }

    public TomcatInstallConfigOpt() {
        this.tics = new ArrayList<TomcatInstallConfigOpt>();
    }

    public TomcatInstallConfigOpt(String name, String jdkVersion, String tomcatVersion) {
        this.name = name;
        this.jdkVersion = jdkVersion;
        this.tomcatVersion = tomcatVersion;
    }

    private String installName = "all";
    private String configUrl = "-";

    public String getLine() {
        String result;
        result="'"+this.name+"' '"+this.jdkVersion+"' '"+this.tomcatVersion+"' '"+this.installName+"' '"+this.configUrl+"' \n";
        return result;
    }

    public void put(TomcatInstallConfigOpt  tico){
        if (tico == null) return ;
        this.tics.add(tico);
    }

    public static TomcatInstallConfigOpt  buider(String name, String jdkVersion, String tomcatVersion){
        if (name == null) return null;
        if (jdkVersion ==null) return null;
        if (tomcatVersion ==null ) return null;
        TomcatInstallConfigOpt tico = new TomcatInstallConfigOpt(name,jdkVersion,tomcatVersion);
        return tico;
    }

    public String toBody() {
        String body= new String();
        body="TOMCAT_INSTALL_CONFIG_OPT=( \n";
        for(TomcatInstallConfigOpt t:this.tics){
            body=body+t.getLine();
        }
        body=body+")\n";
        return body;
    }


}
