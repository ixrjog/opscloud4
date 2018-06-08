package com.sdg.cmdb.util.prometheus;

/**
 * Created by liangjian on 16/10/17.
 */
public class InstallJavaTool extends AbsTools {


    @Override
    protected String getBinPath(){
        return "bin/install_java";
    }



    private String java_install_version = "7";

    public void setJava_install_version(String java_install_version) {
        this.java_install_version = java_install_version;
    }


    public InstallJavaTool(String javaVersion) {
        super();
        setJava_install_version(javaVersion);
    }

    public InstallJavaTool() {
        super();
    }



    @Override
    protected void makeParams() {
        putParam("-java.install.version=" + this.java_install_version);
    }


}
