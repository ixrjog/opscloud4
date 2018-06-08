package com.sdg.cmdb.domain.gitlab;

import java.io.Serializable;
import java.util.HashMap;

public class CommitsVO implements Serializable {
    private static final long serialVersionUID = -3996960341362594495L;

    /**
     * {
     * "id": "b6568db1bc1dcd7f8b4d5a946b0b91f9dacd7327",
     * "message": "Update Catalan translation to e38cb41.",
     * "timestamp": "2011-12-12T14:27:31+02:00",
     * "url": "http://example.com/diaspora/commits/b6568db1bc1dcd7f8b4d5a946b0b91f9dacd7327",
     * "author": {
     * "name": "Jordi Mallach",
     * "email": "jordi@softcatala.org"
     * }
     * }
     */
    private String id;

    private String message;

    private String timestamp;

    private String url;

    private HashMap<String, String> author;

    @Override
    public String toString() {
        return "CommitsVO{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", url='" + url + '\'' +
                ", autherName='" + author.get("name") + '\'' +
                ", autherEmail='" + author.get("email") +  '\'' +
                '}';
    }

    public CommitsVO() {
        this.author = new HashMap<>();
        this.author.put("name", "");
        this.author.put("email", "");
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

    public HashMap<String, String> getAuthor() {
        return author;
    }

    public void setAuthor(HashMap<String, String> author) {
        this.author = author;
    }

    public String getAuthorName(){
        if(this.author == null) return "";
        return this.author.get("name");
    }

    public String getAuthorEmail(){
        if(this.author == null) return "";
        return this.author.get("email");
    }

}
