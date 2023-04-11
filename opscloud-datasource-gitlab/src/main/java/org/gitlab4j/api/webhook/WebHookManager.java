
package org.gitlab4j.api.webhook;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.HookManager;
import org.gitlab4j.api.utils.HttpRequestUtils;
import org.gitlab4j.api.utils.JacksonJson;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides a handler for processing GitLab WebHook callouts.
 */
public class WebHookManager implements HookManager {

    private final static Logger LOGGER = Logger.getLogger(WebHookManager.class.getName());
    private final JacksonJson jacksonJson = new JacksonJson();

    // Collection of objects listening for WebHook events.
    private final List<WebHookListener> webhookListeners = new CopyOnWriteArrayList<WebHookListener>();

    private String secretToken;

    /**
     * Create a HookManager to handle GitLab webhook events.
     */
    public WebHookManager() {
    }

    /**
     * Create a HookManager to handle GitLab webhook events which will be verified
     * against the specified secretToken.
     *
     * @param secretToken the secret token to verify against
     */
    public WebHookManager(String secretToken) {
        this.secretToken = secretToken;
    }

    /**
     * Get the secret token that received hook events should be validated against.
     *
     * @return the secret token that received hook events should be validated against
     */
    public String getSecretToken() {
        return (secretToken);
    }

    /**
     * Set the secret token that received hook events should be validated against.
     *
     * @param secretToken the secret token to verify against
     */
    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }

    /**
     * Parses and verifies an Event instance from the HTTP request and
     * fires it off to the registered listeners.
     *
     * @param request the HttpServletRequest to read the Event instance from
     * @throws GitLabApiException if the parsed event is not supported
     */
    public void handleEvent(HttpServletRequest request) throws GitLabApiException {
        handleRequest(request);
    }

    /**
     * Parses and verifies an Event instance from the HTTP request and
     * fires it off to the registered listeners.
     *
     * @param request the HttpServletRequest to read the Event instance from
     * @return the Event instance that was read from the request body, null if the request
     * not contain a webhook event
     * @throws GitLabApiException if the parsed event is not supported
     */
    public Event handleRequest(HttpServletRequest request) throws GitLabApiException {

        String eventName = request.getHeader("X-Gitlab-Event");
        if (eventName == null || eventName.trim().isEmpty()) {
            LOGGER.warning("X-Gitlab-Event header is missing!");
            return (null);
        }

        if (!isValidSecretToken(request)) {
            String message = "X-Gitlab-Token mismatch!";
            LOGGER.warning(message);
            throw new GitLabApiException(message);
        }

        LOGGER.info("handleEvent: X-Gitlab-Event=" + eventName);
        switch (eventName) {

        case IssueEvent.X_GITLAB_EVENT:
        case JobEvent.JOB_HOOK_X_GITLAB_EVENT:
        case MergeRequestEvent.X_GITLAB_EVENT:
        case NoteEvent.X_GITLAB_EVENT:
        case PipelineEvent.X_GITLAB_EVENT:
        case PushEvent.X_GITLAB_EVENT:
        case TagPushEvent.X_GITLAB_EVENT:
        case WikiPageEvent.X_GITLAB_EVENT:
        case DeploymentEvent.X_GITLAB_EVENT:
        case ReleaseEvent.X_GITLAB_EVENT:
            break;

        default:
            String message = "Unsupported X-Gitlab-Event, event Name=" + eventName;
            LOGGER.warning(message);
            throw new GitLabApiException(message);
        }

        Event event;
        try {

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(HttpRequestUtils.getShortRequestDump(eventName + " webhook", true, request));
                String postData = HttpRequestUtils.getPostDataAsString(request);
                LOGGER.fine("Raw POST data:\n" + postData);
                event = jacksonJson.unmarshal(Event.class, postData);
                LOGGER.fine(event.getObjectKind() + " event:\n" + jacksonJson.marshal(event) + "\n");
            } else {
                InputStreamReader reader = new InputStreamReader(request.getInputStream());
                event = jacksonJson.unmarshal(Event.class, reader);
            }

        } catch (Exception e) {
            LOGGER.warning(String.format("Error processing JSON data, exception=%s, error=%s",
                    e.getClass().getSimpleName(), e.getMessage()));
            throw new GitLabApiException(e);
        }

        try {

            event.setRequestUrl(request.getRequestURL().toString());
            event.setRequestQueryString(request.getQueryString());

            String secretToken = request.getHeader("X-Gitlab-Token");
            event.setRequestSecretToken(secretToken);

            fireEvent(event);
            return (event);

        } catch (Exception e) {
            LOGGER.warning(String.format("Error processing event, exception=%s, error=%s",
                    e.getClass().getSimpleName(), e.getMessage()));
            throw new GitLabApiException(e);
        }
    }

    /**
     * Verifies the provided Event and fires it off to the registered listeners.
     *
     * @param event the Event instance to handle
     * @throws GitLabApiException if the event is not supported
     */
    public void handleEvent(Event event) throws GitLabApiException {

        LOGGER.info("handleEvent: object_kind=" + event.getObjectKind());

        switch (event.getObjectKind()) {
        case BuildEvent.OBJECT_KIND:
        case IssueEvent.OBJECT_KIND:
        case JobEvent.OBJECT_KIND:
        case MergeRequestEvent.OBJECT_KIND:
        case NoteEvent.OBJECT_KIND:
        case PipelineEvent.OBJECT_KIND:
        case PushEvent.OBJECT_KIND:
        case TagPushEvent.OBJECT_KIND:
        case WikiPageEvent.OBJECT_KIND:
        case ReleaseEvent.OBJECT_KIND:
        case DeploymentEvent.OBJECT_KIND:
            fireEvent(event);
            break;

        default:
            String message = "Unsupported event object_kind, object_kind=" + event.getObjectKind();
            LOGGER.warning(message);
            throw new GitLabApiException(message);
        }
    }

    /**
     * Adds a WebHook event listener.
     *
     * @param listener the SystemHookListener to add
     */
    public void addListener(WebHookListener listener) {

        if (!webhookListeners.contains(listener)) {
            webhookListeners.add(listener);
        }
    }

    /**
     * Removes a WebHook event listener.
     *
     * @param listener the SystemHookListener to remove
     */
    public void removeListener(WebHookListener listener) {
        webhookListeners.remove(listener);
    }

    /**
     * Fire the event to the registered listeners.
     *
     * @param event the Event instance to fire to the registered event listeners
     * @throws GitLabApiException if the event is not supported
     */
    public void fireEvent(Event event) throws GitLabApiException {

        switch (event.getObjectKind()) {
        case BuildEvent.OBJECT_KIND:
            fireBuildEvent((BuildEvent) event);
            break;

        case IssueEvent.OBJECT_KIND:
            fireIssueEvent((IssueEvent) event);
            break;

        case JobEvent.OBJECT_KIND:
            fireJobEvent((JobEvent) event);
            break;

        case MergeRequestEvent.OBJECT_KIND:
            fireMergeRequestEvent((MergeRequestEvent) event);
            break;

        case NoteEvent.OBJECT_KIND:
            fireNoteEvent((NoteEvent) event);
            break;

        case PipelineEvent.OBJECT_KIND:
            firePipelineEvent((PipelineEvent) event);
            break;

        case PushEvent.OBJECT_KIND:
            firePushEvent((PushEvent) event);
            break;

        case TagPushEvent.OBJECT_KIND:
            fireTagPushEvent((TagPushEvent) event);
            break;

        case WikiPageEvent.OBJECT_KIND:
            fireWikiPageEvent((WikiPageEvent) event);
            break;

        case ReleaseEvent.OBJECT_KIND:
            fireReleaseEvent((ReleaseEvent) event);
            break;

        case DeploymentEvent.OBJECT_KIND:
            fireDeploymentEvent((DeploymentEvent) event);
            break;

        default:
            String message = "Unsupported event object_kind, object_kind=" + event.getObjectKind();
            LOGGER.warning(message);
            throw new GitLabApiException(message);
        }
    }

    protected void fireBuildEvent(BuildEvent buildEvent) {
        for (WebHookListener listener : webhookListeners) {
            listener.onBuildEvent(buildEvent);
        }
    }

    protected void fireIssueEvent(IssueEvent issueEvent) {
        for (WebHookListener listener : webhookListeners) {
            listener.onIssueEvent(issueEvent);
        }
    }

    protected void fireJobEvent(JobEvent jobEvent) {
        for (WebHookListener listener : webhookListeners) {
            listener.onJobEvent(jobEvent);
        }
    }

    protected void fireMergeRequestEvent(MergeRequestEvent mergeRequestEvent) {
        for (WebHookListener listener : webhookListeners) {
            listener.onMergeRequestEvent(mergeRequestEvent);
        }
    }

    protected void fireNoteEvent(NoteEvent noteEvent) {
        for (WebHookListener listener : webhookListeners) {
            listener.onNoteEvent(noteEvent);
        }
    }

    protected void firePipelineEvent(PipelineEvent pipelineEvent) {
        for (WebHookListener listener : webhookListeners) {
            listener.onPipelineEvent(pipelineEvent);
        }
    }

    protected void firePushEvent(PushEvent pushEvent) {
        for (WebHookListener listener : webhookListeners) {
            listener.onPushEvent(pushEvent);
        }
    }

    protected void fireTagPushEvent(TagPushEvent tagPushEvent) {
        for (WebHookListener listener : webhookListeners) {
            listener.onTagPushEvent(tagPushEvent);
        }
    }

    protected void fireWikiPageEvent(WikiPageEvent wikiPageEvent) {
        for (WebHookListener listener : webhookListeners) {
            listener.onWikiPageEvent(wikiPageEvent);
        }
    }

    protected void fireDeploymentEvent(DeploymentEvent deploymentEvent) {
        for (WebHookListener listener : webhookListeners) {
            listener.onDeploymentEvent(deploymentEvent);
        }
    }

    protected void fireReleaseEvent(ReleaseEvent releaseEvent) {
        for (WebHookListener listener : webhookListeners) {
            listener.onReleaseEvent(releaseEvent);
        }
    }
}
