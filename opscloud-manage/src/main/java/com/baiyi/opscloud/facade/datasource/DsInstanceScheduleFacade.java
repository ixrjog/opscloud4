package com.baiyi.opscloud.facade.datasource;

import com.baiyi.opscloud.domain.vo.datasource.ScheduleVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/3/23 15:42
 * @Version 1.0
 */
public interface DsInstanceScheduleFacade {

    List<ScheduleVO.Job> queryJob(int instanceId);

}
