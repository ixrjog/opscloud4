package com.sdg.cmdb.domain.ansibleTask;

import com.sdg.cmdb.domain.task.PlaybookHostPattern;
import lombok.Data;

import java.io.Serializable;

@Data
public class PlaybookTaskHostDO implements Serializable {
    private static final long serialVersionUID = -3290536778725127736L;

    public PlaybookTaskHostDO() {
    }

    public PlaybookTaskHostDO(long playbookTaskId, PlaybookHostPattern hostPattern, long logId) {
        this.playbookTaskId = playbookTaskId;
        this.setHostPattern(hostPattern.getHostPatternSelected());
        this.logId = logId;
    }

    private long id;
    private long playbookTaskId;
    private long serverGroupId;
    private String hostPattern;
    private long logId;
    private boolean complete = false;
    private String gmtCreate;
    private String gmtModify;

}
