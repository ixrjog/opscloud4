
package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab4j.api.utils.JacksonJson;

public class Diff {
    
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
        return this.a_mode;
    }

    public void setAMode(String aMode) {
        this.a_mode = aMode;
    }

    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty("b_mode")
    public String getBMode() {
        return this.b_mode;
    }

    public void setBMode(String bMode) {
        this.b_mode = bMode;
    }

    public Boolean getDeletedFile() {
        return this.deletedFile;
    }

    public void setDeletedFile(Boolean deletedFile) {
        this.deletedFile = deletedFile;
    }

    public String getDiff() {
        return this.diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public Boolean getNewFile() {
        return this.newFile;
    }

    public void setNewFile(Boolean newFile) {
        this.newFile = newFile;
    }

    public String getNewPath() {
        return this.newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    public String getOldPath() {
        return this.oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    public Boolean getRenamedFile() {
        return this.renamedFile;
    }

    public void setRenamedFile(Boolean renamedFile) {
        this.renamedFile = renamedFile;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
