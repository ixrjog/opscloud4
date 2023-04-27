package com.baiyi.opscloud.leo.parser;

import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

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
    public static LeoBuildVO.MavenPublishInfo parse(LeoBuildParam.BuildTools buildTools, String content) {
        if (MAVEN.equalsIgnoreCase(buildTools.getType())) {
            return parseMaven(content);
        }
        if (GRADLE.equalsIgnoreCase(buildTools.getType())) {
            return LeoBuildVO.MavenPublishInfo.EMPTY_INFO;
        }
        return LeoBuildVO.MavenPublishInfo.EMPTY_INFO;
    }

    /**
     * 解析Maven pom配置
     *
     * @param content
     * @return
     */
    private static LeoBuildVO.MavenPublishInfo parseMaven(String content) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try {
            Model pomModel = reader.read(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
            final String groupId = Optional.of(pomModel)
                    .map(Model::getGroupId)
                    .orElse(Optional.of(pomModel)
                            .map(Model::getParent)
                            .map(Parent::getGroupId)
                            .orElse(""));
            return LeoBuildVO.MavenPublishInfo.builder()
                    .groupId(groupId)
                    .artifactId(pomModel.getArtifactId())
                    .version(pomModel.getVersion())
                    .build();
        } catch (IOException | XmlPullParserException ignored) {
        }
        return LeoBuildVO.MavenPublishInfo.EMPTY_INFO;
    }

}
