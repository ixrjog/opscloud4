package com.sdg.cmdb.domain.logService;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class LogServiceGroupCfgVO extends LogServiceGroupCfgDO implements Serializable {
    private static final long serialVersionUID = 1411284059513875262L;

    private List<LogServiceMemberDO> memberList;

}
