package com.baiyi.opscloud.datasource.jenkins.model;

import lombok.Getter;

@Getter
public class ExtractHeader extends BaseModel {

    private String location;

    public ExtractHeader setLocation(String value) {
        location = value;
        return this;
    }

}