package com.sdg.cmdb.domain.ci;

import lombok.Data;

import java.io.Serializable;

@Data
public class LabelDO implements Serializable {
    private static final long serialVersionUID = 780852140266782002L;
    private long id;
    private String labelName;
    private String content;
    private int labelType;
    private String gmtCreate;
    private String gmtModify;

}
