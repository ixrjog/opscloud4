/*
 * Copyright (c) 2018 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */
package com.baiyi.opscloud.datasource.jenkins.model;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.datasource.jenkins.helper.FunctionalHelper.SET_CLIENT;
import static java.util.stream.Collectors.toList;

/**
 * Model Class for Maven Modules
 * 
 * @author Jakub Zacek
 */
public class MavenModuleWithDetails extends BaseModel {

    private List<Build> builds = Collections.emptyList();
    private List actions = Collections.emptyList();
    private String displayName;
    private BuildResult result;
    private String url;
    private long duration;
    private long timestamp;

    public List getActions() {
        return actions;
    }

    public void setActions(List actions) {
        this.actions = actions;
    }

    public void setBuilds(List<Build> builds) {
        this.builds = builds;
    }

    public List<Build> getBuilds() {
        return builds.stream()
            .map(SET_CLIENT(this.getClient()))
            .collect(toList());
    }
    
    public Build getBuildByNumber(final int buildNumber) {
        return builds.stream()
            .filter(isBuildNumberEqualTo(buildNumber))
            .map(SET_CLIENT(this.getClient()))
            .findFirst()
            .orElse(Build.BUILD_HAS_NEVER_RUN);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public BuildResult getResult() {
        return result;
    }

    public void setResult(BuildResult result) {
        this.result = result;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getConsoleOutputText() throws IOException {
        return client.get(getUrl() + "/logText/progressiveText");
    }

    public TestReport getTestReport() throws IOException {
        return client.get(this.getUrl() + "/testReport/?depth=1", TestReport.class);
    }

}
