package com.baiyi.opscloud.leo.supervisor;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.log.BuildingLogHelper;
import com.baiyi.opscloud.leo.supervisor.base.ISupervisor;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author baiyi
 * @Date 2022/11/9 11:43
 * @Version 1.0
 */
@Slf4j
public class BuildingSupervisor implements ISupervisor {

    private final LeoBuild leoBuild;

    private final LeoBuildService leoBuildService;

    private final BuildingLogHelper logHelper;

    public BuildingSupervisor(LeoBuildService leoBuildService,
                              LeoBuild leoBuild,
                              BuildingLogHelper logHelper) {
        this.leoBuildService = leoBuildService;
        this.leoBuild = leoBuild;
        this.logHelper = logHelper;
    }

    @Override
    public void run() {
        try {
            while (true) {

            }
        } catch (Exception e) {
            logHelper.error(leoBuild, "异常错误任务结束: err={}", e.getMessage());
        }
    }

}
