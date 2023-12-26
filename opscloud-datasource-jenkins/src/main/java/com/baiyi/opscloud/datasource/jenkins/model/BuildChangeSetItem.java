/**
 *
 */
package com.baiyi.opscloud.datasource.jenkins.model;

import java.util.List;

/**
 * Build ChangeSet Item
 *
 * @author Karl Heinz Marbaise
 *
 */
public class BuildChangeSetItem {

    private List<String> affectedPaths;
    private String commitId;
    private String timestamp; // May be we should date/?
    private BuildChangeSetAuthor author;
    private String comment;
    private String date; // Better use Date
    private String id; // seemed to be the same as the commitId?
    private String msg; // Message difference to comment?
    private List<BuildChangeSetPath> paths;

    public List<String> getAffectedPaths() {
        return affectedPaths;
    }

    public BuildChangeSetItem setAffectedPaths(List<String> affectedPaths) {
        this.affectedPaths = affectedPaths;
        return this;
    }

    public String getCommitId() {
        return commitId;
    }

    public BuildChangeSetItem setCommitId(String commitId) {
        this.commitId = commitId;
        return this;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public BuildChangeSetItem setTimestamp(String timeStamp) {
        this.timestamp = timeStamp;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public BuildChangeSetItem setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getDate() {
        return date;
    }

    public BuildChangeSetItem setDate(String date) {
        this.date = date;
        return this;
    }

    public String getId() {
        return id;
    }

    public BuildChangeSetItem setId(String id) {
        this.id = id;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BuildChangeSetItem setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public List<BuildChangeSetPath> getPaths() {
        return paths;
    }

    public BuildChangeSetItem setPaths(List<BuildChangeSetPath> paths) {
        this.paths = paths;
        return this;
    }

    public BuildChangeSetAuthor getAuthor() {
        return author;
    }

    public BuildChangeSetItem setAuthor(BuildChangeSetAuthor author) {
        this.author = author;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((affectedPaths == null) ? 0 : affectedPaths.hashCode());
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        result = prime * result + ((commitId == null) ? 0 : commitId.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((msg == null) ? 0 : msg.hashCode());
        result = prime * result + ((paths == null) ? 0 : paths.hashCode());
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
        BuildChangeSetItem other = (BuildChangeSetItem) obj;
        if (affectedPaths == null) {
            if (other.affectedPaths != null)
                return false;
        } else if (!affectedPaths.equals(other.affectedPaths))
            return false;
        if (author == null) {
            if (other.author != null)
                return false;
        } else if (!author.equals(other.author))
            return false;
        if (comment == null) {
            if (other.comment != null)
                return false;
        } else if (!comment.equals(other.comment))
            return false;
        if (commitId == null) {
            if (other.commitId != null)
                return false;
        } else if (!commitId.equals(other.commitId))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (msg == null) {
            if (other.msg != null)
                return false;
        } else if (!msg.equals(other.msg))
            return false;
        if (paths == null) {
            if (other.paths != null)
                return false;
        } else if (!paths.equals(other.paths))
            return false;
        if (timestamp == null) {
            return other.timestamp == null;
        } else return timestamp.equals(other.timestamp);
    }

}