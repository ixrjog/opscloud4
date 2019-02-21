package com.sdg.cmdb.domain.aliyunMQ;

import lombok.Data;

import java.io.Serializable;

@Data
public class AliyunMqGroupUserDO implements Serializable {
    private static final long serialVersionUID = -7561574323194224776L;
    private long id;
    private long groupId;
    private long userId;
    private String gmtCreate;
    private String gmtModify;
}
