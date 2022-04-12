package com.baiyi.opscloud.datasource.jenkins.model;

public class ExtractHeader extends BaseModel {

    private String location;

    public ExtractHeader setLocation(String value) {
        location = value;
        return this;
    }

    public String getLocation() {
        return location;
    }

}
