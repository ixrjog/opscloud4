package org.gitlab4j.api.webhook;

import org.gitlab4j.api.models.Assignee;
import org.gitlab4j.api.models.User;
import org.gitlab4j.api.utils.JacksonJson;
import org.gitlab4j.api.webhook.MergeRequestEvent.ObjectAttributes;

import java.util.List;

public class ExternalStatusCheckEvent {

    private String objectKind;
    private String eventType;
    private User user;
    private EventProject project;
    private EventRepository repository;
    private ObjectAttributes objectAttributes;
    private List<EventLabel> labels;
    private MergeRequestChanges changes;
    private List<Assignee> assignees;
    private EventExternalStatusCheck externalApprovalRule;

    public String getObjectKind() {
        return objectKind;
    }

    public void setObjectKind(String objectKind) {
        this.objectKind = objectKind;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EventProject getProject() {
        return project;
    }

    public void setProject(EventProject project) {
        this.project = project;
    }

    public EventRepository getRepository() {
        return repository;
    }

    public void setRepository(EventRepository repository) {
        this.repository = repository;
    }

    public ObjectAttributes getObjectAttributes() {
        return this.objectAttributes;
    }

    public void setObjectAttributes(ObjectAttributes objectAttributes) {
        this.objectAttributes = objectAttributes;
    }

    public List<EventLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<EventLabel> labels) {
        this.labels = labels;
    }

    public MergeRequestChanges getChanges() {
        return changes;
    }

    public void setChanges(MergeRequestChanges changes) {
        this.changes = changes;
    }

    public List<Assignee> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<Assignee> assignees) {
        this.assignees = assignees;
    }

    public EventExternalStatusCheck getExternalApprovalRule() {
        return externalApprovalRule;
    }

    public void setExternalApprovalRule(EventExternalStatusCheck externalApprovalRule) {
        this.externalApprovalRule = externalApprovalRule;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
