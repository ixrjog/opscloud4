package org.gitlab4j.api.systemhooks;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

public class KeySystemHookEvent extends AbstractSystemHookEvent {
    
    public static final String KEY_CREATE_EVENT = "key_create";
    public static final String KEY_DESTROY_EVENT = "key_destroy";
    
    private Date createdAt;
    private Date updatedAt;
    private String eventName;
    private String username;
    private String key;
    private Long id;

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

    public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
