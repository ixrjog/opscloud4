package com.sdg.cmdb.domain.logService.logServiceStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class LogServiceStatusVO implements Serializable {
    private static final long serialVersionUID = -8195218469297970447L;

    private HashMap<String, Integer> cntMap;

    private LogServiceCfgVO logServiceCfgVO;


    //统计热门项目
    private List<LogServiceGroupVO> topGroupList;

    //统计热门用户
    private List<LogServiceUserVO> topUserList;


    public HashMap<String, Integer> getCntMap() {
        return cntMap;
    }

    public void setCntMap(HashMap<String, Integer> cntMap) {
        this.cntMap = cntMap;
    }

    public LogServiceCfgVO getLogServiceCfgVO() {
        return logServiceCfgVO;
    }

    public void setLogServiceCfgVO(LogServiceCfgVO logServiceCfgVO) {
        this.logServiceCfgVO = logServiceCfgVO;
    }

    public List<LogServiceGroupVO> getTopGroupList() {
        return topGroupList;
    }

    public void setTopGroupList(List<LogServiceGroupVO> topGroupList) {
        this.topGroupList = topGroupList;
    }

    public List<LogServiceUserVO> getTopUserList() {
        return topUserList;
    }

    public void setTopUserList(List<LogServiceUserVO> topUserList) {
        this.topUserList = topUserList;
    }
}
