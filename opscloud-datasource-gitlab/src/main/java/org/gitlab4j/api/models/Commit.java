
package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;
import java.util.List;

public class Commit {

    private Author author;
    private Date authoredDate;
    private String authorEmail;
    private String authorName;
    private Date committedDate;
    private String committerEmail;
    private String committerName;
    private Date createdAt;
    private String id;
    private String message;
    private List<String> parentIds;
    private String shortId;
    private CommitStats stats;
    private String status;
    private Date timestamp;
    private String title;
    private String url;
    private String webUrl;
    private Pipeline lastPipeline;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Date getAuthoredDate() {
        return authoredDate;
    }

    public void setAuthoredDate(Date authoredDate) {
        this.authoredDate = authoredDate;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getCommittedDate() {
        return committedDate;
    }

    public void setCommittedDate(Date committedDate) {
        this.committedDate = committedDate;
    }

    public String getCommitterEmail() {
        return committerEmail;
    }

    public void setCommitterEmail(String committerEmail) {
        this.committerEmail = committerEmail;
    }

    public String getCommitterName() {
        return committerName;
    }

    public void setCommitterName(String committerName) {
        this.committerName = committerName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getParentIds() {
        return parentIds;
    }

    public void setParentIds(List<String> parentIds) {
        this.parentIds = parentIds;
    }

    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
    }

    public CommitStats getStats() {
        return stats;
    }

    public void setStats(CommitStats stats) {
        this.stats = stats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public Pipeline getLastPipeline() {
	return lastPipeline;
    }

    public void setLastPipeline(Pipeline lastPipeline) {
	this.lastPipeline = lastPipeline;
    }

    public Commit withAuthor(Author author) {
        this.author = author;
        return this;
    }

    public Commit withAuthoredDate(Date authoredDate) {
        this.authoredDate = authoredDate;
        return this;
    }

    public Commit withAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
        return this;
    }

    public Commit withAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public Commit withCommittedDate(Date committedDate) {
        this.committedDate = committedDate;
        return this;
    }

    public Commit withCommitterEmail(String committerEmail) {
        this.committerEmail = committerEmail;
        return this;
    }

    public Commit withCommitterName(String committerName) {
        this.committerName = committerName;
        return this;
    }

    public Commit withCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Commit withId(String id) {
        this.id = id;
        return this;
    }

    public Commit withMessage(String message) {
        this.message = message;
        return this;
    }

    public Commit withParentIds(List<String> parentIds) {
        this.parentIds = parentIds;
        return this;
    }

    public Commit withShorwId(String shortId) {
        this.shortId = shortId;
        return this;
    }

    public Commit withStats(CommitStats stats) {
        this.stats = stats;
        return this;
    }

    public Commit withStatus(String status) {
        this.status = status;
        return this;
    }

    public Commit withTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Commit withTitle(String title) {
        this.title = title;
        return this;
    }

    public Commit withUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
