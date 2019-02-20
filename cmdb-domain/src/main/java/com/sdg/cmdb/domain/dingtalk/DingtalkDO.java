package com.sdg.cmdb.domain.dingtalk;

import lombok.Data;

import java.io.Serializable;

@Data
public class DingtalkDO implements Serializable{

    private static final long serialVersionUID = -559011899139267309L;
    private long id;
    private String name;
    private String webhook;
    // TODO  0:持续中心  1:告警
    private int dingtalkType;
    private String username;
    private String gmtCreate;
    private String gmtModify;

}
