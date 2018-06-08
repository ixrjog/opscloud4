package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.ci.CiDeployServerVersionDO;
import com.sdg.cmdb.domain.ci.CiDeployStatisticsDO;
import com.sdg.cmdb.domain.ci.ciStatus.CiDeployVO;
import com.sdg.cmdb.domain.ci.ciStatus.CiProjectVO;
import com.sdg.cmdb.domain.ci.ciStatus.CiRepoVO;
import com.sdg.cmdb.domain.ci.ciStatus.CiDeployUserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liangjian on 2017/2/17.
 */
@Component
public interface CiDao {


    /**
     * 查询指定条件的属性组数目
     *
     * @param projectName
     * @param status
     * @param deployType
     * @param bambooDeployRollback
     * @param errorCode
     * @return
     */
    long getCiDeployStatisticsSize(
            @Param("projectName") String projectName,
            @Param("status") int status,
            @Param("deployType") int deployType,
            @Param("bambooDeployRollback") int bambooDeployRollback,
            @Param("errorCode") int errorCode
    );

    /**
     * 查询指定条件的属性组分页数据
     *
     * @param projectName
     * @param status
     * @param deployType
     * @param bambooDeployRollback
     * @param errorCode
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<CiDeployStatisticsDO> getCiDeployStatisticsPage(
            @Param("projectName") String projectName,
            @Param("status") int status,
            @Param("deployType") int deployType,
            @Param("bambooDeployRollback") int bambooDeployRollback,
            @Param("errorCode") int errorCode,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);


    /**
     * 新增部署统计
     *
     * @param ciDeployStatisticsDO
     * @return
     */
    long addCiDeployStatistics(CiDeployStatisticsDO ciDeployStatisticsDO);

    CiDeployStatisticsDO getCiDeployStatisticsByDeployId(@Param("deployId") long deployId);

    CiDeployStatisticsDO getCiDeployStatisticsById(@Param("id") long id);

    long updateCiDeployStatistics(CiDeployStatisticsDO ciDeployStatisticsDO);

    /**
     * 新增部署版本统计
     *
     * @param ciDeployServerVersionDO
     * @return
     */
    long addCiDeployServerVersion(CiDeployServerVersionDO ciDeployServerVersionDO);


    long updateCiDeployServerVersion(CiDeployServerVersionDO ciDeployServerVersionDO);

    CiDeployServerVersionDO getCiDeployServerVersionByServerId(@Param("serverId") long serverId);

    /**
     * 统计部署次数
     *
     * @return
     */
    int getCiDeployedCnt();

    /**
     * 统计项目总数
     *
     * @return
     */
    int getCiProjectCnt();

    /**
     * 统计项目部署Top
     *
     * @return
     */
    List<CiProjectVO> statusCiDeployProject();

    /**
     * 统计部署次数最多的用户Top
     *
     * @return
     */
    List<CiDeployUserVO> statusCiDeployUser();

    /**
     * 统计部署最多的仓库Top
     * @return
     */
    List<CiRepoVO> statusCiRepo();

    /**
     * 统计最新部署
     * @return
     */
    List<CiDeployVO> statusCiDeploy();


}
