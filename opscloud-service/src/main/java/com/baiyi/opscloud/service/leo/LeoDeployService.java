package com.baiyi.opscloud.service.leo;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.param.leo.LeoMonitorParam;
import com.baiyi.opscloud.domain.param.leo.request.SubscribeLeoDeployRequestParam;
import com.baiyi.opscloud.domain.vo.base.ReportVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/12/5 18:02
 * @Version 1.0
 */
public interface LeoDeployService {

    LeoDeploy getById(Integer id);

    int getMaxDeployNumberWithJobId(Integer jobId);

    void add(LeoDeploy leoDeploy);

    void updateByPrimaryKeySelective(LeoDeploy leoDeploy);

    DataTable<LeoDeploy> queryDeployPage(SubscribeLeoDeployRequestParam pageQuery);

    DataTable<LeoDeploy> queryDeployPage(LeoJobParam.JobDeployPageQuery pageQuery);

    /**
     * 查询未完成的任务
     *
     * @return
     */
    List<LeoDeploy> queryNotFinishDeployWithOcInstance(String ocInstance);

    List<LeoDeploy> queryWithJobId(Integer jobId);

    List<LeoDeploy> queryWithBuildId(Integer buildId);

    int countRunningWithJobId(int jobId);

    int countRunningWithJobIdAndAssetId(int jobId, int assetId);

    int countWithJobId(int jobId);

    List<ReportVO.Report> statByMonth();

    List<ReportVO.Report> statLast30Days();

    int countWithReport();

    void deleteById(Integer id);

    List<ReportVO.Report> statByEnvName();

    List<LeoDeploy> queryLatestLeoDeploy(LeoMonitorParam.QueryLatestDeploy queryLatestDeploy);

    /**
     * 按项目统计
     *
     * @param projectId
     * @return
     */
    int countByProjectId(int projectId);

    Integer countByEnvProjectId(Integer projectId, Integer envType);

}