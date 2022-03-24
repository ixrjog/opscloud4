package com.offbytwo.jenkins.model;

import java.io.IOException;

public class MavenBuild extends Build {

    /**
     * This will be returned by the API in cases where no build has ever 
     * been executed like {@link JobWithDetails#getLastBuild()} etc.
     */
    public static final MavenBuild BUILD_HAS_NEVER_RUN = new MavenBuild(-1, -1, "UNKNOWN");

    private MavenBuild(int number, int queueId, String url) {
        setNumber(number);
        setQueueId(queueId);
        setUrl(url);
    }

    public MavenBuild() {
    }

    public MavenBuild(Build from) {
        this(from.getNumber(), from.getUrl());
    }

    public MavenBuild(int number, String url) {
        super(number, url);
    }

    public MavenModule getMavenModule() throws IOException {
        return client.get(this.getUrl() + "/mavenArtifacts/", MavenModule.class);
    }

    public JacocoCoverageReport getJacocoCodeCoverageReport() throws IOException {
        return client.get(this.getUrl() + "/jacoco/", JacocoCoverageReport.class);
    }
}
