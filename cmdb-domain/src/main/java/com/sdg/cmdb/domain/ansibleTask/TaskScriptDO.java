package com.sdg.cmdb.domain.ansibleTask;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskScriptDO implements Serializable {

    private static final long serialVersionUID = -4555277124479409664L;

    private long id;
    private String scriptName;
    private String content;
    private long userId;
    private String username;
    private String script;
    // 脚本类型0:私有 1:公开
    private int scriptType = 0;
    // 1系统脚本
    private int sysScript = 0;
    private String modeType = "sh"; // 文本模式

    private String gmtCreate;
    private String gmtModify;
    public TaskScriptDO() {
    }


}
