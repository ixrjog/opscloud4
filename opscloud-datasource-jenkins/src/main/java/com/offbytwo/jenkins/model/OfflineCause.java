package com.offbytwo.jenkins.model;

/**
 * @author Karl Heinz Marbaise
 */
public class OfflineCause extends BaseModel {

    private Long timestamp;

    private String description;

    public Long getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public OfflineCause setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public OfflineCause setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
        OfflineCause other = (OfflineCause) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        return true;
    }

}
