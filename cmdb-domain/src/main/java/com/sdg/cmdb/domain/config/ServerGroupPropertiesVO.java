package com.sdg.cmdb.domain.config;

import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by zxxiao on 2016/10/21.
 */
public class ServerGroupPropertiesVO implements Serializable {
    private static final long serialVersionUID = 1675781778696930589L;

    /**
     * 服务器组
     */
    private ServerGroupDO serverGroupDO;

    /**
     * 服务器
     */
    private ServerDO serverDO;

    /**
     * 属性组集合
     */
    private Map<Long, ConfigPropertyGroupDO> propertyGroupDOMap;

    /**
     * 属性组属性集合
     */
    private Map<Long, List<ConfigPropertyDO>> groupPropertyMap;

    /**
     * 客户端请求提供的属性组
     */
    private ConfigPropertyGroupDO propertyGroupDO;

    /**
     * 客户端请求提供的属性集合
     */
    private List<ConfigPropertyDO> propertyDOList;

    public ServerGroupPropertiesVO() {
    }

    public ServerGroupPropertiesVO(ServerGroupDO serverGroupDO, Map<Long, ConfigPropertyGroupDO> propertyGroupDOMap,
            Map<Long, List<ConfigPropertyDO>> groupPropertyMap) {
        this.serverGroupDO = serverGroupDO;
        this.propertyGroupDOMap = propertyGroupDOMap;
        this.groupPropertyMap = groupPropertyMap;
    }

    public ServerGroupPropertiesVO(ServerDO serverDO, Map<Long, ConfigPropertyGroupDO> propertyGroupDOMap,
                                   Map<Long, List<ConfigPropertyDO>> groupPropertyMap) {
        this.serverDO = serverDO;
        this.propertyGroupDOMap = propertyGroupDOMap;
        this.groupPropertyMap = groupPropertyMap;
    }

    public ServerGroupDO getServerGroupDO() {
        return serverGroupDO;
    }

    public void setServerGroupDO(ServerGroupDO serverGroupDO) {
        this.serverGroupDO = serverGroupDO;
    }

    public ServerDO getServerDO() {
        return serverDO;
    }

    public void setServerDO(ServerDO serverDO) {
        this.serverDO = serverDO;
    }

    public Map<Long, ConfigPropertyGroupDO> getPropertyGroupDOMap() {
        return propertyGroupDOMap;
    }

    public void setPropertyGroupDOMap(Map<Long, ConfigPropertyGroupDO> propertyGroupDOMap) {
        this.propertyGroupDOMap = propertyGroupDOMap;
    }

    public Map<Long, List<ConfigPropertyDO>> getGroupPropertyMap() {
        return groupPropertyMap;
    }

    public void setGroupPropertyMap(Map<Long, List<ConfigPropertyDO>> groupPropertyMap) {
        this.groupPropertyMap = groupPropertyMap;
    }

    public ConfigPropertyGroupDO getPropertyGroupDO() {
        return propertyGroupDO;
    }

    public void setPropertyGroupDO(ConfigPropertyGroupDO propertyGroupDO) {
        this.propertyGroupDO = propertyGroupDO;
    }

    public List<ConfigPropertyDO> getPropertyDOList() {
        return propertyDOList;
    }

    public void setPropertyDOList(List<ConfigPropertyDO> propertyDOList) {
        this.propertyDOList = propertyDOList;
    }



    @Override
    public String toString() {
        return "ServerGroupPropertiesVO{" +
                "serverGroupDO=" + serverGroupDO +
                ", serverDO=" + serverDO +
                ", propertyGroupDOMap=" + propertyGroupDOMap +
                ", groupPropertyMap=" + groupPropertyMap +
                ", propertyGroupDO=" + propertyGroupDO +
                ", propertyDOList=" + propertyDOList +
                '}';
    }
}
