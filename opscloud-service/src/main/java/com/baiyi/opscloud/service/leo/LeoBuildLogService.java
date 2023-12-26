package com.baiyi.opscloud.service.leo;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuildLog;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/9 14:02
 * @Version 1.0
 */
public interface LeoBuildLogService {

    /**
     * 新增日志
     *
     * @param leoBuildLog
     */
    void add(LeoBuildLog leoBuildLog);

    /**
     * 查询构建的所有日志
     *
     * @param buildId
     * @return
     */
    List<LeoBuildLog> queryByBuildId(Integer buildId);

    /**
     * 查询构建最新的日志
     *
     * @param buildId
     * @param size    日志条数
     * @return
     */
    List<LeoBuildLog> queryLatestLogByBuildId(Integer buildId, int size);

    void deleteWithBuildId(Integer buildId);

}