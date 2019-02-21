package com.sdg.cmdb.domain.server;

import com.sdg.cmdb.domain.ci.CiDeployHistoryVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HostPatternCi implements Serializable {

    private static final long serialVersionUID = 3044521208265176295L;

    private String hostPattern;
    private List<CiDeployHistoryVO> servers;

    public HostPatternCi() {
    }

    public HostPatternCi(String hostPattern, List<CiDeployHistoryVO> servers) {
        this.hostPattern = hostPattern;
        this.servers = servers;
    }

}
