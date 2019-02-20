package com.sdg.cmdb.domain.gitlab.v1;

import java.io.Serializable;

public class Repository implements Serializable {
    private static final long serialVersionUID = -5063790932768714354L;

    /**
     *
     "repository":{
     "name": "Diaspora",
     "url": "git@example.com:mike/diaspora.git",
     "description": "",
     "homepage": "http://example.com/mike/diaspora",
     "git_http_url":"http://example.com/mike/diaspora.git",
     "git_ssh_url":"git@example.com:mike/diaspora.git",
     "visibility_level":0
     }
     */
    private String name;
    private String url;
    private String description;
    private String homepage;
    private String git_http_url;
    private String git_ssh_url;
    private int visibility_level;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getGit_http_url() {
        return git_http_url;
    }

    public void setGit_http_url(String git_http_url) {
        this.git_http_url = git_http_url;
    }

    public String getGit_ssh_url() {
        return git_ssh_url;
    }

    public void setGit_ssh_url(String git_ssh_url) {
        this.git_ssh_url = git_ssh_url;
    }

    public int getVisibility_level() {
        return visibility_level;
    }

    public void setVisibility_level(int visibility_level) {
        this.visibility_level = visibility_level;
    }
}
