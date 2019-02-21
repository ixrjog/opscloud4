package com.sdg.cmdb.domain.gitlab.v1;

import java.io.Serializable;

public class Author implements Serializable {
    private static final long serialVersionUID = -121774108962903513L;

    private String name;

    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
