package org.gitlab4j.api.webhook;

import org.gitlab4j.api.utils.JacksonJson;

public class EventReleaseLink {
    private Long id;
    private Boolean external;
    private String linkType;
    private String name;
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Boolean getExternal() {
        return external;
    }

    public void setExternal(final Boolean external) {
        this.external = external;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(final String linkType) {
        this.linkType = linkType;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
