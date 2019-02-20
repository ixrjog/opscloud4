package com.sdg.cmdb.domain.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class PreviewConfig implements Serializable {
    private static final long serialVersionUID = -3803050754613181476L;

    public PreviewConfig() {
    }

    public PreviewConfig(String title, String config) {
        this.title = title;
        this.config = config;
    }

    public PreviewConfig(String title, String config, int envType) {
        this.title = title;
        this.config = config;
        this.envType = envType;
    }

    private String title;
    private String config;
    private int envType = 0;

}
