package org.gitlab4j.api.systemhooks;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

public class UserSystemHookEvent extends AbstractSystemHookEvent {

    public static final String USER_CREATE_EVENT = "user_create";
    public static final String USER_DESTROY_EVENT = "user_destroy";
    public static final String USER_RENAME_EVENT = "user_rename";
    public static final String USER_FAILED_LOGIN_EVENT = "user_failed_login";

    private String eventName;
    private Date createdAt;
    private Date updatedAt;
    private String email;
    private String name;
    private String username;
    private Long userId;
    private String oldUsername;
    private String state;

    @Override
	public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOldUsername() {
        return oldUsername;
    }

    public void setOldUsername(String oldUsername) {
        this.oldUsername = oldUsername;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
