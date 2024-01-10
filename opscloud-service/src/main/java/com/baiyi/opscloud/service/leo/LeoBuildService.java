package com.baiyi.opscloud.service.leo;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.param.leo.LeoDeployParam;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.param.leo.LeoMonitorParam;
import com.baiyi.opscloud.domain.param.leo.request.SubscribeLeoBuildRequestParam;
import com.baiyi.opscloud.domain.vo.base.ReportVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/8 16:03
 * @Version 1.0
 */
public interface LeoBuildService {

    void add(LeoBuild leoBuild);

    LeoBuild getById(Integer id);

    void updateByPrimaryKeySelective(LeoBuild leoBuild);

    /**
     * 查询job的最大构建编号
     *
     * @param jobId
     * @return
     */
    int getMaxBuildNumberWithJobId(Integer jobId);

    List<LeoBuild> queryTheHistoricalBuildToBeDeleted(Integer jobId);

    List<LeoBuild> queryWithJobId(Integer jobId);

    DataTable<LeoBuild> queryBuildPage(SubscribeLeoBuildRequestParam pageQuery);

    DataTable<LeoBuild> queryBuildPage(LeoJobParam.JobBuildPageQuery pageQuery);

    List<LeoBuild> queryBuildVersion(LeoDeployParam.QueryDeployVersion queryBuildVersion);

    int countWithJobId(Integer jobId);

    List<LeoBuild> queryLatestBuildWithJobId(Integer jobId, int size);

    List<LeoBuild> queryNotFinishBuildWithOcInstance(String ocInstance);

    List<LeoBuild> queryNotFinishBuild();

    int countRunningWithJobId(int jobId);

    List<ReportVO.Report> statByMonth();

    List<ReportVO.Report> queryMonth();

    List<ReportVO.Report> statByEnvName();

    int countWithReport();

    int statUserTotal();

    void deleteById(Integer id);

    List<LeoBuild> queryLatestLeoBuild(LeoMonitorParam.QueryLatestBuild queryLatestBuild);

}