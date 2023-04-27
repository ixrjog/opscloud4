package com.baiyi.opscloud.leo.parser;

import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;

/**
 * @Author baiyi
 * @Date 2023/4/24 20:35
 * @Version 1.0
 */
public class MavenPublishParser {

    private MavenPublishParser() {
    }

    public static final String MAVEN = "maven";

    public static final String GRADLE = "gradle";

    /**
     * 解析构建工具配置
     *
     * @param buildTools
     * @param content
     * @return
     */
    public static LeoBuildVO.MavenPublishInfo parse(LeoBuildParam.BuildTools buildTools, LeoBaseModel.Nexus nexus, String content) {
        if (MAVEN.equalsIgnoreCase(buildTools.getType())) {
            return MavenParser.parse(nexus, content);
        }
        if (GRADLE.equalsIgnoreCase(buildTools.getType())) {
            return GradleParser.parse(nexus, content);
        }
        return LeoBuildVO.MavenPublishInfo.EMPTY_INFO;
    }

}