package com.sdg.cmdb.util.prometheus;

import java.util.ArrayList;

/**
 * Created by liangjian on 16/10/14.
 */
public class InstallTomcatTool extends AbsTools {

    /**
     * tomcatName & projectName
     */
    private String tomcatName;

    public void setTomcatName(String tomcatName) {
        this.tomcatName = tomcatName;
    }

    private String prometheusHome = "/usr/local/prometheus";

    @Override
    protected String getBinPath() {
        return "bin/install_tomcat";
    }


    private String tomcat_install_version = "7";

    public void setTomcat_install_version(String tomcat_install_version) {
        this.tomcat_install_version = tomcat_install_version;
    }

    private String tomcat_install_name = "all";

    private String tomcat_setenv_file;

    public void setTomcat_setenv_file(String tomcat_setenv_file) {
        this.tomcat_setenv_file = tomcat_setenv_file;
    }

    public InstallTomcatTool(String tomcatVersion, String setenvFile) {
        super();
        this.setTomcat_install_version(tomcatVersion);
        this.setTomcat_setenv_file(setenvFile);
        this.setType(2);
    }

    public InstallTomcatTool(String tomcatName) {
        super();
        this.setTomcatName(tomcatName);
        this.setType(1);
    }

    public InstallTomcatTool() {
        super();
        this.setType(0);
    }


    /**
     * 0 自动安装(按主机名)
     * 1 自动安装(参数指定项目名)
     * 2 多参数非自动安装
     */
    private int type = 0;

    private void setType(int type) {
        this.type = type;
    }


    @Override
    protected void makeParams() {

        switch (this.type) {
            case 0:
                putParam("-a");
                break;
            case 1:
                putParam("-auto=" + this.tomcatName);
                break;
            case 2:
                putParam("-tomcat.install.version=" + this.tomcat_install_version);
                putParam("-tomcat.install.name=" + this.tomcat_install_name);
                putParam("-tomcat.setenv.file=" + this.tomcat_setenv_file);
                break;
        }
    }
}



