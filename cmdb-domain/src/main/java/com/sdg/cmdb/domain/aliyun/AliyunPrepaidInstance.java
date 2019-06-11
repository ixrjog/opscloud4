package com.sdg.cmdb.domain.aliyun;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class AliyunPrepaidInstance implements Serializable{
    private static final long serialVersionUID = 5561637622590629314L;

    private long id;
    private String instanceId;
    // TODO 包月时长
    private int period;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
