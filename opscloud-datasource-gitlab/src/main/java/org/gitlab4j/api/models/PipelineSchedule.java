package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;
import java.util.List;

public class PipelineSchedule {

    private Long id;
    private String description;
    private String ref;
    private String cron;
    private String cronTimezone;
    private Date nextRunAt;
    private Date createdAt;
    private Date updatedAt;
    private Boolean active;
    private Pipeline lastPipeline;
    private Owner owner;
    private List<Variable> variables;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getCronTimezone() {
        return cronTimezone;
    }

    public void setCronTimezone(String cronTimezone) {
        this.cronTimezone = cronTimezone;
    }

    public Date getNextRunAt() {
        return nextRunAt;
    }

    public void setNextRunAt(Date nextRunAt) {
        this.nextRunAt = nextRunAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Pipeline getLastPipeline() {
        return lastPipeline;
    }

    public void setLastPipeline(Pipeline lastPipeline) {
        this.lastPipeline = lastPipeline;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}

