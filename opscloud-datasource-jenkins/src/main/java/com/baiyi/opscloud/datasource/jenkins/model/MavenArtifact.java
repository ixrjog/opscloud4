package com.baiyi.opscloud.datasource.jenkins.model;

import lombok.Getter;

@Getter
public class MavenArtifact extends BaseModel {

    private String artifactId;
    private String canonicalName;
    private String classifier;
    private String fileName;
    private String groupId;
    private String md5sum;
    private String type;
    private String version;

    public MavenArtifact() {
    }

    public MavenArtifact setArtifactId(String artifactId) {
        this.artifactId = artifactId;
        return this;
    }

    public MavenArtifact setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
        return this;
    }

    public MavenArtifact setClassifier(String classifier) {
        this.classifier = classifier;
        return this;
    }

    public MavenArtifact setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public MavenArtifact setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public MavenArtifact setMd5sum(String md5sum) {
        this.md5sum = md5sum;
        return this;
    }

    public MavenArtifact setType(String type) {
        this.type = type;
        return this;
    }

    public MavenArtifact setVersion(String version) {
        this.version = version;
        return this;
    }

}