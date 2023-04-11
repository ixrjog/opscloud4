package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab4j.api.utils.JacksonJson;

public class Changes {

    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty("a_mode")
    private String a_mode;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty("b_mode")
    private String b_mode;

    private Boolean deletedFile;
    private String diff;
    private Boolean newFile;
    private String newPath;
    private String oldPath;
    private Boolean renamedFile;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty("a_mode")
    public String getAMode() {
        return a_mode;
    }

    public void setAMode(String a_mode) {
        this.a_mode = a_mode;
    }

    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty("b_mode")
    public String getBMode() {
        return b_mode;
    }

    public void setBMode(String b_mode) {
        this.b_mode = b_mode;
    }

    public Boolean getDeletedFile() {
        return deletedFile;
    }

    public void setDeletedFile(Boolean deletedFile) {
        this.deletedFile = deletedFile;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public Boolean getNewFile() {
        return newFile;
    }

    public void setNewFile(Boolean newFile) {
        this.newFile = newFile;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    public Boolean getRenamedFile() {
        return renamedFile;
    }

    public void setRenamedFile(Boolean renamedFile) {
        this.renamedFile = renamedFile;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
