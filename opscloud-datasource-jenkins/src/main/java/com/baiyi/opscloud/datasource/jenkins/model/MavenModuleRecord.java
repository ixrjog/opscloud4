package com.baiyi.opscloud.datasource.jenkins.model;

import lombok.Getter;

import java.util.List;

@Getter
public class MavenModuleRecord extends BaseModel {

    private List<MavenArtifact> attachedArtifacts;
    private Build parent;
    private MavenArtifact mainArtifact;
    private MavenArtifact pomArtifact;
    private String url;

    public MavenModuleRecord() {
    }

    public MavenModuleRecord setAttachedArtifacts(List<MavenArtifact> attachedArtifacts) {
        this.attachedArtifacts = attachedArtifacts;
        return this;
    }

    public MavenModuleRecord setParent(Build parent) {
        this.parent = parent;
        return this;
    }

    public MavenModuleRecord setMainArtifact(MavenArtifact mainArtifact) {
        this.mainArtifact = mainArtifact;
        return this;
    }

    public MavenModuleRecord setPomArtifact(MavenArtifact pomArtifact) {
        this.pomArtifact = pomArtifact;
        return this;
    }

    public MavenModuleRecord setUrl(String url) {
        this.url = url;
        return this;
    }

}