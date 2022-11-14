package com.baiyi.opscloud.leo.build;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;

/**
 * @Author baiyi
 * @Date 2022/11/11 14:53
 * @Version 1.0
 */
public interface IBuildHandler {

    void doBuild(LeoBuild leoBuild);

    String getBuildType();

}
