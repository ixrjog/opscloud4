package com.sdg.cmdb.domain.gitlab.v1;

import lombok.Data;

import java.io.Serializable;

@Data
public class Author implements Serializable {
    private static final long serialVersionUID = -121774108962903513L;

    private String name;
    private String email;

}
