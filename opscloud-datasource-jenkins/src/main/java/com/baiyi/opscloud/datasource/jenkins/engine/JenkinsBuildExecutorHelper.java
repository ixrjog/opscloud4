package com.baiyi.opscloud.datasource.jenkins.engine;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.datasource.jenkins.status.JenkinsBuildExecutorStatusVO;

/**
 * @Author baiyi
 * @Date 2022/8/1 10:01
 * @Version 1.0
 */
public interface JenkinsBuildExecutorHelper {

    JenkinsBuildExecutorStatusVO.Children generatorBuildExecutorStatus(DatasourceInstance instance);

}