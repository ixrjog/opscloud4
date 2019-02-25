package com.sdg.cmdb.domain.jumpserver;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;


/**
 * 资产关联系统账户
 */
@Data
public class AvssetsSystemuserAssetsDO implements Serializable {

    private static final long serialVersionUID = -3185003557857447589L;
    private int id;
    private String systemuser_id;
    private String  asset_id;

    public  AvssetsSystemuserAssetsDO(){}

    public  AvssetsSystemuserAssetsDO(String systemuser_id,String asset_id){
        this.systemuser_id = systemuser_id;
        this.asset_id = asset_id;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
