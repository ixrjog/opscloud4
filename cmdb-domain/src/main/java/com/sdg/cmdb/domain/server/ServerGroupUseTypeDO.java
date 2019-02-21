package com.sdg.cmdb.domain.server;

import lombok.Data;

import java.io.Serializable;

@Data
public class ServerGroupUseTypeDO implements Serializable {
    private static final long serialVersionUID = -8813239640110953573L;

    private long id;
    private int useType;
    private String typeName;
    private String color = "#777";
    private String content;
    private String gmtCreate;
    private String gmtModify;

}
