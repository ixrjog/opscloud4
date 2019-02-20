package com.sdg.cmdb.domain.task;

import com.sdg.cmdb.domain.server.HostPattern;
import lombok.Data;

import java.io.Serializable;

@Data
public class PlaybookHostPattern extends HostPattern implements Serializable {
    private static final long serialVersionUID = 5638639075179493483L;

    private boolean choose;
    private String hostPatternSelected;

}
