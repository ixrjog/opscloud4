package com.baiyi.opscloud.datasource.jenkins.model;

import java.io.IOException;

public class MavenJob extends Job {

    public MavenJob() {
    }

    public MavenJob(String name, String url) {
        super(name, url);
    }
    
    public MavenJob(String name, String url, String fullName) {
        super(name, url, fullName);
    }

    public MavenJobWithDetails mavenDetails() throws IOException {
        return client.get(getUrl(), MavenJobWithDetails.class);
    }

}