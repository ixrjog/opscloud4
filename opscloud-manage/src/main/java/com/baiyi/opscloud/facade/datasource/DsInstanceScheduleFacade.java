package com.baiyi.opscloud.facade.datasource;

import com.baiyi.opscloud.domain.param.datasource.DsInstanceScheduleParam;
import com.baiyi.opscloud.domain.vo.datasource.ScheduleVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/3/23 15:42
 * @Version 1.0
 */
public interface DsInstanceScheduleFacade {

    List<ScheduleVO.Job> queryJob(int instanceId);

    void addJob(DsInstanceScheduleParam.AddJob param);

    void pauseJob(DsInstanceScheduleParam.UpdateJob param);

    void resumeJob(DsInstanceScheduleParam.UpdateJob param);

    void deleteJob(DsInstanceScheduleParam.DeleteJob deleteJob);

    List<String> checkCron(DsInstanceScheduleParam.CheckCron param);
}
