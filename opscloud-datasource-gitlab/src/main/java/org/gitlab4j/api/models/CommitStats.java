package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

public class CommitStats {

    private Integer additions;
    private Integer deletions;
    private Integer total;

    public Integer getAdditions() {
        return additions;
    }

    public void setAdditions(Integer additions) {
        this.additions = additions;
    }

    public Integer getDeletions() {
        return deletions;
    }

    public void setDeletions(Integer deletions) {
        this.deletions = deletions;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
