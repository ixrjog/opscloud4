package com.sdg.cmdb.domain.gitlab.v1;

import java.io.Serializable;

public class Commits implements Serializable {
    private static final long serialVersionUID = 5881824486422012468L;

    /**
     * {
     * "id": "b6568db1bc1dcd7f8b4d5a946b0b91f9dacd7327",
     * "message": "Update Catalan translation to e38cb41.",
     * "timestamp": "2011-12-12T14:27:31+02:00",
     * "url": "http://example.com/mike/diaspora/commit/b6568db1bc1dcd7f8b4d5a946b0b91f9dacd7327",
     * "author": {
     * "name": "Jordi Mallach",
     * "email": "jordi@softcatala.org"
     * },
     * "added": ["CHANGELOG"],
     * "modified": ["app/controller/application.rb"],
     * "removed": []
     * }
     */
    private String id;
    private String message;
    private String timestamp;
    private String url;

    private String added;
    private String modified;

    private Author author;

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
