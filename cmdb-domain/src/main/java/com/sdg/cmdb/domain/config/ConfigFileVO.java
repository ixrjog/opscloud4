package com.sdg.cmdb.domain.config;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.server.ServerGroupUseTypeDO;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by zxxiao on 2016/12/23.
 */
public class ConfigFileVO implements Serializable {
    private static final long serialVersionUID = -2877859581453004815L;

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
    private int fileType;

    private ServerGroupUseTypeDO useTypeDO;

    private int useType;

    private int envType;

    /**
     * 执行命令
     */
    private String invokeCmd;

    /**
     * invoke参数
     */
    private List<Object> params;

    /**
     * 文件内容
     */
    private String fileContent;

    /**
     * 文件组
     */
    private ConfigFileGroupDO fileGroupDO;

    private String gmtCreate;

    private String gmtModify;

    public ConfigFileVO() {
    }

    public ConfigFileVO(ConfigFileDO configFileDO, ConfigFileGroupDO fileGroupDO) {
        this.id = configFileDO.getId();
        this.fileName = configFileDO.getFileName();
        this.fileDesc = configFileDO.getFileDesc();
        this.filePath = configFileDO.getFilePath();
        this.fileType = configFileDO.getFileType();
        this.invokeCmd = configFileDO.getInvokeCmd();
        this.envType = configFileDO.getEnvType();
        this.useType = configFileDO.getUseType();

        if (!StringUtils.isEmpty(configFileDO.getParams())) {
            this.params = JSON.parseArray(configFileDO.getParams());
        } else {
            this.params = Collections.EMPTY_LIST;
        }
        this.fileGroupDO = fileGroupDO;
        this.fileContent = configFileDO.getFileContent();
        this.gmtCreate = configFileDO.getGmtCreate();
        this.gmtModify = configFileDO.getGmtModify();
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

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getInvokeCmd() {
        return invokeCmd;
    }

    public void setInvokeCmd(String invokeCmd) {
        this.invokeCmd = invokeCmd;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public ConfigFileGroupDO getFileGroupDO() {
        return fileGroupDO;
    }

    public void setFileGroupDO(ConfigFileGroupDO fileGroupDO) {
        this.fileGroupDO = fileGroupDO;
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

    public ServerGroupUseTypeDO getUseTypeDO() {
        return useTypeDO;
    }

    public void setUseTypeDO(ServerGroupUseTypeDO useTypeDO) {
        this.useTypeDO = useTypeDO;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    @Override
    public String toString() {
        return "ConfigFileVO{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileDesc='" + fileDesc + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileType=" + fileType +
                ", invokeCmd='" + invokeCmd + '\'' +
                ", params=" + params +
                ", fileContent='" + fileContent + '\'' +
                ", fileGroupDO=" + fileGroupDO +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
