package com.baiyi.opscloud.leo.dict.impl;

import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.dict.IBuildDictGenerator;
import com.baiyi.opscloud.leo.dict.factory.BuildDictGeneratorFactory;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2023/4/25 11:41
 * @Version 1.0
 */
@Slf4j
public abstract class BaseBuildDictGenerator implements IBuildDictGenerator, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        BuildDictGeneratorFactory.register(this);
    }

    @Override
    public Map<String, String> generate(LeoBuildParam.DoBuild doBuild) {
        Map<String, String> dict = Maps.newHashMap();
        dict.put(BuildDictConstants.BRANCH.getKey(), doBuild.getBranch());
        postHandle(doBuild, dict);
        return dict;
    }

    /**
     * 重写
     *
     * @param doBuild
     * @param dict
     */
    protected void postHandle(LeoBuildParam.DoBuild doBuild, Map<String, String> dict) {
    }

}