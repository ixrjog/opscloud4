package com.sdg.cmdb.domain.auth;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/19.
 */
@Data
public class RoleDO implements Serializable {
    private static final long serialVersionUID = -741705059808733260L;

    /**
     * 基础角色
     */
    public static final String roleBase = "base";

    /**
     * 开发角色
     */
    public static final String roleDevelop = "dev";

    /**
     * 管理员
     */
    public static final String roleAdmin = "admin";

    /**
     * DevOps
     */
    public static final String roleDevOps = "devOps";

    public static final String roleDeptLeader = "deptLeader";

    public static final String roleDevelopFt = "devFt";
    public static final String roleDevelopAndroid = "devAndroid";
    public static final String roleDevelopIos = "devIos";

    //QUALITY ASSURANCE
    public static final String roleQualityAssurance = "qa";


    private long id;
    private String roleName;
    private String roleDesc;
    private boolean workflow;  // 是否允许工作流申请此角色
    private String gmtCreate;
    private String gmtModify;

    public RoleDO() {

    }

    public RoleDO(String roleName) {
        this.roleName = roleName;
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
