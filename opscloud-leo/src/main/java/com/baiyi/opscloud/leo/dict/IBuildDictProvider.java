package com.baiyi.opscloud.leo.dict;

import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2023/4/25 11:33
 * @Version 1.0
 */
public interface IBuildDictProvider {

    /**
     * 构建类型
     * @return
     */
    String getBuildType();

    /**
     * 生产字典
     * @param doBuild
     * @return
     */
    Map<String, String> produce(LeoBuildParam.DoBuild doBuild);

}
