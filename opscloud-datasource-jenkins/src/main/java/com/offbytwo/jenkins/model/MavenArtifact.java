package com.offbytwo.jenkins.model;

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

    public String getArtifactId() {
        return artifactId;
    }

    public MavenArtifact setArtifactId(String artifactId) {
        this.artifactId = artifactId;
        return this;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public MavenArtifact setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
        return this;
    }

    public String getClassifier() {
        return classifier;
    }

    public MavenArtifact setClassifier(String classifier) {
        this.classifier = classifier;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public MavenArtifact setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getGroupId() {
        return groupId;
    }

    public MavenArtifact setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getMd5sum() {
        return md5sum;
    }

    public MavenArtifact setMd5sum(String md5sum) {
        this.md5sum = md5sum;
        return this;
    }

    public String getType() {
        return type;
    }

    public MavenArtifact setType(String type) {
        this.type = type;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public MavenArtifact setVersion(String version) {
        this.version = version;
        return this;
    }
}
