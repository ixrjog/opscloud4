package com.sdg.cmdb.domain.ci.android;

import lombok.Data;

import java.io.Serializable;

@Data
public class AndroidChannel implements Serializable {

    private static final long serialVersionUID = -5634530902628580807L;
    private String code;
    private String name;

}
