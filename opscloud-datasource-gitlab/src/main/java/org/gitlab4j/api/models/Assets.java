package org.gitlab4j.api.models;

import org.gitlab4j.api.Constants.ArchiveFormat;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.List;

/**
 * This class is part of the Release class model.
 */
public class Assets {

    public static class Source {

        private ArchiveFormat format;
        private String url;

        public ArchiveFormat getFormat() {
            return format;
        }

        public void setFormat(ArchiveFormat format) {
            this.format = format;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return (JacksonJson.toJsonString(this));
        }
    }

    public static class Link {

        private Long id;
        private String name;
        private String url;
        private Boolean external;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Boolean getExternal() {
            return external;
        }

        public void setExternal(Boolean external) {
            this.external = external;
        }

        @Override
        public String toString() {
            return (JacksonJson.toJsonString(this));
        }
    }

    private Integer count;
    private List<Source> sources;
    private List<Link> links;
    private String evidenceFilePath;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getEvidenceFilePath() {
        return evidenceFilePath;
    }

    public void setEvidenceFilePath(String evidenceFilePath) {
        this.evidenceFilePath = evidenceFilePath;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}

