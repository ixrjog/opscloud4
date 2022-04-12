package com.baiyi.opscloud.datasource.jenkins.model;

import java.util.List;

public class MavenModuleRecord extends BaseModel {

    private List<MavenArtifact> attachedArtifacts;
    private Build parent;
    private MavenArtifact mainArtifact;
    private MavenArtifact pomArtifact;
    private String url;

    public MavenModuleRecord() {
    }

    public List<MavenArtifact> getAttachedArtifacts() {
        return attachedArtifacts;
    }

    public MavenModuleRecord setAttachedArtifacts(List<MavenArtifact> attachedArtifacts) {
        this.attachedArtifacts = attachedArtifacts;
        return this;
    }

    public Build getParent() {
        return parent;
    }

    public MavenModuleRecord setParent(Build parent) {
        this.parent = parent;
        return this;
    }

    public MavenArtifact getMainArtifact() {
        return mainArtifact;
    }

    public MavenModuleRecord setMainArtifact(MavenArtifact mainArtifact) {
        this.mainArtifact = mainArtifact;
        return this;
    }

    public MavenArtifact getPomArtifact() {
        return pomArtifact;
    }

    public MavenModuleRecord setPomArtifact(MavenArtifact pomArtifact) {
        this.pomArtifact = pomArtifact;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public MavenModuleRecord setUrl(String url) {
        this.url = url;
        return this;
    }
}
