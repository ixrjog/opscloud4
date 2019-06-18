package com.sdg.cmdb.domain.config;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zxxiao on 2016/10/20.
 */
@Data
public class ConfigPropertyVO implements Serializable {
    private static final long serialVersionUID = -6198021597822505240L;

    private long id;

    /*
    属性名称
     */
    private String proName;

    /*
    属性默认值
     */
    private String proValue;

    /*
    属性描述
     */
    private String proDesc;

    /*
    组
     */
    private ConfigPropertyGroupDO groupDO;

    private String gmtCreate;

    private String gmtModify;

    public ConfigPropertyVO() {
    }

    public ConfigPropertyVO(ConfigPropertyDO propertyDO, ConfigPropertyGroupDO groupDO) {
        this.id = propertyDO.getId();
        this.proName = propertyDO.getProName();
        this.proValue = propertyDO.getProValue();
        this.proDesc = propertyDO.getProDesc();
        this.groupDO = groupDO;
        this.gmtCreate = propertyDO.getGmtCreate();
        this.gmtModify = propertyDO.getGmtModify();
    }


}
