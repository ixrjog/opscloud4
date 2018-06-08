package com.sdg.cmdb.domain.jenkins;

import java.io.Serializable;

public class NewAppLink implements Serializable {

    private String appName;

    private String link;

    private String os = "android";

    public NewAppLink() {

    }

    public NewAppLink(String appName, String link) {
        this.appName = appName;
        this.link = link;
    }

    @Override
    public String toString() {
        return "NewAppLink{" +
                "appName='" + appName + '\'' +
                ", link='" + link+ '\'' +
                ", os='" + os + '\'' +
                '}';
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
