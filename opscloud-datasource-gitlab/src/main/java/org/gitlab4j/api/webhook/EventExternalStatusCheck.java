package org.gitlab4j.api.webhook;

import org.gitlab4j.api.utils.JacksonJson;

public class EventExternalStatusCheck {

    private Long id;
    private String name;
    private String externalUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
