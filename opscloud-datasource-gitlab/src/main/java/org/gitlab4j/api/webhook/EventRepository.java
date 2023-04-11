package org.gitlab4j.api.webhook;

import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.utils.JacksonJson;

public class EventRepository {

    private String name;
    private String url;
    private String description;
    private String homepage;
    private String git_http_url;
    private String git_ssh_url;
    private AccessLevel visibility_level;

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

    public AccessLevel getVisibility_level() {
        return visibility_level;
    }

    public void setVisibility_level(AccessLevel visibility_level) {
        this.visibility_level = visibility_level;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
