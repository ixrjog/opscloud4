package com.sdg.cmdb.domain.gatewayAdmin;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class SetResult implements Serializable {

    private static final long serialVersionUID = -7436998931329949087L;
    private int status = 0;
    private String code;
    private String message;
    private boolean data;

    public boolean isSuccess() {
        if (status == 1) return true;
        return false;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
