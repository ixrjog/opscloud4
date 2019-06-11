package com.sdg.cmdb.domain.readmeMD;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReadmeMDVO extends ReadmeMDDO implements Serializable {
    private static final long serialVersionUID = 5171710687959862636L;

    private String preview;

    public ReadmeMDVO(){}

}
