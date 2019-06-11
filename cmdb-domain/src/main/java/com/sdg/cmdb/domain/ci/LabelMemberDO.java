package com.sdg.cmdb.domain.ci;

import lombok.Data;

import java.io.Serializable;

@Data
public class LabelMemberDO implements Serializable {
    private static final long serialVersionUID = 6320878144164491278L;

    private long id;
    private long labelId;
    private long appId;
    private String gmtCreate;
    private String gmtModify;

    public LabelMemberDO(long labelId, long appId) {
        this.labelId = labelId;
        this.appId = appId;
    }

    public LabelMemberDO() {
    }

}
