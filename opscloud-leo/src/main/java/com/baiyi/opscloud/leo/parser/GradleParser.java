package com.baiyi.opscloud.leo.parser;

import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/4/27 17:12
 * @Version 1.0
 */
public class GradleParser {

    private GradleParser() {
    }

    /**
     * 解析Gradle 配置
     *
     * @param nexus
     * @param content build.gradle
     * @return
     */
    public static LeoBuildVO.MavenPublishInfo parse(LeoBaseModel.Nexus nexus, String content) {
        final String groupId = Optional.of(nexus)
                .map(LeoBaseModel.Nexus::getComponent)
                .map(LeoBaseModel.NexusComponent::getGroup)
                .orElseThrow(() -> new LeoBuildException("Configuration does not exist: nexus->component->group"));
        final String artifactId = Optional.of(nexus)
                .map(LeoBaseModel.Nexus::getComponent)
                .map(LeoBaseModel.NexusComponent::getName)
                .orElseThrow(() -> new LeoBuildException("Configuration does not exist: nexus->component->name"));
        String version = findVersion(content)
                .replace("version", "")
                .replaceAll("'", "")
                // 删除空格
                .trim();
        return LeoBuildVO.MavenPublishInfo.builder()
                .groupId(groupId)
                .artifactId(artifactId)
                .version(version)
                .build();
    }

    private static String findVersion(String versionFile) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(versionFile.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8))
        ) {
            String line = reader.readLine();
            while (line != null) {
                String regex = "^\\s*version\\s*\\S*";
                if (line.matches(regex)) {
                    return line;
                }
                line = reader.readLine();
            }
        } catch (IOException ignored) {
        }
        throw new LeoBuildException("无法从gradle配置文件中获取版本信息！");
    }

}