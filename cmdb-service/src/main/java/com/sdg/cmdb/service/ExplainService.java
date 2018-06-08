package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.explain.ExplainDTO;
import com.sdg.cmdb.domain.explain.ExplainInfo;
import com.sdg.cmdb.domain.explain.ExplainJob;

import java.util.List;
import java.util.Set;

/**
 * Created by zxxiao on 2017/3/21.
 */
public interface ExplainService {

    /**
     * 添加一个计划订阅
     * @param explainDTO
     */
    void addRepoExplainSub(ExplainDTO explainDTO);

    /**
     * 删除一个计划订阅
     * @param id
     */
    void delRepoExplainSub(long id);

    /**
     * 获取分页的计划订阅集合
     * @param repo
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ExplainDTO>> queryRepoExplainSubList(String repo, int page, int length);

    /**
     * 主动扫描计划订阅的内容
     * @param explainDTO
     */
    Set<String> doScanRepo(ExplainDTO explainDTO);

    /**
     * 查询已订阅仓库
     * @param repo
     * @param page
     * @param length
     * @return
     */
    TableVO<List<String>> queryRepoList(String repo, int page, int length);

    /**
     * 查询指定的订阅
     * @param id
     * @return
     */
    ExplainDTO getRepoSubById(long id);

    /**
     * 获取分页任务数据集合
     * @param explainJob
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ExplainJob>> queryJobs(ExplainJob explainJob, int page, int length);

    /**
     * 订阅任务
     * @param explainJob
     * @return
     */
    boolean subJob(ExplainJob explainJob);

    /**
     * 取消订阅任务
     * @param explainJob
     * @return
     */
    boolean unsubJob(ExplainJob explainJob);
}
