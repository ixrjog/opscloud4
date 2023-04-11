package org.gitlab4j.api.systemhooks;

import org.gitlab4j.api.webhook.MergeRequestEvent;

public class MergeRequestSystemHookEvent extends MergeRequestEvent implements SystemHookEvent {

    public static final String X_GITLAB_EVENT = "System Hook";
    public static final String MERGE_REQUEST_EVENT = "merge_request";

    private String eventType;

    @Override
    public String getObjectKind() {
        return (MERGE_REQUEST_EVENT);
    }

    @Override
    public String getEventName() {
        return (MERGE_REQUEST_EVENT);
    }

    public String getEventType() {
        return eventType;
    }

    public void setEvent_type(String eventType) {
        this.eventType = eventType;
    }
}
