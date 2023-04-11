package org.gitlab4j.api.systemhooks;

import org.gitlab4j.api.utils.JacksonJson;
import org.gitlab4j.api.webhook.AbstractPushEvent;

public class PushSystemHookEvent extends AbstractPushEvent implements SystemHookEvent {

    public static final String PUSH_EVENT = "push";

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
