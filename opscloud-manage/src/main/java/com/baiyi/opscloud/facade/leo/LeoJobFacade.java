package com.baiyi.opscloud.facade.leo;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.generator.opscloud.LeoTemplate;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVO;

/**
 * @Author baiyi
 * @Date 2022/11/4 14:44
 * @Version 1.0
 */
public interface LeoJobFacade {

    DataTable<LeoJobVO.Job> queryLeoJobPage(LeoJobParam.JobPageQuery pageQuery);

    void addLeoJob(LeoJobParam.AddJob addJob);

    void updateLeoJob(LeoJobParam.UpdateJob updateJob);

    /**
     * 升级任务模板内容
     *
     * @param jobId
     */
    void upgradeLeoJobTemplateContent(int jobId);

    /**
     * 升级任务模板内容(内部使用)
     * @param leoJob
     * @param leoTemplate
     * @param templateVersion
     */
    void upgradeLeoJobTemplateContent(LeoJob leoJob, LeoTemplate leoTemplate, String templateVersion);

    void deleteLeoJobById(int jobId);

    void createCrRepositoryWithLeoJobId(int jobId);

    void cloneJob(LeoJobParam.CloneJob cloneJob);

}
