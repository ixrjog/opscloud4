package com.sdg.cmdb.domain.copy;

import com.sdg.cmdb.domain.nginx.VhostDO;
import com.sdg.cmdb.domain.nginx.VhostEnvDO;

import java.io.Serializable;

/**
 * 同步日志
 */
public class CopyLogDO implements Serializable {

    private static final long serialVersionUID = 3206032820380024663L;

    public CopyLogDO() {

    }

    public CopyLogDO( CopyVO copyVO) {
        this.copyId = copyVO.getId();
        this.vhostId = copyVO.getVhostVO().getId();
        this.serverName = copyVO.getVhostVO().getServerName();
        this.envType = copyVO.getVhostEnvDO().getEnvType();
    }

    private long id;

    private long copyId;

    private long vhostId;

    /**
     * 域名
     */
    private String serverName;

    /**
     * 环境
     */
    private int envType;

    private long serverId;

    private String serverContent;


    /**
     * ansible返回消息
     */
    private String copyMsg;

    /**
     * copy是否成功
     */
    private boolean copySuccess;

    /**
     * copy返回消息:SUCCESS/FAILED!/UNREACHABLE!等
     */
    private String copyResult;

    private boolean copyChanged;

    /**
     * 是否执行脚本，有判断：按环境配置 和 copy是否成功+copyChanged 一起计算得到
     */
    private boolean doScript = false;

    /**
     * script执行否成功
     */
    private boolean scriptSuccess;

    private boolean scriptChanged;

    /**
     * 脚本stdout_linens文件存储位置,内容可能太大所以不写mysql了
     */
    private String  scriptStdoutPath;

    private int scriptRc;

    private String scriptStderr;

    /**
     * script返回消息:SUCCESS/FAILED!/UNREACHABLE!等
     */
    private String scriptResult;


    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCopyId() {
        return copyId;
    }

    public void setCopyId(long copyId) {
        this.copyId = copyId;
    }

    public long getVhostId() {
        return vhostId;
    }

    public void setVhostId(long vhostId) {
        this.vhostId = vhostId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getEnvType() {
        return envType;
    }

    public void setEnvType(int envType) {
        this.envType = envType;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public String getServerContent() {
        return serverContent;
    }

    public void setServerContent(String serverContent) {
        this.serverContent = serverContent;
    }

    public String getCopyMsg() {
        return copyMsg;
    }

    public void setCopyMsg(String copyMsg) {
        this.copyMsg = copyMsg;
    }

    public boolean isCopySuccess() {
        return copySuccess;
    }

    public void setCopySuccess(boolean copySuccess) {
        this.copySuccess = copySuccess;
    }

    public String getCopyResult() {
        return copyResult;
    }

    public void setCopyResult(String copyResult) {
        this.copyResult = copyResult;
    }

    public boolean isCopyChanged() {
        return copyChanged;
    }

    public void setCopyChanged(boolean copyChanged) {
        this.copyChanged = copyChanged;
    }

    public boolean isDoScript() {
        return doScript;
    }

    public void setDoScript(boolean doScript) {
        this.doScript = doScript;
    }

    public boolean isScriptSuccess() {
        return scriptSuccess;
    }

    public void setScriptSuccess(boolean scriptSuccess) {
        this.scriptSuccess = scriptSuccess;
    }

    public boolean isScriptChanged() {
        return scriptChanged;
    }

    public void setScriptChanged(boolean scriptChanged) {
        this.scriptChanged = scriptChanged;
    }

    public String getScriptStdoutPath() {
        return scriptStdoutPath;
    }

    public void setScriptStdoutPath(String scriptStdoutPath) {
        this.scriptStdoutPath = scriptStdoutPath;
    }

    public int getScriptRc() {
        return scriptRc;
    }

    public void setScriptRc(int scriptRc) {
        this.scriptRc = scriptRc;
    }

    public String getScriptStderr() {
        return scriptStderr;
    }

    public void setScriptStderr(String scriptStderr) {
        this.scriptStderr = scriptStderr;
    }

    public String getScriptResult() {
        return scriptResult;
    }

    public void setScriptResult(String scriptResult) {
        this.scriptResult = scriptResult;
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
