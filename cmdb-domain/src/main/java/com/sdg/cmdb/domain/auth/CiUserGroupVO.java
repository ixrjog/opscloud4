package com.sdg.cmdb.domain.auth;

import com.sdg.cmdb.domain.server.ServerGroupDO;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/8/18.
 */
public class CiUserGroupVO extends CiUserGroupDO implements Serializable {
    private static final long serialVersionUID = -2701687660086688410L;

    private ServerGroupDO serverGroupDO;

    // 搜索用的权限组名称名称
    private String searchGroupName;

    public ServerGroupDO getServerGroupDO() {
        return serverGroupDO;
    }

    public void setServerGroupDO(ServerGroupDO serverGroupDO) {
        this.serverGroupDO = serverGroupDO;
    }

    public CiUserGroupVO() {

    }

    public String getSearchGroupName() {
        return searchGroupName;
    }

    public void setSearchGroupName(String searchGroupName) {
        this.searchGroupName = searchGroupName;
    }

    public CiUserGroupVO(CiUserGroupDO ciUserGroupDO) {
        setId(ciUserGroupDO.getId());
        setGroupName(ciUserGroupDO.getGroupName());
        setContent(ciUserGroupDO.getContent());
        setServerGroupId(ciUserGroupDO.getServerGroupId());
        setEnvType(ciUserGroupDO.getEnvType());
        setGmtCreate(ciUserGroupDO.getGmtCreate());
        setGmtModify(ciUserGroupDO.getGmtModify());
        initSearchGroupName();
    }

    public CiUserGroupVO(CiUserGroupDO ciUserGroupDO, ServerGroupDO serverGroupDO) {
        setId(ciUserGroupDO.getId());
        setGroupName(ciUserGroupDO.getGroupName());
        setContent(ciUserGroupDO.getContent());
        setServerGroupId(ciUserGroupDO.getServerGroupId());
        setEnvType(ciUserGroupDO.getEnvType());
        setGmtCreate(ciUserGroupDO.getGmtCreate());
        setGmtModify(ciUserGroupDO.getGmtModify());
        this.serverGroupDO = serverGroupDO;
        initSearchGroupName();
    }

    private void initSearchGroupName(){
        String name = getGroupName();
        name+= "-" + EnvTypeEnum.getEnvTypeName(getEnvType());
        setSearchGroupName(name);
    }


    @Override
    public String toString() {
        return "CiUserGroupVO{" +
                "id=" + getId() +
                ", groupName='" + getGroupName() + '\'' +
                ", serverGroupDO='" + serverGroupDO + '\'' +
                ", serverGroupId=" + getServerGroupId() +
                ", envType=" + getEnvType() +
                ", gmtModify='" + getGmtModify() + '\'' +
                ", gmtCreate='" + getGmtCreate() + '\'' +
                '}';
    }


    public enum EnvTypeEnum {
        //0 保留／在组中代表的是所有权限
        all(0, "all"),
        dev(1, "dev"),
        daily(2, "daily"),
        gray(3, "gray"),
        prod(4, "production"),
        test(5, "test"),
        back(6, "back");
        private int code;
        private String desc;

        EnvTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getEnvTypeName(int code) {
            for (EnvTypeEnum envTypeEnum : EnvTypeEnum.values()) {
                if (envTypeEnum.getCode() == code) {
                    return envTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }


}
