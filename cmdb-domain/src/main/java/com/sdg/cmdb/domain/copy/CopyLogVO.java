package com.sdg.cmdb.domain.copy;

import java.io.Serializable;

public class CopyLogVO extends CopyLogDO implements Serializable {
    private static final long serialVersionUID = -5795638822323091621L;

    public CopyLogVO() {

    }

    public CopyLogVO(CopyLogDO copyLogDO) {
        this.setId(copyLogDO.getId());
        this.setCopyId(copyLogDO.getCopyId());
        this.setVhostId(copyLogDO.getVhostId());
        this.setServerName(copyLogDO.getServerName());
        this.setEnvType(copyLogDO.getEnvType());
        this.setServerContent(copyLogDO.getServerContent());
        this.setCopyMsg(copyLogDO.getCopyMsg());
        this.setCopySuccess(copyLogDO.isCopySuccess());

        this.setCopyResult(copyLogDO.getCopyResult());
        this.setCopyChanged(copyLogDO.isCopyChanged());
        this.setDoScript(copyLogDO.isDoScript());
        this.setScriptSuccess(copyLogDO.isScriptSuccess());
        this.setScriptChanged(copyLogDO.isScriptChanged());
        this.setScriptRc(copyLogDO.getScriptRc());
        this.setScriptStderr(copyLogDO.getScriptStderr());
        this.setScriptResult(copyLogDO.getScriptResult());
        this.setScriptStdoutPath(copyLogDO.getScriptStdoutPath());
    }

    private String timeView;

    private String scriptMsg;

    public String getTimeView() {
        return timeView;
    }

    public void setTimeView(String timeView) {
        this.timeView = timeView;
    }

    public String getScriptMsg() {
        return scriptMsg;
    }

    public void setScriptMsg(String scriptMsg) {
        this.scriptMsg = scriptMsg;
    }




}
