package com.sdg.cmdb.domain.ci;

import java.io.Serializable;

public class JobParam implements Serializable {
    private static final long serialVersionUID = -3399643598557932062L;

    public JobParam() {
    }

    public JobParam(String name, String value) {
        this.name = name;
        this.value = value;
    }


    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
