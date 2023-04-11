package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

public class WikiAttachment {

    public static class Link {

        private String url;
        private String markdown;

        public String getUrl() {
            return url;
        }
    
        public void setUrl(String url) {
            this.url = url;
        }
    
        public String getMarkdown() {
            return markdown;
        }
    
        public void setMarkdown(String markdown) {
            this.markdown = markdown;
        }

        @Override
        public String toString() {
            return (JacksonJson.toJsonString(this));
        }
    }


    private String fileName;
    private String filePath;
    private String branch;
    private Link link;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
