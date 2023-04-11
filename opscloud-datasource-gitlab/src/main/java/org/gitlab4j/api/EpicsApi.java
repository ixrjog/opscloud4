package org.gitlab4j.api;

import org.gitlab4j.api.models.Epic;
import org.gitlab4j.api.models.EpicIssue;

import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This class implements the client side API for the GitLab Epics and Epic Issues API calls.
 *
 * NOTE:
 *  - If a user is not a member of a group and the group is private, a GET request on that group will result to a 404 status code.
 *  - Epics are available only in Ultimate. If epics feature is not available a 403 status code will be returned.
 *
 * @see <a href="https://docs.gitlab.com/ee/api/epics.html">GitLab Epics API Documentaion</a>
 * @see <a href="https://docs.gitlab.com/ee/api/epic_issues.html">GitLab Epic Issues API Documentation</a>
 */
public class EpicsApi extends AbstractApi {

    public EpicsApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Gets all epics of the requested group and its subgroups.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/epics</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @return a list of all epics of the requested group and its subgroups
     * @throws GitLabApiException if any exception occurs
     */
    public List<Epic> getEpics(Object groupIdOrPath) throws GitLabApiException {
        return (getEpics(groupIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Gets all epics of the requested group and its subgroups using the specified page and per page setting.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/epics</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param page the page to get
     * @param perPage the number of issues per page
     * @return a list of all epics of the requested group and its subgroups in the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public List<Epic> getEpics(Object groupIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "groups", getGroupIdOrPath(groupIdOrPath), "epics");
        return (response.readEntity(new GenericType<List<Epic>>() { }));
    }

    /**
     * Get a Pager of all epics of the requested group and its subgroups.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/epics</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param itemsPerPage the number of issues per page
     * @return the Pager of all epics of the requested group and its subgroups
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Epic> getEpics(Object groupIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Epic>(this, Epic.class, itemsPerPage, null, "groups", getGroupIdOrPath(groupIdOrPath), "epics"));
    }

    /**
     * Gets all epics of the requested group and its subgroups as a Stream.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/epics</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @return a Stream of all epics of the requested group and its subgroups
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Epic> getEpicsStream(Object groupIdOrPath) throws GitLabApiException {
        return (getEpics(groupIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Gets all epics of the requested group and its subgroups.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/epics</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param authorId returns epics created by the given user id
     * @param labels return epics matching a comma separated list of labels names.
     *        Label names from the epic group or a parent group can be used
     * @param orderBy return epics ordered by CREATED_AT or UPDATED_AT. Default is CREATED_AT
     * @param sortOrder return epics sorted in ASC or DESC order. Default is DESC
     * @param search search epics against their title and description
     * @return a list of matching epics of the requested group and its subgroups
     * @throws GitLabApiException if any exception occurs
     */
    public List<Epic> getEpics(Object groupIdOrPath, Long authorId, String labels, EpicOrderBy orderBy,
            SortOrder sortOrder, String search) throws GitLabApiException {
        return (getEpics(groupIdOrPath, authorId, labels, orderBy, sortOrder, search, getDefaultPerPage()).all());
    }

    /**
     * Gets all epics of the requested group and its subgroups using the specified page and per page setting.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/epics</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param authorId returns epics created by the given user id
     * @param labels return epics matching a comma separated list of labels names
     *        Label names from the epic group or a parent group can be used
     * @param orderBy return epics ordered by CREATED_AT or UPDATED_AT. Default is CREATED_AT
     * @param sortOrder return epics sorted in ASC or DESC order. Default is DESC
     * @param search search epics against their title and description
     * @param page the page to get
     * @param perPage the number of issues per page
     * @return a list of matching epics of the requested group and its subgroups in the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public List<Epic> getEpics(Object groupIdOrPath, Long authorId, String labels,
            EpicOrderBy orderBy, SortOrder sortOrder, String search, int page, int perPage) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm(page, perPage)
                .withParam("author_id", authorId)
                .withParam("labels", labels)
                .withParam("order_by", orderBy)
                .withParam("sort", sortOrder)
                .withParam("search", search);
        Response response = get(Response.Status.OK, formData.asMap(), "groups", getGroupIdOrPath(groupIdOrPath), "epics");
        return (response.readEntity(new GenericType<List<Epic>>() { }));
    }

    /**
     * Get a Pager of all epics of the requested group and its subgroups.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/epics</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param authorId returns epics created by the given user id
     * @param labels return epics matching a comma separated list of labels names.
     *        Label names from the epic group or a parent group can be used
     * @param itemsPerPage the number of issues per page
     * @param orderBy return epics ordered by CREATED_AT or UPDATED_AT. Default is CREATED_AT
     * @param sortOrder return epics sorted in ASC or DESC order. Default is DESC
     * @param search search epics against their title and description
     * @return the Pager of matching epics of the requested group and its subgroups
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Epic> getEpics(Object groupIdOrPath, Long authorId, String labels,
            EpicOrderBy orderBy, SortOrder sortOrder, String search, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("author_id", authorId)
                .withParam("labels", labels)
                .withParam("order_by", orderBy)
                .withParam("sort", sortOrder)
                .withParam("search", search);
        return (new Pager<Epic>(this, Epic.class, itemsPerPage, formData.asMap(), "groups", getGroupIdOrPath(groupIdOrPath), "epics"));
    }

    /**
     * Gets all epics of the requested group and its subgroups as a Stream.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/epics</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param authorId returns epics created by the given user id
     * @param labels return epics matching a comma separated list of labels names.
     *        Label names from the epic group or a parent group can be used
     * @param orderBy return epics ordered by CREATED_AT or UPDATED_AT. Default is CREATED_AT
     * @param sortOrder return epics sorted in ASC or DESC order. Default is DESC
     * @param search search epics against their title and description
     * @return a Stream of matching epics of the requested group and its subgroups
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Epic> getEpicsStream(Object groupIdOrPath, Long authorId, String labels, EpicOrderBy orderBy,
            SortOrder sortOrder, String search) throws GitLabApiException {
        return (getEpics(groupIdOrPath, authorId, labels, orderBy, sortOrder, search, getDefaultPerPage()).stream());
    }

    /**
     * Get a single epic for the specified group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/epics/:epic_iid</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param epicIid the IID of the epic to get
     * @return an Epic instance for the specified Epic
     * @throws GitLabApiException if any exception occurs
     */
    public Epic getEpic(Object groupIdOrPath, Long epicIid) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "groups", getGroupIdOrPath(groupIdOrPath), "epics", epicIid);
        return (response.readEntity(Epic.class));
    }

    /**
     * Get an Optional instance with the value for the specific Epic.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/epics/:epic_iid</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param epicIid the IID of the epic to get
     * @return an Optional instance with the specified Epic as a value
     */
    public Optional<Epic> getOptionalEpic(Object groupIdOrPath, Long epicIid) {
        try {
            return (Optional.ofNullable(getEpic(groupIdOrPath, epicIid)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Creates a new epic.
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/epics</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param title the title of the epic (required)
     * @param labels comma separated list of labels (optional)
     * @param description the description of the epic (optional)
     * @param startDate the start date of the epic (optional)
     * @param endDate the end date of the epic (optional)
     * @return an Epic instance containing info on the newly created epic
     * @throws GitLabApiException if any exception occurs
     */
    public Epic createEpic(Object groupIdOrPath, String title, String labels, String description,
            Date startDate, Date endDate) throws GitLabApiException {
        Form formData = new GitLabApiForm()
                .withParam("title", title, true)
                .withParam("labels", labels)
                .withParam("description", description)
                .withParam("start_date", startDate)
                .withParam("end_date", endDate);
        Response response = post(Response.Status.CREATED, formData.asMap(),
                "groups", getGroupIdOrPath(groupIdOrPath), "epics");
        return (response.readEntity(Epic.class));
    }

    /**
     * Creates a new epic using the information contained in the provided Epic instance.  Only the following
     * fields from the Epic instance are used:
     * <pre><code>
     *      title - the title of the epic (required)
     *      labels - comma separated list of labels (optional)
     *      description - the description of the epic (optional)
     *      startDate - the start date of the epic (optional)
     *      endDate - the end date of the epic (optional)
     * </code></pre>
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/epics</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param epic the Epic instance with information for the new epic
     * @return an Epic instance containing info on the newly created epic
     * @throws GitLabApiException if any exception occurs
     */
    public Epic createEpic(Object groupIdOrPath, Epic epic) throws GitLabApiException {
        Form formData = new GitLabApiForm()
                .withParam("title", epic.getTitle(), true)
                .withParam("labels", epic.getLabels())
                .withParam("description", epic.getDescription())
                .withParam("start_date", epic.getStartDate())
                .withParam("end_date", epic.getEndDate());
        Response response = post(Response.Status.CREATED, formData.asMap(),
                "groups", getGroupIdOrPath(groupIdOrPath), "epics");
        return (response.readEntity(Epic.class));
    }

    /**
     * Updates an existing epic.
     *
     * <pre><code>GitLab Endpoint: PUT /groups/:id/epics/:epic_iid</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param epicIid the IID of the epic to update
     * @param title the title of the epic (optional)
     * @param labels comma separated list of labels (optional)
     * @param description the description of the epic (optional)
     * @param startDate the start date of the epic (optional)
     * @param endDate the end date of the epic (optional)
     * @return an Epic instance containing info on the newly created epic
     * @throws GitLabApiException if any exception occurs
     */
    public Epic updateEpic(Object groupIdOrPath, Long epicIid, String title, String labels, String description,
            Date startDate, Date endDate) throws GitLabApiException {
        Form formData = new GitLabApiForm()
                .withParam("title", title, true)
                .withParam("labels", labels)
                .withParam("description", description)
                .withParam("start_date", startDate)
                .withParam("end_date", endDate);
        Response response = put(Response.Status.OK, formData.asMap(),
                "groups", getGroupIdOrPath(groupIdOrPath), "epics", epicIid);
        return (response.readEntity(Epic.class));
    }

    /**
     * Updates an epic using the information contained in the provided Epic instance.  Only the following
     * fields from the Epic instance are used:
     * <pre><code>
     *      title - the title of the epic (optional)
     *      labels - comma separated list of labels (optional)
     *      description - the description of the epic (optional)
     *      startDate - the start date of the epic (optional)
     *      endDate - the end date of the epic (optional)
     * </code></pre>
     * <pre><code>GitLab Endpoint: PUT /groups/:id/epics/:epic_iid</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param epicIid the IID of the epic to update
     * @param epic the Epic instance with update information
     * @return an Epic instance containing info on the updated epic
     * @throws GitLabApiException if any exception occurs
     */
    public Epic updateEpic(Object groupIdOrPath, Long epicIid, Epic epic) throws GitLabApiException {
        Form formData = new GitLabApiForm()
                .withParam("title", epic.getTitle(), true)
                .withParam("labels", epic.getLabels())
                .withParam("description", epic.getDescription())
                .withParam("start_date", epic.getStartDate())
                .withParam("end_date", epic.getEndDate());
        Response response = put(Response.Status.OK, formData.asMap(),
                "groups", getGroupIdOrPath(groupIdOrPath), "epics", epicIid);
        return (response.readEntity(Epic.class));
    }

    /**
     * Deletes an epic.
     *
     * <pre><code>GitLab Endpoint: DELETE /groups/:id/epics/:epic_iid</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param epicIid the IID of the epic to delete
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteEpic(Object groupIdOrPath, Long epicIid) throws GitLabApiException {
        delete(Response.Status.NO_CONTENT, null, "groups", getGroupIdOrPath(groupIdOrPath), "epics", epicIid);
    }

    /**
     * Gets all issues that are assigned to an epic and the authenticated user has access to.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/epics/:epic_iid/issues</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param epicIid the IID of the epic to get issues for
     * @return a list of all epic issues belonging to the specified epic
     * @throws GitLabApiException if any exception occurs
     */
    public List<Epic> getEpicIssues(Object groupIdOrPath, Long epicIid) throws GitLabApiException {
        return (getEpicIssues(groupIdOrPath, epicIid, getDefaultPerPage()).all());
    }

    /**
     * Gets all issues that are assigned to an epic and the authenticated user has access to
     * using the specified page and per page setting.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/epics/:epic_iid/issues</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param epicIid the IID of the epic to get issues for
     * @param page the page to get
     * @param perPage the number of issues per page
     * @return a list of all epic issues belonging to the specified epic in the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public List<Epic> getEpicIssues(Object groupIdOrPath, Long epicIid, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "groups", getGroupIdOrPath(groupIdOrPath), "epics", epicIid, "issues");
        return (response.readEntity(new GenericType<List<Epic>>() { }));
    }

    /**
     * Get a Pager of all issues that are assigned to an epic and the authenticated user has access to.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/epics/:epic_iid/issues</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param epicIid the IID of the epic to get issues for
     * @param itemsPerPage the number of issues per page
     * @return the Pager of all epic issues belonging to the specified epic
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Epic> getEpicIssues(Object groupIdOrPath, Long epicIid, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Epic>(this, Epic.class, itemsPerPage, null, "groups", getGroupIdOrPath(groupIdOrPath), "epics", epicIid, "issues"));
    }

    /**
     * Gets all issues that are assigned to an epic and the authenticated user has access to as a Stream.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/epics/:epic_iid/issues</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param epicIid the IID of the epic to get issues for
     * @return a Stream of all epic issues belonging to the specified epic
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Epic> getEpicIssuesStream(Object groupIdOrPath, Long epicIid) throws GitLabApiException {
        return (getEpicIssues(groupIdOrPath, epicIid, getDefaultPerPage()).stream());
    }

    /**
     * Creates an epic - issue association. If the issue in question belongs to another epic
     * it is unassigned from that epic.
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/epics/:epic_iid/issues/:issue_id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param epicIid the Epic IID to assign the issue to
     * @param issueIid the issue IID of the issue to assign to the epic
     * @return an EpicIssue instance containing info on the newly assigned epic issue
     * @throws GitLabApiException if any exception occurs
     */
    public EpicIssue assignIssue(Object groupIdOrPath, Long epicIid, Long issueIid) throws GitLabApiException {
        Response response = post(Response.Status.CREATED, (Form)null,
                "groups", getGroupIdOrPath(groupIdOrPath), "epics", epicIid, "issues", issueIid);
        return (response.readEntity(EpicIssue.class));
    }

    /**
     * Remove an epic - issue association.
     *
     * <pre><code>GitLab Endpoint: DELETE /groups/:id/epics/:epic_iid/issues/:issue_id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param epicIid the Epic IID to remove the issue from
     * @param issueIid the issue IID of the issue to remove from the epic
     * @return an EpicIssue instance containing info on the removed issue
     * @throws GitLabApiException if any exception occurs
     */
    public EpicIssue removeIssue(Object groupIdOrPath, Long epicIid, Long issueIid) throws GitLabApiException {
        Response response = delete(Response.Status.OK, null,
                "groups", getGroupIdOrPath(groupIdOrPath), "epics", epicIid, "issues", issueIid);
        return (response.readEntity(EpicIssue.class));
    }

    /**
     * Updates an epic - issue association.
     *
     * <pre><code>GitLab Endpoint: PUT /groups/:id/epics/:epic_iid/issues/:issue_id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param epicIid the Epic IID that the issue is assigned to
     * @param issueIid the issue IID to update
     * @param moveBeforeId the ID of the issue - epic association that should be placed before the link in the question (optional)
     * @param moveAfterId the ID of the issue - epic association that should be placed after the link in the question (optional)
     * @return an EpicIssue instance containing info on the newly assigned epic issue
     * @throws GitLabApiException if any exception occurs
     */
    public EpicIssue updateIssue(Object groupIdOrPath, Long epicIid, Long issueIid, Long moveBeforeId, Long moveAfterId) throws GitLabApiException {
        GitLabApiForm form = new GitLabApiForm()
            .withParam("move_before_id", moveBeforeId)
            .withParam("move_after_id", moveAfterId);
        Response response = post(Response.Status.OK, form,
                "groups", getGroupIdOrPath(groupIdOrPath), "epics", epicIid, "issues", issueIid);
        return (response.readEntity(EpicIssue.class));
    }
}
