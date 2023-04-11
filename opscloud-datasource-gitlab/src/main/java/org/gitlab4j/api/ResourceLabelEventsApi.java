package org.gitlab4j.api;

import org.gitlab4j.api.models.LabelEvent;

import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * This class provides an entry point to all the GitLab Resource label events API
 * @see <a href="https://docs.gitlab.com/ce/api/resource_label_events.html">Resource label events API at GitLab</a>
 */
public class ResourceLabelEventsApi extends AbstractApi {

    public ResourceLabelEventsApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Gets a list of all label events for a single issue.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/resource_label_events</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param issueIid the IID of the issue
     * @return a List of LabelEvent for the specified issue
     * @throws GitLabApiException if any exception occurs
     */
    public List<LabelEvent> getIssueLabelEvents(Object projectIdOrPath, Long issueIid) throws GitLabApiException {
        return (getIssueLabelEvents(projectIdOrPath, issueIid, getDefaultPerPage()).all());
    }

    /**
     * Gets a Pager of all label events for a single issue.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/resource_label_events</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param issueIid the IID of the issue
     * @param itemsPerPage the number of LabelEvent instances that will be fetched per page
     * @return the Pager of LabelEvent instances for the specified issue IID
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<LabelEvent> getIssueLabelEvents(Object projectIdOrPath, Long issueIid, int itemsPerPage) throws GitLabApiException {
        return (new Pager<LabelEvent>(this, LabelEvent.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "resource_label_events"));
    }

    /**
     * Gets a Stream of all label events for a single issue.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/resource_label_events</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param issueIid the IID of the issue
     * @return a Stream of LabelEvent for the specified issue
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<LabelEvent> getIssueLabelEventsStream(Object projectIdOrPath, Long issueIid) throws GitLabApiException {
        return (getIssueLabelEvents(projectIdOrPath, issueIid, getDefaultPerPage()).stream());
    }

    /**
     * Get a single label event for a specific project issue.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/resource_label_events/:resource_label_event_id</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param issueIid the IID of the issue
     * @param resourceLabelEventId the ID of a label event
     * @return LabelEvent instance for the specified project issue
     * @throws GitLabApiException if any exception occurs
     */
    public LabelEvent getIssueLabelEvent(Object projectIdOrPath, Long issueIid, Long resourceLabelEventId) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath),
                "issues", issueIid, "resource_label_events", resourceLabelEventId);
        return (response.readEntity(LabelEvent.class));
    }

    /**
     * Get an Optional instance holding a LabelEvent for a specific project issue
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/resource_label_events/:resource_label_event_id</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param issueIid the IID of the issue
     * @param resourceLabelEventId the ID of a label event
     * @return an Optional instance with the specified LabelEvent as the value
     * @throws GitLabApiException if any exception occurs
     */
    public Optional<LabelEvent> getOptionalIssueLabelEvent(Object projectIdOrPath,
	    Long issueIid, Long resourceLabelEventId) throws GitLabApiException {

        try {
            return (Optional.ofNullable(getIssueLabelEvent(projectIdOrPath, issueIid, resourceLabelEventId)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Gets a list of all label events for an epic.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/epics/:epic_id/resource_label_events</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param epicId the ID of the epic
     * @return a List of LabelEvent for the specified epic
     * @throws GitLabApiException if any exception occurs
     */
    public List<LabelEvent> getEpicLabelEvents(Object projectIdOrPath, Long epicId) throws GitLabApiException {
        return (getEpicLabelEvents(projectIdOrPath, epicId, getDefaultPerPage()).all());
    }

    /**
     * Gets a Pager of all label events for the specified epic.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/epics/:epic_id/resource_label_events</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param epicId the ID of the epic
     * @param itemsPerPage the number of LabelEvent instances that will be fetched per page
     * @return the Pager of LabelEvent instances for the specified epic
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<LabelEvent> getEpicLabelEvents(Object projectIdOrPath, Long epicId, int itemsPerPage) throws GitLabApiException {
        return (new Pager<LabelEvent>(this, LabelEvent.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "epics", epicId, "resource_label_events"));
    }

    /**
     * Gets a Stream of all label events for he specified epic.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/epics/:epic_id/resource_label_events</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param epicId the ID of the epic
     * @return a Stream of LabelEvent for the specified epic
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<LabelEvent> getEpicLabelEventsStream(Object projectIdOrPath, Long epicId) throws GitLabApiException {
        return (getEpicLabelEvents(projectIdOrPath, epicId, getDefaultPerPage()).stream());
    }

    /**
     * Get a single label event for a specific epic label event.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/epics/:epic_id/resource_label_events/:resource_label_event_id</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param epicId the ID of the epic
     * @param resourceLabelEventId the ID of a label event
     * @return LabelEvent instance for the specified epic label event
     * @throws GitLabApiException if any exception occurs
     */
    public LabelEvent getEpicLabelEvent(Object projectIdOrPath, Long epicId, Long resourceLabelEventId) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath),
                "epics", epicId, "resource_label_events", resourceLabelEventId);
        return (response.readEntity(LabelEvent.class));
    }

    /**
     * Get an Optional instance holding a LabelEvent for a specific epic label event.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/epics/:epic_id/resource_label_events/:resource_label_event_id</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param epicId the ID of the epic
     * @param resourceLabelEventId the ID of a label event
     * @return an Optional instance with the specified LabelEvent as the value
     * @throws GitLabApiException if any exception occurs
     */
    public Optional<LabelEvent> getOptionalEpicLabelEvent(Object projectIdOrPath,
	    Long epicId, Long resourceLabelEventId) throws GitLabApiException {

        try {
            return (Optional.ofNullable(getEpicLabelEvent(projectIdOrPath, epicId, resourceLabelEventId)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Gets a list of all label events for a merge request.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/merge_requests/:epic_id/resource_label_events</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param mergeRequestIid the IID of the merge request
     * @return a List of LabelEvent for the specified merge request
     * @throws GitLabApiException if any exception occurs
     */
    public List<LabelEvent> getMergeRequestLabelEvents(Object projectIdOrPath, Long mergeRequestIid) throws GitLabApiException {
        return (getMergeRequestLabelEvents(projectIdOrPath, mergeRequestIid, getDefaultPerPage()).all());
    }

    /**
     * Gets a Pager of all label events for the specified merge request.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/merge_requests/:epic_id/resource_label_events</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param mergeRequestIid the IID of the merge request
     * @param itemsPerPage the number of LabelEvent instances that will be fetched per page
     * @return the Pager of LabelEvent instances for the specified merge request
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<LabelEvent> getMergeRequestLabelEvents(Object projectIdOrPath, Long mergeRequestIid, int itemsPerPage) throws GitLabApiException {
        return (new Pager<LabelEvent>(this, LabelEvent.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "merge_requests", mergeRequestIid, "resource_label_events"));
    }

    /**
     * Gets a Stream of all label events for he specified merge request.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/merge_requests/:issue_iid/resource_label_events</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param mergeRequestIid the IID of the merge request
     * @return a Stream of LabelEvent for the specified merge request
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<LabelEvent> getMergeRequestLabelEventsStream(Object projectIdOrPath, Long mergeRequestIid) throws GitLabApiException {
        return (getMergeRequestLabelEvents(projectIdOrPath, mergeRequestIid, getDefaultPerPage()).stream());
    }

    /**
     * Get a single label event for a specific merge request label event.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/merge_requests/:epic_id/resource_label_events/:resource_label_event_id</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param mergeRequestIid the IID of the merge request
     * @param resourceLabelEventId the ID of a label event
     * @return LabelEvent instance for the specified epic label event
     * @throws GitLabApiException if any exception occurs
     */
    public LabelEvent getMergeRequestLabelEvent(Object projectIdOrPath, Long mergeRequestIid, Long resourceLabelEventId) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath),
                "merge_requests", mergeRequestIid, "resource_label_events", resourceLabelEventId);
        return (response.readEntity(LabelEvent.class));
    }

    /**
     * Get an Optional instance holding a LabelEvent for a specific merge request label event.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/merge_requests/:issue_iid/resource_label_events/:resource_label_event_id</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param mergeRequestIid the IID of the merge request
     * @param resourceLabelEventId the ID of a label event
     * @return an Optional instance with the specified LabelEvent as the value
     * @throws GitLabApiException if any exception occurs
     */
    public Optional<LabelEvent> getOptionalMergeRequestLabelEvent(Object projectIdOrPath,
	    Long mergeRequestIid, Long resourceLabelEventId) throws GitLabApiException {

        try {
            return (Optional.ofNullable(getMergeRequestLabelEvent(projectIdOrPath, mergeRequestIid, resourceLabelEventId)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }
}
