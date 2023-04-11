package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractUser<U extends AbstractUser<U>> {

    private String avatarUrl;
    private Date createdAt;
    private String email;
    private Long id;
    private String name;
    private String state;
    private String username;
    private String webUrl;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    @SuppressWarnings("unchecked")
    public U withAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return (U)this;
    }

    @SuppressWarnings("unchecked")
    public U withCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return (U)this;
    }

    @SuppressWarnings("unchecked")
    public U withEmail(String email) {
        this.email = email;
        return (U)this;
    }

    @SuppressWarnings("unchecked")
    public U withId(Long id) {
        this.id = id;
        return (U)this;
    }

    @SuppressWarnings("unchecked")
    public U withName(String name) {
        this.name = name;
        return (U)this;
    }

    @SuppressWarnings("unchecked")
    public U withState(String state) {
        this.state = state;
        return (U)this;
    }

    @SuppressWarnings("unchecked")
    public U withUsername(String username) {
        this.username = username;
        return (U)this;
    }

    @SuppressWarnings("unchecked")
    public U withWebUrl(String webUrl) {
        this.webUrl = webUrl;
        return (U)this;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
