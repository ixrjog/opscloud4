package com.sdg.cmdb.domain.nginx;

import com.sdg.cmdb.domain.server.ServerDO;

import java.io.Serializable;

public class EnvFileDO implements Serializable {
    private static final long serialVersionUID = -3459701164182480952L;

    private long id;

    private long envId;

    /**
     * 0:upstream 1:location
     */
    private int fileType;

    private String filePath;

    private String fileName;

    private String fileKey;

    private String gmtCreate;

    private String gmtModify;

    public enum FileTypeEnum {
        //0 保留／在组中代表的是所有权限
        location(0, "location"),
        upstream(1, "upstream");
        private int code;
        private String desc;

        FileTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getFileTypeName(int code) {
            for (FileTypeEnum fileTypeEnum : FileTypeEnum.values()) {
                if (fileTypeEnum.getCode() == code) {
                    return fileTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    public EnvFileDO() {

    }

    public EnvFileDO(long envId, int fileType) {
        this.envId = envId;
        this.fileType = fileType;
        if (fileType == FileTypeEnum.location.getCode()) {
            this.filePath =  FileTypeEnum.location.getDesc();
            this.fileName = FileTypeEnum.location.getDesc() + "_default.conf";
            this.fileKey = FileTypeEnum.location.getDesc().toUpperCase();
        }
        if (fileType == FileTypeEnum.upstream.getCode()) {
            this.filePath =  FileTypeEnum.upstream.getDesc();
            this.fileName = FileTypeEnum.upstream.getDesc() + "_default.conf";
            this.fileKey = FileTypeEnum.upstream.getDesc().toUpperCase();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEnvId() {
        return envId;
    }

    public void setEnvId(long envId) {
        this.envId = envId;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
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
