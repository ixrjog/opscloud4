package com.baiyi.opscloud.service.leo;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.param.leo.request.SubscribeLeoJobRequestParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/4 14:38
 * @Version 1.0
 */
public interface LeoJobService {

    DataTable<LeoJob> queryJobPage(LeoJobParam.JobPageQuery pageQuery);

    DataTable<LeoJob> queryJobPage(SubscribeLeoJobRequestParam pageQuery);

    List<LeoJob> querJobWithApplicationIdAndEnvType(Integer applicationId, Integer envType);

    List<LeoJob> querJobWithApplicationId(Integer applicationId);

    void add(LeoJob leoJob);

    void update(LeoJob leoJob);

    LeoJob getById(Integer id);

    void deleteById(Integer id);

    void updateByPrimaryKeySelective(LeoJob leoJob);

    int countWithTemplateId(Integer templateId);

    int countWithReport();

}
