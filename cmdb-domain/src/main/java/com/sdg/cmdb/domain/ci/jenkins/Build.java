package com.sdg.cmdb.domain.ci.jenkins;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Build implements Serializable {

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

    /**
     *  "full_url": "http://localhost:8080/job/asgard/18/"
     */
    private String fullUrl;
    /**
     *  "number": 18
     */
    private int number;
    /**
     *  "phase": "COMPLETED"
     */
    private String phase;
    /**
     *  "status": "SUCCESS"
     */
    private String status;
    /**
     *     "url": "job/asgard/18/"
     */
    private String url;

    private Scm scm;

    private Map<String, HashMap<String, String>> artifacts = new HashMap<>();

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

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

    public Scm getScm() {
        return scm;
    }

    public void setScm(Scm scm) {
        this.scm = scm;
    }

    public Map<String, HashMap<String, String>> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(Map<String, HashMap<String, String>> artifacts) {
        this.artifacts = artifacts;
    }
}
