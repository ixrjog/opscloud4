package com.baiyi.opscloud.service.leo;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.param.leo.request.QueryLeoDeployRequestParam;

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

    DataTable<LeoDeploy> queryDeployPage(QueryLeoDeployRequestParam pageQuery);

    int countDeployingWithJobId(int jobId);

}
