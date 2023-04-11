package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

public class RegistryRepositoryTag {
    private String name;
    private String path;
    private String location;
    private String revision;
    private String shortRevision;
    private String digest;
    private Date createdAt;
    private Long totalSize;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getShortRevision() {
        return shortRevision;
    }

    public void setShortRevision(String shortRevision) {
        this.shortRevision = shortRevision;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }

}
