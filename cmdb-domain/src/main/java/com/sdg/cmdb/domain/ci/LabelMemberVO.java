package com.sdg.cmdb.domain.ci;

import lombok.Data;

import java.io.Serializable;

@Data
public class LabelMemberVO extends LabelMemberDO implements Serializable {
    private static final long serialVersionUID = 2489024361293661911L;

    private String appName;

}
