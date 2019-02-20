package com.sdg.cmdb.domain.ansibleTask;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlaybookTaskHostVO extends PlaybookTaskHostDO implements Serializable {

    private static final long serialVersionUID = 8081439345489474808L;

    private PlaybookLogVO playbookLogVO;

}
