package com.sdg.cmdb.domain.jenkins;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BuildVO implements Serializable {

    /**
     * "build": {
     * "full_url": "http://localhost:8080/job/asgard/18/",
     * "number": 18,
     * "phase": "COMPLETED",
     * "status": "SUCCESS",
     * "url": "job/asgard/18/",
     * "scm": {
     * "url": "https://github.com/evgeny-goldin/asgard.git",
     * "branch": "origin/master",
     * "commit": "c6d86dc654b12425e706bcf951adfe5a8627a517"
     * },
     * "artifacts": {
     * "asgard.war": {
     * "archive": "http://localhost:8080/job/asgard/18/artifact/asgard.war"
     * },
     * "asgard-standalone.jar": {
     * "archive": "http://localhost:8080/job/asgard/18/artifact/asgard-standalone.jar",
     * "s3": "https://s3-eu-west-1.amazonaws.com/evgenyg-bakery/asgard/asgard-standalone.jar"
     * }
     * }
     * }
     */

    private String fullUrl;
    private int number;
    private String phase;
    private String status;
    private String url;
    private Map<String, String> scm = new HashMap<String, String>();

    private Map<String, HashMap<String, String>> artifacts = new HashMap<>();

    @Override
    public String toString() {
        String string = "BuildVO{" +
                "fullUrl='" + fullUrl + '\'' +
                ", number='" + number + '\'' +
                ", phase='" + phase + '\'' +
                ", status='" + status + '\'' +
                ", url='" + url + '\'' +
                ", scm={" + "url:" + getScmUrl() + ';' +
                "branch:" + getScmBranch() + ';' +
                "commit:" + getScmCommit() + "}" +
                ", artifacts={";

        for (String key : artifacts.keySet()) {
            HashMap<String, String> artifact = artifacts.get(key);
            string += key + ":";

            for (String name : artifact.keySet()) {
                String artifactUrl = artifact.get(name);
                string += "{" + name + ":" + artifactUrl + "}";
            }
        }
        string += "}}";

        return string;
    }


    public String getScmUrl() {
        if (this.scm == null) return "";
        return this.scm.get("url");
    }

    public String getScmBranch() {
        if (this.scm == null) return "";
        return this.scm.get("branch");
    }

    public String getScmCommit() {
        if (this.scm == null) return "";
        return this.scm.get("commit");
    }

    private ParametersVO parameters;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFull_url(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setParameters(ParametersVO parameters) {
        this.parameters = parameters;
    }

    public ParametersVO getParameters() {
        return parameters;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Map<String, String> getScm() {
        return scm;
    }

    public void setScm(Map<String, String> scm) {
        this.scm = scm;
    }

    public Map<String, HashMap<String, String>> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(Map<String, HashMap<String, String>> artifacts) {
        this.artifacts = artifacts;
    }
}
