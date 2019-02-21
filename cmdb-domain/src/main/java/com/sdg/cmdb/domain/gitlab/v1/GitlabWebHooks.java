package com.sdg.cmdb.domain.gitlab.v1;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

public class GitlabWebHooks implements Serializable {

    private static final long serialVersionUID = -4806578823588739972L;

    private String object_kind;

    private String before;
    private String after;
    private String ref;
    private String checkout_sha;
    private long user_id;
    private String user_name;
    private String user_username;
    private String user_email;
    private String user_avatar;
    private long project_id;

    private Project project;

    private Repository repository;

    private List<Commits> commits;

    private int total_commits_count;

    public String getObject_kind() {
        return object_kind;
    }

    public void setObject_kind(String object_kind) {
        this.object_kind = object_kind;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getCheckout_sha() {
        return checkout_sha;
    }

    public void setCheckout_sha(String checkout_sha) {
        this.checkout_sha = checkout_sha;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_username() {
        return user_username;
    }

    public void setUser_username(String user_username) {
        this.user_username = user_username;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public long getProject_id() {
        return project_id;
    }

    public void setProject_id(long project_id) {
        this.project_id = project_id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public List<Commits> getCommits() {
        return commits;
    }

    public void setCommits(List<Commits> commits) {
        this.commits = commits;
    }

    public int getTotal_commits_count() {
        return total_commits_count;
    }

    public void setTotal_commits_count(int total_commits_count) {
        this.total_commits_count = total_commits_count;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
