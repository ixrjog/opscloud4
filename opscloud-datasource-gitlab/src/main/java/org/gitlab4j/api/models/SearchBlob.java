package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

public class SearchBlob {

    private String basename;
    private String data;
    private String filename;
    private Long id;
    private String ref;
    private Integer startline;
    private Long projectId;

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Integer getStartline() {
        return startline;
    }

    public void setStartline(Integer startline) {
        this.startline = startline;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
