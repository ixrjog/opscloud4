package com.sdg.cmdb.domain.config;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zxxiao on 2016/10/20.
 */
@Data
public class ConfigPropertyDO implements Serializable {
    private static final long serialVersionUID = 2140523078156398557L;

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
    组id
     */
    private long groupId;

    private String gmtCreate;

    private String gmtModify;

}
