package com.baiyi.opscloud.leo.parser;

import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
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
 * @Date 2023/4/27 17:11
 * @Version 1.0
 */
public class MavenParser {

    private MavenParser() {
    }

    /**
     * 解析Maven pom配置
     *
     * @param content pom.xml
     * @return
     */
    public static LeoBuildVO.MavenPublishInfo parse(LeoBaseModel.Nexus nexus, String content) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try {
            Model pomModel = reader.read(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
            final String groupId = Optional.of(pomModel)
                    .map(Model::getGroupId)
                    .orElse(Optional.of(pomModel)
                            .map(Model::getParent)
                            .map(Parent::getGroupId)
                            .orElse(Optional.of(nexus)
                                    .map(LeoBaseModel.Nexus::getComponent)
                                    .map(LeoBaseModel.NexusComponent::getGroup)
                                    .orElse("")));
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