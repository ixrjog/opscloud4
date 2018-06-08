package com.sdg.cmdb.domain.config;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by zxxiao on 2016/12/23.
 */
public class ConfigFileDO implements Serializable {
    private static final long serialVersionUID = 7428660139755427792L;

    public static final int divConfigFile = 0;  //自定义类型
    public static final int systemConfigFile = 1;   //系统保留类型

    private long id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件说明
     */
    private String fileDesc;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件类型.0：自定义类型；1：系统保留类型
     */
    private Integer fileType;

    private int envType;

    private int useType;

    /**
     * 执行命令
     */
    private String invokeCmd;

    /**
     * invoke参数
     */
    private String params;

    /***
     * 文件内容
     */
    private String fileContent;

    /**
     * 文件组id
     */
    private long fileGroupId;

    private String gmtCreate;

    private String gmtModify;

    public ConfigFileDO() {
    }

    public ConfigFileDO(ConfigFileVO configFileVO) {
        this.id = configFileVO.getId();
        this.fileName = configFileVO.getFileName();
        this.fileDesc = configFileVO.getFileDesc();
        this.filePath = configFileVO.getFilePath();
        this.fileType = configFileVO.getFileType();
        this.invokeCmd = configFileVO.getInvokeCmd();
        this.params = JSON.toJSONString(configFileVO.getParams());
        this.fileContent = configFileVO.getFileContent();
        this.fileGroupId = configFileVO.getFileGroupDO().getId();
        this.gmtCreate = configFileVO.getGmtCreate();
        this.gmtModify = configFileVO.getGmtModify();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getInvokeCmd() {
        return invokeCmd;
    }

    public void setInvokeCmd(String invokeCmd) {
        this.invokeCmd = invokeCmd;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public long getFileGroupId() {
        return fileGroupId;
    }

    public void setFileGroupId(long fileGroupId) {
        this.fileGroupId = fileGroupId;
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

    public int getEnvType() {
        return envType;
    }

    public void setEnvType(int envType) {
        this.envType = envType;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    @Override
    public String toString() {
        return "ConfigFileDO{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileDesc='" + fileDesc + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileType=" + fileType +
                ", invokeCmd='" + invokeCmd + '\'' +
                ", params='" + params + '\'' +
                ", fileContent='" + fileContent + '\'' +
                ", fileGroupId=" + fileGroupId +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
