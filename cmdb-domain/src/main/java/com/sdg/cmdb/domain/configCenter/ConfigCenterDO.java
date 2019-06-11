package com.sdg.cmdb.domain.configCenter;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/5/26.
 */
@Data
public class ConfigCenterDO implements Serializable {

    private static final long serialVersionUID = -637891957421927825L;
    private long id;

    private String itemGroup;

    private String env;

    private String item;


    private String value;


    private String content;


    private String gmtCreate;

    private String gmtModify;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
