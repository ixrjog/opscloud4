package org.gitlab4j.api;

import org.gitlab4j.api.GitLabApi.ApiVersion;
import org.gitlab4j.api.models.*;
import org.gitlab4j.api.utils.DurationUtils;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This class provides an entry point to all the GitLab API Issue calls.
 * @see <a href="https://docs.gitlab.com/ce/api/issues.html">Issues API at GitLab</a>
 * @see <a href="https://docs.gitlab.com/ce/api/issue_links.html">Issue Links API at GitLab</a>
 * @see <a href="https://docs.gitlab.com/ce/api/issues_statistics.html">Issues Statistics API at GitLab</a>
 */
public class IssuesApi extends AbstractApi implements Constants {

    public IssuesApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Get all issues the authenticated user has access to. Only returns issues created by the current user.
     *
     * <pre><code>GitLab Endpoint: GET /issues</code></pre>
     *
     * @return a list of user's issues
     * @throws GitLabApiException if any exception occurs
     */
    public List<Issue> getIssues() throws GitLabApiException {
        return (getIssues(getDefaultPerPage()).all());
    }

    /**
     * Get all issues the authenticated user has access to using the specified page and per page setting. Only returns issues created by the current user.
     *
     * <pre><code>GitLab Endpoint: GET /issues</code></pre>
     *
     * @param page the page to get
     * @param perPage the number of issues per page
     * @return the list of issues in the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public List<Issue> getIssues(int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "issues");
        return (response.readEntity(new GenericType<List<Issue>>() {}));
    }

    /**
     * Get a Pager of all issues the authenticated user has access to. Only returns issues created by the current user.
     *
     * <pre><code>GitLab Endpoint: GET /issues</code></pre>
     *r
     * @param itemsPerPage the number of issues per page
     * @return the Pager of issues in the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Issue> getIssues(int itemsPerPage) throws GitLabApiException {
        return (new Pager<Issue>(this, Issue.class, itemsPerPage, null, "issues"));
    }

    /**
     * Get all issues the authenticated user has access to as a Stream. Only returns issues created by the current user.
     *
     * <pre><code>GitLab Endpoint: GET /issues</code></pre>
     *
     * @return a Stream of user's issues
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Issue> getIssuesStream() throws GitLabApiException {
        return (getIssues(getDefaultPerPage()).stream());
    }

    /**
     * Get a list of project's issues.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a list of project's issues
     * @throws GitLabApiException if any exception occurs
     */
    public List<Issue> getIssues(Object projectIdOrPath) throws GitLabApiException {
        return (getIssues(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a list of project's issues using the specified page and per page settings.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param page the page to get
     * @param perPage the number of issues per page
     * @return the list of issues in the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public List<Issue> getIssues(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "projects", getProjectIdOrPath(projectIdOrPath), "issues");
        return (response.readEntity(new GenericType<List<Issue>>() {}));
    }

    /**
     * Get a Pager of project's issues.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of issues per page
     * @return the Pager of issues in the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Issue> getIssues(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Issue>(this, Issue.class, itemsPerPage, null, "projects", getProjectIdOrPath(projectIdOrPath), "issues"));
    }

    /**
     * Get a Stream of project's issues. Only returns the first page
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a Stream of project's issues
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Issue> getIssuesStream(Object projectIdOrPath) throws GitLabApiException {
        return (getIssues(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of project's issues.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filter {@link IssueFilter} a IssueFilter instance with the filter settings
     * @return the list of issues in the specified range.
     * @throws GitLabApiException if any exception occurs
     */
    public List<Issue> getIssues(Object projectIdOrPath, IssueFilter filter) throws GitLabApiException {
        return (getIssues(projectIdOrPath, filter, getDefaultPerPage()).all());
    }

    /**
     * Get a list of project's issues.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filter {@link IssueFilter} a IssueFilter instance with the filter settings.
     * @param page the page to get.
     * @param perPage the number of projects per page.
     * @return the list of issues in the specified range.
     * @throws GitLabApiException if any exception occurs
     */
    public List<Issue> getIssues(Object projectIdOrPath, IssueFilter filter, int page, int perPage) throws GitLabApiException {
        GitLabApiForm formData = filter.getQueryParams(page, perPage);
        Response response = get(Response.Status.OK, formData.asMap(), "projects", getProjectIdOrPath(projectIdOrPath), "issues");
        return (response.readEntity(new GenericType<List<Issue>>() {}));
    }

    /**
     * Get a list of project's issues.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filter {@link IssueFilter} a IssueFilter instance with the filter settings.
     * @param itemsPerPage the number of Project instances that will be fetched per page.
     * @return the Pager of issues in the specified range.
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Issue> getIssues(Object projectIdOrPath, IssueFilter filter, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = filter.getQueryParams();
        return (new Pager<Issue>(this, Issue.class, itemsPerPage, formData.asMap(), "projects", getProjectIdOrPath(projectIdOrPath), "issues"));
    }

    /**
     * Get a Stream of project's issues.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filter {@link IssueFilter} a IssueFilter instance with the filter settings
     * @return a Stream of issues in the specified range.
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Issue> getIssuesStream(Object projectIdOrPath, IssueFilter filter) throws GitLabApiException {
        return (getIssues(projectIdOrPath, filter, getDefaultPerPage()).stream());
    }

    /**
     * Get all issues the authenticated user has access to.
     * By default it returns only issues created by the current user.
     *
     * <pre><code>GitLab Endpoint: GET /issues</code></pre>
     *
     * @param filter {@link IssueFilter} a IssueFilter instance with the filter settings
     * @return the list of issues in the specified range.
     * @throws GitLabApiException if any exception occurs
     */
    public List<Issue> getIssues(IssueFilter filter) throws GitLabApiException {
        return (getIssues(filter, getDefaultPerPage()).all());
    }

    /**
     * Get all issues the authenticated user has access to.
     * By default it returns only issues created by the current user.
     *
     * <pre><code>GitLab Endpoint: GET /issues</code></pre>
     *
     * @param filter {@link IssueFilter} a IssueFilter instance with the filter settings.
     * @param page the page to get.
     * @param perPage the number of projects per page.
     * @return the list of issues in the specified range.
     * @throws GitLabApiException if any exception occurs
     */
    public List<Issue> getIssues(IssueFilter filter, int page, int perPage) throws GitLabApiException {
        GitLabApiForm formData = filter.getQueryParams(page, perPage);
        Response response = get(Response.Status.OK, formData.asMap(), "issues");
        return (response.readEntity(new GenericType<List<Issue>>() {}));
    }

    /**
     * Get all issues the authenticated user has access to.
     * By default it returns only issues created by the current user.
     *
     * <pre><code>GitLab Endpoint: GET /issues</code></pre>
     *
     * @param filter {@link IssueFilter} a IssueFilter instance with the filter settings.
     * @param itemsPerPage the number of Project instances that will be fetched per page.
     * @return the Pager of issues in the specified range.
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Issue> getIssues(IssueFilter filter, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = filter.getQueryParams();
        return (new Pager<Issue>(this, Issue.class, itemsPerPage, formData.asMap(),  "issues"));
    }

    /**
     * Get all issues the authenticated user has access to.
     * By default it returns only issues created by the current user.
     *
     * <pre><code>GitLab Endpoint: GET /issues</code></pre>
     *
     * @param filter {@link IssueFilter} a IssueFilter instance with the filter settings
     * @return the Stream of issues in the specified range.
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Issue> getIssuesStream(IssueFilter filter) throws GitLabApiException {
        return (getIssues(filter, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of a group’s issues.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/issues</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @return a List of issues for the specified group
     * @throws GitLabApiException if any exception occurs
     */
    public List<Issue> getGroupIssues(Object groupIdOrPath) throws GitLabApiException {
	return (getGroupIssues(groupIdOrPath, null, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of groups's issues.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/issues</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param itemsPerPage the number of Issue instances that will be fetched per page.
     * @return the Pager of issues for the specified group
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Issue> getGroupIssues(Object groupIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (getGroupIssues(groupIdOrPath, null, itemsPerPage));
    }

    /**
     * Get a Stream of a group’s issues.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/issues</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @return a Stream of issues for the specified group and filter
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Issue> getGroupIssuesStream(Object groupIdOrPath) throws GitLabApiException {
	return (getGroupIssues(groupIdOrPath, null, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of a group’s issues.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/issues</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param filter {@link IssueFilter} a IssueFilter instance with the filter settings.
     * @return a List of issues for the specified group and filter
     * @throws GitLabApiException if any exception occurs
     */
    public List<Issue> getGroupIssues(Object groupIdOrPath, IssueFilter filter) throws GitLabApiException {
	return (getGroupIssues(groupIdOrPath, filter, getDefaultPerPage()).all());
    }

    /**
     * Get a list of groups's issues.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/issues</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param filter {@link IssueFilter} a IssueFilter instance with the filter settings.
     * @param itemsPerPage the number of Issue instances that will be fetched per page.
     * @return the Pager of issues for the specified group and filter
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Issue> getGroupIssues(Object groupIdOrPath, IssueFilter filter, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = (filter != null ? filter.getQueryParams() : new GitLabApiForm());
        return (new Pager<Issue>(this, Issue.class, itemsPerPage, formData.asMap(),
                "groups", getGroupIdOrPath(groupIdOrPath), "issues"));
    }

    /**
     * Get a Stream of a group’s issues.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/issues</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param filter {@link IssueFilter} a IssueFilter instance with the filter settings.
     * @return a Stream of issues for the specified group and filter
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Issue> getGroupIssuesStream(Object groupIdOrPath, IssueFilter filter) throws GitLabApiException {
	return (getGroupIssues(groupIdOrPath, filter, getDefaultPerPage()).stream());
    }

    /**
     * Get a single project issue.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of a project's issue
     * @return the specified Issue instance
     * @throws GitLabApiException if any exception occurs
     */
    public Issue getIssue(Object projectIdOrPath, Long issueIid) throws GitLabApiException {
        Response response = get(Response.Status.OK, getDefaultPerPageParam(),
                "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid);
        return (response.readEntity(Issue.class));
    }

    /**
     * Get a single project issue as an Optional instance.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of a project's issue
     * @return the specified Issue as an Optional instance
     */
    public Optional<Issue> getOptionalIssue(Object projectIdOrPath, Long issueIid) {
        try {
            return (Optional.ofNullable(getIssue(projectIdOrPath, issueIid)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Create an issue for the project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/issues</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param title the title of an issue, required
     * @param description the description of an issue, optional
     * @return an instance of Issue
     * @throws GitLabApiException if any exception occurs
     */
    public Issue createIssue(Object projectIdOrPath, String title, String description) throws GitLabApiException {
        return (createIssue(projectIdOrPath, title, description, null, null, null, null, null, null, null, null));
    }

    /**
     * Create an issue for the project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/issues</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param title the issue title of an issue, required
     * @param description the description of an issue, optional
     * @param confidential set the issue to be confidential, default is false, optional
     * @param assigneeIds the IDs of the users to assign issue, optional
     * @param milestoneId the ID of a milestone to assign issue, optional
     * @param labels comma-separated label names for an issue, optional
     * @param createdAt the date the issue was created at, optional
     * @param dueDate the due date, optional
     * @param mergeRequestToResolveId the IID of a merge request in which to resolve all issues. This will fill the issue with a default
     *        description and mark all discussions as resolved. When passing a description or title, these values will take precedence over the default values. Optional
     * @param discussionToResolveId the ID of a discussion to resolve. This will fill in the issue with a default description and mark the discussion as resolved.
     *        Use in combination with merge_request_to_resolve_discussions_of. Optional
     * @return an instance of Issue
     * @throws GitLabApiException if any exception occurs
     */
    public Issue createIssue(Object projectIdOrPath, String title, String description, Boolean confidential, List<Long> assigneeIds, Long milestoneId, String labels,
            Date createdAt, Date dueDate, Long mergeRequestToResolveId, Long discussionToResolveId) throws GitLabApiException {

        return (createIssue(projectIdOrPath, title, description, confidential, assigneeIds, milestoneId, labels, createdAt, dueDate, mergeRequestToResolveId, discussionToResolveId, null));
    }

    /**
     * Create an issue for the project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/issues</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param title the issue title of an issue, required
     * @param description the description of an issue, optional
     * @param confidential set the issue to be confidential, default is false, optional
     * @param assigneeIds the IDs of the users to assign issue, optional
     * @param milestoneId the ID of a milestone to assign issue, optional
     * @param labels comma-separated label names for an issue, optional
     * @param createdAt the date the issue was created at, optional
     * @param dueDate the due date, optional
     * @param mergeRequestToResolveId the IID of a merge request in which to resolve all issues. This will fill the issue with a default
     *        description and mark all discussions as resolved. When passing a description or title, these values will take precedence over the default values. Optional
     * @param discussionToResolveId the ID of a discussion to resolve. This will fill in the issue with a default description and mark the discussion as resolved.
     *        Use in combination with merge_request_to_resolve_discussions_of. Optional
     * @param iterationTitle the iteration title of an issue, optional
     * @return an instance of Issue
     * @throws GitLabApiException if any exception occurs
     */
    public Issue createIssue(Object projectIdOrPath, String title, String description, Boolean confidential, List<Long> assigneeIds, Long milestoneId, String labels,
        Date createdAt, Date dueDate, Long mergeRequestToResolveId, Long discussionToResolveId, String iterationTitle) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
            .withParam("title", title, true)
            .withParam("description", description)
            .withParam("confidential", confidential)
            .withParam("assignee_ids", assigneeIds)
            .withParam("milestone_id", milestoneId)
            .withParam("labels", labels)
            .withParam("created_at", createdAt)
            .withParam("due_date", dueDate)
            .withParam("merge_request_to_resolve_discussions_of", mergeRequestToResolveId)
            .withParam("discussion_to_resolve", discussionToResolveId)
            .withParam("iteration_title", iterationTitle);
        Response response = post(Response.Status.CREATED, formData, "projects", getProjectIdOrPath(projectIdOrPath), "issues");
        return (response.readEntity(Issue.class));
    }

    /**
     * Closes an existing project issue.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/issues/:issue_iid</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param issueIid the issue IID to update, required
     * @return an instance of the updated Issue
     * @throws GitLabApiException if any exception occurs
     */
    public Issue closeIssue(Object projectIdOrPath, Long issueIid) throws GitLabApiException {

        if (issueIid == null) {
            throw new RuntimeException("issue IID cannot be null");
        }

        GitLabApiForm formData = new GitLabApiForm().withParam("state_event", StateEvent.CLOSE);
        Response response = put(Response.Status.OK, formData.asMap(), "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid);
        return (response.readEntity(Issue.class));
    }

    /**
     * Updates an existing project issue. This call can also be used to mark an issue as closed.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/issues/:issue_iid</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param issueIid the issue IID to update, required
     * @param title the title of an issue, optional
     * @param description the description of an issue, optional
     * @param confidential set the issue to be confidential, default is false, optional
     * @param assigneeIds the IDs of the users to assign issue, optional
     * @param milestoneId the ID of a milestone to assign issue, optional
     * @param labels comma-separated label names for an issue, optional
     * @param stateEvent the state event of an issue. Set close to close the issue and reopen to reopen it, optional
     * @param updatedAt sets the updated date, requires admin or project owner rights, optional
     * @param dueDate the due date, optional
     * @return an instance of the updated Issue
     * @throws GitLabApiException if any exception occurs
     */
    public Issue updateIssue(Object projectIdOrPath, Long issueIid, String title, String description, Boolean confidential, List<Long> assigneeIds,
            Long milestoneId, String labels, StateEvent stateEvent, Date updatedAt, Date dueDate) throws GitLabApiException {

        if (issueIid == null) {
            throw new RuntimeException("issue IID cannot be null");
        }

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("title", title)
                .withParam("description", description)
                .withParam("confidential", confidential)
                .withParam("assignee_ids", assigneeIds)
                .withParam("milestone_id", milestoneId)
                .withParam("labels", labels)
                .withParam("state_event", stateEvent)
                .withParam("updated_at", updatedAt)
                .withParam("due_date", dueDate);
        Response response = put(Response.Status.OK, formData.asMap(), "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid);
        return (response.readEntity(Issue.class));
    }

    /**
     * Updates an existing project issue. This call can also be used to mark an issue as closed.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/issues/:issue_iid</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param issueIid the issue IID to update, required
     * @param assigneeId the ID of the user to assign issue to, required
     * @return an instance of the updated Issue
     * @throws GitLabApiException if any exception occurs
     */
    public Issue assignIssue(Object projectIdOrPath, Long issueIid, Long assigneeId) throws GitLabApiException {

        if (issueIid == null) {
            throw new RuntimeException("issue IID cannot be null");
        }

        GitLabApiForm formData = new GitLabApiForm().withParam("assignee_ids", Collections.singletonList(assigneeId));
        Response response = put(Response.Status.OK, formData.asMap(), "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid);
        return (response.readEntity(Issue.class));
    }

    /**
     * Delete an issue.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/issues/:issue_iid</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param issueIid the internal ID of a project's issue
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteIssue(Object projectIdOrPath, Long issueIid) throws GitLabApiException {

        if (issueIid == null) {
            throw new RuntimeException("issue IID cannot be null");
        }

        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.NO_CONTENT);
        delete(expectedStatus, getDefaultPerPageParam(), "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid);
    }

    /**
     * Sets an estimated time of work in this issue
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/issues/:issue_iid/time_estimate</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param issueIid the internal ID of a project's issue
     * @param duration estimated time in seconds
     * @return a TimeSTats instance
     * @throws GitLabApiException if any exception occurs
     */
    public TimeStats estimateTime(Object projectIdOrPath, Long issueIid, int duration) throws GitLabApiException {
        return (estimateTime(projectIdOrPath, issueIid, new Duration(duration)));
    }

    /**
     * Sets an estimated time of work in this issue
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/issues/:issue_iid/time_estimate</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param issueIid the internal ID of a project's issue
     * @param duration Human readable format, e.g. 3h30m
     * @return a TimeSTats instance
     * @throws GitLabApiException if any exception occurs
     */
    public TimeStats estimateTime(Object projectIdOrPath, Long issueIid, String duration) throws GitLabApiException {
        return (estimateTime(projectIdOrPath, issueIid, new Duration(duration)));
    }

    /**
     * Sets an estimated time of work in this issue
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/issues/:issue_iid/time_estimate</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of a project's issue
     * @param duration set the estimate of time to this duration
     * @return a TimeSTats instance
     * @throws GitLabApiException if any exception occurs
     */
    public TimeStats estimateTime(Object projectIdOrPath, Long issueIid, Duration duration) throws GitLabApiException {

        if (issueIid == null) {
            throw new RuntimeException("issue IID cannot be null");
        }

        String durationString = (duration != null ? DurationUtils.toString(duration.getSeconds(), false) : null);
        GitLabApiForm formData = new GitLabApiForm().withParam("duration", durationString, true);

        Response response = post(Response.Status.OK, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "time_estimate");
        return (response.readEntity(TimeStats.class));
    }

    /**
     * Resets the estimated time for this issue to 0 seconds.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/issues/:issue_iid/reset_time_estimate</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of a project's issue
     * @return a TimeSTats instance
     * @throws GitLabApiException if any exception occurs
     */
    public TimeStats resetEstimatedTime(Object projectIdOrPath, Long issueIid) throws GitLabApiException {

        if (issueIid == null) {
            throw new RuntimeException("issue IID cannot be null");
        }

        Response response = post(Response.Status.OK, new GitLabApiForm().asMap(), "projects",
                getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "reset_time_estimate");
        return (response.readEntity(TimeStats.class));
    }

    /**
     * Adds spent time for this issue
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/issues/:issue_iid/add_spent_time</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of a project's issue
     * @param duration the duration in seconds
     * @return a TimeSTats instance
     * @throws GitLabApiException if any exception occurs
     */
    public TimeStats addSpentTime(Object projectIdOrPath, Long issueIid, int duration) throws GitLabApiException {
        return (addSpentTime(projectIdOrPath, issueIid, new Duration(duration)));
    }

    /**
     * Adds spent time for this issue
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/issues/:issue_iid/add_spent_time</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of a project's issue
     * @param duration Human readable format, e.g. 3h30m
     * @return a TimeSTats instance
     * @throws GitLabApiException if any exception occurs
     */
    public TimeStats addSpentTime(Object projectIdOrPath, Long issueIid, String duration) throws GitLabApiException {
        return (addSpentTime(projectIdOrPath, issueIid, new Duration(duration)));
    }

    /**
     * Adds spent time for this issue
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/issues/:issue_iid/add_spent_time</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of a project's issue
     * @param duration the duration of time spent
     * @return a TimeSTats instance
     * @throws GitLabApiException if any exception occurs
     */
    public TimeStats addSpentTime(Object projectIdOrPath, Long issueIid, Duration duration) throws GitLabApiException {

        if (issueIid == null) {
            throw new RuntimeException("issue IID cannot be null");
        }

        String durationString = (duration != null ? DurationUtils.toString(duration.getSeconds(), false) : null);
        GitLabApiForm formData = new GitLabApiForm().withParam("duration", durationString, true);

        Response response = post(Response.Status.CREATED, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "add_spent_time");
        return (response.readEntity(TimeStats.class));
    }

    /**
     * Resets the total spent time for this issue to 0 seconds.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/issues/:issue_iid/reset_spent_time</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of a project's issue
     * @return a TimeSTats instance
     * @throws GitLabApiException if any exception occurs
     */
    public TimeStats resetSpentTime(Object projectIdOrPath, Long issueIid) throws GitLabApiException {

        if (issueIid == null) {
            throw new RuntimeException("issue IID cannot be null");
        }

        Response response = post(Response.Status.OK, new GitLabApiForm().asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "reset_spent_time");
        return (response.readEntity(TimeStats.class));
    }

    /**
     * Get time tracking stats.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/time_stats</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of a project's issue
     * @return a TimeStats instance
     * @throws GitLabApiException if any exception occurs
     */
    public TimeStats getTimeTrackingStats(Object projectIdOrPath, Long issueIid) throws GitLabApiException {

        if (issueIid == null) {
            throw new RuntimeException("issue IID cannot be null");
        }

        Response response = get(Response.Status.OK, new GitLabApiForm().asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "time_stats");
        return (response.readEntity(TimeStats.class));
    }

    /**
     * Get time tracking stats as an Optional instance
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/time_stats</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of a project's issue
     * @return a TimeStats as an Optional instance
     */
    public Optional<TimeStats> getOptionalTimeTrackingStats(Object projectIdOrPath, Long issueIid) {
        try {
            return (Optional.ofNullable(getTimeTrackingStats(projectIdOrPath, issueIid)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Get list containing all the merge requests that will close issue when merged.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/closed_by</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param issueIid the internal ID of a project's issue
     * @return a List containing all the merge requests what will close the issue when merged.
     * @throws GitLabApiException if any exception occurs
     */
    public List<MergeRequest> getClosedByMergeRequests(Object projectIdOrPath, Long issueIid) throws GitLabApiException {
        return (getClosedByMergeRequests(projectIdOrPath, issueIid, getDefaultPerPage()).all());
    }

    /**
     * Get list containing all the merge requests that will close issue when merged.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/closed_by</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param issueIid the internal ID of a project's issue
     * @param page the page to get
     * @param perPage the number of issues per page
     * @return a List containing all the merge requests what will close the issue when merged.
     * @throws GitLabApiException if any exception occurs
     */
    public List<MergeRequest> getClosedByMergeRequests(Object projectIdOrPath, Long issueIid, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage),
                "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "closed_by");
        return (response.readEntity(new GenericType<List<MergeRequest>>() { }));
    }

    /**
     * Get a Pager containing all the merge requests that will close issue when merged.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/closed_by</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param issueIid the internal ID of a project's issue
     * @param itemsPerPage the number of Issue instances that will be fetched per page
     * @return a Pager containing all the issues that would be closed by merging the provided merge request
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<MergeRequest> getClosedByMergeRequests(Object projectIdOrPath, Long issueIid, int itemsPerPage) throws GitLabApiException {
        return new Pager<MergeRequest>(this, MergeRequest.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "closed_by");
    }

    /**
     * Get list containing all the merge requests that will close issue when merged.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/closed_by</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param issueIid the internal ID of a project's issue
     * @return a List containing all the merge requests what will close the issue when merged.
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<MergeRequest> getClosedByMergeRequestsStream(Object projectIdOrPath, Long issueIid) throws GitLabApiException {
        return (getClosedByMergeRequests(projectIdOrPath, issueIid, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of related issues of a given issue, sorted by the relationship creation datetime (ascending).
     * Issues will be filtered according to the user authorizations.
     *
     * <p>NOTE: Only available in GitLab Starter, GitLab Bronze, and higher tiers.</p>
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/links</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of a project's issue
     * @return a list of related issues of a given issue, sorted by the relationship creation datetime (ascending)
     * @throws GitLabApiException if any exception occurs
     */
    public List<Issue> getIssueLinks(Object projectIdOrPath, Long issueIid) throws GitLabApiException {
        return (getIssueLinks(projectIdOrPath, issueIid, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of related issues of a given issue, sorted by the relationship creation datetime (ascending).
     * Issues will be filtered according to the user authorizations.
     *
     * <p>NOTE: Only available in GitLab Starter, GitLab Bronze, and higher tiers.</p>
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/links</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of a project's issue
     * @param itemsPerPage the number of issues per page
     * @return a Pager of related issues of a given issue, sorted by the relationship creation datetime (ascending)
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Issue> getIssueLinks(Object projectIdOrPath, Long issueIid, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Issue>(this, Issue.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "links"));
    }

    /**
     * Get a Stream of related issues of a given issue, sorted by the relationship creation datetime (ascending).
     * Issues will be filtered according to the user authorizations.
     *
     * <p>NOTE: Only available in GitLab Starter, GitLab Bronze, and higher tiers.</p>
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/links</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of a project's issue
     * @return a Stream of related issues of a given issue, sorted by the relationship creation datetime (ascending)
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Issue> getIssueLinksStream(Object projectIdOrPath, Long issueIid) throws GitLabApiException {
        return (getIssueLinks(projectIdOrPath, issueIid, getDefaultPerPage()).stream());
    }

    /**
     * Creates a two-way relation between two issues. User must be allowed to update both issues in order to succeed.
     *
     * <p>NOTE: Only available in GitLab Starter, GitLab Bronze, and higher tiers.</p>
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/issues/:issue_iid/links</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of a project's issue
     * @param targetProjectIdOrPath the project in the form of an Long(ID), String(path), or Project instance of the target project
     * @param targetIssueIid the internal ID of a target project’s issue
     * @return an instance of IssueLink holding the link relationship
     * @throws GitLabApiException if any exception occurs
     */
    public IssueLink createIssueLink(Object projectIdOrPath, Long issueIid,
            Object targetProjectIdOrPath, Long targetIssueIid) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("target_project_id", getProjectIdOrPath(targetProjectIdOrPath), true)
                .withParam("target_issue_iid", targetIssueIid, true);

        Response response = post(Response.Status.OK, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "links");
        return (response.readEntity(IssueLink.class));
    }

    /**
     * Deletes an issue link, thus removes the two-way relationship.
     *
     * <p>NOTE: Only available in GitLab Starter, GitLab Bronze, and higher tiers.</p>
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/issues/:issue_iid/links/:issue_link_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of a project's issue, required
     * @param issueLinkId the ID of an issue relationship, required
     * @return an instance of IssueLink holding the deleted link relationship
     * @throws GitLabApiException if any exception occurs
     */
    public IssueLink deleteIssueLink(Object projectIdOrPath, Long issueIid, Long issueLinkId) throws GitLabApiException {
        Response response = delete(Response.Status.OK, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "links", issueLinkId);
        return (response.readEntity(IssueLink.class));
    }

    /**
     * Get list of participants for an issue.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/participants</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the IID of the issue to get the participants for
     * @return a List containing all participants for the specified issue
     * @throws GitLabApiException if any exception occurs
     */
    public List<Participant> getParticipants(Object projectIdOrPath, Long issueIid) throws GitLabApiException {
        return (getParticipants(projectIdOrPath, issueIid, getDefaultPerPage()).all());
    }

    /**
     * Get list of participants for an issue and in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/participants</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the IID of the issue to get the participants for
     * @param page the page to get
     * @param perPage the number of projects per page
     * @return a List containing all participants for the specified issue
     * @throws GitLabApiException if any exception occurs
     */
    public List<Participant> getParticipants(Object projectIdOrPath, Long issueIid, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage),
                    "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "participants");
        return (response.readEntity(new GenericType<List<Participant>>() { }));
    }

    /**
     * Get a Pager of the participants for an issue.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/participants</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the IID of the issue to get the participants for
     * @param itemsPerPage the number of Participant instances that will be fetched per page
     * @return a Pager containing all participants for the specified issue
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Participant> getParticipants(Object projectIdOrPath, Long issueIid, int itemsPerPage) throws GitLabApiException {
        return new Pager<Participant>(this, Participant.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "participants");
    }

    /**
     * Get Stream of participants for an issue.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/participants</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the IID of the issue to get the participants for
     * @return a Stream containing all participants for the specified issue
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Participant> getParticipantsStream(Object projectIdOrPath, Long issueIid) throws GitLabApiException {
        return (getParticipants(projectIdOrPath, issueIid, getDefaultPerPage()).stream());
    }

    /**
     * Gets issues count statistics on all issues the authenticated user has access to. By default it returns
     * only issues created by the current user. To get all issues, use parameter scope=all.
     *
     * <pre><code>GitLab Endpoint: GET /issues_statistics</code></pre>
     *
     * @param filter {@link IssuesStatisticsFilter} a IssuesStatisticsFilter instance with the filter settings.
     * @return an IssuesStatistics instance with the statistics for the matched issues.
     * @throws GitLabApiException if any exception occurs
     */
    public IssuesStatistics getIssuesStatistics(IssuesStatisticsFilter filter) throws GitLabApiException {
        GitLabApiForm formData = filter.getQueryParams();
        Response response = get(Response.Status.OK, formData.asMap(), "issues_statistics");
        return (response.readEntity(IssuesStatistics.class));
    }

    /**
     * Gets issues count statistics for given group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:groupId/issues_statistics</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param filter {@link IssuesStatisticsFilter} a IssuesStatisticsFilter instance with the filter settings
     * @return an IssuesStatistics instance with the statistics for the matched issues
     * @throws GitLabApiException if any exception occurs
     */
    public IssuesStatistics getGroupIssuesStatistics(Object groupIdOrPath, IssuesStatisticsFilter filter) throws GitLabApiException {
        GitLabApiForm formData = filter.getQueryParams();
        Response response = get(Response.Status.OK, formData.asMap(), "groups", this.getGroupIdOrPath(groupIdOrPath), "issues_statistics");
        return (response.readEntity(IssuesStatistics.class));
    }

    /**
     * Gets issues count statistics for given project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:projectId/issues_statistics</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param filter {@link IssuesStatisticsFilter} a IssuesStatisticsFilter instance with the filter settings.
     * @return an IssuesStatistics instance with the statistics for the matched issues
     * @throws GitLabApiException if any exception occurs
     */
    public IssuesStatistics geProjectIssuesStatistics(Object projectIdOrPath, IssuesStatisticsFilter filter) throws GitLabApiException {
        GitLabApiForm formData = filter.getQueryParams();
        Response response = get(Response.Status.OK, formData.asMap(), "projects", this.getProjectIdOrPath(projectIdOrPath), "issues_statistics");
        return (response.readEntity(IssuesStatistics.class));
    }

    /**
     * <p>Moves an issue to a different project. If the target project equals the source project or
     * the user has insufficient permissions to move an issue, error 400 together with an
     * explaining error message is returned.</p>
     *
     * <p>If a given label and/or milestone with the same name also exists in the target project,
     * it will then be assigned to the issue that is being moved.</p>
     *
     * <pre><code>GitLab Endpoint: POST /projects/:projectId/issues/:issue_iid/move</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param issueIid the IID of the issue to move
     * @param toProjectId the ID of the project to move the issue to
     * @return an Issue instance for the moved issue
     * @throws GitLabApiException if any exception occurs
     */
    public Issue moveIssue(Object projectIdOrPath, Long issueIid, Object toProjectId) throws GitLabApiException {
	GitLabApiForm formData = new GitLabApiForm().withParam("to_project_id", toProjectId, true);
        Response response = post(Response.Status.OK, formData,
        	"projects", this.getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "move");
        return (response.readEntity(Issue.class));
    }
}
