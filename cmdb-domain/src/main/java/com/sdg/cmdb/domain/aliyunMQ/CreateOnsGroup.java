package com.sdg.cmdb.domain.aliyunMQ;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreateOnsGroup implements Serializable {
    private static final long serialVersionUID = -7497336183835961739L;

    // TODO 创建的消息消费集群的 GID
    private String groupId;
    private String remark;

}
