package com.sdg.cmdb.domain.ci;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LabelVO extends LabelDO implements Serializable {
    private static final long serialVersionUID = -7009051497272161542L;

    public static final String ACTIVE_COLOR = "#EEEEEE";
    public static final String DEF_COLOR = "white";

    private boolean active = false;
    private String color = DEF_COLOR;

    private List<LabelMemberVO> memberList;

}
