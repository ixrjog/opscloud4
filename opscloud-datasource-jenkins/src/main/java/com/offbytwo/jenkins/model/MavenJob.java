package com.offbytwo.jenkins.model;

import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.MavenJobWithDetails;

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

    public com.offbytwo.jenkins.model.MavenJobWithDetails mavenDetails() throws IOException {
        return client.get(getUrl(), MavenJobWithDetails.class);
    }
}
