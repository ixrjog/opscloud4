package com.baiyi.opscloud.leo.dict.impl;

import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.leo.constants.MavenPublishConstants;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.baiyi.opscloud.leo.handler.build.strategy.verification.PostBuildVerificationWithMavenPublishStrategy.MAVEN_PUBLISH;

/**
 * @Author baiyi
 * @Date 2023/4/25 13:22
 * @Version 1.0
 */
@Slf4j
@Component
public class MavenPublishBuildDictProvider extends BaseBuildDictProvider {

    @Override
    public String getBuildType() {
        return MAVEN_PUBLISH;
    }

    @Override
    protected void postHandle(LeoBuildParam.DoBuild doBuild, Map<String, String> dict) {
        Map<String, String> params = doBuild.getParams();
        if (params.containsKey(MavenPublishConstants.ARTIFACT_ID.getParam())) {
            dict.put(MavenPublishConstants.ARTIFACT_ID.getParam(), params.get(MavenPublishConstants.ARTIFACT_ID.getParam()));
        } else {
            throw new LeoBuildException("缺少参数: artifactId");
        }
        if (params.containsKey(MavenPublishConstants.GROUP_ID.getParam())) {
            dict.put(MavenPublishConstants.GROUP_ID.getParam(), params.get(MavenPublishConstants.GROUP_ID.getParam()));
        }
        if (StringUtils.isNotBlank(doBuild.getVersionName())) {
            dict.put(MavenPublishConstants.VERSION.getParam(), doBuild.getVersionName());
        } else {
            throw new LeoBuildException("缺少参数: versionName");
        }
    }

}