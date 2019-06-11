package com.sdg.cmdb.domain.config;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by zxxiao on 2016/10/21.
 */
@Data
public class ServerGroupPropertiesDO implements Serializable {
    private static final long serialVersionUID = -6577131273984933361L;

    private long id;
    /**
     * 服务器组Id
     */
    private Long groupId;
    /**
     * 服务器Id
     */
    private Long serverId;
    /**
     * 属性id
     */
    private Long propertyId;
    /**
     * 属性值
     */
    private String propertyValue;
    /**
     * 属性组id
     */
    private Long propertyGroupId;
    private String gmtCreate;
    private String gmtModify;

    public ServerGroupPropertiesDO(ServerGroupDO serverGroupDO,ConfigPropertyDO configPropertyDO,String value){
        this.groupId = serverGroupDO.getId();
        this.propertyId = configPropertyDO.getId();
        this.propertyGroupId = configPropertyDO.getGroupId();
        this.propertyValue = value;

    }

    public ServerGroupPropertiesDO(ServerDO serverDO, ConfigPropertyDO configPropertyDO, String value){
        this.serverId = serverDO.getId();
        this.propertyId = configPropertyDO.getId();
        this.propertyGroupId = configPropertyDO.getGroupId();
        this.propertyValue = value;

    }

    public ServerGroupPropertiesDO(){}

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
