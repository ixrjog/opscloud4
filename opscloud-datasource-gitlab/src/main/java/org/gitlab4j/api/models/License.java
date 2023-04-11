package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;
import java.util.Map;

public class License {

    private Long id;
    private String plan;
    private Date createdAt;
    private Date startsAt;
    private Date expiresAt;
    private Integer historicalMax;
    private Boolean expired;
    private Integer overage;
    private Integer userLimit;
    private Integer activeUsers;
    private Map<String, String> licensee;
    private Map<String, Integer> addOns;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(Date startsAt) {
        this.startsAt = startsAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Integer getHistoricalMax() {
        return historicalMax;
    }

    public void setHistoricalMax(Integer historicalMax) {
        this.historicalMax = historicalMax;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public Integer getOverage() {
        return overage;
    }

    public void setOverage(Integer overage) {
        this.overage = overage;
    }

    public Integer getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(Integer userLimit) {
        this.userLimit = userLimit;
    }

    public Integer getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(Integer activeUsers) {
        this.activeUsers = activeUsers;
    }

    public Map<String, String> getLicensee() {
        return licensee;
    }

    public void setLicensee(Map<String, String> licensee) {
        this.licensee = licensee;
    }

    public Map<String, Integer> getAddOns() {
        return addOns;
    }

    public void setAddOns(Map<String, Integer> addOns) {
        this.addOns = addOns;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
