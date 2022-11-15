package com.baiyi.opscloud.leo.building.base;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.building.IBuildHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author baiyi
 * @Date 2022/11/11 14:51
 * @Version 1.0
 */
@Slf4j
public abstract class BaseBuildHandler implements IBuildHandler {

    @Override
    public void doBuild(LeoBuild leoBuild) {
        // 设置上下文


    }

}
