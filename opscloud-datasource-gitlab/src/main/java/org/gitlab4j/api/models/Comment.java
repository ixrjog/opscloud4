package org.gitlab4j.api.models;

import org.gitlab4j.api.Constants.LineType;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

public class Comment {

    private Author author;
    private Date createdAt;
    private LineType lineType;
    private String path;
    private Integer line;
    private String note;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public LineType getLineType() {
        return lineType;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
