package com.sdg.cmdb.domain.aliyun;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AliyunRenewInstances implements Serializable {
    private static final long serialVersionUID = -7543942571025141468L;
    /**
     * $scope.ecsInstances = ecsInstances;
     * $scope.period = 3;
     */
    /**
     * 包月时长
     */
    private int period;

    private List<AliyunRenewInstance> ecsInstances;
}
