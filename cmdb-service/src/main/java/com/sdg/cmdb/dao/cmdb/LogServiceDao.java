package com.sdg.cmdb.dao.cmdb;


import com.sdg.cmdb.domain.logService.*;
import com.sdg.cmdb.domain.logService.logServiceStatus.LogServiceGroupVO;
import com.sdg.cmdb.domain.logService.logServiceStatus.LogServiceUserVO;
import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceCfgDO;
import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceServerGroupCfgDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LogServiceDao {

    /**
     * 获取指定serverName的cfg
     *
     * @param start
     * @param length
     * @param serverName
     * @return
     */
    List<LogServiceCfgDO> queryLogServiceCfgPage(
            @Param("start") int start,
            @Param("length") int length,
            @Param("serverName") String serverName
    );


    int addLogService(LogServiceDO logServiceDO);


    int addLogHistograms(LogHistogramsDO logHistogramsDO);

    int getLogHistogramsSize(@Param("logServiceId") long logServiceId);

    List<LogHistogramsDO> getLogHistogramsPage(
            @Param("logServiceId") long logServiceId,
            @Param("pageStart") long pageStart, @Param("length") int length);

    LogServiceDO queryLogServiceById(@Param("id") long id);


    List<LogServicePathDO> queryLogServicePathPage(
            @Param("start") int start,
            @Param("length") int length,
            @Param("tagPath") String tagPath,
            @Param("serverGroupId") long serverGroupId
    );

    int getLogServicePathSize(@Param("tagPath") String tagPath,
                              @Param("serverGroupId") long serverGroupId);

    LogServiceServerGroupCfgDO queryLogServiceServerGroupCfg(@Param("serverGroupId") long serverGroupId);

    int addLogServiceServerGroupCfg(LogServiceServerGroupCfgDO logServiceServerGroupCfgDO);

    int updateLogServiceServerGroupCfg(LogServiceServerGroupCfgDO logServiceServerGroupCfgDO);


    LogServicePathDO queryLogServicePath(@Param("tagPath") String tagPath,
                                         @Param("serverGroupId") long serverGroupId);

    int addLogServicePath(LogServicePathDO logServicePathDO);

    int updateLogServicePath(LogServicePathDO logServicePathDO);

    /**
     * 统计使用日志服务的热门用户
     *
     * @return
     */
    List<LogServiceUserVO> statusLogServiceUser();

    /**
     * 统计热门项目组
     *
     * @return
     */
    List<LogServiceGroupVO> statusLogServiceGroup();

    /**
     * 按天数统计使用次数
     *
     * @param dayCnt
     * @return
     */
    int statusLogServiceCntByDay(@Param("dayCnt") int dayCnt);

    /**
     * 获取配置的服务器组数量
     *
     * @return
     */
    int getLogServiceServerGroupCfgCnt();

    /**
     * 获取已经配置的服务器数量
     *
     * @return
     */
    int getLogServiceServerGroupServerCnt();

    int getLogServiceGroupSize(@Param("project") String project, @Param("logstore") String logstore);

    int updateLogServiceGroupCfg(LogServiceGroupCfgDO logServiceGroupCfgDO);

    int addLogServiceGroupCfg(LogServiceGroupCfgDO logServiceGroupCfgDO);

    List<LogServiceGroupCfgDO> getLogServiceGroupPage(
            @Param("project") String project,
            @Param("logstore") String logstore,
            @Param("pageStart") long pageStart, @Param("length") int length);

    LogServiceGroupCfgDO getLogServiceGroup(@Param("id") long id);

    int addLogServiceMember(LogServiceMemberDO logServiceMemberDO);

    int delLogServiceMember(@Param("id") long id);

    List<LogServiceMemberDO> getLogServiceMemberByGroupCfgId(@Param("groupCfgId") long groupCfgId);

}
