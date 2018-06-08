package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.explain.ExplainInfo;
import com.sdg.cmdb.domain.explain.ExplainJob;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 2017/3/21.
 */
@Component
public interface ExplainDao {

    /**
     * 新增计划订阅
     * @param explainInfo
     * @return
     */
    int addRepoExplainSub(ExplainInfo explainInfo);

    /**
     * 删除计划订阅
     * @param id
     * @return
     */
    int delRepoExplainSub(@Param("id") long id);

    /**
     * 更新计划订阅
     * @param explainInfo
     * @return
     */
    int updateRepoExplainSub(ExplainInfo explainInfo);

    /**
     * 查询总记录数
     * @param repo
     * @return
     */
    long queryRepoExplainSubListSize(@Param("repo") String repo);

    /**
     * 查询分页数据
     * @param repo
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<ExplainInfo> queryRepoExplainSubListPage(@Param("repo") String repo,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * 查询已订阅仓库
     * @param repo
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<String> queryRepoList(@Param("repo") String repo,
                               @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * 查询指定的订阅
     * @param id
     * @return
     */
    ExplainInfo getRepoSubById(@Param("id") long id);

    /**
     * 新增执行计划
     * @param explainJob
     * @return
     */
    int addExplainJob(ExplainJob explainJob);

    /**
     * 查询指定条件的执行计划
     * @param explainJob
     * @return
     */
    List<ExplainJob> queryExplainJobs(ExplainJob explainJob);

    /**
     * 查询指定条件的执行计划数目
     * @param explainJob
     * @return
     */
    long queryExplainJobSize(ExplainJob explainJob);

    /**
     * 查询指定条件的执行计划分页数据
     * @param explainJob
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<ExplainJob> queryExplainJobPage(
            @Param("job") ExplainJob explainJob, @Param("pageStart") long pageStart,
            @Param("pageLength") int pageLength);

    /**
     * 更新计划任务
     * @param explainJob
     * @return
     */
    int updateExplainJob(ExplainJob explainJob);

    /**
     * 获取权重版本规则下的job集合
     * @return
     */
    List<ExplainJob> queryExplainJobsOrderByWeightVersion();
}
