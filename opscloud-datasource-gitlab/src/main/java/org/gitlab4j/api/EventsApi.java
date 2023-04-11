package org.gitlab4j.api;

import org.gitlab4j.api.models.Event;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class implements the client side API for the GitLab events calls.
 */
public class EventsApi extends AbstractApi {

    public EventsApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Get a list of events for the authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /events</code></pre>
     *
     * @param action include only events of a particular action type, optional
     * @param targetType include only events of a particular target type, optional
     * @param before include only events created before a particular date, optional
     * @param after include only events created after a particular date, optional
     * @param sortOrder sort events in ASC or DESC order by created_at. Default is DESC, optional
     * @return a list of events for the authenticated user and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public List<Event> getAuthenticatedUserEvents(ActionType action, TargetType targetType,
            Date before, Date after, SortOrder sortOrder) throws GitLabApiException {
        return (getAuthenticatedUserEvents(action, targetType, before, after, sortOrder, getDefaultPerPage()).all());
    }

    /**
     * Get a list of all events for the authenticated user, across all of the user's projects.
     *
     * <pre><code>GitLab Endpoint: GET /events</code></pre>
     *
     * @param action include only events of a particular action type, optional
     * @param targetType include only events of a particular target type, optional
     * @param before include only events created before a particular date, optional
     * @param after include only events created after a particular date, optional
     * @param sortOrder sort events in ASC or DESC order by created_at. Default is DESC, optional
     * @return a list of events for the authenticated user and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public List<Event> getAllAuthenticatedUserEvents(ActionType action, TargetType targetType,
            Date before, Date after, SortOrder sortOrder) throws GitLabApiException {
        return (getAuthenticatedUserEvents(action, targetType, before, after, sortOrder, getDefaultPerPage(), EventScope.ALL).all());
    }

    /**
     * Get a list of events for the authenticated user and in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /events</code></pre>
     *
     * @param action include only events of a particular action type, optional
     * @param targetType include only events of a particular target type, optional
     * @param before include only events created before a particular date, optional
     * @param after include only events created after a particular date, optional
     * @param sortOrder sort events in ASC or DESC order by created_at. Default is DESC, optional
     * @param page the page to get
     * @param perPage the number of projects per page
     * @return a list of events for the authenticated user and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public List<Event> getAuthenticatedUserEvents(ActionType action, TargetType targetType,
            Date before, Date after, SortOrder sortOrder, int page, int perPage) throws GitLabApiException {
        return (getAuthenticatedUserEvents(action, targetType, before, after, sortOrder, page, perPage, null));
    }

    /**
     * Get a list of events for the authenticated user and in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /events</code></pre>
     *
     * @param action include only events of a particular action type, optional
     * @param targetType include only events of a particular target type, optional
     * @param before include only events created before a particular date, optional
     * @param after include only events created after a particular date, optional
     * @param sortOrder sort events in ASC or DESC order by created_at. Default is DESC, optional
     * @param page the page to get
     * @param perPage the number of projects per page
     * @param scope include all events across a user’s projects, optional 
     * @return a list of events for the authenticated user and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public List<Event> getAuthenticatedUserEvents(ActionType action, TargetType targetType,
            Date before, Date after, SortOrder sortOrder, int page, int perPage, EventScope scope) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("action", action)
                .withParam("target_type", targetType != null ? targetType.toValue().toLowerCase() : null)
                .withParam("before", before)
                .withParam("after", after)
                .withParam("sort", sortOrder)
                .withParam(PAGE_PARAM,  page)
                .withParam(PER_PAGE_PARAM, perPage)
                .withParam("scope", scope != null ? scope.toValue().toLowerCase() : null);

        Response response = get(Response.Status.OK, formData.asMap(), "events");
        return (response.readEntity(new GenericType<List<Event>>() {}));
    }

    /**
     * Get a list of events for the authenticated user and in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /events</code></pre>
     *
     * @param action include only events of a particular action type, optional
     * @param targetType include only events of a particular target type, optional
     * @param before include only events created before a particular date, optional
     * @param after include only events created after a particular date, optional
     * @param sortOrder sort events in ASC or DESC order by created_at. Default is DESC, optional
     * @param itemsPerPage the number of Event instances that will be fetched per page
     * @return a Pager of events for the authenticated user and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Event> getAuthenticatedUserEvents(ActionType action, TargetType targetType, Date before, Date after,
            SortOrder sortOrder, int itemsPerPage) throws GitLabApiException {
        return (getAuthenticatedUserEvents(action, targetType, before, after, sortOrder, itemsPerPage, null));
    }

    /**
     * Get a list of events for the authenticated user and in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /events</code></pre>
     *
     * @param action include only events of a particular action type, optional
     * @param targetType include only events of a particular target type, optional
     * @param before include only events created before a particular date, optional
     * @param after include only events created after a particular date, optional
     * @param sortOrder sort events in ASC or DESC order by created_at. Default is DESC, optional
     * @param itemsPerPage the number of Event instances that will be fetched per page
     * @param scope include all events across a user’s projects, optional 
     * @return a Pager of events for the authenticated user and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Event> getAuthenticatedUserEvents(ActionType action, TargetType targetType, Date before, Date after,
            SortOrder sortOrder, int itemsPerPage, EventScope scope) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("action", action)
                .withParam("target_type", targetType != null ? targetType.toValue().toLowerCase() : null)
                .withParam("before", before)
                .withParam("after", after)
                .withParam("sort", sortOrder)
                .withParam("scope", scope != null ? scope.toValue().toLowerCase() : null);

        return (new Pager<Event>(this, Event.class, itemsPerPage, formData.asMap(), "events"));
    }

    /**
     * Get a Stream of events for the authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /events</code></pre>
     *
     * @param action include only events of a particular action type, optional
     * @param targetType include only events of a particular target type, optional
     * @param before include only events created before a particular date, optional
     * @param after include only events created after a particular date, optional
     * @param sortOrder sort events in ASC or DESC order by created_at. Default is DESC, optional
     * @return a Stream of events for the authenticated user and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Event> getAuthenticatedUserEventsStream(ActionType action, TargetType targetType,
            Date before, Date after, SortOrder sortOrder) throws GitLabApiException {
        return (getAuthenticatedUserEvents(action, targetType, before, after, sortOrder, getDefaultPerPage(), null).stream());
    }

    /**
     * Get a Stream of all events for the authenticated user, across all of the user's projects.
     *
     * <pre><code>GitLab Endpoint: GET /events</code></pre>
     *
     * @param action include only events of a particular action type, optional
     * @param targetType include only events of a particular target type, optional
     * @param before include only events created before a particular date, optional
     * @param after include only events created after a particular date, optional
     * @param sortOrder sort events in ASC or DESC order by created_at. Default is DESC, optional
     * @return a Stream of events for the authenticated user and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Event> getAllAuthenticatedUserEventsStream(ActionType action, TargetType targetType,
            Date before, Date after, SortOrder sortOrder) throws GitLabApiException {
        return (getAuthenticatedUserEvents(action, targetType, before, after, sortOrder, getDefaultPerPage(), EventScope.ALL).stream());
    }

    /**
     * Get a list of events for the specified user.
     *
     * <pre><code>GitLab Endpoint: GET /users/:userId/events</code></pre>
     *
     * @param userIdOrUsername the user ID, username of the user, or a User instance holding the user ID or username
     * @param action include only events of a particular action type, optional
     * @param targetType include only events of a particular target type, optional
     * @param before include only events created before a particular date, optional
     * @param after include only events created after a particular date, optional
     * @param sortOrder sort events in ASC or DESC order by created_at. Default is DESC, optional
     * @return a list of events for the specified user and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public List<Event> getUserEvents(Object userIdOrUsername, ActionType action, TargetType targetType,
            Date before, Date after, SortOrder sortOrder) throws GitLabApiException {
        return (getUserEvents(userIdOrUsername, action, targetType, before, after, sortOrder, getDefaultPerPage()).all());
    }

    /**
     * Get a list of events for the specified user and in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /users/:userId/events</code></pre>
     *
     * @param userIdOrUsername the user ID, username of the user, or a User instance holding the user ID or username
     * @param action include only events of a particular action type, optional
     * @param targetType include only events of a particular target type, optional
     * @param before include only events created before a particular date, optional
     * @param after include only events created after a particular date, optional
     * @param sortOrder sort events in ASC or DESC order by created_at. Default is DESC, optional
     * @param page the page to get
     * @param perPage the number of projects per page
     * @return a list of events for the specified user and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public List<Event> getUserEvents(Object userIdOrUsername, ActionType action, TargetType targetType,
            Date before, Date after, SortOrder sortOrder, int page, int perPage) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("action", action)
                .withParam("target_type", targetType != null ? targetType.toValue().toLowerCase() : null)
                .withParam("before", before)
                .withParam("after", after)
                .withParam("sort", sortOrder)
                .withParam(PAGE_PARAM,  page)
                .withParam(PER_PAGE_PARAM, perPage);

        Response response = get(Response.Status.OK, formData.asMap(),
                "users", getUserIdOrUsername(userIdOrUsername), "events");
        return (response.readEntity(new GenericType<List<Event>>() {}));
    }

    /**
     * Get a list of events for the specified user and in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /users/:userId/events</code></pre>
     *
     * @param userIdOrUsername the user ID, username of the user, or a User instance holding the user ID or username
     * @param action include only events of a particular action type, optional
     * @param targetType include only events of a particular target type, optional
     * @param before include only events created before a particular date, optional
     * @param after include only events created after a particular date, optional
     * @param sortOrder sort events in ASC or DESC order by created_at. Default is DESC, optional
     * @param itemsPerPage the number of Event instances that will be fetched per page
     * @return a Pager of events for the specified user and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Event> getUserEvents(Object userIdOrUsername, ActionType action, TargetType targetType, Date before, Date after,
            SortOrder sortOrder, int itemsPerPage) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("action", action)
                .withParam("target_type", targetType != null ? targetType.toValue().toLowerCase() : null)
                .withParam("before", before)
                .withParam("after", after)
                .withParam("sort", sortOrder);

        return (new Pager<Event>(this, Event.class, itemsPerPage, formData.asMap(),
                "users", getUserIdOrUsername(userIdOrUsername), "events"));
    }

    /**
     * Get a Stream of events for the specified user.
     *
     * <pre><code>GitLab Endpoint: GET /users/:userId/events</code></pre>
     *
     * @param userIdOrUsername the user ID, username of the user, or a User instance holding the user ID or username
     * @param action include only events of a particular action type, optional
     * @param targetType include only events of a particular target type, optional
     * @param before include only events created before a particular date, optional
     * @param after include only events created after a particular date, optional
     * @param sortOrder sort events in ASC or DESC order by created_at. Default is DESC, optional
     * @return a Stream of events for the specified user and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Event> getUserEventsStream(Object userIdOrUsername, ActionType action, TargetType targetType,
            Date before, Date after, SortOrder sortOrder) throws GitLabApiException {
        return (getUserEvents(userIdOrUsername, action, targetType, before, after, sortOrder, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of events for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /:projectId/events</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param action include only events of a particular action type, optional
     * @param targetType include only events of a particular target type, optional
     * @param before include only events created before a particular date, optional
     * @param after include only events created after a particular date, optional
     * @param sortOrder sort events in ASC or DESC order by created_at. Default is DESC, optional
     * @return a list of events for the specified project and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public List<Event> getProjectEvents(Object projectIdOrPath, ActionType action, TargetType targetType,
            Date before, Date after, SortOrder sortOrder) throws GitLabApiException {
        return (getProjectEvents(projectIdOrPath, action, targetType, before, after, sortOrder, getDefaultPerPage()).all());
    }

    /**
     * Get a list of events for the specified project and in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:projectId/events</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param action include only events of a particular action type, optional
     * @param targetType include only events of a particular target type, optional
     * @param before include only events created before a particular date, optional
     * @param after include only events created after a particular date, optional
     * @param sortOrder sort events in ASC or DESC order by created_at. Default is DESC, optional
     * @param page the page to get
     * @param perPage the number of projects per page
     * @return a list of events for the specified project and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public List<Event> getProjectEvents(Object projectIdOrPath, ActionType action, TargetType targetType,
            Date before, Date after, SortOrder sortOrder, int page, int perPage) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("action", action)
                .withParam("target_type", targetType != null ? targetType.toValue().toLowerCase() : null)
                .withParam("before", before)
                .withParam("after", after)
                .withParam("sort", sortOrder)
                .withParam(PAGE_PARAM,  page)
                .withParam(PER_PAGE_PARAM, perPage);

        Response response = get(Response.Status.OK, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "events");
        return (response.readEntity(new GenericType<List<Event>>() {}));
    }

    /**
     * Get a list of events for the specified project and in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:projectId/events</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param action include only events of a particular action type, optional
     * @param targetType include only events of a particular target type, optional
     * @param before include only events created before a particular date, optional
     * @param after include only events created after a particular date, optional
     * @param sortOrder sort events in ASC or DESC order by created_at. Default is DESC, optional
     * @param itemsPerPage the number of Event instances that will be fetched per page
     * @return a Pager of events for the specified project and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Event> getProjectEvents(Object projectIdOrPath, ActionType action, TargetType targetType, Date before, Date after,
            SortOrder sortOrder, int itemsPerPage) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("action", action)
                .withParam("target_type", targetType != null ? targetType.toValue().toLowerCase() : null)
                .withParam("before", before)
                .withParam("after", after)
                .withParam("sort", sortOrder);

        return (new Pager<Event>(this, Event.class, itemsPerPage, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "events"));
    }

    /**
     * Get a Stream of events for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /:projectId/events</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param action include only events of a particular action type, optional
     * @param targetType include only events of a particular target type, optional
     * @param before include only events created before a particular date, optional
     * @param after include only events created after a particular date, optional
     * @param sortOrder sort events in ASC or DESC order by created_at. Default is DESC, optional
     * @return a Stream of events for the specified project and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Event> getProjectEventsStream(Object projectIdOrPath, ActionType action, TargetType targetType,
            Date before, Date after, SortOrder sortOrder) throws GitLabApiException {
        return (getProjectEvents(projectIdOrPath, action, targetType, before, after, sortOrder, getDefaultPerPage()).stream());
    }
}
