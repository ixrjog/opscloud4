package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

public class Repository {

    private String description;
    private String homepage;
    private String name;
    private String url;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomepage() {
        return this.homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
