package org.gitlab4j.api.systemhooks;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

public class GroupMemberSystemHookEvent extends AbstractSystemHookEvent {

    public static final String NEW_GROUP_MEMBER_EVENT = "user_add_to_group";
    public static final String GROUP_MEMBER_REMOVED_EVENT = "user_remove_from_group";

    private Date createdAt;
    private Date updatedAt;
    private String eventName;
    private String groupAccess;
    private String groupName;
    private String groupPath;
    private Long groupId;
    private String userEmail;
    private String userName;
    private String userUsername;
    private Long userId;

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

    @Override
	public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getGroupAccess() {
        return groupAccess;
    }

    public void setGroupAccess(String groupAccess) {
        this.groupAccess = groupAccess;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupPath() {
        return groupPath;
    }

    public void setGroupPath(String groupPath) {
        this.groupPath = groupPath;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
