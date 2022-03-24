package com.offbytwo.jenkins.model;

import java.util.List;

public class MavenModuleRecord extends BaseModel {

    private List<com.offbytwo.jenkins.model.MavenArtifact> attachedArtifacts;
    private Build parent;
    private com.offbytwo.jenkins.model.MavenArtifact mainArtifact;
    private com.offbytwo.jenkins.model.MavenArtifact pomArtifact;
    private String url;

    public MavenModuleRecord() {
    }

    public List<com.offbytwo.jenkins.model.MavenArtifact> getAttachedArtifacts() {
        return attachedArtifacts;
    }

    public MavenModuleRecord setAttachedArtifacts(List<com.offbytwo.jenkins.model.MavenArtifact> attachedArtifacts) {
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

    public com.offbytwo.jenkins.model.MavenArtifact getMainArtifact() {
        return mainArtifact;
    }

    public MavenModuleRecord setMainArtifact(com.offbytwo.jenkins.model.MavenArtifact mainArtifact) {
        this.mainArtifact = mainArtifact;
        return this;
    }

    public com.offbytwo.jenkins.model.MavenArtifact getPomArtifact() {
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
