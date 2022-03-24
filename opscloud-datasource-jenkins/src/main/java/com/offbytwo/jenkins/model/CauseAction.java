package com.offbytwo.jenkins.model;

public class CauseAction extends BaseModel {
    private String shortDescription;
    private String userId;
    private String userName;

    public String getShortDescription() {
        return shortDescription;
    }

    public CauseAction setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public CauseAction setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public CauseAction setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((shortDescription == null) ? 0 : shortDescription.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CauseAction other = (CauseAction) obj;
        if (shortDescription == null) {
            if (other.shortDescription != null)
                return false;
        } else if (!shortDescription.equals(other.shortDescription))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        return true;
    }

}
