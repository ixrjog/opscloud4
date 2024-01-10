package com.baiyi.opscloud.service.leo;

import com.baiyi.opscloud.domain.generator.opscloud.LeoDeployLog;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/12/5 20:34
 * @Version 1.0
 */
public interface LeoDeployLogService {

    void add(LeoDeployLog leoDeployLog);

    List<LeoDeployLog> queryByDeployId(Integer deployId);

    List<LeoDeployLog> queryLatestLogByDeployId(Integer deployId, int size);

    void deleteWithDeployId(Integer deployId);

}