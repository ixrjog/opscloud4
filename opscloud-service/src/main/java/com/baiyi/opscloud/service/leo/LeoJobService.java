package com.baiyi.opscloud.service.leo;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;

/**
 * @Author baiyi
 * @Date 2022/11/4 14:38
 * @Version 1.0
 */
public interface LeoJobService {

    DataTable<LeoJob> queryJobPage(LeoJobParam.JobPageQuery pageQuery);

    void add(LeoJob leoJob);

    void update(LeoJob leoJob);

    LeoJob getById(Integer id);

    void deleteById(Integer id);

    void updateByPrimaryKeySelective(LeoJob leoJob);

    int countWithTemplateId(Integer templateId);

}
