package com.sdg.cmdb.domain.aliyun;

import lombok.Data;

import java.io.Serializable;

@Data
public class AliyunRenewInstance implements Serializable {
    private static final long serialVersionUID = -8248170024811044538L;

    private String instanceId;
    private String serverName;
    private int period = 0;

}
