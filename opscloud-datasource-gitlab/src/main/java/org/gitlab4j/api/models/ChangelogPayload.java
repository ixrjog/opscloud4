package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gitlab4j.api.GitLabApiForm;
import org.gitlab4j.api.utils.ISO8601;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

public class ChangelogPayload {

    private String version;
    private String from;
    private String to;
    private Date date;
    private String branch;
    private String trailer;
    private String file;
    private String message;

    public ChangelogPayload(String version) {
        this.version = version;
    }

    @JsonIgnore
    public GitLabApiForm getFormData() {
        return new GitLabApiForm()
                .withParam("version", version, true)
                .withParam("from", from)
                .withParam("to", to)
                .withParam("date", ISO8601.dateOnly(date))
                .withParam("branch", branch)
                .withParam("trailer", trailer)
                .withParam("file", file)
                .withParam("message", message);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
