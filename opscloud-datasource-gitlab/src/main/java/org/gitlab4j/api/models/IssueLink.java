package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

public class IssueLink {

    private Issue sourceIssue;
    private Issue targetIssue;

    public Issue getSourceIssue() {
        return sourceIssue;
    }

    public void setSourceIssue(Issue sourceIssue) {
        this.sourceIssue = sourceIssue;
    }

    public Issue getTargetIssue() {
        return targetIssue;
    }

    public void setTargetIssue(Issue targetIssue) {
        this.targetIssue = targetIssue;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
