package com.sdg.cmdb.domain.ansibleTask;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlaybookTaskVO extends PlaybookTaskDO implements Serializable {
    private static final long serialVersionUID = -8618205182227434353L;
    private List<PlaybookTaskHostVO> taskHostList;
}
