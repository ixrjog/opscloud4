package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;
import java.util.List;

public class MergeRequestDiff {

    private Long id;
    private String headCommitSha;
    private String baseCommitSha;
    private String startCommitSha;
    private Date createdAt;
    private Long mergeRequestId;
    private String state;
    private String realSize;
    private List<Commit> commits;
    private List<Diff> diffs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeadCommitSha() {
        return headCommitSha;
    }

    public void setHeadCommitSha(String headCommitSha) {
        this.headCommitSha = headCommitSha;
    }

    public String getBaseCommitSha() {
        return baseCommitSha;
    }

    public void setBaseCommitSha(String baseCommitSha) {
        this.baseCommitSha = baseCommitSha;
    }

    public String getStartCommitSha() {
        return startCommitSha;
    }

    public void setStartCommitSha(String startCommitSha) {
        this.startCommitSha = startCommitSha;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getMergeRequestId() {
        return mergeRequestId;
    }

    public void setMergeRequestId(Long mergeRequestId) {
        this.mergeRequestId = mergeRequestId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRealSize() {
        return realSize;
    }

    public void setRealSize(String realSize) {
        this.realSize = realSize;
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }

    public List<Diff> getDiffs() {
        return diffs;
    }

    public void setDiffs(List<Diff> diffs) {
        this.diffs = diffs;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
