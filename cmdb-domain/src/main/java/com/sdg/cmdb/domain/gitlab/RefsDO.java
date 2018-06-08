package com.sdg.cmdb.domain.gitlab;

import java.io.Serializable;

public class RefsDO implements Serializable {
    private static final long serialVersionUID = 1720389097336522522L;

    private long id;

    private long repositoryId;

    private int refType;

    private String ref;

    private String gmtCreate;

    private String gmtModify;

    public RefsDO() {

    }

    public RefsDO(long repositoryId, int refType, String ref) {
        this.repositoryId = repositoryId;
        this.refType = refType;
        this.ref = ref;
    }

    @Override
    public String toString() {
        return "RepositoryDO{" +
                "id=" + id +
                ", repositoryId=" + repositoryId +
                ", refType=" + refType +
                ", ref='" + ref + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }

    public enum RefTypeEnum {
        branches(0, "branches"),
        tags(1, "tags");

        private int code;
        private String desc;

        RefTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getRefTypeName(int code) {
            for (RefTypeEnum refTypeEnum : RefTypeEnum.values()) {
                if (refTypeEnum.getCode() == code) {
                    return refTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public int getRefType() {
        return refType;
    }

    public void setRefType(int refType) {
        this.refType = refType;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
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
}
