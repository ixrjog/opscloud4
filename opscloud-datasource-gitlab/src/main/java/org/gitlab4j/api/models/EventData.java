
package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.List;

public class EventData {

    private String after;
    private String before;
    private List<Commit> commits;
    private String ref;
    private Repository repository;
    private Integer totalCommitsCount;
    private Long userId;
    private String userName;

    public String getAfter() {
        return this.after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getBefore() {
        return this.before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public List<Commit> getCommits() {
        return this.commits;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }

    public String getRef() {
        return this.ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Repository getRepository() {
        return this.repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Integer getTotalCommitsCount() {
        return this.totalCommitsCount;
    }

    public void setTotalCommitsCount(Integer totalCommitsCount) {
        this.totalCommitsCount = totalCommitsCount;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public EventData withAfter(String after) {
        this.after = after;
        return this;
    }

    public EventData withBefore(String before) {
        this.before = before;
        return this;
    }

    public EventData withCommits(List<Commit> commits) {
        this.commits = commits;
        return this;
    }

    public EventData withRef(String ref) {
        this.ref = ref;
        return this;
    }

    public EventData withRepository(Repository repository) {
        this.repository = repository;
        return this;
    }

    public EventData withTotalCommitsCount(Integer totalCommitsCount) {
        this.totalCommitsCount = totalCommitsCount;
        return this;
    }

    public EventData withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public EventData withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
