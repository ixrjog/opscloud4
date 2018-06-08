package com.sdg.cmdb.domain.auth;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/19.
 */
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



    public static final String roleDevOps = "devOps";

    public static final String roleDevelopFt = "devFt";
    public static final String roleDevelopAndroid = "devAndroid";
    public static final String roleDevelopIos = "devIos";

    //QUALITY ASSURANCE
    public static final String roleQualityAssurance = "qa";


    private long id;

    private String roleName;

    private String roleDesc;

    private String gmtCreate;

    private String gmtModify;

    public RoleDO(){

    }

    public RoleDO(String roleName){
        this.roleName = roleName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    @Override
    public String toString() {
        return "RoleDO{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", roleDesc='" + roleDesc + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
