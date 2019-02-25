package com.sdg.cmdb.domain.aliyunMQ;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class AliyunMqGroupVO extends AliyunMqGroupDO implements Serializable {
    private static final long serialVersionUID = 1820467267050682277L;

    private int status;
    private String statusName;
    private List<AliyunMqGroupUserVO> userList;

    /**
     * 控件最大值，默认10,000
     */
    private long uibProgressbarMax = 10000;

}
