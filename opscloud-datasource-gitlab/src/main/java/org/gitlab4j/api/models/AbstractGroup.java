
package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.gitlab4j.api.utils.JacksonJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractGroup<G extends AbstractGroup<G>> {

    private Long id;
    private String name;
    private String avatarUrl;
    private String webUrl;
    private String fullName;
    private String fullPath;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    @SuppressWarnings("unchecked")
    public G withId(Long id) {
        this.id = id;
        return (G)this;
    }

    @SuppressWarnings("unchecked")
    public G withName(String name) {
        this.name = name;
        return (G)this;
    }

    @SuppressWarnings("unchecked")
    public G withAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return (G)this;
    }

    @SuppressWarnings("unchecked")
    public G withWebUrl(String url) {
        this.webUrl = url;
        return (G)this;
    }

    @SuppressWarnings("unchecked")
    public G withFullName(String fullName) {
        this.fullName = fullName;
        return (G)this;
    }

    @SuppressWarnings("unchecked")
    public G withFullPath(String fullPath) {
        this.fullPath = fullPath;
        return (G)this;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
