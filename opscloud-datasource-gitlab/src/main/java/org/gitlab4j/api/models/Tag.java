package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

public class Tag {

    private Commit commit;
    private String message;
    private String name;
    private Release release;

    public Commit getCommit() {
        return this.commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
