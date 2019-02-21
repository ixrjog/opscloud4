package com.sdg.cmdb.domain.ansibleTask;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.task.DoPlaybook;
import lombok.Data;

import java.io.Serializable;

@Data
public class PlaybookTaskDO implements Serializable {
    private static final long serialVersionUID = 4169369174326315915L;

    public PlaybookTaskDO() {}

    public PlaybookTaskDO(DoPlaybook doPlaybook, UserDO userDO) {
        this.extraVars = doPlaybook.getExtraVars();
        this.userId = userDO.getId();
        this.displayName = userDO.getDisplayName();
    }

    private long id;
    private long userId;
    private String displayName;
    private String extraVars;
    private boolean complete = false;
    private String gmtCreate;
    private String gmtModify;

}
