package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.ci.CiDeployServerVersionVO;
import com.sdg.cmdb.domain.ci.CiDeployStatisticsDO;
import com.sdg.cmdb.domain.ci.ciStatus.CiStatusVO;


import java.util.List;

/**
 * Created by liangjian on 2017/2/17.
 */
public interface CiService {


    TableVO<List<CiDeployStatisticsDO>> getCiDeployStatisticsPage(String project, int status, int deployType, int rollback,
                                                                  int errorCode, int page, int length);


    /**
     * 获取服务器分页数据
     *
     * @param serverGroupId
     * @param serverName
     * @param envType
     * @param queryIp
     * @param page
     * @param length
     * @return
     */
    TableVO<List<CiDeployServerVersionVO>> getServerPage(long serverGroupId, String serverName, int envType, String queryIp, int page, int length);


    /**
     * 记录部署日志
     * @param ciDeployStatisticsDO
     */
    void doCiDeployStatistics(CiDeployStatisticsDO ciDeployStatisticsDO);

    CiStatusVO ciStatus();


}
