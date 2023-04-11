/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Greg Messner <greg@messners.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.gitlab4j.api;

import org.gitlab4j.api.GitLabApi.ApiVersion;
import org.gitlab4j.api.models.*;
import org.gitlab4j.api.utils.ISO8601;

import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Stream;

/**
 * This class provides an entry point to all the GitLab API project calls.
 *
 * @see <a href="https://docs.gitlab.com/ce/api/projects.html">Projects API at GitLab</a>
 * @see <a href="https://docs.gitlab.com/ce/api/project_statistics.html">Project statistics API</a>
 * @see <a href="https://docs.gitlab.com/ce/api/members.html">Group and project members API at GitLab</a>
 * @see <a href="https://docs.gitlab.com/ce/api/access_requests.html#group-and-project-access-requests-api">Group and project access requests API</a>
 * @see <a href="https://docs.gitlab.com/ee/api/project_badges.html">Project badges API</a>
 * @see <a href="https://docs.gitlab.com/ce/api/merge_request_approvals.html">Merge request approvals API (Project-level) at GitLab</a>
 * @see <a href="https://docs.gitlab.com/ce/api/audit_events.html#retrieve-all-project-audit-events">Project audit events API</a>
 * @see <a href="https://docs.gitlab.com/ce/api/custom_attributes.html">Custom Attributes API</a>
 * @see <a href="https://docs.gitlab.com/ce/api/remote_mirrors.html">Project remote mirrors API</a>
 */
public class ProjectApi extends AbstractApi implements Constants {

    public ProjectApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Get the project fetch statistics for the last 30 days. Retrieving the statistics requires
     * write access to the repository. Currently only HTTP fetches statistics are returned.
     * Fetches statistics includes both clones and pulls count and are HTTP only,
     * SSH fetches are not included.
     *
     * <pre><code>GitLab Endpoint: GET /project/:id/statistics</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a ProjectFetches instance with the project fetch statistics for the last 30 days
     * @throws GitLabApiException if any exception occurs during execution
     */
    public ProjectFetches getProjectStatistics(Object projectIdOrPath) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "statistics");
        return (response.readEntity(ProjectFetches.class));
    }

    /**
     * Get an Optional instance with the value for the project fetch statistics for the last 30 days.
     *
     * <pre><code>GitLab Endpoint: GET /project/:id/statistics</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return an Optional instance with the value for the project fetch statistics for the last 30 day
     */
    public Optional<ProjectFetches> getOptionalProjectStatistics(Object projectIdOrPath) {
        try {
            return (Optional.ofNullable(getProjectStatistics(projectIdOrPath)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * <p>Get a list of projects accessible by the authenticated user.</p>
     *
     * <strong>WARNING:</strong> Do not use this method to fetch projects from https://gitlab.com,
     * gitlab.com has many 100,000's of public projects and it will take hours to fetch all of them.
     * Instead use {@link #getProjects(int itemsPerPage)} which will return a Pager of Project instances.
     *
     * <pre><code>GitLab Endpoint: GET /projects</code></pre>
     *
     * @return a list of projects accessible by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getProjects() throws GitLabApiException {

       String url = this.gitLabApi.getGitLabServerUrl();
       if (url.startsWith("https://gitlab.com")) {
           GitLabApi.getLogger().warning("Fetching all projects from " + url +
                   " may take many hours to complete, use Pager<Project> getProjects(int) instead.");
       }

       return (getProjects(getDefaultPerPage()).all());
    }

    /**
     * Get a list of projects accessible by the authenticated user and in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /projects</code></pre>
     *
     * @param page the page to get
     * @param perPage the number of projects per page
     * @return a list of projects accessible by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getProjects(int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "projects");
        return (response.readEntity(new GenericType<List<Project>>() { }));
    }

    /**
     * Get a Pager instance of projects accessible by the authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /projects</code></pre>
     *
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager instance of projects accessible by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Project> getProjects(int itemsPerPage) throws GitLabApiException {
        return (new Pager<Project>(this, Project.class, itemsPerPage, null, "projects"));
    }

    /**
     * Get a Stream of projects accessible by the authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /projects</code></pre>
     *
     * @return a Stream of projects accessible by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Project> getProjectsStream() throws GitLabApiException {
       return (getProjects(getDefaultPerPage()).stream());
    }

    /**
     * Get a list of projects accessible by the authenticated user and matching the supplied filter parameters.
     * All filter parameters are optional.
     *
     * <pre><code>GitLab Endpoint: GET /projects</code></pre>
     *
     * @param archived limit by archived status
     * @param visibility limit by visibility public, internal, or private
     * @param orderBy return projects ordered by id, name, path, created_at, updated_at, or last_activity_at fields, default is created_at
     * @param sort return projects sorted in asc or desc order. Default is desc
     * @param search return list of projects matching the search criteria
     * @param simple return only the ID, URL, name, and path of each project
     * @param owned limit by projects owned by the current user
     * @param membership limit by projects that the current user is a member of
     * @param starred limit by projects starred by the current user
     * @param statistics include project statistics
     * @return a list of projects accessible by the authenticated user and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     * @deprecated  Will be removed in version 6.0, replaced by {@link #getProjects(Boolean, Visibility,
     *      ProjectOrderBy, SortOrder, String, Boolean, Boolean, Boolean, Boolean, Boolean)}
     */
    @Deprecated
    public List<Project> getProjects(Boolean archived, Visibility visibility, String orderBy,
            String sort, String search, Boolean simple, Boolean owned, Boolean membership,
            Boolean starred, Boolean statistics) throws GitLabApiException {

        ProjectOrderBy projectOrderBy = ProjectOrderBy.valueOf(orderBy);
        SortOrder sortOrder = SortOrder.valueOf(sort);
        return (getProjects(archived, visibility, projectOrderBy, sortOrder, search, simple,
                owned, membership, starred, statistics, getDefaultPerPage()).all());
    }

    /**
     * Get a list of projects accessible by the authenticated user and matching the supplied filter parameters.
     * All filter parameters are optional.
     *
     * <pre><code>GitLab Endpoint: GET /projects</code></pre>
     *
     * @param archived limit by archived status
     * @param visibility limit by visibility public, internal, or private
     * @param orderBy return projects ordered by ID, NAME, PATH, CREATED_AT, UPDATED_AT, or
     *          LAST_ACTIVITY_AT fields, default is CREATED_AT
     * @param sort return projects sorted in asc or desc order. Default is desc
     * @param search return list of projects matching the search criteria
     * @param simple return only the ID, URL, name, and path of each project
     * @param owned limit by projects owned by the current user
     * @param membership limit by projects that the current user is a member of
     * @param starred limit by projects starred by the current user
     * @param statistics include project statistics
     * @return a list of projects accessible by the authenticated user and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getProjects(Boolean archived, Visibility visibility, ProjectOrderBy orderBy,
            SortOrder sort, String search, Boolean simple, Boolean owned, Boolean membership,
            Boolean starred, Boolean statistics) throws GitLabApiException {

        return (getProjects(archived, visibility, orderBy, sort, search, simple,
                owned, membership, starred, statistics, getDefaultPerPage()).all());
    }

    /**
     * Get a list of projects accessible by the authenticated user and matching the supplied filter parameters.
     * All filter parameters are optional.
     *
     * <pre><code>GitLab Endpoint: GET /projects</code></pre>
     *
     * @param archived limit by archived status
     * @param visibility limit by visibility public, internal, or private
     * @param orderBy return projects ordered by ID, NAME, PATH, CREATED_AT, UPDATED_AT, or
     *          LAST_ACTIVITY_AT fields, default is CREATED_AT
     * @param sort return projects sorted in asc or desc order. Default is desc
     * @param search return list of projects matching the search criteria
     * @param simple return only the ID, URL, name, and path of each project
     * @param owned limit by projects owned by the current user
     * @param membership limit by projects that the current user is a member of
     * @param starred limit by projects starred by the current user
     * @param statistics include project statistics
     * @param page the page to get
     * @param perPage the number of projects per page
     * @return a list of projects accessible by the authenticated user and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getProjects(Boolean archived, Visibility visibility, ProjectOrderBy orderBy,
            SortOrder sort, String search, Boolean simple, Boolean owned, Boolean membership,
            Boolean starred, Boolean statistics, int page, int perPage) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("archived", archived)
                .withParam("visibility", visibility)
                .withParam("order_by", orderBy)
                .withParam("sort", sort)
                .withParam("search", search)
                .withParam("simple", simple)
                .withParam("owned", owned)
                .withParam("membership", membership)
                .withParam("starred", starred)
                .withParam("statistics", statistics)
                .withParam(PAGE_PARAM,  page)
                .withParam(PER_PAGE_PARAM, perPage);

        Response response = get(Response.Status.OK, formData.asMap(), "projects");
        return (response.readEntity(new GenericType<List<Project>>() {}));
    }

    /**
     * Get a Pager of projects accessible by the authenticated user and matching the supplied filter parameters.
     * All filter parameters are optional.
     *
     * <pre><code>GitLab Endpoint: GET /projects</code></pre>
     *
     * @param archived limit by archived status
     * @param visibility limit by visibility public, internal, or private
     * @param orderBy return projects ordered by ID, NAME, PATH, CREATED_AT, UPDATED_AT, or
     *          LAST_ACTIVITY_AT fields, default is CREATED_AT
     * @param sort return projects sorted in asc or desc order. Default is desc
     * @param search return list of projects matching the search criteria
     * @param simple return only the ID, URL, name, and path of each project
     * @param owned limit by projects owned by the current user
     * @param membership limit by projects that the current user is a member of
     * @param starred limit by projects starred by the current user
     * @param statistics include project statistics
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of projects accessible by the authenticated user and matching the supplied parameters
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Project> getProjects(Boolean archived, Visibility visibility, ProjectOrderBy orderBy,
            SortOrder sort, String search, Boolean simple, Boolean owned, Boolean membership,
            Boolean starred, Boolean statistics, int itemsPerPage) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("archived", archived)
                .withParam("visibility", visibility)
                .withParam("order_by", orderBy)
                .withParam("sort", sort)
                .withParam("search", search)
                .withParam("simple", simple)
                .withParam("owned", owned)
                .withParam("membership", membership)
                .withParam("starred", starred)
                .withParam("statistics", statistics);

        return (new Pager<Project>(this, Project.class, itemsPerPage, formData.asMap(), "projects"));
    }

    /**
     * Get a list of projects accessible by the authenticated user that match the provided search string.
     *
     * <pre><code>GitLab Endpoint: GET /projects?search=search</code></pre>
     *
     * @param search the project name search criteria
     * @return a list of projects accessible by the authenticated user that match the provided search string
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getProjects(String search) throws GitLabApiException {
        return (getProjects(search, getDefaultPerPage()).all());
    }

    /**
     * Get a list of projects accessible by the authenticated user that match the provided search string.
     *
     * <pre><code>GitLab Endpoint: GET /projects?search=search</code></pre>
     *
     * @param search the project name search criteria
     * @param page the page to get
     * @param perPage the number of projects per page
     * @return a list of projects accessible by the authenticated user that match the provided search string
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getProjects(String search, int page, int perPage) throws GitLabApiException {
        Form formData = new GitLabApiForm().withParam("search", search).withParam(PAGE_PARAM,  page).withParam(PER_PAGE_PARAM, perPage);
        Response response = get(Response.Status.OK, formData.asMap(), "projects");
        return (response.readEntity(new GenericType<List<Project>>() {}));
    }

    /**
     * Get a Pager of projects accessible by the authenticated user that match the provided search string.
     *
     * <pre><code>GitLab Endpoint: GET /projects?search=search</code></pre>
     *
     * @param search the project name search criteria
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of projects accessible by the authenticated user that match the provided search string
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Project> getProjects(String search, int itemsPerPage) throws GitLabApiException {
        Form formData = new GitLabApiForm().withParam("search", search);
        return (new Pager<Project>(this, Project.class, itemsPerPage, formData.asMap(), "projects"));
    }

    /**
     * Get a Stream of projects accessible by the authenticated user that match the provided search string.
     *
     * <pre><code>GitLab Endpoint: GET /projects?search=search</code></pre>
     *
     * @param search the project name search criteria
     * @return a Stream of projects accessible by the authenticated user that match the provided search string
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Project> getProjectsStream(String search) throws GitLabApiException {
        return (getProjects(search, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of projects that the authenticated user is a member of.
     *
     * <pre><code>GitLab Endpoint: GET /projects?membership=true</code></pre>
     *
     * @return a list of projects that the authenticated user is a member of
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getMemberProjects() throws GitLabApiException {
        return (getMemberProjects(getDefaultPerPage()).all());
    }

    /**
     * Get a list of projects that the authenticated user is a member of in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /projects?membership=true</code></pre>
     *
     * @param page the page to get
     * @param perPage the number of projects per page
     * @return a list of projects that the authenticated user is a member of
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getMemberProjects(int page, int perPage) throws GitLabApiException {
        Form formData = new GitLabApiForm().withParam("membership", true).withParam(PAGE_PARAM,  page).withParam(PER_PAGE_PARAM, perPage);
        Response response = get(Response.Status.OK, formData.asMap(), "projects");
        return (response.readEntity(new GenericType<List<Project>>() {}));
    }

    /**
     * Get a Pager of projects that the authenticated user is a member of.
     *
     * <pre><code>GitLab Endpoint: GET /projects?membership=true</code></pre>
     *
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager o Project instances that the authenticated user is a member of
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Project> getMemberProjects(int itemsPerPage) throws GitLabApiException {
        Form formData = new GitLabApiForm().withParam("membership", true);
        return (new Pager<Project>(this, Project.class, itemsPerPage, formData.asMap(), "projects"));
    }

    /**
     * Get a Stream of projects that the authenticated user is a member of.
     *
     * <pre><code>GitLab Endpoint: GET /projects?membership=true</code></pre>
     *
     * @return a list of projects that the authenticated user is a member of
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Project> getMemberProjectsStream() throws GitLabApiException {
        return (getMemberProjects(getDefaultPerPage()).stream());
    }

    /**
     * Get a list of projects owned by the authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /projects?owned=true</code></pre>
     *
     * @return a list of projects owned by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getOwnedProjects() throws GitLabApiException {
        return (getOwnedProjects(getDefaultPerPage()).all());
    }

    /**
     * Get a list of projects owned by the authenticated user in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /projects?owned=true</code></pre>
     *
     * @param page the page to get
     * @param perPage the number of projects per page
     * @return a list of projects owned by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getOwnedProjects(int page, int perPage) throws GitLabApiException {
        Form formData = new GitLabApiForm().withParam("owned", true).withParam(PAGE_PARAM,  page).withParam(PER_PAGE_PARAM, perPage);
        Response response = get(Response.Status.OK, formData.asMap(), "projects");
        return (response.readEntity(new GenericType<List<Project>>() {}));
    }

    /**
     * Get a Pager of projects owned by the authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /projects?owned=true</code></pre>
     *
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a list of projects owned by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Project> getOwnedProjects(int itemsPerPage) throws GitLabApiException {
        Form formData = new GitLabApiForm().withParam("owned", true);
        return (new Pager<Project>(this, Project.class, itemsPerPage, formData.asMap(), "projects"));
    }

    /**
     * Get a Stream of projects owned by the authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /projects?owned=true</code></pre>
     *
     * @return a Stream of projects owned by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Project> getOwnedProjectsStream() throws GitLabApiException {
        return (getOwnedProjects(getDefaultPerPage()).stream());
    }

    /**
     * Get a list of projects starred by the authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /projects?starred=true</code></pre>
     *
     * @return a list of projects starred by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getStarredProjects() throws GitLabApiException {
        return (getStarredProjects(getDefaultPerPage()).all());
    }

    /**
     * Get a list of projects starred by the authenticated user in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /projects?starred=true</code></pre>
     *
     * @param page the page to get
     * @param perPage the number of projects per page
     * @return a list of projects starred by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getStarredProjects(int page, int perPage) throws GitLabApiException {
        Form formData = new GitLabApiForm().withParam("starred", true).withParam(PAGE_PARAM, page).withParam(PER_PAGE_PARAM, perPage);
        Response response = get(Response.Status.OK, formData.asMap(), "projects");
        return (response.readEntity(new GenericType<List<Project>>() {}));
    }

    /**
     * Get a Pager of projects starred by the authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /projects?starred=true</code></pre>
     *
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of projects starred by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Project> getStarredProjects(int itemsPerPage) throws GitLabApiException {
        Form formData = new GitLabApiForm().withParam("starred", true).withParam(PER_PAGE_PARAM, getDefaultPerPage());
        return (new Pager<Project>(this, Project.class, itemsPerPage, formData.asMap(), "projects"));
    }

    /**
     * Get a Stream of projects starred by the authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /projects?starred=true</code></pre>
     *
     * @return a Stream of projects starred by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Project> getStarredProjectsStream() throws GitLabApiException {
        return (getStarredProjects(getDefaultPerPage()).stream());
    }

    /**
     * Get a list of all visible projects across GitLab for the authenticated user using the provided filter.
     *
     * <pre><code>GitLab Endpoint: GET /projects</code></pre>
     *
     * @param filter the ProjectFilter instance holding the filter values for the query
     * @return a list of all visible projects across GitLab for the authenticated use
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getProjects(ProjectFilter filter) throws GitLabApiException {
        return (getProjects(filter, getDefaultPerPage()).all());
    }

    /**
     * Get a list of all visible projects across GitLab for the authenticated user in the specified page range
     * using the provided filter.
     *
     * <pre><code>GitLab Endpoint: GET /projects</code></pre>
     *
     * @param filter the ProjectFilter instance holding the filter values for the query
     * @param page the page to get
     * @param perPage the number of projects per page
     * @return a list of all visible projects across GitLab for the authenticated use
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getProjects(ProjectFilter filter, int page, int perPage) throws GitLabApiException {
        GitLabApiForm formData = filter.getQueryParams(page, perPage);
        Response response = get(Response.Status.OK, formData.asMap(), "projects");
        return (response.readEntity(new GenericType<List<Project>>() {}));
    }

    /**
     * Get a Pager of all visible projects across GitLab for the authenticated user using the provided filter.
     *
     * <pre><code>GitLab Endpoint: GET /projects</code></pre>
     *
     * @param filter the ProjectFilter instance holding the filter values for the query
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of all visible projects across GitLab for the authenticated use
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Project> getProjects(ProjectFilter filter, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = filter.getQueryParams();
        return (new Pager<Project>(this, Project.class, itemsPerPage, formData.asMap(), "projects"));
    }

    /**
     * Get a Stream of all visible projects across GitLab for the authenticated user using the provided filter.
     *
     * <pre><code>GitLab Endpoint: GET /projects</code></pre>
     *
     * @param filter the ProjectFilter instance holding the filter values for the query
     * @return a Stream of all visible projects across GitLab for the authenticated use
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Project> getProjectsStream(ProjectFilter filter) throws GitLabApiException {
        return (getProjects(filter, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of visible projects owned by the given user.
     *
     * <pre><code>GitLab Endpoint: GET /users/:user_id/projects</code></pre>
     *
     * @param userIdOrUsername the user ID, username of the user, or a User instance holding the user ID or username
     * @param filter the ProjectFilter instance holding the filter values for the query
     * @return a list of visible projects owned by the given user
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getUserProjects(Object userIdOrUsername, ProjectFilter filter) throws GitLabApiException {
        return (getUserProjects(userIdOrUsername, filter, getDefaultPerPage()).all());
    }

    /**
     * Get a list of visible projects owned by the given user in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /users/:user_id/projects</code></pre>
     *
     * @param userIdOrUsername the user ID, username of the user, or a User instance holding the user ID or username
     * @param filter the ProjectFilter instance holding the filter values for the query
     * @param page the page to get
     * @param perPage the number of projects per page
     * @return a list of visible projects owned by the given user
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getUserProjects(Object userIdOrUsername, ProjectFilter filter, int page, int perPage) throws GitLabApiException {
        GitLabApiForm formData = filter.getQueryParams(page, perPage);
        Response response = get(Response.Status.OK, formData.asMap(),
                "users", getUserIdOrUsername(userIdOrUsername), "projects");
        return (response.readEntity(new GenericType<List<Project>>() {}));
    }

    /**
     * Get a Pager of visible projects owned by the given user.
     *
     * <pre><code>GitLab Endpoint: GET /users/:user_id/projects</code></pre>
     *
     * @param userIdOrUsername the user ID, username of the user, or a User instance holding the user ID or username
     * @param filter the ProjectFilter instance holding the filter values for the query
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of visible projects owned by the given user
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Project> getUserProjects(Object userIdOrUsername, ProjectFilter filter, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = filter.getQueryParams();
        return (new Pager<Project>(this, Project.class, itemsPerPage, formData.asMap(),
                "users", getUserIdOrUsername(userIdOrUsername), "projects"));
    }

    /**
     * Get a Stream of visible projects owned by the given user.
     *
     * <pre><code>GitLab Endpoint: GET /users/:user_id/projects</code></pre>
     *
     * @param userIdOrUsername the user ID, username of the user, or a User instance holding the user ID or username
     * @param filter the ProjectFilter instance holding the filter values for the query
     * @return a Stream of visible projects owned by the given user
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Project> getUserProjectsStream(Object userIdOrUsername, ProjectFilter filter) throws GitLabApiException {
        return (getUserProjects(userIdOrUsername, filter, getDefaultPerPage()).stream());
    }

    /**
     * Get a specific project, which is owned by the authentication user.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Project getProject(Object projectIdOrPath) throws GitLabApiException {
	return (getProject(projectIdOrPath, null, null, null));
    }

    /**
     * Get an Optional instance with the value for the specific project, which is owned by the authentication user.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return an Optional instance with the specified project as a value
     */
    public Optional<Project> getOptionalProject(Object projectIdOrPath) {
        try {
            return (Optional.ofNullable(getProject(projectIdOrPath, null, null, null)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Get a specific project, which is owned by the authentication user.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param includeStatistics include project statistics
     * @return the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Project getProject(Object projectIdOrPath, Boolean includeStatistics) throws GitLabApiException {
	return (getProject(projectIdOrPath, includeStatistics, null, null));
    }

    /**
     * Get an Optional instance with the value for the specific project, which is owned by the authentication user.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param includeStatistics include project statistics
     * @return an Optional instance with the specified project as a value
     */
    public Optional<Project> getOptionalProject(Object projectIdOrPath, Boolean includeStatistics) {
        try {
            return (Optional.ofNullable(getProject(projectIdOrPath, includeStatistics, null, null)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Get a specific project, which is owned by the authentication user.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param includeStatistics include project statistics
     * @param includeLicense include project license data
     * @param withCustomAttributes include custom attributes in response (admins only)
     * @return the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Project getProject(Object projectIdOrPath, Boolean includeStatistics,
	    Boolean includeLicense, Boolean withCustomAttributes) throws GitLabApiException {
        Form formData = new GitLabApiForm()
        	.withParam("statistics", includeStatistics)
        	.withParam("license", includeLicense)
        	.withParam("with_custom_attributes", withCustomAttributes);
        Response response = get(Response.Status.OK, formData.asMap(),
        	"projects", getProjectIdOrPath(projectIdOrPath));
        return (response.readEntity(Project.class));
    }

    /**
     * Get an Optional instance with the value for the specific project, which is owned by the authentication user.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param includeStatistics include project statistics
     * @param includeLicense include project license data
     * @param withCustomAttributes include custom attributes in response (admins only)
     * @return an Optional instance with the specified project as a value
     */
    public Optional<Project> getOptionalProject(Object projectIdOrPath, Boolean includeStatistics,
	    Boolean includeLicense, Boolean withCustomAttributes) {
        try {
            return (Optional.ofNullable(getProject(projectIdOrPath,
        	    includeStatistics, includeLicense, withCustomAttributes)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Get a specific project, which is owned by the authentication user.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id</code></pre>
     *
     * @param namespace the name of the project namespace or group
     * @param project the name of the project to get
     * @return the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Project getProject(String namespace, String project) throws GitLabApiException {

        if (namespace == null) {
            throw new RuntimeException("namespace cannot be null");
        }

        if (project == null) {
            throw new RuntimeException("project cannot be null");
        }

        String projectPath = null;
        try {
            projectPath = URLEncoder.encode(namespace + "/" + project, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            throw (new GitLabApiException(uee));
        }

        Response response = get(Response.Status.OK, null, "projects", projectPath);
        return (response.readEntity(Project.class));
    }

    /**
     * Get an Optional instance with the value for the specific project, which is owned by the authentication user.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id</code></pre>
     *
     * @param namespace the name of the project namespace or group
     * @param project the name of the project
     * @return an Optional instance with the specified project as a value
     */
    public Optional<Project> getOptionalProject(String namespace, String project) {
        try {
            return (Optional.ofNullable(getProject(namespace, project)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Get a specific project, which is owned by the authentication user.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id</code></pre>
     *
     * @param namespace the name of the project namespace or group
     * @param project the name of the project to get
     * @param includeStatistics include project statistics
     * @return the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Project getProject(String namespace, String project, Boolean includeStatistics) throws GitLabApiException {

        if (namespace == null) {
            throw new RuntimeException("namespace cannot be null");
        }

        if (project == null) {
            throw new RuntimeException("project cannot be null");
        }

        String projectPath = null;
        try {
            projectPath = URLEncoder.encode(namespace + "/" + project, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            throw (new GitLabApiException(uee));
        }

        Form formData = new GitLabApiForm().withParam("statistics", includeStatistics);
        Response response = get(Response.Status.OK, formData.asMap(), "projects", projectPath);
        return (response.readEntity(Project.class));
    }

    /**
     * Get an Optional instance with the value for the specific project, which is owned by the authentication user.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id</code></pre>
     *
     * @param namespace the name of the project namespace or group
     * @param project the name of the project
     * @param includeStatistics include project statistics
     * @return an Optional instance with the specified project as a value
     */
    public Optional<Project> getOptionalProject(String namespace, String project, Boolean includeStatistics) {
        try {
            return (Optional.ofNullable(getProject(namespace, project, includeStatistics)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Create a new project belonging to the namespace ID.  A namespace ID is either a user or group ID.
     *
     * @param namespaceId the namespace ID to create the project under
     * @param projectName the name of the project top create
     * @return the created project
     * @throws GitLabApiException if any exception occurs
     */
    public Project createProject(Long namespaceId, String projectName) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("namespace_id", namespaceId).withParam("name", projectName, true);
        Response response = post(Response.Status.CREATED, formData, "projects");
        return (response.readEntity(Project.class));
    }

    /**
     * Create a new project belonging to the namespace ID and project configuration.  A namespace ID is either a user or group ID.
     *
     * @param namespaceId the namespace ID to create the project under
     * @param project the Project instance holding the new project configuration
     * @return the created project
     * @throws GitLabApiException if any exception occurs
     */
    public Project createProject(Long namespaceId, Project project) throws GitLabApiException {

        if (project == null) {
            throw new RuntimeException("Project instance cannot be null.");
        }

        Namespace namespace = new Namespace().withId(namespaceId);
        project.setNamespace(namespace);
        return (createProject(project));
    }

    /**
     * Create a new project with the current user's namespace.
     *
     * @param projectName the name of the project top create
     * @return the created project
     * @throws GitLabApiException if any exception occurs
     */
    public Project createProject(String projectName) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("name", projectName, true);
        Response response = post(Response.Status.CREATED, formData, "projects");
        return (response.readEntity(Project.class));
    }

    /**
     * Creates a new project owned by the authenticated user.
     *
     * @param name the name of the project top create.  Equals path if not provided.
     * @param path repository name for new project. Generated based on name if not provided (generated lowercased with dashes).
     * @return the created project
     * @throws GitLabApiException if any exception occurs
     */
    public Project createProject(String name, String path) throws GitLabApiException {

	if ((name == null || name.trim().isEmpty()) && (path == null || path.trim().isEmpty())) {
	    throw new RuntimeException("Either name or path must be specified.");
	}

        GitLabApiForm formData = new GitLabApiForm().withParam("name", name).withParam("path", path);
        Response response = post(Response.Status.CREATED, formData, "projects");
        return (response.readEntity(Project.class));
    }

    /**
     * Creates new project owned by the current user.
     *
     * @param project the Project instance with the configuration for the new project
     * @return a Project instance with the newly created project info
     * @throws GitLabApiException if any exception occurs
     */
    public Project createProject(Project project) throws GitLabApiException {
        return (createProject(project, null));
    }

    /**
     * Creates new project owned by the current user. The following properties on the Project instance
     * are utilized in the creation of the project:
     *
     * name (name or path are required) - new project name
     * path (name or path are required) - new project path
     * defaultBranch (optional) - master by default
     * description (optional) - short project description
     * visibility (optional) - Limit by visibility public, internal, or private
     * visibilityLevel (optional)
     * issuesEnabled (optional) - Enable issues for this project
     * mergeMethod (optional) - Set the merge method used
     * mergeRequestsEnabled (optional) - Enable merge requests for this project
     * wikiEnabled (optional) - Enable wiki for this project
     * snippetsEnabled (optional) - Enable snippets for this project
     * jobsEnabled (optional) - Enable jobs for this project
     * containerRegistryEnabled (optional) - Enable container registry for this project
     * sharedRunnersEnabled (optional) - Enable shared runners for this project
     * publicJobs (optional) - If true, jobs can be viewed by non-project-members
     * onlyAllowMergeIfPipelineSucceeds (optional) - Set whether merge requests can only be merged with successful jobs
     * onlyAllowMergeIfAllDiscussionsAreResolved (optional) - Set whether merge requests can only be merged when all the discussions are resolved
     * lfsEnabled (optional) - Enable LFS
     * requestAccessEnabled (optional) - Allow users to request member access
     * repositoryStorage (optional) - Which storage shard the repository is on. Available only to admins
     * approvalsBeforeMerge (optional) - How many approvers should approve merge request by default
     * printingMergeRequestLinkEnabled (optional) - Show link to create/view merge request when pushing from the command line
     * resolveOutdatedDiffDiscussions (optional) - Automatically resolve merge request diffs discussions on lines changed with a push
     * initialize_with_readme (optional) - Initialize project with README file
     * packagesEnabled (optional) - Enable or disable mvn packages repository feature
     * buildGitStrategy (optional) - set the build git strategy
     * buildCoverageRegex (optional) - set build coverage regex
     * ciConfigPath (optional) - Set path to CI configuration file
     * squashOption (optional) - set squash option for merge requests
     *
     * @param project the Project instance with the configuration for the new project
     * @param importUrl the URL to import the repository from
     * @return a Project instance with the newly created project info
     * @throws GitLabApiException if any exception occurs
     */
    public Project createProject(Project project, String importUrl) throws GitLabApiException {

        if (project == null) {
            return (null);
        }

        String name = project.getName();
        String path = project.getPath();

        if ((name == null || name.trim().length() == 0) && (path == null || path.trim().length() == 0)) {
            return (null);
        }

        GitLabApiForm formData = new GitLabApiForm()
            .withParam("name", name)
            .withParam("path", path)
            .withParam("default_branch", project.getDefaultBranch())
            .withParam("description", project.getDescription())
            .withParam("issues_enabled", project.getIssuesEnabled())
            .withParam("merge_method",  project.getMergeMethod())
            .withParam("merge_requests_enabled", project.getMergeRequestsEnabled())
            .withParam("jobs_enabled", project.getJobsEnabled())
            .withParam("wiki_enabled", project.getWikiEnabled())
            .withParam("container_registry_enabled", project.getContainerRegistryEnabled())
            .withParam("snippets_enabled", project.getSnippetsEnabled())
            .withParam("shared_runners_enabled", project.getSharedRunnersEnabled())
            .withParam("public_jobs", project.getPublicJobs())
            .withParam("visibility_level", project.getVisibilityLevel())
            .withParam("only_allow_merge_if_pipeline_succeeds", project.getOnlyAllowMergeIfPipelineSucceeds())
            .withParam("only_allow_merge_if_all_discussions_are_resolved", project.getOnlyAllowMergeIfAllDiscussionsAreResolved())
            .withParam("lfs_enabled", project.getLfsEnabled())
            .withParam("request_access_enabled", project.getRequestAccessEnabled())
            .withParam("repository_storage", project.getRepositoryStorage())
            .withParam("approvals_before_merge", project.getApprovalsBeforeMerge())
            .withParam("import_url", importUrl)
            .withParam("printing_merge_request_link_enabled", project.getPrintingMergeRequestLinkEnabled())
            .withParam("resolve_outdated_diff_discussions", project.getResolveOutdatedDiffDiscussions())
            .withParam("initialize_with_readme", project.getInitializeWithReadme())
            .withParam("packages_enabled", project.getPackagesEnabled())
            .withParam("build_git_strategy", project.getBuildGitStrategy())
            .withParam("build_coverage_regex", project.getBuildCoverageRegex())
            .withParam("ci_config_path", project.getCiConfigPath())
            .withParam("suggestion_commit_message", project.getSuggestionCommitMessage())
            .withParam("remove_source_branch_after_merge", project.getRemoveSourceBranchAfterMerge())
            .withParam("squash_option", project.getSquashOption());

        Namespace namespace = project.getNamespace();
        if (namespace != null && namespace.getId() != null) {
            formData.withParam("namespace_id", namespace.getId());
        }

        if (isApiVersion(ApiVersion.V3)) {
            boolean isPublic = (project.getPublic() != null ? project.getPublic() : project.getVisibility() == Visibility.PUBLIC);
            formData.withParam("public", isPublic);

            if (project.getTagList() != null && !project.getTagList().isEmpty()) {
                throw new IllegalArgumentException("GitLab API v3 does not support tag lists when creating projects");
            }
        } else {
            Visibility visibility = (project.getVisibility() != null ? project.getVisibility() :
                project.getPublic() == Boolean.TRUE ? Visibility.PUBLIC : null);
            formData.withParam("visibility", visibility);

            if (project.getTagList() != null && !project.getTagList().isEmpty()) {
                formData.withParam("tag_list", String.join(",", project.getTagList()));
            }
        }

        Response response = post(Response.Status.CREATED, formData, "projects");
        return (response.readEntity(Project.class));
    }

    /**
     * Creates a Project
     *
     * @param name The name of the project
     * @param namespaceId The Namespace for the new project, otherwise null indicates to use the GitLab default (user)
     * @param description A description for the project, null otherwise
     * @param issuesEnabled Whether Issues should be enabled, otherwise null indicates to use GitLab default
     * @param mergeRequestsEnabled Whether Merge Requests should be enabled, otherwise null indicates to use GitLab default
     * @param wikiEnabled Whether a Wiki should be enabled, otherwise null indicates to use GitLab default
     * @param snippetsEnabled Whether Snippets should be enabled, otherwise null indicates to use GitLab default
     * @param visibility The visibility of the project, otherwise null indicates to use GitLab default
     * @param visibilityLevel The visibility level of the project, otherwise null indicates to use GitLab default
     * @param importUrl The Import URL for the project, otherwise null
     * @return the GitLab Project
     * @throws GitLabApiException if any exception occurs
     */
    public Project createProject(String name, Long namespaceId, String description, Boolean issuesEnabled, Boolean mergeRequestsEnabled,
            Boolean wikiEnabled, Boolean snippetsEnabled, Visibility visibility, Integer visibilityLevel, String importUrl) throws GitLabApiException {

        if (isApiVersion(ApiVersion.V3)) {
            Boolean isPublic = Visibility.PUBLIC == visibility;
            return (createProject(name, namespaceId, description, issuesEnabled, mergeRequestsEnabled,
                    wikiEnabled, snippetsEnabled, isPublic, visibilityLevel, importUrl));
        }

        if (name == null || name.trim().length() == 0) {
            return (null);
        }

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("name", name, true)
                .withParam("namespace_id", namespaceId)
                .withParam("description", description)
                .withParam("issues_enabled", issuesEnabled)
                .withParam("merge_requests_enabled", mergeRequestsEnabled)
                .withParam("wiki_enabled", wikiEnabled)
                .withParam("snippets_enabled", snippetsEnabled)
                .withParam("visibility_level", visibilityLevel)
                .withParam("visibility", visibility)
                .withParam("import_url", importUrl);

        Response response = post(Response.Status.CREATED, formData, "projects");
        return (response.readEntity(Project.class));
    }

    /**
     * Creates a Project
     *
     * @param name The name of the project
     * @param namespaceId The Namespace for the new project, otherwise null indicates to use the GitLab default (user)
     * @param description A description for the project, null otherwise
     * @param issuesEnabled Whether Issues should be enabled, otherwise null indicates to use GitLab default
     * @param mergeRequestsEnabled Whether Merge Requests should be enabled, otherwise null indicates to use GitLab default
     * @param wikiEnabled Whether a Wiki should be enabled, otherwise null indicates to use GitLab default
     * @param snippetsEnabled Whether Snippets should be enabled, otherwise null indicates to use GitLab default
     * @param visibility The visibility of the project, otherwise null indicates to use GitLab default
     * @param visibilityLevel The visibility level of the project, otherwise null indicates to use GitLab default
     * @param printingMergeRequestLinkEnabled Show link to create/view merge request when pushing from the command line
     * @param importUrl The Import URL for the project, otherwise null
     * @return the GitLab Project
     * @throws GitLabApiException if any exception occurs
     */
    public Project createProject(String name, Long namespaceId, String description, Boolean issuesEnabled, Boolean mergeRequestsEnabled,
            Boolean wikiEnabled, Boolean snippetsEnabled, Visibility visibility, Integer visibilityLevel,
            Boolean printingMergeRequestLinkEnabled, String importUrl) throws GitLabApiException {

        if (isApiVersion(ApiVersion.V3)) {
            Boolean isPublic = Visibility.PUBLIC == visibility;
            return (createProject(name, namespaceId, description, issuesEnabled, mergeRequestsEnabled,
                    wikiEnabled, snippetsEnabled, isPublic, visibilityLevel, importUrl));
        }

        if (name == null || name.trim().length() == 0) {
            return (null);
        }

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("name", name, true)
                .withParam("namespace_id", namespaceId)
                .withParam("description", description)
                .withParam("issues_enabled", issuesEnabled)
                .withParam("merge_requests_enabled", mergeRequestsEnabled)
                .withParam("wiki_enabled", wikiEnabled)
                .withParam("snippets_enabled", snippetsEnabled)
                .withParam("visibility_level", visibilityLevel)
                .withParam("visibility", visibility)
                .withParam("printing_merge_request_link_enabled", printingMergeRequestLinkEnabled)
                .withParam("import_url", importUrl);

        Response response = post(Response.Status.CREATED, formData, "projects");
        return (response.readEntity(Project.class));
    }

    /**
     * Creates a Project
     *
     * @param name The name of the project
     * @param namespaceId The Namespace for the new project, otherwise null indicates to use the GitLab default (user)
     * @param description A description for the project, null otherwise
     * @param issuesEnabled Whether Issues should be enabled, otherwise null indicates to use GitLab default
     * @param mergeRequestsEnabled Whether Merge Requests should be enabled, otherwise null indicates to use GitLab default
     * @param wikiEnabled Whether a Wiki should be enabled, otherwise null indicates to use GitLab default
     * @param snippetsEnabled Whether Snippets should be enabled, otherwise null indicates to use GitLab default
     * @param isPublic Whether the project is public or private, if true same as setting visibilityLevel = 20, otherwise null indicates to use GitLab default
     * @param visibilityLevel The visibility level of the project, otherwise null indicates to use GitLab default
     * @param importUrl The Import URL for the project, otherwise null
     * @return the GitLab Project
     * @throws GitLabApiException if any exception occurs
     * @deprecated As of release 4.2.0, replaced by {@link #createProject(String, Long, String, Boolean, Boolean,
     *      Boolean, Boolean, Visibility, Integer, String)}
     */
    @Deprecated
    public Project createProject(String name, Long namespaceId, String description, Boolean issuesEnabled, Boolean mergeRequestsEnabled,
            Boolean wikiEnabled, Boolean snippetsEnabled, Boolean isPublic, Integer visibilityLevel, String importUrl) throws GitLabApiException {

        if (name == null || name.trim().length() == 0) {
            return (null);
        }

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("name", name, true)
                .withParam("namespace_id", namespaceId)
                .withParam("description", description)
                .withParam("issues_enabled", issuesEnabled)
                .withParam("merge_requests_enabled", mergeRequestsEnabled)
                .withParam("wiki_enabled", wikiEnabled)
                .withParam("snippets_enabled", snippetsEnabled)
                .withParam("visibility_level", visibilityLevel)
                .withParam("import_url", importUrl);

        if (isApiVersion(ApiVersion.V3)) {
            formData.withParam("public", isPublic);
        } else if (isPublic) {
            formData.withParam("visibility", Visibility.PUBLIC);
        }

        Response response = post(Response.Status.CREATED, formData, "projects");
        return (response.readEntity(Project.class));
    }

    /**
     * Create a new project from a template, belonging to the namespace ID.  A namespace ID is either a user or group ID.
     *
     * @param namespaceId the namespace ID to create the project under
     * @param projectName the name of the project top create
     * @param groupWithProjectTemplatesId Id of the Gitlab Group, which contains the relevant templates.
     * @param templateName name of the template to use
     * @param visibility Visibility of the new create project
     * @return the created project
     * @throws GitLabApiException if any exception occurs
     */
    public Project createProjectFromTemplate(Integer namespaceId, String projectName, Integer groupWithProjectTemplatesId, String templateName, Visibility visibility) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
            .withParam("namespace_id", namespaceId)
            .withParam("name", projectName, true)
            .withParam("use_custom_template", true)
            .withParam("group_with_project_templates_id", groupWithProjectTemplatesId, true)
            .withParam("template_name", templateName, true)
            .withParam("visibility", visibility)
            ;
        Response response = post(Response.Status.CREATED, formData, "projects");
        return (response.readEntity(Project.class));
    }

    /**
     * Updates a project. The following properties on the Project instance
     * are utilized in the edit of the project, null values are not updated:
     *
     * id (required) - existing project id, either id or path must be provided
     * name (optional) - project name
     * path (optional) - project path, either id or path must be provided
     * defaultBranch (optional) - master by default
     * description (optional) - short project description
     * visibility (optional) - Limit by visibility public, internal, or private
     * issuesEnabled (optional) - Enable issues for this project
     * mergeMethod (optional) - Set the merge method used
     * mergeRequestsEnabled (optional) - Enable merge requests for this project
     * wikiEnabled (optional) - Enable wiki for this project
     * snippetsEnabled (optional) - Enable snippets for this project
     * jobsEnabled (optional) - Enable jobs for this project
     * containerRegistryEnabled (optional) - Enable container registry for this project
     * sharedRunnersEnabled (optional) - Enable shared runners for this project
     * publicJobs (optional) - If true, jobs can be viewed by non-project-members
     * onlyAllowMergeIfPipelineSucceeds (optional) - Set whether merge requests can only be merged with successful jobs
     * onlyAllowMergeIfAllDiscussionsAreResolved (optional) - Set whether merge requests can only be merged when all the discussions are resolved
     * lfsEnabled (optional) - Enable LFS
     * requestAccessEnabled (optional) - Allow users to request member access
     * repositoryStorage (optional) - Which storage shard the repository is on. Available only to admins
     * approvalsBeforeMerge (optional) - How many approvers should approve merge request by default
     * printingMergeRequestLinkEnabled (optional) - Show link to create/view merge request when pushing from the command line
     * resolveOutdatedDiffDiscussions (optional) - Automatically resolve merge request diffs discussions on lines changed with a push
     * packagesEnabled (optional) - Enable or disable mvn packages repository feature
     * buildGitStrategy (optional) - set the build git strategy
     * buildCoverageRegex (optional) - set build coverage regex
     * ciConfigPath (optional) - Set path to CI configuration file
     * ciForwardDeploymentEnabled (optional) - When a new deployment job starts, skip older deployment jobs that are still pending
     * squashOption (optional) - set squash option for merge requests
     *
     * NOTE: The following parameters specified by the GitLab API edit project are not supported:
     *     import_url
     *     tag_list array
     *     avatar
     *     initialize_with_readme
     *
     * @param project the Project instance with the configuration for the new project
     * @return a Project instance with the newly updated project info
     * @throws GitLabApiException if any exception occurs
     */
    public Project updateProject(Project project) throws GitLabApiException {

        if (project == null) {
            throw new RuntimeException("Project instance cannot be null.");
        }

        // This will throw an exception if both id and path are not present
        Object projectIdentifier = getProjectIdOrPath(project);

        GitLabApiForm formData = new GitLabApiForm()
            .withParam("name", project.getName())
            .withParam("path", project.getPath())
            .withParam("default_branch", project.getDefaultBranch())
            .withParam("description", project.getDescription())
            .withParam("issues_enabled", project.getIssuesEnabled())
            .withParam("merge_method",  project.getMergeMethod())
            .withParam("merge_requests_enabled", project.getMergeRequestsEnabled())
            .withParam("jobs_enabled", project.getJobsEnabled())
            .withParam("wiki_enabled", project.getWikiEnabled())
            .withParam("snippets_enabled", project.getSnippetsEnabled())
            .withParam("container_registry_enabled", project.getContainerRegistryEnabled())
            .withParam("shared_runners_enabled", project.getSharedRunnersEnabled())
            .withParam("public_jobs", project.getPublicJobs())
            .withParam("only_allow_merge_if_pipeline_succeeds", project.getOnlyAllowMergeIfPipelineSucceeds())
            .withParam("only_allow_merge_if_all_discussions_are_resolved", project.getOnlyAllowMergeIfAllDiscussionsAreResolved())
            .withParam("lfs_enabled", project.getLfsEnabled())
            .withParam("request_access_enabled", project.getRequestAccessEnabled())
            .withParam("repository_storage", project.getRepositoryStorage())
            .withParam("approvals_before_merge", project.getApprovalsBeforeMerge())
            .withParam("printing_merge_request_link_enabled", project.getPrintingMergeRequestLinkEnabled())
            .withParam("resolve_outdated_diff_discussions", project.getResolveOutdatedDiffDiscussions())
            .withParam("packages_enabled", project.getPackagesEnabled())
            .withParam("build_git_strategy", project.getBuildGitStrategy())
            .withParam("build_coverage_regex", project.getBuildCoverageRegex())
            .withParam("ci_config_path", project.getCiConfigPath())
            .withParam("ci_forward_deployment_enabled", project.getCiForwardDeploymentEnabled())
            .withParam("merge_method", project.getMergeMethod())
            .withParam("suggestion_commit_message", project.getSuggestionCommitMessage())
            .withParam("remove_source_branch_after_merge", project.getRemoveSourceBranchAfterMerge())
            .withParam("squash_option", project.getSquashOption());

        if (isApiVersion(ApiVersion.V3)) {
            formData.withParam("visibility_level", project.getVisibilityLevel());
            boolean isPublic = (project.getPublic() != null ? project.getPublic() : project.getVisibility() == Visibility.PUBLIC);
            formData.withParam("public", isPublic);

            if (project.getTagList() != null && !project.getTagList().isEmpty()) {
                throw new IllegalArgumentException("GitLab API v3 does not support tag lists when updating projects");
            }
        } else {
            Visibility visibility = (project.getVisibility() != null ? project.getVisibility() :
                project.getPublic() == Boolean.TRUE ? Visibility.PUBLIC : null);
            formData.withParam("visibility", visibility);

            if (project.getTagList() != null && !project.getTagList().isEmpty()) {
                formData.withParam("tag_list", String.join(",", project.getTagList()));
            }
        }

        Response response = putWithFormData(Response.Status.OK, formData, "projects", projectIdentifier);
        return (response.readEntity(Project.class));
    }

    /**
     * Removes project with all resources(issues, merge requests etc).
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteProject(Object projectIdOrPath) throws GitLabApiException {
        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.ACCEPTED);
        delete(expectedStatus, null, "projects", getProjectIdOrPath(projectIdOrPath));
    }

    /**
     * Forks a project into the user namespace of the authenticated user or the one provided.
     * The forking operation for a project is asynchronous and is completed in a background job.
     * The request will return immediately.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/fork</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param namespace       path of the namespace that the project will be forked to
     * @return the newly forked Project instance
     * @throws GitLabApiException if any exception occurs
     */
    public Project forkProject(Object projectIdOrPath, String namespace) throws GitLabApiException {
        return forkProject(projectIdOrPath, namespace, null, null);
    }

    /**
     * Forks a project into the user namespace of the authenticated user or the one provided.
     * The forking operation for a project is asynchronous and is completed in a background job.
     * The request will return immediately.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/fork</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param namespaceId     ID of the namespace that the project will be forked to
     * @return the newly forked Project instance
     * @throws GitLabApiException if any exception occurs
     */
    public Project forkProject(Object projectIdOrPath, Long namespaceId) throws GitLabApiException {
        return forkProject(projectIdOrPath, Long.toString(namespaceId));
    }

    /**
     * Forks a project into the user namespace of the authenticated user or the one provided.
     * The forking operation for a project is asynchronous and is completed in a background job.
     * The request will return immediately.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/fork</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param namespace       path of the namespace that the project will be forked to
     * @param path            the path that will be assigned to the resultant project after forking
     * @param name            the name that will be assigned to the resultant project after forking
     * @return the newly forked Project instance
     * @throws GitLabApiException if any exception occurs
     */
    public Project forkProject(Object projectIdOrPath, String namespace, String path, String name) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("namespace", namespace, true)
                .withParam("path", path)
                .withParam("name", name);
        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.CREATED);
        Response response = post(expectedStatus, formData, "projects", getProjectIdOrPath(projectIdOrPath), "fork");
        return (response.readEntity(Project.class));
    }

    /**
     * Create a forked from/to relation between existing projects.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/fork/:forkFromId</code></pre>
     *
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param forkedFromId the ID of the project that was forked from
     * @return the updated Project instance
     * @throws GitLabApiException if any exception occurs
     */
    public Project createForkedFromRelationship(Object projectIdOrPath, Long forkedFromId) throws GitLabApiException {
        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.CREATED);
        Response response = post(expectedStatus, (Form)null, "projects", this.getProjectIdOrPath(projectIdOrPath), "fork", forkedFromId);
        return (response.readEntity(Project.class));
    }

    /**
     * Delete an existing forked from relationship.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/fork</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteForkedFromRelationship(Object projectIdOrPath) throws GitLabApiException {
        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.ACCEPTED);
        delete(expectedStatus, null, "projects", getProjectIdOrPath(projectIdOrPath), "fork");
    }

    /**
     * Get a list of project team members.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/members</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return the members belonging to the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public List<Member> getMembers(Object projectIdOrPath) throws GitLabApiException {
        return (getMembers(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a list of project team members in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/members</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param page the page to get
     * @param perPage the number of Member instances per page
     * @return the members belonging to the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public List<Member> getMembers(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "projects", getProjectIdOrPath(projectIdOrPath), "members");
        return (response.readEntity(new GenericType<List<Member>>() {}));
    }

    /**
     * Get a Pager of project team members.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/members</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return the members belonging to the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Member> getMembers(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Member>(this, Member.class, itemsPerPage, null, "projects", getProjectIdOrPath(projectIdOrPath), "members"));
    }

    /**
     * Get a Stream of project team members.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/members</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a Stream of the members belonging to the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Member> getMembersStream(Object projectIdOrPath) throws GitLabApiException {
        return (getMembers(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Gets a list of project members viewable by the authenticated user,
     * including inherited members through ancestor groups. Returns multiple
     * times the same user (with different member attributes) when the user is
     * a member of the project/group and of one or more ancestor group.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/members/all</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return the project members viewable by the authenticated user, including inherited members through ancestor groups
     * @throws GitLabApiException if any exception occurs
     */
    public List<Member> getAllMembers(Object projectIdOrPath) throws GitLabApiException {
        return (getAllMembers(projectIdOrPath, null, null));
    }

    /**
     * Gets a list of project members viewable by the authenticated user,
     * including inherited members through ancestor groups. Returns multiple
     * times the same user (with different member attributes) when the user is
     * a member of the project/group and of one or more ancestor group.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/members</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param page the page to get
     * @param perPage the number of Member instances per page
     * @return the project members viewable by the authenticated user, including inherited members through ancestor groups
     * @throws GitLabApiException if any exception occurs
     * @deprecated  Will be removed in version 6.0
     */
    @Deprecated
    public List<Member> getAllMembers(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage),
                "projects", getProjectIdOrPath(projectIdOrPath), "members", "all");
        return (response.readEntity(new GenericType<List<Member>>() {}));
    }

    /**
     * Gets a Pager of project members viewable by the authenticated user,
     * including inherited members through ancestor groups. Returns multiple
     * times the same user (with different member attributes) when the user is
     * a member of the project/group and of one or more ancestor group.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/members/all</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of the project members viewable by the authenticated user,
     * including inherited members through ancestor groups
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Member> getAllMembers(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (getAllMembers(projectIdOrPath, null, null, itemsPerPage));
    }

    /**
     * Gets a Stream of project members viewable by the authenticated user,
     * including inherited members through ancestor groups. Returns multiple
     * times the same user (with different member attributes) when the user is
     * a member of the project/group and of one or more ancestor group.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/members/all</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a Stream of the project members viewable by the authenticated user,
     * including inherited members through ancestor groups
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Member> getAllMembersStream(Object projectIdOrPath) throws GitLabApiException {
        return (getAllMembersStream(projectIdOrPath, null, null));
    }

    /**
     * Gets a list of project members viewable by the authenticated user,
     * including inherited members through ancestor groups. Returns multiple
     * times the same user (with different member attributes) when the user is
     * a member of the project/group and of one or more ancestor group.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/members/all</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param query a query string to search for members
     * @param userIds filter the results on the given user IDs
     * @return the project members viewable by the authenticated user, including inherited members through ancestor groups
     * @throws GitLabApiException if any exception occurs
     */
    public List<Member> getAllMembers(Object projectIdOrPath, String query, List<Long> userIds) throws GitLabApiException {
        return (getAllMembers(projectIdOrPath, query, userIds, getDefaultPerPage()).all());
    }

    /**
     * Gets a Pager of project members viewable by the authenticated user,
     * including inherited members through ancestor groups. Returns multiple
     * times the same user (with different member attributes) when the user is
     * a member of the project/group and of one or more ancestor group.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/members/all</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param query a query string to search for members
     * @param userIds filter the results on the given user IDs
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of the project members viewable by the authenticated user,
     * including inherited members through ancestor groups
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Member> getAllMembers(Object projectIdOrPath, String query, List<Long> userIds, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm form = new GitLabApiForm().withParam("query", query).withParam("user_ids", userIds);
        return (new Pager<Member>(this, Member.class, itemsPerPage, form.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "members", "all"));
    }

    /**
     * Gets a Stream of project members viewable by the authenticated user,
     * including inherited members through ancestor groups. Returns multiple
     * times the same user (with different member attributes) when the user is
     * a member of the project/group and of one or more ancestor group.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/members/all</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param query a query string to search for members
     * @param userIds filter the results on the given user IDs
     * @return a Stream of the project members viewable by the authenticated user,
     * including inherited members through ancestor groups
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Member> getAllMembersStream(Object projectIdOrPath, String query, List<Long> userIds) throws GitLabApiException {
        return (getAllMembers(projectIdOrPath, query, userIds, getDefaultPerPage()).stream());
    }

    /**
     * Gets a project team member.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/members/:user_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param userId the user ID of the member
     * @return the member specified by the project ID/user ID pair
     * @throws GitLabApiException if any exception occurs
     */
    public Member getMember(Object projectIdOrPath, Long userId) throws GitLabApiException {
        return (getMember(projectIdOrPath, userId, false));
    }

    /**
     * Gets a project team member.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/members/:user_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param userId the user ID of the member
     * @return the member specified by the project ID/user ID pair
     */
    public Optional<Member> getOptionalMember(Object projectIdOrPath, Long userId)  {
        try {
            return (Optional.ofNullable(getMember(projectIdOrPath, userId, false)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Gets a project team member, optionally including inherited member.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/members/all/:user_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param userId the user ID of the member
     * @param includeInherited if true will the member even if inherited thru an ancestor group
     * @return the member specified by the project ID/user ID pair
     * @throws GitLabApiException if any exception occurs
     */
    public Member getMember(Object projectIdOrPath, Long userId, Boolean includeInherited) throws GitLabApiException {
        Response response;
        if (includeInherited) {
            response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "members", "all", userId);
        } else {
            response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "members", userId);
        }
        return (response.readEntity(Member.class));
    }

    /**
     * Gets a project team member, optionally including inherited member.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/members/all/:user_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param userId the user ID of the member
     * @param includeInherited if true will the member even if inherited thru an ancestor group
     * @return the member specified by the project ID/user ID pair as the value of an Optional
     */
    public Optional<Member> getOptionalMember(Object projectIdOrPath, Long userId, Boolean includeInherited)  {
        try {
            return (Optional.ofNullable(getMember(projectIdOrPath, userId, includeInherited)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Adds a user to a project team. This is an idempotent method and can be called multiple times
     * with the same parameters. Adding team membership to a user that is already a member does not
     * affect the existing membership.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/members</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param userId the user ID of the member to add, required
     * @param accessLevel the access level for the new member, required
     * @return the added member
     * @throws GitLabApiException if any exception occurs
     */
    public Member addMember(Object projectIdOrPath, Long userId, Integer accessLevel) throws GitLabApiException {
        return (addMember(projectIdOrPath, userId, accessLevel, null));
    }

    /**
     * Adds a user to a project team. This is an idempotent method and can be called multiple times
     * with the same parameters. Adding team membership to a user that is already a member does not
     * affect the existing membership.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/members</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param userId the user ID of the member to add, required
     * @param accessLevel the access level for the new member, required
     * @return the added member
     * @throws GitLabApiException if any exception occurs
     */
    public Member addMember(Object projectIdOrPath, Long userId, AccessLevel accessLevel) throws GitLabApiException {
        return (addMember(projectIdOrPath, userId, accessLevel.toValue(), null));
    }

    /**
     * Adds a user to a project team. This is an idempotent method and can be called multiple times
     * with the same parameters. Adding team membership to a user that is already a member does not
     * affect the existing membership.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/members</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param userId the user ID of the member to add
     * @param accessLevel the access level for the new member
     * @param expiresAt the date the membership in the group will expire
     * @return the added member
     * @throws GitLabApiException if any exception occurs
     */
    public Member addMember(Object projectIdOrPath, Long userId, AccessLevel accessLevel, Date expiresAt) throws GitLabApiException {
        return (addMember(projectIdOrPath, userId, accessLevel.toValue(), expiresAt));
    }

    /**
     * Adds a user to a project team. This is an idempotent method and can be called multiple times
     * with the same parameters. Adding team membership to a user that is already a member does not
     * affect the existing membership.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/members</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param userId the user ID of the member to add
     * @param accessLevel the access level for the new member
     * @param expiresAt the date the membership in the group will expire
     * @return the added member
     * @throws GitLabApiException if any exception occurs
     */
    public Member addMember(Object projectIdOrPath, Long userId, Integer accessLevel, Date expiresAt) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("user_id", userId, true)
                .withParam("access_level", accessLevel, true)
                .withParam("expires_at",  expiresAt, false);
        Response response = post(Response.Status.CREATED, formData, "projects", getProjectIdOrPath(projectIdOrPath), "members");
        return (response.readEntity(Member.class));
    }

    /**
     * Updates a member of a project.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:projectId/members/:userId</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param userId the user ID of the member to update, required
     * @param accessLevel the new access level for the member, required
     * @return the updated member
     * @throws GitLabApiException if any exception occurs
     */
    public Member updateMember(Object projectIdOrPath, Long userId, Integer accessLevel) throws GitLabApiException {
        return (updateMember(projectIdOrPath, userId, accessLevel, null));
    }

    /**
     * Updates a member of a project.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:projectId/members/:userId</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param userId the user ID of the member to update, required
     * @param accessLevel the new access level for the member, required
     * @return the updated member
     * @throws GitLabApiException if any exception occurs
     */
    public Member updateMember(Object projectIdOrPath, Long userId, AccessLevel accessLevel) throws GitLabApiException {
        return (updateMember(projectIdOrPath, userId, accessLevel.toValue(), null));
    }

    /**
     * Updates a member of a project.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:projectId/members/:userId</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param userId the user ID of the member to update, required
     * @param accessLevel the new access level for the member, required
     * @param expiresAt the date the membership in the group will expire, optional
     * @return the updated member
     * @throws GitLabApiException if any exception occurs
     */
    public Member updateMember(Object projectIdOrPath, Long userId, AccessLevel accessLevel, Date expiresAt) throws GitLabApiException {
        return (updateMember(projectIdOrPath, userId, accessLevel.toValue(), expiresAt));
    }

    /**
     * Updates a member of a project.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:projectId/members/:userId</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param userId the user ID of the member to update, required
     * @param accessLevel the new access level for the member, required
     * @param expiresAt the date the membership in the group will expire, optional
     * @return the updated member
     * @throws GitLabApiException if any exception occurs
     */
    public Member updateMember(Object projectIdOrPath, Long userId, Integer accessLevel, Date expiresAt) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("access_level", accessLevel, true)
                .withParam("expires_at",  expiresAt, false);
        Response response = put(Response.Status.OK, formData.asMap(), "projects", getProjectIdOrPath(projectIdOrPath), "members", userId);
        return (response.readEntity(Member.class));
    }

    /**
     * Removes user from project team.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/members/:user_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param userId the user ID of the member to remove
     * @throws GitLabApiException if any exception occurs
     */
    public void removeMember(Object projectIdOrPath, Long userId) throws GitLabApiException {
        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.NO_CONTENT);
        delete(expectedStatus, null, "projects", getProjectIdOrPath(projectIdOrPath), "members", userId);
    }

    /**
     * Get a list of project users. This list includes all project members and all users assigned to project parent groups.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/users</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return the users belonging to the specified project and its parent groups
     * @throws GitLabApiException if any exception occurs
     */
    public List<ProjectUser> getProjectUsers(Object projectIdOrPath) throws GitLabApiException {
        return (getProjectUsers(projectIdOrPath, null, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of project users. This Pager includes all project members and all users assigned to project parent groups.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/users</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of the users matching the search string and belonging to the specified project and its parent groups
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<ProjectUser> getProjectUsers(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (getProjectUsers(projectIdOrPath, null, itemsPerPage));
    }

    /**
     * Get a Stream of project users. This Stream includes all project members and all users assigned to project parent groups.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/users</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a Stream of the users belonging to the specified project and its parent groups
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<ProjectUser> getProjectUsersStream(Object projectIdOrPath) throws GitLabApiException {
        return (getProjectUsers(projectIdOrPath, null, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of project users matching the specified search string. This list
     * includes all project members and all users assigned to project parent groups.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/users</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param search the string to match specific users
     * @return the users matching the search string and belonging to the specified project and its parent groups
     * @throws GitLabApiException if any exception occurs
     */
    public List<ProjectUser> getProjectUsers(Object projectIdOrPath, String search) throws GitLabApiException {
        return (getProjectUsers(projectIdOrPath, search, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of project users matching the specified search string. This Pager includes
     * all project members and all users assigned to project parent groups.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/users</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param search the string to match specific users
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of the users matching the search string and belonging to the specified project and its parent groups
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<ProjectUser> getProjectUsers(Object projectIdOrPath, String search, int itemsPerPage) throws GitLabApiException {
        MultivaluedMap<String, String> params = (search != null ? new GitLabApiForm().withParam("search", search).asMap() : null);
        return (new Pager<ProjectUser>(this, ProjectUser.class, itemsPerPage, params,
                "projects", getProjectIdOrPath(projectIdOrPath), "users"));
    }

    /**
     * Get a Stream of project users matching the specified search string. This Stream
     * includes all project members and all users assigned to project parent groups.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/users</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param search the string to match specific users
     * @return a Stream of the users matching the search string and belonging to the specified project and its parent groups
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<ProjectUser> getProjectUsersStream(Object projectIdOrPath, String search) throws GitLabApiException {
        return (getProjectUsers(projectIdOrPath, search, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of the ancestor groups for a given project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/groups</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return the ancestor groups for a given project
     * @throws GitLabApiException if any exception occurs
     */
    public List<ProjectGroup> getProjectGroups(Object projectIdOrPath) throws GitLabApiException {
        return (getProjectGroups(projectIdOrPath, new ProjectGroupsFilter(), getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of the ancestor groups for a given project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/groups</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of the ancestor groups for a given project
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<ProjectGroup> getProjectGroups(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (getProjectGroups(projectIdOrPath, new ProjectGroupsFilter(), itemsPerPage));
    }

    /**
     * Get a Stream of the ancestor groups for a given project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/groups</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a Stream of the ancestor groups for a given project
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<ProjectGroup> getProjectGroupsStream(Object projectIdOrPath) throws GitLabApiException {
        return (getProjectGroups(projectIdOrPath, new ProjectGroupsFilter(), getDefaultPerPage()).stream());
    }

    /**
     * Get a list of the ancestor groups for a given project matching the specified filter.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/groups</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param filter the ProjectGroupsFilter to match against
     * @return the ancestor groups for a given project
     * @throws GitLabApiException if any exception occurs
     */
    public List<ProjectGroup> getProjectGroups(Object projectIdOrPath, ProjectGroupsFilter filter) throws GitLabApiException {
        return (getProjectGroups(projectIdOrPath, filter, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of the ancestor groups for a given project matching the specified filter.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/groups</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filter the ProjectGroupsFilter to match against
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of the ancestor groups for a given project
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<ProjectGroup> getProjectGroups(Object projectIdOrPath, ProjectGroupsFilter filter, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = filter.getQueryParams();
        return (new Pager<ProjectGroup>(this, ProjectGroup.class, itemsPerPage, formData.asMap(), "projects", getProjectIdOrPath(projectIdOrPath), "groups"));
    }

    /**
     * Get a Stream of the ancestor groups for a given project matching the specified filter.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/groups</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param filter the ProjectGroupsFilter to match against
     * @return a Stream of the ancestor groups for a given project
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<ProjectGroup> getProjectGroupsStream(Object projectIdOrPath, ProjectGroupsFilter filter) throws GitLabApiException {
        return (getProjectGroups(projectIdOrPath, filter, getDefaultPerPage()).stream());
    }

    /**
     * Get the project events for specific project. Sorted from newest to latest.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/events</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return the project events for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public List<Event> getProjectEvents(Object projectIdOrPath) throws GitLabApiException {
        return (getProjectEvents(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get the project events for specific project. Sorted from newest to latest in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/events</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param page the page to get
     * @param perPage the number of Event instances per page
     * @return the project events for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public List<Event> getProjectEvents(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "projects", getProjectIdOrPath(projectIdOrPath), "events");
        return (response.readEntity(new GenericType<List<Event>>() {}));
    }

    /**
     * Get a Pager of project events for specific project. Sorted from newest to latest.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/events</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of project events for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Event> getProjectEvents(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Event>(this, Event.class, itemsPerPage, null, "projects", getProjectIdOrPath(projectIdOrPath), "events"));
    }

    /**
     * Get a Stream of the project events for specific project. Sorted from newest to latest.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/events</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a Stream of the project events for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Event> getProjectEventsStream(Object projectIdOrPath) throws GitLabApiException {
        return (getProjectEvents(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of the project hooks for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/hooks</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a list of project hooks for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public List<ProjectHook> getHooks(Object projectIdOrPath) throws GitLabApiException {
        return (getHooks(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get list of project hooks in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/hooks</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param page the page to get
     * @param perPage the number of ProjectHook instances per page
     * @return a list of project hooks for the specified project in the specified page range
     * @throws GitLabApiException if any exception occurs
     */
    public List<ProjectHook> getHooks(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "projects", getProjectIdOrPath(projectIdOrPath), "hooks");
        return (response.readEntity(new GenericType<List<ProjectHook>>() {}));
    }

    /**
     * Get Pager of project hooks.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/hooks</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of project hooks for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<ProjectHook> getHooks(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<ProjectHook>(this, ProjectHook.class, itemsPerPage, null, "projects", getProjectIdOrPath(projectIdOrPath), "hooks"));
    }

    /**
     * Get a Stream of the project hooks for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/hooks</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a Stream of project hooks for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<ProjectHook> getHooksStream(Object projectIdOrPath) throws GitLabApiException {
        return (getHooks(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get a specific hook for project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/hooks/:hook_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param hookId the ID of the hook to get
     * @return the project hook for the specified project ID/hook ID pair
     * @throws GitLabApiException if any exception occurs
     */
    public ProjectHook getHook(Object projectIdOrPath, Long hookId) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "hooks", hookId);
        return (response.readEntity(ProjectHook.class));
    }

    /**
     * Get a specific hook for project as an Optional instance.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/hooks/:hook_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param hookId the ID of the hook to get
     * @return the project hook for the specified project ID/hook ID pair as an Optional instance
     */
    public Optional<ProjectHook> getOptionalHook(Object projectIdOrPath, Long hookId) {
        try {
            return (Optional.ofNullable(getHook(projectIdOrPath, hookId)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Adds a hook to project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/hooks</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param url the callback URL for the hook
     * @param enabledHooks a ProjectHook instance specifying which hooks to enable
     * @param enableSslVerification enable SSL verification
     * @param secretToken the secret token to pass back to the hook
     * @return the added ProjectHook instance
     * @throws GitLabApiException if any exception occurs
     */
    public ProjectHook addHook(Object projectIdOrPath, String url, ProjectHook enabledHooks,
            boolean enableSslVerification, String secretToken) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("url", url, true)
                .withParam("push_events", enabledHooks.getPushEvents(), false)
                .withParam("push_events_branch_filter", enabledHooks.getPushEventsBranchFilter(), false)
                .withParam("issues_events", enabledHooks.getIssuesEvents(), false)
                .withParam("confidential_issues_events", enabledHooks.getConfidentialIssuesEvents(), false)
                .withParam("merge_requests_events", enabledHooks.getMergeRequestsEvents(), false)
                .withParam("tag_push_events", enabledHooks.getTagPushEvents(), false)
                .withParam("note_events", enabledHooks.getNoteEvents(), false)
                .withParam("confidential_note_events", enabledHooks.getConfidentialNoteEvents(), false)
                .withParam("job_events", enabledHooks.getJobEvents(), false)
                .withParam("pipeline_events", enabledHooks.getPipelineEvents(), false)
                .withParam("wiki_page_events", enabledHooks.getWikiPageEvents(), false)
                .withParam("enable_ssl_verification", enableSslVerification, false)
                .withParam("repository_update_events", enabledHooks.getRepositoryUpdateEvents(), false)
                .withParam("deployment_events", enabledHooks.getDeploymentEvents(), false)
                .withParam("releases_events", enabledHooks.getReleasesEvents(), false)
                .withParam("deployment_events", enabledHooks.getDeploymentEvents(), false)
                .withParam("token", secretToken, false);
        Response response = post(Response.Status.CREATED, formData, "projects", getProjectIdOrPath(projectIdOrPath), "hooks");
        return (response.readEntity(ProjectHook.class));
    }

    /**
     * Adds a hook to project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/hooks</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param url the callback URL for the hook
     * @param doPushEvents flag specifying whether to do push events
     * @param doIssuesEvents flag specifying whether to do issues events
     * @param doMergeRequestsEvents flag specifying whether to do merge requests events
     * @return the added ProjectHook instance
     * @throws GitLabApiException if any exception occurs
     */
    public ProjectHook addHook(Object projectIdOrPath, String url, boolean doPushEvents,
            boolean doIssuesEvents, boolean doMergeRequestsEvents) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("url", url)
                .withParam("push_events", doPushEvents)
                .withParam("issues_events", doIssuesEvents)
                .withParam("merge_requests_events", doMergeRequestsEvents);

        Response response = post(Response.Status.CREATED, formData, "projects", getProjectIdOrPath(projectIdOrPath), "hooks");
        return (response.readEntity(ProjectHook.class));
    }

    /**
     * Deletes a hook from the project.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/hooks/:hook_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param hookId the project hook ID to delete
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteHook(Object projectIdOrPath, Long hookId) throws GitLabApiException {
        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.NO_CONTENT);
        delete(expectedStatus, null, "projects", getProjectIdOrPath(projectIdOrPath), "hooks", hookId);
    }

    /**
     * Deletes a hook from the project.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/hooks/:hook_id</code></pre>
     *
     * @param hook the ProjectHook instance to remove
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteHook(ProjectHook hook) throws GitLabApiException {
        deleteHook(hook.getProjectId(), hook.getId());
    }

    /**
     * Modifies a hook for project.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/hooks/:hook_id</code></pre>
     *
     * @param hook the ProjectHook instance that contains the project hook info to modify
     * @return the modified project hook
     * @throws GitLabApiException if any exception occurs
     */
    public ProjectHook modifyHook(ProjectHook hook) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
            .withParam("url", hook.getUrl(), true)
            .withParam("push_events", hook.getPushEvents(), false)
            .withParam("push_events_branch_filter", hook.getPushEventsBranchFilter(), false)
            .withParam("issues_events", hook.getIssuesEvents(), false)
            .withParam("confidential_issues_events", hook.getConfidentialIssuesEvents(), false)
            .withParam("merge_requests_events", hook.getMergeRequestsEvents(), false)
            .withParam("tag_push_events", hook.getTagPushEvents(), false)
            .withParam("note_events", hook.getNoteEvents(), false)
            .withParam("confidential_note_events", hook.getConfidentialNoteEvents(), false)
            .withParam("job_events", hook.getJobEvents(), false)
            .withParam("pipeline_events", hook.getPipelineEvents(), false)
            .withParam("wiki_page_events", hook.getWikiPageEvents(), false)
            .withParam("enable_ssl_verification", hook.getEnableSslVerification(), false)
            .withParam("repository_update_events", hook.getRepositoryUpdateEvents(), false)
            .withParam("releases_events", hook.getReleasesEvents(), false)
            .withParam("deployment_events", hook.getDeploymentEvents(), false)
            .withParam("token", hook.getToken(), false);

        Response response = put(Response.Status.OK, formData.asMap(), "projects", hook.getProjectId(), "hooks", hook.getId());
        return (response.readEntity(ProjectHook.class));
    }

    /**
     * Get a list of the project's issues.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a list of project's issues
     * @throws GitLabApiException if any exception occurs
     * @deprecated Will be removed in version 6.0, replaced by {@link IssuesApi#getIssues(Object)}
     */
    @Deprecated
    public List<Issue> getIssues(Object projectIdOrPath) throws GitLabApiException {
        return (getIssues(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a list of project's issues using the specified page and per page settings.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param page the page to get
     * @param perPage the number of issues per page
     * @return the list of issues in the specified range
     * @throws GitLabApiException if any exception occurs
     * @deprecated Will be removed in version 6.0, replaced by {@link IssuesApi#getIssues(Object, int, int)}
     */
    @Deprecated
    public List<Issue> getIssues(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "projects", getProjectIdOrPath(projectIdOrPath), "issues");
        return (response.readEntity(new GenericType<List<Issue>>() {}));
    }

    /**
     * Get a Pager of project's issues.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param itemsPerPage the number of issues per page
     * @return the list of issues in the specified range
     * @throws GitLabApiException if any exception occurs
     * @deprecated Will be removed in version 6.0, replaced by {@link IssuesApi#getIssues(Object, int)}
     */
    @Deprecated
    public Pager<Issue> getIssues(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Issue>(this, Issue.class, itemsPerPage, null, "projects", getProjectIdOrPath(projectIdOrPath), "issues"));
    }

    /**
     * Get a Stream of the project's issues.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a Stream of the project's issues
     * @throws GitLabApiException if any exception occurs
     * @deprecated Will be removed in version 6.0, replaced by {@link IssuesApi#getIssues(Object)}
     */
    @Deprecated
    public Stream<Issue> getIssuesStream(Object projectIdOrPath) throws GitLabApiException {
        return (getIssues(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get a single project issue.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param issueId the internal ID of a project's issue
     * @return the specified Issue instance
     * @throws GitLabApiException if any exception occurs
     * @deprecated  Will be removed in version 6.0, replaced by {@link IssuesApi#getIssue(Object, Long)}
     */
    @Deprecated
    public Issue getIssue(Object projectIdOrPath, Long issueId) throws GitLabApiException {
        Response response = get(Response.Status.OK, getDefaultPerPageParam(), "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueId);
        return (response.readEntity(Issue.class));
    }

    /**
     * Delete a project issue.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/issues/:issue_iid</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param issueId the internal ID of a project's issue
     * @throws GitLabApiException if any exception occurs
     * @deprecated  Will be removed in version 6.0, replaced by {@link IssuesApi#deleteIssue(Object, Long)}
     */
    @Deprecated
    public void deleteIssue(Object projectIdOrPath, Long issueId) throws GitLabApiException {
        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.NO_CONTENT);
        delete(expectedStatus, getDefaultPerPageParam(), "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueId);
    }

    /**
     * Get a list of the project snippets.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/snippets</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a list of the project's snippets
     * @throws GitLabApiException if any exception occurs
     */
    public List<Snippet> getSnippets(Object projectIdOrPath) throws GitLabApiException {
        return (getSnippets(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a list of project snippets.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/snippets</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param page the page to get
     * @param perPage the number of snippets per page
     * @return a list of project's snippets for the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public List<Snippet> getSnippets(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "projects", getProjectIdOrPath(projectIdOrPath), "snippets");
        return (response.readEntity(new GenericType<List<Snippet>>() {}));
    }

    /**
     * Get a Pager of project's snippets.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/snippets</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param itemsPerPage the number of snippets per page
     * @return the Pager of snippets
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Snippet> getSnippets(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Snippet>(this, Snippet.class, itemsPerPage, null, "projects", getProjectIdOrPath(projectIdOrPath), "snippets"));
    }

    /**
     * Get a Stream of the project snippets.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/snippets</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a Stream of the project's snippets
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Snippet> getSnippetsStream(Object projectIdOrPath) throws GitLabApiException {
        return (getSnippets(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get a single of project snippet.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/snippets/:snippet_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param snippetId the ID of the project's snippet
     * @return the specified project Snippet
     * @throws GitLabApiException if any exception occurs
     */
    public Snippet getSnippet(Object projectIdOrPath, Long snippetId) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "snippets", snippetId);
        return (response.readEntity(Snippet.class));
    }

    /**
     * Get a single of project snippet as an Optional instance.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/snippets/:snippet_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param snippetId the ID of the project's snippet
     * @return the specified project Snippet as an Optional instance
     */
    public Optional<Snippet> getOptionalSnippet(Object projectIdOrPath, Long snippetId) {
        try {
            return (Optional.ofNullable(getSnippet(projectIdOrPath, snippetId)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Creates a new project snippet. The user must have permission to create new snippets.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/snippets</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param title the title of a snippet, required
     * @param filename the name of a snippet file, required
     * @param description the description of a snippet, optional
     * @param content the content of a snippet, required
     * @param visibility the snippet's visibility, required
     * @return a Snippet instance with info on the created snippet
     * @throws GitLabApiException if any exception occurs
     */
    public Snippet createSnippet(Object projectIdOrPath, String title, String filename, String description,
            String content, Visibility visibility) throws GitLabApiException {

        try {

            GitLabApiForm form = new GitLabApiForm()
                    .withParam("title", title, true)
                    .withParam("file_name", filename, true)
                    .withParam("description", description)
                    .withParam("content", content, true)
                    .withParam("visibility", visibility, true);

            Response response = post(Response.Status.CREATED, form, "projects", getProjectIdOrPath(projectIdOrPath), "snippets");
            return (response.readEntity(Snippet.class));

        } catch (GitLabApiException glae) {

            // GitLab 12.8 and older will return HTTP status 400 if called with content instead of code
            if (glae.getHttpStatus() != Response.Status.BAD_REQUEST.getStatusCode()) {
                throw glae;
            }

            GitLabApiForm form = new GitLabApiForm()
                    .withParam("title", title, true)
                    .withParam("file_name", filename, true)
                    .withParam("description", description)
                    .withParam("code", content, true)
                    .withParam("visibility", visibility, true);

            Response response = post(Response.Status.CREATED, form, "projects", getProjectIdOrPath(projectIdOrPath), "snippets");
            return (response.readEntity(Snippet.class));
        }
    }

    /**
     * Updates an existing project snippet. The user must have permission to change an existing snippet.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/snippets/:snippet_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param snippetId the ID of a project's snippet, required
     * @param title the title of a snippet, optional
     * @param filename the name of a snippet file, optional
     * @param description the description of a snippet, optioptionalonal
     * @param code the content of a snippet, optional
     * @param visibility the snippet's visibility, reqoptionaluired
     * @return a Snippet instance with info on the updated snippet
     * @throws GitLabApiException if any exception occurs
     */
    public Snippet updateSnippet(Object projectIdOrPath, Long snippetId, String title, String filename, String description,
            String code, Visibility visibility) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("title", title)
                .withParam("file_name", filename)
                .withParam("description", description)
                .withParam("code", code)
                .withParam("visibility", visibility);

        Response response = put(Response.Status.OK, formData.asMap(), "projects", getProjectIdOrPath(projectIdOrPath), "snippets", snippetId);
        return (response.readEntity(Snippet.class));
    }

    /*
     * Deletes an existing project snippet. This is an idempotent function and deleting a
     * non-existent snippet does not cause an error.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/snippets/:snippet_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param snippetId the ID of the project's snippet
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteSnippet(Object projectIdOrPath, Long snippetId) throws GitLabApiException {
        delete(Response.Status.NO_CONTENT, null, "projects", getProjectIdOrPath(projectIdOrPath), "snippets", snippetId);
    }

    /**
     * Get the raw project snippet as plain text.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/snippets/:snippet_id/raw</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param snippetId the ID of the project's snippet
     * @return the raw project snippet plain text as an Optional instance
     * @throws GitLabApiException if any exception occurs
     */
    public String getRawSnippetContent(Object projectIdOrPath, Long snippetId) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "snippets", snippetId, "raw");
        return (response.readEntity(String.class));
    }

    /**
     * Get the raw project snippet plain text as an Optional instance.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/snippets/:snippet_id/raw</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param snippetId the ID of the project's snippet
     * @return the raw project snippet plain text as an Optional instance
     */
    public Optional<String> getOptionalRawSnippetContent(Object projectIdOrPath, Long snippetId) {
        try {
            return (Optional.ofNullable(getRawSnippetContent(projectIdOrPath, snippetId)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Share a project with the specified group.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/share</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param groupId the ID of the group to share with, required
     * @param accessLevel the permissions level to grant the group, required
     * @param expiresAt the share expiration date, optional
     * @throws GitLabApiException if any exception occurs
     */
    public void shareProject(Object projectIdOrPath, Long groupId, AccessLevel accessLevel, Date expiresAt)
            throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
            .withParam("group_id", groupId, true)
            .withParam("group_access", accessLevel, true)
            .withParam("expires_at", expiresAt);
        post(Response.Status.CREATED, formData, "projects", getProjectIdOrPath(projectIdOrPath), "share");
    }

    /**
     * Unshare the project from the group.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/share/:group_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param groupId the ID of the group to unshare, required
     * @throws GitLabApiException if any exception occurs
     */
    public void unshareProject(Object projectIdOrPath, Long groupId) throws GitLabApiException {
        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.NO_CONTENT);
        delete(expectedStatus, null, "projects", getProjectIdOrPath(projectIdOrPath), "share", groupId);
    }

    /**
     * Archive a project
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/archive</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return the archived GitLab Project
     * @throws GitLabApiException if any exception occurs
     */
    public Project archiveProject(Object projectIdOrPath)
            throws GitLabApiException {
        Response response = post(Response.Status.CREATED, (new GitLabApiForm()), "projects", getProjectIdOrPath(projectIdOrPath), "archive");
        return (response.readEntity(Project.class));
    }

    /**
     * Unarchive a project
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/unarchive</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return the unarchived GitLab Project
     * @throws GitLabApiException if any exception occurs
     */
    public Project unarchiveProject(Object projectIdOrPath)
            throws GitLabApiException {
        Response response = post(Response.Status.CREATED, (new GitLabApiForm()), "projects", getProjectIdOrPath(projectIdOrPath), "unarchive");
        return (response.readEntity(Project.class));
    }

    /**
     * Uploads a file to the specified project to be used in an issue or merge request description, or a comment.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/uploads</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param fileToUpload the File instance of the file to upload, required
     * @return a FileUpload instance with information on the just uploaded file
     * @throws GitLabApiException if any exception occurs
     */
    public FileUpload uploadFile(Object projectIdOrPath, File fileToUpload) throws GitLabApiException {
        return (uploadFile(projectIdOrPath, fileToUpload, null));
    }

    /**
     * Uploads a file to the specified project to be used in an issue or merge request description, or a comment.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/uploads</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param fileToUpload the File instance of the file to upload, required
     * @param mediaType unused; will be removed in the next major version
     * @return a FileUpload instance with information on the just uploaded file
     * @throws GitLabApiException if any exception occurs
     */
    public FileUpload uploadFile(Object projectIdOrPath, File fileToUpload, String mediaType) throws GitLabApiException {
        Response response = upload(Response.Status.CREATED, "file", fileToUpload, mediaType, "projects", getProjectIdOrPath(projectIdOrPath), "uploads");
        return (response.readEntity(FileUpload.class));
    }

    /**
     * Uploads some data in an {@link InputStream} to the specified project,
     * to be used in an issue or merge request description, or a comment.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/uploads</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Integer(ID), String(path), or Project instance, required
     * @param inputStream the data to upload, required
     * @param filename The filename of the file to upload
     * @param mediaType unused; will be removed in the next major version
     * @return a FileUpload instance with information on the just uploaded file
     * @throws GitLabApiException if any exception occurs
     */
    public FileUpload uploadFile(Object projectIdOrPath, InputStream inputStream, String filename, String mediaType) throws GitLabApiException {
        Response response = upload(Response.Status.CREATED, "file", inputStream, filename, mediaType, "projects", getProjectIdOrPath(projectIdOrPath), "uploads");
        return (response.readEntity(FileUpload.class));
    }

    /**
     * Get the project's push rules.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/push_rule</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return the push rules for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public PushRules getPushRules(Object projectIdOrPath) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "push_rule");
        return (response.readEntity(PushRules.class));
    }

    /**
     * Adds a push rule to a specified project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/push_rule</code></pre>
     *
     * The following properties on the PushRules instance are utilized in the creation of the push rule:
     *
     *<code>
     * denyDeleteTag (optional) - Deny deleting a tag
     * memberCheck (optional) - Restrict commits by author (email) to existing GitLab users
     * preventSecrets (optional) - GitLab will reject any files that are likely to contain secrets
     * commitMessageRegex (optional) - All commit messages must match this, e.g. Fixed \d+\..*
     * commitMessageNegativeRegex (optional) - No commit message is allowed to match this, e.g. ssh\:\/\/
     * branchNameRegex (optional) - All branch names must match this, e.g. `(feature
     * authorEmailRegex (optional) - All commit author emails must match this, e.g. @my-company.com$
     * fileNameRegex (optional) - All committed filenames must not match this, e.g. `(jar
     * maxFileSize (optional) - Maximum file size (MB)
     * commitCommitterCheck (optional) - Users can only push commits to this repository that were committed with one of their own verified emails.
     * rejectUnsignedCommits (optional) - Reject commit when it is not signed through GPG
     *</code>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param pushRule the PushRule instance containing the push rule configuration to add
     * @return a PushRules instance with the newly created push rule info
     * @throws GitLabApiException if any exception occurs
     */
    public PushRules createPushRules(Object projectIdOrPath, PushRules pushRule) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
            .withParam("deny_delete_tag", pushRule.getDenyDeleteTag())
            .withParam("member_check", pushRule.getMemberCheck())
            .withParam("prevent_secrets", pushRule.getPreventSecrets())
            .withParam("commit_message_regex", pushRule.getCommitMessageRegex())
            .withParam("commit_message_negative_regex", pushRule.getCommitMessageNegativeRegex())
            .withParam("branch_name_regex", pushRule.getBranchNameRegex())
            .withParam("author_email_regex", pushRule.getAuthorEmailRegex())
            .withParam("file_name_regex", pushRule.getFileNameRegex())
            .withParam("max_file_size", pushRule.getMaxFileSize())
            .withParam("commit_committer_check", pushRule.getCommitCommitterCheck())
            .withParam("reject_unsigned_commits", pushRule.getRejectUnsignedCommits());

        Response response = post(Response.Status.CREATED, formData, "projects", getProjectIdOrPath(projectIdOrPath), "push_rule");
        return (response.readEntity(PushRules.class));
    }

    /**
     * Updates a push rule for the specified project.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/push_rule/:push_rule_id</code></pre>
     *
     * The following properties on the PushRules instance are utilized when updating the push rule:
     *
     *<code>
     * denyDeleteTag (optional) - Deny deleting a tag
     * memberCheck (optional) - Restrict commits by author (email) to existing GitLab users
     * preventSecrets (optional) - GitLab will reject any files that are likely to contain secrets
     * commitMessageRegex (optional) - All commit messages must match this, e.g. Fixed \d+\..*
     * commitMessageNegativeRegex (optional) - No commit message is allowed to match this, e.g. ssh\:\/\/
     * branchNameRegex (optional) - All branch names must match this, e.g. `(feature
     * authorEmailRegex (optional) - All commit author emails must match this, e.g. @my-company.com$
     * fileNameRegex (optional) - All committed filenames must not match this, e.g. `(jar
     * maxFileSize (optional) - Maximum file size (MB)
     * commitCommitterCheck (optional) - Users can only push commits to this repository that were committed with one of their own verified emails.
     * rejectUnsignedCommits (optional) - Reject commit when it is not signed through GPG
     *</code>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param pushRule the PushRules instance containing the push rule configuration to update
     * @return a PushRules instance with the newly created push rule info
     * @throws GitLabApiException if any exception occurs
     */
    public PushRules updatePushRules(Object projectIdOrPath, PushRules pushRule) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
            .withParam("deny_delete_tag", pushRule.getDenyDeleteTag())
            .withParam("member_check", pushRule.getMemberCheck())
            .withParam("prevent_secrets", pushRule.getPreventSecrets())
            .withParam("commit_message_regex", pushRule.getCommitMessageRegex())
            .withParam("commit_message_negative_regex", pushRule.getCommitMessageNegativeRegex())
            .withParam("branch_name_regex", pushRule.getBranchNameRegex())
            .withParam("author_email_regex", pushRule.getAuthorEmailRegex())
            .withParam("file_name_regex", pushRule.getFileNameRegex())
            .withParam("max_file_size", pushRule.getMaxFileSize())
            .withParam("commit_committer_check", pushRule.getCommitCommitterCheck())
            .withParam("reject_unsigned_commits", pushRule.getRejectUnsignedCommits());

        final Response response = putWithFormData(Response.Status.OK, formData, "projects", getProjectIdOrPath(projectIdOrPath), "push_rule");
        return (response.readEntity(PushRules.class));
    }

    /**
     * Removes a push rule from a project. This is an idempotent method and can be
     * called multiple times. Either the push rule is available or not.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/push_rule</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @throws GitLabApiException if any exception occurs
     */
    public void deletePushRules(Object projectIdOrPath) throws GitLabApiException {
        delete(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "push_rule");
    }

    /**
     * Get a list of projects that were forked from the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/forks</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a List of forked projects
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getForks(Object projectIdOrPath) throws GitLabApiException {
        return (getForks(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a list of projects that were forked from the specified project and in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/forks</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param page the page to get
     * @param perPage the number of projects per page
     * @return a List of forked projects
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getForks(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage),"projects", getProjectIdOrPath(projectIdOrPath), "forks");
        return (response.readEntity(new GenericType<List<Project>>() { }));
    }

    /**
     * Get a Pager of projects that were forked from the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/forks</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of projects
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Project> getForks(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return new Pager<Project>(this, Project.class, itemsPerPage, null, "projects", getProjectIdOrPath(projectIdOrPath), "forks");
    }

    /**
     * Get a Stream of projects that were forked from the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/forks</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a Stream of forked projects
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Project> getForksStream(Object projectIdOrPath) throws GitLabApiException {
        return (getForks(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Star a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/star</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @return a Project instance with the new project info
     * @throws GitLabApiException if any exception occurs
     */
    public Project starProject(Object projectIdOrPath) throws GitLabApiException {
        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.CREATED);
        Response response = post(expectedStatus, (Form) null, "projects", getProjectIdOrPath(projectIdOrPath), "star");
        return (response.readEntity(Project.class));
    }

    /**
     * Unstar a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/unstar</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @return a Project instance with the new project info
     * @throws GitLabApiException if any exception occurs
     */
    public Project unstarProject(Object projectIdOrPath) throws GitLabApiException {
        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.CREATED);
        Response response = post(expectedStatus, (Form) null, "projects", getProjectIdOrPath(projectIdOrPath), "unstar");
        return (response.readEntity(Project.class));
    }

    /**
     * Get languages used in a project with percentage value.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/languages</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a Map instance with the language as the key and the percentage as the value
     * @throws GitLabApiException if any exception occurs
     * @since GitLab 10.8
     */
    public Map<String, Float> getProjectLanguages(Object projectIdOrPath) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "languages");
        return (response.readEntity(new GenericType<Map<String, Float>>() {}));
    }

    /**
     * Transfer a project to a new namespace.  This was added in GitLab 11.1
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/transfer.</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param namespace the namespace to transfer the project to
     * @return the updated Project
     * @throws GitLabApiException if any exception occurs
     */
    public Project transferProject(Object projectIdOrPath, String namespace) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("namespace", namespace, true);
        Response response = put(Response.Status.OK, formData.asMap(), "projects", getProjectIdOrPath(projectIdOrPath), "transfer");
        return (response.readEntity(Project.class));
    }

    /**
     * Uploads and sets the project avatar for the specified project.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param avatarFile the File instance of the avatar file to upload
     * @return the updated Project instance
     * @throws GitLabApiException if any exception occurs
     */
    public Project setProjectAvatar(Object projectIdOrPath, File avatarFile) throws GitLabApiException {
        Response response = putUpload(Response.Status.OK,
                "avatar", avatarFile, "projects", getProjectIdOrPath(projectIdOrPath));
        return (response.readEntity(Project.class));
    }

    /**
     * Get a List of the project audit events viewable by Maintainer or an Owner of the group.
     *
     * <pre><code>GET /projects/:id/audit_events</code></pre>
     *
     * @param projectIdOrPath the project ID, path of the project, or a project instance holding the project ID or path
     * @param created_after Project audit events created on or after the given time.
     * @param created_before Project audit events created on or before the given time.
     * @return a List of project Audit events
     * @throws GitLabApiException if any exception occurs
     */
    public List<AuditEvent> getAuditEvents(Object projectIdOrPath, Date created_after, Date created_before) throws GitLabApiException {
        return (getAuditEvents(projectIdOrPath, created_after, created_before, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of the group audit events viewable by Maintainer or an Owner of the group.
     *
     * <pre><code>GET /projects/:id/audit_events</code></pre>
     *
     * @param projectIdOrPath the project ID, path of the project, or a Project instance holding the project ID or path
     * @param created_after Project audit events created on or after the given time.
     * @param created_before Project audit events created on or before the given time.
     * @param itemsPerPage the number of Audit Event instances that will be fetched per page
     * @return a Pager of project Audit events
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<AuditEvent> getAuditEvents(Object projectIdOrPath, Date created_after, Date created_before, int itemsPerPage) throws GitLabApiException {
        Form form = new GitLabApiForm()
                .withParam("created_before", ISO8601.toString(created_before, false))
                .withParam("created_after", ISO8601.toString(created_after, false));
        return (new Pager<AuditEvent>(this, AuditEvent.class, itemsPerPage, form.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "audit_events"));
    }

    /**
     * Get a Stream of the group audit events viewable by Maintainer or an Owner of the group.
     *
     * <pre><code>GET /projects/:id/audit_events</code></pre>
     *
     * @param projectIdOrPath the project ID, path of the project, or a Project instance holding the project ID or path
     * @param created_after Project audit events created on or after the given time.
     * @param created_before Project audit events created on or before the given time.
     * @return a Stream of project Audit events
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<AuditEvent> getAuditEventsStream(Object projectIdOrPath, Date created_after, Date created_before) throws GitLabApiException {
        return (getAuditEvents(projectIdOrPath, created_after, created_before, getDefaultPerPage()).stream());
    }

    /**
     * Get a specific audit event of a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/audit_events/:id_audit_event</code></pre>
     *
     * @param projectIdOrPath the project ID, path of the project, or a Project instance holding the project ID or path
     * @param auditEventId the auditEventId, required
     * @return the project Audit event
     * @throws GitLabApiException if any exception occurs
     */
    public AuditEvent getAuditEvent(Object projectIdOrPath, Long auditEventId) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "audit_events", auditEventId);
        return (response.readEntity(AuditEvent.class));
    }

    /**
     * Get list of a project's variables.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/variables</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a list of variables belonging to the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public List<Variable> getVariables(Object projectIdOrPath) throws GitLabApiException {
        return (getVariables(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a list of variables for the specified project in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/variables</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param page the page to get
     * @param perPage the number of Variable instances per page
     * @return a list of variables belonging to the specified project in the specified page range
     * @throws GitLabApiException if any exception occurs
     */
    public List<Variable> getVariables(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "projects", getProjectIdOrPath(projectIdOrPath), "variables");
        return (response.readEntity(new GenericType<List<Variable>>() {}));
    }

    /**
     * Get a Pager of variables belonging to the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/variables</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param itemsPerPage the number of Variable instances that will be fetched per page
     * @return a Pager of variables belonging to the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Variable> getVariables(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Variable>(this, Variable.class, itemsPerPage, null, "projects", getProjectIdOrPath(projectIdOrPath), "variables"));
    }

    /**
     * Get a Stream of variables belonging to the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/variables</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a Stream of variables belonging to the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Variable> getVariablesStream(Object projectIdOrPath) throws GitLabApiException {
        return (getVariables(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get the details of a project variable.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/variables/:key</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param key the key of an existing variable, required
     * @return the Variable instance for the specified variable
     * @throws GitLabApiException if any exception occurs
     */
    public Variable getVariable(Object projectIdOrPath, String key) throws GitLabApiException {
      Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "variables", key);
      return (response.readEntity(Variable.class));
    }

    /**
     * Get the details of a variable as an Optional instance.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/variables/:key</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param key the key of an existing variable, required
     * @return the Variable for the specified variable as an Optional instance
     */
    public Optional<Variable> getOptionalVariable(Object projectIdOrPath, String key) {
        try {
            return (Optional.ofNullable(getVariable(projectIdOrPath, key)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Create a new project variable.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/variables</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param key the key of a variable; must have no more than 255 characters; only A-Z, a-z, 0-9, and _ are allowed, required
     * @param value the value for the variable, required
     * @param isProtected whether the variable is protected, optional
     * @return a Variable instance with the newly created variable
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Variable createVariable(Object projectIdOrPath, String key, String value, Boolean isProtected) throws GitLabApiException {
        return (createVariable(projectIdOrPath, key, value, isProtected, (String) null));
    }

    /**
     * Create a new project variable.
     *
     * <p>NOTE: Setting the environmentScope is only available on GitLab EE.</p>
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/variables</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param key the key of a variable; must have no more than 255 characters; only A-Z, a-z, 0-9, and _ are allowed, required
     * @param value the value for the variable, required
     * @param isProtected whether the variable is protected, optional
     * @param environmentScope the environment_scope of the variable, optional
     * @return a Variable instance with the newly created variable
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Variable createVariable(Object projectIdOrPath, String key, String value, Boolean isProtected, String environmentScope) throws GitLabApiException {
        return createVariable(projectIdOrPath, key, value, null, isProtected, null, environmentScope);
    }

    /**
     * Create a new project variable.
     *
     * <p>NOTE: Setting the environmentScope is only available on GitLab EE.</p>
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/variables</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param key the key of a variable; must have no more than 255 characters; only A-Z, a-z, 0-9, and _ are allowed, required
     * @param value the value for the variable, required
     * @param variableType the type of variable. Available types are: env_var (default) and file
     * @param isProtected whether the variable is protected, optional
     * @param isMasked whether the variable is masked, optional
     * @return a Variable instance with the newly created variable
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Variable createVariable(Object projectIdOrPath, String key, String value, Variable.Type variableType,
            Boolean isProtected, Boolean isMasked) throws GitLabApiException {
        return createVariable(projectIdOrPath, key, value, variableType, isProtected, isMasked, null);
    }

    /**
     * Create a new project variable.
     *
     * <p>NOTE: Setting the environmentScope is only available on GitLab EE.</p>
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/variables</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param key the key of a variable; must have no more than 255 characters; only A-Z, a-z, 0-9, and _ are allowed, required
     * @param value the value for the variable, required
     * @param variableType the type of variable. Available types are: env_var (default) and file
     * @param isProtected whether the variable is protected, optional
     * @param isMasked whether the variable is masked, optional
     * @param environmentScope the environment_scope of the variable, optional
     * @return a Variable instance with the newly created variable
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Variable createVariable(Object projectIdOrPath, String key, String value, Variable.Type variableType,
            Boolean isProtected, Boolean isMasked, String environmentScope) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("key", key, true)
                .withParam("value", value, true)
                .withParam("variable_type", variableType)
                .withParam("protected", isProtected)
                .withParam("masked", isMasked)
                .withParam("environment_scope",  environmentScope);
        Response response = post(Response.Status.CREATED, formData, "projects", getProjectIdOrPath(projectIdOrPath), "variables");
        return (response.readEntity(Variable.class));
    }

    /**
     * Update a project variable.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/variables/:key</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param key the key of an existing variable, required
     * @param value the value for the variable, required
     * @param isProtected whether the variable is protected, optional
     * @return a Variable instance with the updated variable
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Variable updateVariable(Object projectIdOrPath, String key, String value, Boolean isProtected) throws GitLabApiException {
        return (updateVariable(projectIdOrPath, key, value, null, isProtected, null, null));
    }

    /**
     * Update a project variable.
     *
     * <p>NOTE: Updating the environmentScope is only available on GitLab EE.</p>
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/variables/:key</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param key the key of an existing variable, required
     * @param value the value for the variable, required
     * @param isProtected whether the variable is protected, optional
     * @param environmentScope the environment_scope of the variable, optional.
     * @return a Variable instance with the updated variable
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Variable updateVariable(Object projectIdOrPath, String key, String value, Boolean isProtected, String environmentScope) throws GitLabApiException {
        return updateVariable(projectIdOrPath, key, value, null, isProtected, null, environmentScope);
    }

    /**
     * Update a project variable.
     *
     * <p>NOTE: Updating the environmentScope is only available on GitLab EE.</p>
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/variables/:key</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param key the key of an existing variable, required
     * @param value the value for the variable, required
     * @param variableType the type of variable. Available types are: env_var (default) and file
     * @param isProtected whether the variable is protected, optional
     * @param masked whether the variable is masked, optional
     * @return a Variable instance with the updated variable
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Variable updateVariable(Object projectIdOrPath, String key, String value, Variable.Type variableType,
            Boolean isProtected, Boolean masked) throws GitLabApiException {
        return updateVariable(projectIdOrPath, key, value, variableType, isProtected, masked, null);
    }

    /**
     * Update a project variable.
     *
     * <p>NOTE: Updating the environmentScope is only available on GitLab EE.</p>
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/variables/:key</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param key the key of an existing variable, required
     * @param value the value for the variable, required
     * @param variableType the type of variable. Available types are: env_var (default) and file
     * @param isProtected whether the variable is protected, optional
     * @param masked whether the variable is masked, optional
     * @param environmentScope the environment_scope of the variable, optional.
     * @return a Variable instance with the updated variable
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Variable updateVariable(Object projectIdOrPath, String key, String value, Variable.Type variableType,
            Boolean isProtected, Boolean masked, String environmentScope) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("value", value, true)
                .withParam("variable_type", variableType)
                .withParam("protected", isProtected)
                .withParam("masked", masked)
                .withParam("environment_scope", environmentScope);
        Response response = putWithFormData(Response.Status.OK, formData, "projects", getProjectIdOrPath(projectIdOrPath), "variables", key);
        return (response.readEntity(Variable.class));
    }

    /**
     * Deletes a project variable.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/variables/:key</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param key the key of an existing variable, required
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteVariable(Object projectIdOrPath, String key) throws GitLabApiException {
        delete(Response.Status.NO_CONTENT, null, "projects", getProjectIdOrPath(projectIdOrPath), "variables", key);
    }

    /**
     * Get a List of the project access requests viewable by the authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/access_requests</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a List of project AccessRequest instances accessible by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public List<AccessRequest> getAccessRequests(Object projectIdOrPath) throws GitLabApiException {
        return (getAccessRequests(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of the project access requests viewable by the authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/access_requests</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of AccessRequest instances that will be fetched per page
     * @return a Pager of project AccessRequest instances accessible by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<AccessRequest> getAccessRequests(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<AccessRequest>(this, AccessRequest.class, itemsPerPage, null, "projects", getProjectIdOrPath(projectIdOrPath), "access_requests"));
    }

    /**
     * Get a Stream of the project access requests viewable by the authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/access_requests</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a Stream of project AccessRequest instances accessible by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<AccessRequest> getAccessRequestsStream(Object projectIdOrPath) throws GitLabApiException {
       return (getAccessRequests(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Requests access for the authenticated user to the specified project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/access_requests</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return the created AccessRequest instance
     * @throws GitLabApiException if any exception occurs
     */
    public AccessRequest requestAccess(Object projectIdOrPath) throws GitLabApiException {
        Response response = post(Response.Status.CREATED, (Form)null, "projects", getProjectIdOrPath(projectIdOrPath), "access_requests");
        return (response.readEntity(AccessRequest.class));
    }

    /**
     * Approve access for the specified user to the specified project.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/access_requests/:user_id/approve</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param userId the user ID to approve access for
     * @param accessLevel the access level the user is approved for, if null will be developer (30)
     * @return the approved AccessRequest instance
     * @throws GitLabApiException if any exception occurs
     */
    public AccessRequest approveAccessRequest(Object projectIdOrPath, Long userId, AccessLevel accessLevel) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("access_level", accessLevel);
        Response response = this.putWithFormData(Response.Status.CREATED, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "access_requests", userId, "approve");
        return (response.readEntity(AccessRequest.class));
    }

    /**
     * Deny access for the specified user to the specified project.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/access_requests/:user_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param userId the user ID to deny access for
     * @throws GitLabApiException if any exception occurs
     */
    public void denyAccessRequest(Object projectIdOrPath, Long userId) throws GitLabApiException {
        delete(Response.Status.NO_CONTENT, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "access_requests", userId);
    }

    /**
     * Start the Housekeeping task for a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/housekeeping</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @throws GitLabApiException if any exception occurs
     */
    public void triggerHousekeeping(Object projectIdOrPath) throws GitLabApiException {
        post(Response.Status.OK, (Form) null, "projects", getProjectIdOrPath(projectIdOrPath), "housekeeping");
    }

    /**
     * Gets a list of a project’s badges and its group badges.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/badges</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a List of Badge instances for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public List<Badge> getBadges(Object projectIdOrPath) throws GitLabApiException {
	return getBadges(projectIdOrPath, null);
    }

    /**
     * Gets a list of a project’s badges and its group badges, case-sensitively filtered on bagdeName if non-null.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/badges?name=:name</code></pre>
     *
     * @param projectIdOrPath the project in the form of a Long(ID), String(path), or Project instance
     * @param bagdeName The name to filter on (case-sensitive), ignored if null.
     * @return All badges of the GitLab item, case insensitively filtered on name.
     * @throws GitLabApiException If any problem is encountered
     */
    public List<Badge> getBadges(Object projectIdOrPath, String bagdeName) throws GitLabApiException {
    Form queryParam = new GitLabApiForm().withParam("name", bagdeName);
    Response response = get(Response.Status.OK, queryParam.asMap(), "projects", getProjectIdOrPath(projectIdOrPath), "badges");
    return (response.readEntity(new GenericType<List<Badge>>() {}));
    }

    /**
     * Gets a badge of a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/badges/:badge_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param badgeId the ID of the badge to get
     * @return a Badge instance for the specified project/badge ID pair
     * @throws GitLabApiException if any exception occurs
     */
    public Badge getBadge(Object projectIdOrPath, Long badgeId) throws GitLabApiException {
	Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "badges", badgeId);
	return (response.readEntity(Badge.class));
    }

    /**
     * Get an Optional instance with the value for the specified badge.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/badges/:badge_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param badgeId the ID of the badge to get
     * @return an Optional instance with the specified badge as the value
     */
    public Optional<Badge> getOptionalBadge(Object projectIdOrPath, Long badgeId) {
	try {
	    return (Optional.ofNullable(getBadge(projectIdOrPath, badgeId)));
	} catch (GitLabApiException glae) {
	    return (GitLabApi.createOptionalFromException(glae));
	}
    }

    /**
     * Add a badge to a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/badges</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param linkUrl the URL of the badge link
     * @param imageUrl the URL of the image link
     * @return a Badge instance for the added badge
     * @throws GitLabApiException if any exception occurs
     */
    public Badge addBadge(Object projectIdOrPath, String linkUrl, String imageUrl) throws GitLabApiException {
    return addBadge(projectIdOrPath, null, linkUrl, imageUrl);
    }

    /**
     * Add a badge to a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/badges</code></pre>
     *
     * @param projectIdOrPath the project in the form of a Long(ID), String(path), or Project instance
     * @param name The name to give the badge (may be null)
     * @param linkUrl the URL of the badge link
     * @param imageUrl the URL of the image link
     * @return A Badge instance for the added badge
     * @throws GitLabApiException if any exception occurs
     */
    public Badge addBadge(Object projectIdOrPath, String name, String linkUrl, String imageUrl) throws GitLabApiException {
    GitLabApiForm formData = new GitLabApiForm()
        .withParam("name", name, false)
        .withParam("link_url", linkUrl, true)
        .withParam("image_url", imageUrl, true);
    Response response = post(Response.Status.OK, formData, "projects", getProjectIdOrPath(projectIdOrPath), "badges");
    return (response.readEntity(Badge.class));
    }

    /**
     * Edit a badge of a project.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/badges</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param badgeId the ID of the badge to get
     * @param linkUrl the URL of the badge link
     * @param imageUrl the URL of the image link
     * @return a Badge instance for the editted badge
     * @throws GitLabApiException if any exception occurs
     */
    public Badge editBadge(Object projectIdOrPath, Long badgeId, String linkUrl, String imageUrl) throws GitLabApiException {
	return (editBadge(projectIdOrPath, badgeId, null, linkUrl, imageUrl));
    }

    /**
     * Edit a badge of a project.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/badges</code></pre>
     *
     * @param projectIdOrPath the project in the form of a Long(ID), String(path), or Project instance
     * @param badgeId the ID of the badge to edit
     * @param name The name of the badge to edit (may be null)
     * @param linkUrl the URL of the badge link
     * @param imageUrl the URL of the image link
     * @return a Badge instance for the editted badge
     * @throws GitLabApiException if any exception occurs
     */
    public Badge editBadge(Object projectIdOrPath, Long badgeId, String name, String linkUrl, String imageUrl) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
            .withParam("name", name, false)
            .withParam("link_url", linkUrl, false)
            .withParam("image_url", imageUrl, false);
        Response response = putWithFormData(Response.Status.OK, formData, "projects", getProjectIdOrPath(projectIdOrPath), "badges", badgeId);
        return (response.readEntity(Badge.class));
    }

    /**
     * Remove a badge from a project.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/badges/:badge_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param badgeId the ID of the badge to remove
     * @throws GitLabApiException if any exception occurs
     */
    public void removeBadge(Object projectIdOrPath, Long badgeId) throws GitLabApiException {
	delete(Response.Status.NO_CONTENT, null, "projects", getProjectIdOrPath(projectIdOrPath), "badges", badgeId);
    }

    /**
     * Returns how the link_url and image_url final URLs would be after resolving the placeholder interpolation.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/badges/render</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param linkUrl the URL of the badge link
     * @param imageUrl the URL of the image link
     * @return a Badge instance for the rendered badge
     * @throws GitLabApiException if any exception occurs
     */
    public Badge previewBadge(Object projectIdOrPath,  String linkUrl, String imageUrl) throws GitLabApiException {
	GitLabApiForm formData = new GitLabApiForm()
		.withParam("link_url", linkUrl, true)
		.withParam("image_url", imageUrl, true);
	Response response = get(Response.Status.OK, formData.asMap(), "projects", getProjectIdOrPath(projectIdOrPath), "badges", "render");
	return (response.readEntity(Badge.class));
    }

    /**
     * Get the project's approval information.
     * Note: This API endpoint is only available on 10.6 Starter and above.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/approvals</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a ProjectApprovalsConfig instance with the project's approvals configuration
     * @throws GitLabApiException if any exception occurs
     */
    public ProjectApprovalsConfig getApprovalsConfiguration(Object projectIdOrPath) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "approvals");
        return (response.readEntity(ProjectApprovalsConfig.class));
    }

    /**
     * Set the project's approvals configuration.
     * Note: This API endpoint is only available on 10.6 Starter and above.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/approvals</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param config a ProjectApprovalsConfig instance with the approval configuration
     * @return a ProjectApprovalsConfig instance with the project's approvals configuration
     * @throws GitLabApiException if any exception occurs
     */
    public ProjectApprovalsConfig setApprovalsConfiguration(Object projectIdOrPath, ProjectApprovalsConfig config) throws GitLabApiException {
	GitLabApiForm formData = config.getForm();
        Response response = post(Response.Status.OK, formData, "projects", getProjectIdOrPath(projectIdOrPath), "approvals");
        return (response.readEntity(ProjectApprovalsConfig.class));
    }

    /**
     * Get a list of the project-level approval rules.
     * Note: This API endpoint is only available on 12.3 Starter and above.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/approval_rules</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a List of ApprovalRuke instances for the specified project.
     * @throws GitLabApiException if any exception occurs
     */
    public List<ApprovalRule> getApprovalRules(Object projectIdOrPath) throws GitLabApiException {
        return (getApprovalRules(projectIdOrPath, -1).all());
    }

    /**
     * Get a Pager of the project-level approval rules.
     * Note: This API endpoint is only available on 12.3 Starter and above.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/approval_rules</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of ApprovalRule instances that will be fetched per page
     * @return a Pager of ApprovalRuke instances for the specified project.
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<ApprovalRule> getApprovalRules(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {

	return (new Pager<ApprovalRule>(this, ApprovalRule.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "approval_rules"));
    }

    /**
     * Get a Stream of the project-level approval rules.
     * Note: This API endpoint is only available on 12.3 Starter and above.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/approval_rules</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a Stream of ApprovalRule instances for the specified project.
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<ApprovalRule> getApprovalRulesStream(Object projectIdOrPath) throws GitLabApiException {
        return (getApprovalRules(projectIdOrPath, -1).stream());
    }

    /**
     * Create a project-level approval rule.
     * Note: This API endpoint is only available on 12.3 Starter and above.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/approval_rules</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param params the ApprovalRuleParams instance holding the parameters for the approval rule
     * @return a ApprovalRule instance with approval configuration
     * @throws GitLabApiException if any exception occurs
     */
    public ApprovalRule createApprovalRule(Object projectIdOrPath, ApprovalRuleParams params) throws GitLabApiException {
        GitLabApiForm formData = params.getForm();
        Response response = post(Response.Status.OK, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "approval_rules");
        return (response.readEntity(ApprovalRule.class));
    }

    /**
     * Update the specified the project-level approval rule.
     * Note: This API endpoint is only available on 12.3 Starter and above.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/approval_rules/:approval_rule_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param approvalRuleId the ID of the approval rule
     * @param params the ApprovalRuleParams instance holding the parameters for the approval rule update
     * @return a ApprovalRule instance with approval configuration
     * @throws GitLabApiException if any exception occurs
     */
    public ApprovalRule updateApprovalRule(Object projectIdOrPath, Long approvalRuleId, ApprovalRuleParams params) throws GitLabApiException {

        if (approvalRuleId == null) {
            throw new RuntimeException("approvalRuleId cannot be null");
        }

        GitLabApiForm formData = params.getForm();
        Response response = putWithFormData(Response.Status.OK, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "approval_rules", approvalRuleId);
        return (response.readEntity(ApprovalRule.class));
    }

    /**
     * Delete the specified the project-level approval rule.
     * Note: This API endpoint is only available on 12.3 Starter and above.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/approval_rules/:approval_rule_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param approvalRuleId the ID of the approval rule
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteApprovalRule(Object projectIdOrPath, Long approvalRuleId) throws GitLabApiException {

        if (approvalRuleId == null) {
            throw new RuntimeException("approvalRuleId cannot be null");
        }

        delete(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "approval_rules", approvalRuleId);
    }

    /**
     * Get all custom attributes for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/custom_attributes</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a list of project's CustomAttributes
     * @throws GitLabApiException if any exception occurs
     */
    public List<CustomAttribute> getCustomAttributes(final Object projectIdOrPath) throws GitLabApiException {
        return (getCustomAttributes(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of custom attributes for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/custom_attributes</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of items per page
     * @return a Pager of project's custom attributes
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<CustomAttribute> getCustomAttributes(final Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<CustomAttribute>(this, CustomAttribute.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "custom_attributes"));
    }

    /**
     * Get a Stream of all custom attributes for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/custom_attributes</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a Stream of project's custom attributes
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<CustomAttribute> getCustomAttributesStream(final Object projectIdOrPath) throws GitLabApiException {
        return (getCustomAttributes(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get a single custom attribute for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/custom_attributes/:key</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param key the key for the custom attribute
     * @return a CustomAttribute instance for the specified key
     * @throws GitLabApiException if any exception occurs
     */
    public CustomAttribute getCustomAttribute(final Object projectIdOrPath, final String key) throws GitLabApiException {
	Response response = get(Response.Status.OK, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "custom_attributes", key);
        return (response.readEntity(CustomAttribute.class));
    }

    /**
     * Get an Optional instance with the value for a single custom attribute for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/custom_attributes/:key</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param key the key for the custom attribute, required
     * @return an Optional instance with the value for a single custom attribute for the specified project
     */
    public Optional<CustomAttribute> geOptionalCustomAttribute(final Object projectIdOrPath, final String key) {
        try {
            return (Optional.ofNullable(getCustomAttribute(projectIdOrPath, key)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Set a custom attribute for the specified project. The attribute will be updated if it already exists,
     * or newly created otherwise.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/custom_attributes/:key</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param key the key for the custom attribute
     * @param value the value for the customAttribute
     * @return a CustomAttribute instance for the updated or created custom attribute
     * @throws GitLabApiException if any exception occurs
     */
    public CustomAttribute setCustomAttribute(final Object projectIdOrPath, final String key, final String value) throws GitLabApiException {

        if (Objects.isNull(key) || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Value cannot be null or empty");
        }

        GitLabApiForm formData = new GitLabApiForm().withParam("value", value);
        Response response = putWithFormData(Response.Status.OK, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "custom_attributes", key);
        return (response.readEntity(CustomAttribute.class));
    }

    /**
     * Delete a custom attribute for the specified project.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/custom_attributes/:key</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param key the key of the custom attribute to delete
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteCustomAttribute(final Object projectIdOrPath, final String key) throws GitLabApiException {

        if (Objects.isNull(key) || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key can't be null or empty");
        }

        delete(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "custom_attributes", key);
    }

    /**
     * Get all remote mirrors and their statuses for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/remote_mirrors</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a list of project's remote mirrors
     * @throws GitLabApiException if any exception occurs
     */
    public List<RemoteMirror> getRemoteMirrors(final Object projectIdOrPath) throws GitLabApiException {
        return (getRemoteMirrors(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of remote mirrors and their statuses for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/remote_mirrors</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of items per page
     * @return a Pager of project's remote mirrors
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<RemoteMirror> getRemoteMirrors(final Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<RemoteMirror>(this, RemoteMirror.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "remote_mirrors"));
    }

    /**
     * Get a Stream of all remote mirrors and their statuses for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/remote_mirrors</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a Stream of project's remote mirrors
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<RemoteMirror> getRemoteMirrorsStream(final Object projectIdOrPath) throws GitLabApiException {
        return (getRemoteMirrors(projectIdOrPath, getDefaultPerPage()).stream());
    }


    /**
     * Create a remote mirror for a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/remote_mirrors</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param url the URL of the remote repository to be mirrored
     * @param enabled determines if the mirror is enabled
     * @param onlyProtectedBranches determines if only protected branches are mirrored
     * @param keepDivergentRefs determines if divergent refs are skipped
     * @return a RemoteMirror instance with remote mirror configuration
     * @throws GitLabApiException if any exception occurs
     */
    public RemoteMirror createRemoteMirror(Object projectIdOrPath, String url, Boolean enabled,
	    Boolean onlyProtectedBranches, Boolean keepDivergentRefs) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("url", url, true)
                .withParam("enabled", enabled)
                .withParam("only_protected_branches", onlyProtectedBranches)
                .withParam("keep_divergent_refs", keepDivergentRefs);
        Response response = post(Response.Status.OK, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "remote_mirrors");
        return (response.readEntity(RemoteMirror.class));
    }

    /**
     * Toggle a remote mirror on or off, or change which types of branches are mirrored.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/remote_mirrors/:mirror_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param mirrorId the ID of the remote mirror
     * @param enabled determines if the mirror is enabled
     * @param onlyProtectedBranches determines if only protected branches are mirrored
     * @param keepDivergentRefs determines if divergent refs are skipped
     * @return a RemoteMirror instance with the updated remote mirror configuration
     * @throws GitLabApiException if any exception occurs
     */
    public RemoteMirror updateRemoteMirror(Object projectIdOrPath, Long mirrorId, Boolean enabled,
	    Boolean onlyProtectedBranches, Boolean keepDivergentRefs) throws GitLabApiException {

	GitLabApiForm formData = new GitLabApiForm()
		.withParam("enabled", enabled)
		.withParam("only_protected_branches", onlyProtectedBranches)
		.withParam("keep_divergent_refs", keepDivergentRefs);
        Response response = putWithFormData(Response.Status.OK, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "remote_mirrors", mirrorId);
        return (response.readEntity(RemoteMirror.class));
    }
}
