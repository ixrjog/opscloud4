package com.baiyi.opscloud.service.leo;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.param.leo.request.QueryLeoBuildLeoRequestParam;

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
     * @param jobId
     * @return
     */
    int getMaxBuildNumberWithJobId(Integer jobId);

    List<LeoBuild> queryTheHistoricalBuildToBeDeleted(Integer jobId);

    DataTable<LeoBuild> queryBuildPage(QueryLeoBuildLeoRequestParam pageQuery);

    int countWithJobId(Integer jobId);

}
