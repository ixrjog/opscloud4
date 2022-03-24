/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

public class BuildCause {
    private String shortDescription;

    // For upstreams
    private Integer upstreamBuild;
    private String upstreamProject;
    private String upstreamUrl;

    // For manual kickoffs
    private String userId;
    private String userName;

    public BuildCause() {
        this.upstreamBuild = new Integer(0);
        //TODO: Think about initialization of the other
        // attributes as well.
        // userId = StringConstant.EMPTY;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public BuildCause setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public int getUpstreamBuild() {
        return upstreamBuild;
    }

    public BuildCause setUpstreamBuild(Integer upstreamBuild) {
        this.upstreamBuild = upstreamBuild;
        return this;
    }

    public String getUpstreamProject() {
        return upstreamProject;
    }

    public BuildCause setUpstreamProject(String upstreamProject) {
        this.upstreamProject = upstreamProject;
        return this;
    }

    public String getUpstreamUrl() {
        return upstreamUrl;
    }

    public BuildCause setUpstreamUrl(String upstreamUrl) {
        this.upstreamUrl = upstreamUrl;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public BuildCause setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public BuildCause setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BuildCause that = (BuildCause) o;

        if (shortDescription != null ? !shortDescription.equals(that.shortDescription) : that.shortDescription != null)
            return false;
        if (upstreamBuild != null ? !upstreamBuild.equals(that.upstreamBuild) : that.upstreamBuild != null)
            return false;
        if (upstreamProject != null ? !upstreamProject.equals(that.upstreamProject) : that.upstreamProject != null)
            return false;
        if (upstreamUrl != null ? !upstreamUrl.equals(that.upstreamUrl) : that.upstreamUrl != null)
            return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null)
            return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = shortDescription != null ? shortDescription.hashCode() : 0;
        result = 31 * result + (upstreamBuild != null ? upstreamBuild.hashCode() : 0);
        result = 31 * result + (upstreamProject != null ? upstreamProject.hashCode() : 0);
        result = 31 * result + (upstreamUrl != null ? upstreamUrl.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        return result;
    }
}
