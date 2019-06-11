package com.sdg.cmdb.domain.readmeMD;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReadmeMDDO implements Serializable {
    private static final long serialVersionUID = 4200920744093006985L;

    private long id;
    private String mdName;
    private String mdBody;
    private int mdType = 0;
    private String mdKey;
    private String gmtCreate;
    private String gmtModify;

}
