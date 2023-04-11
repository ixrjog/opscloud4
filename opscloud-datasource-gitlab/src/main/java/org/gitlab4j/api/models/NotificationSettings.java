package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.gitlab4j.api.utils.JacksonJson;
import org.gitlab4j.api.utils.JacksonJsonEnumHelper;

public class NotificationSettings {

    /** Notification level */
    public static enum Level {

        DISABLED, PARTICIPATING, WATCH, GLOBAL, MENTION, CUSTOM;

        private static JacksonJsonEnumHelper<Level> enumHelper = new JacksonJsonEnumHelper<>(Level.class);

        @JsonCreator
        public static Level forValue(String value) {
            return enumHelper.forValue(value);
        }

        @JsonValue
        public String toValue() {
            return (enumHelper.toString(this));
        }

        @Override
        public String toString() {
            return (enumHelper.toString(this));
        }
    }

    public static class Events {

        private Boolean newNote;
        private Boolean newIssue;
        private Boolean reopenIssue;
        private Boolean closeIssue;
        private Boolean reassignIssue;
        private Boolean newMergeRequest;
        private Boolean reopenMergeRequest;
        private Boolean closeMergeRequest;
        private Boolean reassignMergeRequest;
        private Boolean mergeMergeRequest;
        private Boolean failedPipeline;
        private Boolean successPipeline;

        public Boolean getNewNote() {
            return newNote;
        }

        public void setNewNote(Boolean newNote) {
            this.newNote = newNote;
        }

        public Boolean getNewIssue() {
            return newIssue;
        }

        public void setNewIssue(Boolean newIssue) {
            this.newIssue = newIssue;
        }

        public Boolean getReopenIssue() {
            return reopenIssue;
        }

        public void setReopenIssue(Boolean reopenIssue) {
            this.reopenIssue = reopenIssue;
        }

        public Boolean getCloseIssue() {
            return closeIssue;
        }

        public void setCloseIssue(Boolean closeIssue) {
            this.closeIssue = closeIssue;
        }

        public Boolean getReassignIssue() {
            return reassignIssue;
        }

        public void setReassignIssue(Boolean reassignIssue) {
            this.reassignIssue = reassignIssue;
        }

        public Boolean getNewMergeRequest() {
            return newMergeRequest;
        }

        public void setNewMergeRequest(Boolean newMergeRequest) {
            this.newMergeRequest = newMergeRequest;
        }

        public Boolean getReopenMergeRequest() {
            return reopenMergeRequest;
        }

        public void setReopenMergeRequest(Boolean reopenMergeRequest) {
            this.reopenMergeRequest = reopenMergeRequest;
        }

        public Boolean getCloseMergeRequest() {
            return closeMergeRequest;
        }

        public void setCloseMergeRequest(Boolean closeMergeRequest) {
            this.closeMergeRequest = closeMergeRequest;
        }

        public Boolean getReassignMergeRequest() {
            return reassignMergeRequest;
        }

        public void setReassignMergeRequest(Boolean reassignMergeRequest) {
            this.reassignMergeRequest = reassignMergeRequest;
        }

        public Boolean getMergeMergeRequest() {
            return mergeMergeRequest;
        }

        public void setMergeMergeRequest(Boolean mergeMergeRequest) {
            this.mergeMergeRequest = mergeMergeRequest;
        }

        public Boolean getFailedPipeline() {
            return failedPipeline;
        }

        public void setFailedPipeline(Boolean failedPipeline) {
            this.failedPipeline = failedPipeline;
        }

        public Boolean getSuccessPipeline() {
            return successPipeline;
        }

        public void setSuccessPipeline(Boolean successPipeline) {
            this.successPipeline = successPipeline;
        }

        @Override
        public String toString() {
            return (JacksonJson.toJsonString(this));
        }
    }

    private Level level;
    private String email;
    private Events events;

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}