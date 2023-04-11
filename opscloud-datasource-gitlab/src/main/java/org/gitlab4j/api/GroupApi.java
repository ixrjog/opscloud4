package org.gitlab4j.api;

import org.gitlab4j.api.GitLabApi.ApiVersion;
import org.gitlab4j.api.models.*;
import org.gitlab4j.api.utils.ISO8601;

import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This class implements the client side API for the GitLab groups calls.
 * @see <a href="https://docs.gitlab.com/ce/api/groups.html">Groups API at GitLab</a>
 * @see <a href="https://docs.gitlab.com/ce/api/members.html">Group and project members API at GitLab</a>
 * @see <a href="https://docs.gitlab.com/ce/api/access_requests.html">Group and project access requests API</a>
 * @see <a href="https://docs.gitlab.com/ce/api/group_badges.html">Group badges API</a>
 * @see <a href="https://docs.gitlab.com/ee/api/audit_events.html#retrieve-all-group-audit-events">Group audit events API</a>
 */
public class GroupApi extends AbstractApi {

    public GroupApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * <p>Get a list of groups. (As user: my groups, as admin: all groups)</p>
     *
     * <strong>WARNING:</strong> Do not use this method to fetch groups from https://gitlab.com,
     * gitlab.com has many 1,000's of public groups and it will a long time to fetch all of them.
     * Instead use {@link #getGroups(int itemsPerPage)} which will return a Pager of Group instances.
     *
     * <pre><code>GitLab Endpoint: GET /groups</code></pre>
     *
     * @return the list of groups viewable by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public List<Group> getGroups() throws GitLabApiException {

        String url = this.gitLabApi.getGitLabServerUrl();
        if (url.startsWith("https://gitlab.com")) {
            GitLabApi.getLogger().warning("Fetching all groups from " + url +
                    " may take many minutes to complete, use Pager<Group> getGroups(int) instead.");
        }

        return (getGroups(getDefaultPerPage()).all());
    }

    /**
     * Get a list of groups (As user: my groups, as admin: all groups) and in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /groups</code></pre>
     *
     * @param page the page to get
     * @param perPage the number of Group instances per page
     * @return the list of groups viewable by the authenticated userin the specified page range
     * @throws GitLabApiException if any exception occurs
     */
    public List<Group> getGroups(int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "groups");
        return (response.readEntity(new GenericType<List<Group>>() {}));
    }

    /**
     * Get a Pager of groups. (As user: my groups, as admin: all groups)
     *
     * <pre><code>GitLab Endpoint: GET /groups</code></pre>
     *
     * @param itemsPerPage the number of Group instances that will be fetched per page
     * @return the list of groups viewable by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Group> getGroups(int itemsPerPage) throws GitLabApiException {
        return (new Pager<Group>(this, Group.class, itemsPerPage, null, "groups"));
    }

    /**
     * Get a Stream of groups. (As user: my groups, as admin: all groups)
     *
     * <pre><code>GitLab Endpoint: GET /groups</code></pre>
     *
     * @return a Stream of groups viewable by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Group> getGroupsStream() throws GitLabApiException {
        return (getGroups(getDefaultPerPage()).stream());
    }

    /**
     * Get all groups that match your string in their name or path.
     *
     * @param search the group name or path search criteria
     * @return a List containing matching Group instances
     * @throws GitLabApiException if any exception occurs
     */
    public List<Group> getGroups(String search) throws GitLabApiException {
        return (getGroups(search, getDefaultPerPage()).all());
    }

    /**
     * Get all groups that match your string in their name or path.
     *
     * @param search the group name or path search criteria
     * @param page the page to get
     * @param perPage the number of Group instances per page
     * @return a List containing matching Group instances
     * @throws GitLabApiException if any exception occurs
     */
    public List<Group> getGroups(String search, int page, int perPage) throws GitLabApiException {
        Form formData = new GitLabApiForm().withParam("search", search).withParam(PAGE_PARAM, page).withParam(PER_PAGE_PARAM, perPage);
        Response response = get(Response.Status.OK, formData.asMap(), "groups");
        return (response.readEntity(new GenericType<List<Group>>() {}));
    }

    /**
     * Get all groups that match your string in their name or path.
     *
     * @param search the group name or path search criteria
     * @param itemsPerPage the number of Group instances that will be fetched per page
     * @return a Pager containing matching Group instances
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Group> getGroups(String search, int itemsPerPage) throws GitLabApiException {
        Form formData = new GitLabApiForm().withParam("search", search);
        return (new Pager<Group>(this, Group.class, itemsPerPage, formData.asMap(), "groups"));
    }

    /**
     * Get all groups that match your string in their name or path as a Stream.
     *
     * @param search the group name or path search criteria
     * @return a Stream containing matching Group instances
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Group> getGroupsStream(String search) throws GitLabApiException {
        return (getGroups(search, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of visible groups for the authenticated user using the provided filter.
     *
     * <pre><code>GitLab Endpoint: GET /groups</code></pre>
     *
     * @param filter the GroupFilter to match against
     * @return a List&lt;Group&gt; of the matching groups
     * @throws GitLabApiException if any exception occurs
     */
    public List<Group> getGroups(GroupFilter filter) throws GitLabApiException {
        return (getGroups(filter, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of visible groups for the authenticated user using the provided filter.
     *
     * <pre><code>GitLab Endpoint: GET /groups</code></pre>
     *
     * @param filter the GroupFilter to match against
     * @param itemsPerPage the number of Group instances that will be fetched per page
     * @return a Pager containing matching Group instances
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Group> getGroups(GroupFilter filter, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = filter.getQueryParams();
        return (new Pager<Group>(this, Group.class, itemsPerPage, formData.asMap(), "groups"));
    }

    /**
     * Get a Stream of visible groups for the authenticated user using the provided filter.
     *
     * <pre><code>GitLab Endpoint: GET /groups</code></pre>
     *
     * @param filter the GroupFilter to match against
     * @return a Stream&lt;Group&gt; of the matching groups
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Group> getGroupsStream(GroupFilter filter) throws GitLabApiException {
        return (getGroups(filter, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of visible direct subgroups in this group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/subgroups</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @return a List&lt;Group&gt; containing the group's sub-groups
     * @throws GitLabApiException if any exception occurs
     * @since GitLab 10.3.0
     */
    public List<Group> getSubGroups(Object groupIdOrPath) throws GitLabApiException {
        return (getSubGroups(groupIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of visible direct subgroups in this group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/subgroups</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param itemsPerPage the number of Group instances that will be fetched per page
     * @return a Pager containing matching Group instances
     * @throws GitLabApiException if any exception occurs
     * @since GitLab 10.3.0
     */
    public Pager<Group> getSubGroups(Object groupIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Group>(this, Group.class, itemsPerPage, null, "groups", getGroupIdOrPath(groupIdOrPath), "subgroups"));
    }

    /**
     * Get a Stream of visible direct subgroups in this group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/subgroups</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @return a Stream&lt;Group&gt; containing the group's sub-groups
     * @throws GitLabApiException if any exception occurs
     * @since GitLab 10.3.0
     */
    public Stream<Group> getSubGroupsStream(Object groupIdOrPath) throws GitLabApiException {
        return (getSubGroups(groupIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of visible direct subgroups in this group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/subgroups</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param skipGroups skip the group IDs passed
     * @param allAvailable show all the groups you have access to (defaults to false for authenticated users)
     * @param search return the list of authorized groups matching the search criteria
     * @param orderBy order groups by NAME or PATH. Default is NAME
     * @param sortOrder order groups in ASC or DESC order. Default is ASC
     * @param statistics include group statistics (admins only)
     * @param owned limit to groups owned by the current user
     * @return a List&lt;Group&gt; of the matching subgroups
     * @throws GitLabApiException if any exception occurs
     * @since GitLab 10.3.0
     */
    public List<Group> getSubGroups(Object groupIdOrPath, List<Integer> skipGroups, Boolean allAvailable, String search,
            GroupOrderBy orderBy, SortOrder sortOrder, Boolean statistics, Boolean owned) throws GitLabApiException {
        return (getSubGroups(groupIdOrPath, skipGroups, allAvailable, search, orderBy, sortOrder, statistics, owned, getDefaultPerPage()).all());
    }

    /**
     * Get a list of visible direct subgroups in this group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/subgroups</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param skipGroups skip the group IDs passed
     * @param allAvailable show all the groups you have access to (defaults to false for authenticated users)
     * @param search return the list of authorized groups matching the search criteria
     * @param orderBy order groups by NAME or PATH. Default is NAME
     * @param sortOrder order groups in ASC or DESC order. Default is ASC
     * @param statistics include group statistics (admins only)
     * @param owned limit to groups owned by the current user
     * @param page the page to get
     * @param perPage the number of Group instances per page
     * @return a List&lt;Group&gt; of the matching subgroups
     * @throws GitLabApiException if any exception occurs
     * @since GitLab 10.3.0
     */
    public List<Group> getSubGroups(Object groupIdOrPath, List<Integer> skipGroups, Boolean allAvailable, String search,
            GroupOrderBy orderBy, SortOrder sortOrder, Boolean statistics, Boolean owned,  int page, int perPage)
            throws GitLabApiException {
        Form formData = new GitLabApiForm()
                .withParam("skip_groups", skipGroups)
                .withParam("all_available", allAvailable)
                .withParam("search", search)
                .withParam("order_by", orderBy)
                .withParam("sort_order", sortOrder)
                .withParam("statistics", statistics)
                .withParam("owned", owned)
                .withParam(PAGE_PARAM, page)
                .withParam(PER_PAGE_PARAM, perPage);
        Response response = get(Response.Status.OK, formData.asMap(), "groups", getGroupIdOrPath(groupIdOrPath), "subgroups");
        return (response.readEntity(new GenericType<List<Group>>() {}));
    }

    /**
     * Get a Pager of visible direct subgroups in this group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/subgroups</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param skipGroups skip the group IDs passed
     * @param allAvailable show all the groups you have access to (defaults to false for authenticated users)
     * @param search return the list of authorized groups matching the search criteria
     * @param orderBy order groups by NAME or PATH. Default is NAME
     * @param sortOrder order groups in ASC or DESC order. Default is ASC
     * @param statistics include group statistics (admins only)
     * @param owned limit to groups owned by the current user
     * @param itemsPerPage the number of Group instances that will be fetched per page
     * @return a Pager containing matching Group instances
     * @throws GitLabApiException if any exception occurs
     * @since GitLab 10.3.0
     */
    public Pager<Group> getSubGroups(Object groupIdOrPath, List<Integer> skipGroups, Boolean allAvailable, String search,
            GroupOrderBy orderBy, SortOrder sortOrder, Boolean statistics, Boolean owned, int itemsPerPage) throws GitLabApiException {
        Form formData = new GitLabApiForm()
                .withParam("skip_groups", skipGroups)
                .withParam("all_available", allAvailable)
                .withParam("search", search)
                .withParam("order_by", orderBy)
                .withParam("sort_order", sortOrder)
                .withParam("statistics", statistics)
                .withParam("owned", owned);
        return (new Pager<Group>(this, Group.class, itemsPerPage, formData.asMap(), "groups", getGroupIdOrPath(groupIdOrPath), "subgroups"));
    }

    /**
     * Get a Stream of visible direct subgroups in this group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/subgroups</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param skipGroups skip the group IDs passed
     * @param allAvailable show all the groups you have access to (defaults to false for authenticated users)
     * @param search return the list of authorized groups matching the search criteria
     * @param orderBy order groups by NAME or PATH. Default is NAME
     * @param sortOrder order groups in ASC or DESC order. Default is ASC
     * @param statistics include group statistics (admins only)
     * @param owned limit to groups owned by the current user
     * @return a Stream&lt;Group&gt; of the matching subgroups
     * @throws GitLabApiException if any exception occurs
     * @since GitLab 10.3.0
     */
    public Stream<Group> getSubGroupsStream(Object groupIdOrPath, List<Integer> skipGroups, Boolean allAvailable, String search,
            GroupOrderBy orderBy, SortOrder sortOrder, Boolean statistics, Boolean owned) throws GitLabApiException {
        return (getSubGroups(groupIdOrPath, skipGroups, allAvailable, search, orderBy, sortOrder, statistics, owned, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of visible descendant groups of a given group for the authenticated user using the provided filter.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/descendant_groups</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param filter the GroupFilter to match against
     * @return a List&lt;Group&gt; of the matching groups
     * @throws GitLabApiException if any exception occurs
     */
    public List<Group> getDescendantGroups(Object groupIdOrPath, GroupFilter filter) throws GitLabApiException {
        return (getDescendantGroups(groupIdOrPath, filter, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of visible descendant groups of a given group for the authenticated user using the provided filter.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/descendant_groups</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param filter the GroupFilter to match against
     * @param itemsPerPage the number of Group instances that will be fetched per page
     * @return a Pager containing matching Group instances
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Group> getDescendantGroups(Object groupIdOrPath, GroupFilter filter, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = filter.getQueryParams();
        return (new Pager<Group>(this, Group.class, itemsPerPage, formData.asMap(), "groups", getGroupIdOrPath(groupIdOrPath), "descendant_groups"));
    }

    /**
     * Get a Stream of visible descendant groups of a given group for the authenticated user using the provided filter.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/descendant_groups</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param filter the GroupFilter to match against
     * @return a Stream&lt;Group&gt; of the matching groups
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Group> getDescendantGroupsStream(Object groupIdOrPath, GroupFilter filter) throws GitLabApiException {
        return (getDescendantGroups(groupIdOrPath, filter, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of projects belonging to the specified group ID and filter.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/projects</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param filter the GroupProjectsFilter instance holding the filter values for the query
     * @return a List containing Project instances that belong to the group and match the provided filter
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getProjects(Object groupIdOrPath, GroupProjectsFilter filter) throws GitLabApiException {
        return (getProjects(groupIdOrPath, filter, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of projects belonging to the specified group ID and filter.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/projects</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param filter the GroupProjectsFilter instance holding the filter values for the query
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager containing Project instances that belong to the group and match the provided filter
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Project> getProjects(Object groupIdOrPath, GroupProjectsFilter filter, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = filter.getQueryParams();
        return (new Pager<Project>(this, Project.class, itemsPerPage, formData.asMap(), "groups", getGroupIdOrPath(groupIdOrPath), "projects"));
    }

    /**
     * Get a Stream of projects belonging to the specified group ID and filter.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/projects</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param filter the GroupProjectsFilter instance holding the filter values for the query
     * @return a Stream containing Project instances that belong to the group and match the provided filter
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Project> getProjectsStream(Object groupIdOrPath, GroupProjectsFilter filter) throws GitLabApiException {
        return (getProjects(groupIdOrPath, filter, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of projects belonging to the specified group ID.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/projects</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @return a list of projects belonging to the specified group ID
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getProjects(Object groupIdOrPath) throws GitLabApiException {
        return (getProjects(groupIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a list of projects belonging to the specified group ID in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/projects</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param page the page to get
     * @param perPage the number of Project instances per page
     * @return a list of projects belonging to the specified group ID in the specified page range
     * @throws GitLabApiException if any exception occurs
     */
    public List<Project> getProjects(Object groupIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "groups", getGroupIdOrPath(groupIdOrPath), "projects");
        return (response.readEntity(new GenericType<List<Project>>() {}));
    }

    /**
     * Get a Pager of projects belonging to the specified group ID.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/projects</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of projects belonging to the specified group ID
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Project> getProjects(Object groupIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Project>(this, Project.class, itemsPerPage, null, "groups", getGroupIdOrPath(groupIdOrPath), "projects"));
    }

    /**
     * Get a Stream of projects belonging to the specified group ID.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/projects</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @return a Stream of projects belonging to the specified group ID
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Project> getProjectsStream(Object groupIdOrPath) throws GitLabApiException {
        return (getProjects(groupIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get all details of a group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @return the Group instance for the specified group path
     * @throws GitLabApiException if any exception occurs
     */
    public Group getGroup(Object groupIdOrPath) throws GitLabApiException {
      Response response = get(Response.Status.OK, null, "groups", getGroupIdOrPath(groupIdOrPath));
      return (response.readEntity(Group.class));
    }

    /**
     * Get all details of a group as an Optional instance.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @return the Group for the specified group path as an Optional instance
     */
    public Optional<Group> getOptionalGroup(Object groupIdOrPath) {
        try {
            return (Optional.ofNullable(getGroup(groupIdOrPath)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Creates a new project group. Available only for users who can create groups.
     *
     * <pre><code>GitLab Endpoint: POST /groups</code></pre>
     *
     * @param params a GroupParams instance holding the parameters for the group creation
     * @return the created Group instance
     * @throws GitLabApiException if any exception occurs
     */
    public Group createGroup(GroupParams params) throws GitLabApiException {
        Response response = post(Response.Status.CREATED, params.getForm(true), "groups");
        return (response.readEntity(Group.class));
    }

    /**
     * Updates the project group. Only available to group owners and administrators.
     *
     * <pre><code>GitLab Endpoint: PUT /groups</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param params the GroupParams instance holding the properties to update
     * @return updated Group instance
     * @throws GitLabApiException at any exception
     */
    public Group updateGroup(Object groupIdOrPath, GroupParams params) throws GitLabApiException {
        Response response = putWithFormData(Response.Status.OK,
                params.getForm(false), "groups",  getGroupIdOrPath(groupIdOrPath));
        return (response.readEntity(Group.class));
    }

    /**
     * Creates a new project group. Available only for users who can create groups.
     *
     * <pre><code>GitLab Endpoint: POST /groups</code></pre>
     *
     * @param name the name of the group to add
     * @param path the path for the group
     * @return the created Group instance
     * @throws GitLabApiException if any exception occurs
     */
    public Group addGroup(String name, String path) throws GitLabApiException {

        Form formData = new Form();
        formData.param("name", name);
        formData.param("path", path);
        Response response = post(Response.Status.CREATED, formData, "groups");
        return (response.readEntity(Group.class));
    }

    public Group addGroup(Group group) throws GitLabApiException {
        Form formData = new GitLabApiForm()
                .withParam("name", group.getName(), true)
                .withParam("path", group.getPath(), true)
                .withParam("description", group.getDescription())
                .withParam("visibility", group.getVisibility())
                .withParam("lfs_enabled", group.getLfsEnabled())
                .withParam("request_access_enabled", group.getRequestAccessEnabled())
                .withParam("parent_id", isApiVersion(ApiVersion.V3) ? null : group.getParentId());
        Response response = post(Response.Status.CREATED, formData, "groups");
        return (response.readEntity(Group.class));
    }

    /**
     * Creates a new project group. Available only for users who can create groups.
     *
     * <pre><code>GitLab Endpoint: POST /groups</code></pre>
     *
     * @param name the name of the group to add
     * @param path the path for the group
     * @param description (optional) - The group's description
     * @param visibility (optional) - The group's visibility. Can be private, internal, or public.
     * @param lfsEnabled (optional) - Enable/disable Large File Storage (LFS) for the projects in this group
     * @param requestAccessEnabled (optional) - Allow users to request member access
     * @param parentId (optional) - The parent group id for creating nested group
     * @return the created Group instance
     * @throws GitLabApiException if any exception occurs
     */
    public Group addGroup(String name, String path, String description, Visibility visibility,
            Boolean lfsEnabled, Boolean requestAccessEnabled, Long parentId) throws GitLabApiException {

        Form formData = new GitLabApiForm()
                .withParam("name", name, true)
                .withParam("path", path, true)
                .withParam("description", description)
                .withParam("visibility", visibility)
                .withParam("lfs_enabled", lfsEnabled)
                .withParam("request_access_enabled", requestAccessEnabled)
                .withParam("parent_id", isApiVersion(ApiVersion.V3) ? null : parentId);
        Response response = post(Response.Status.CREATED, formData, "groups");
        return (response.readEntity(Group.class));
    }

    /**
     * Updates a project group. Available only for users who can create groups.
     *
     * <pre><code>GitLab Endpoint: PUT /groups</code></pre>
     *
     * @param group to update
     * @return updated group instance
     * @throws GitLabApiException at any exception
     */
    public Group updateGroup(Group group) throws GitLabApiException {
        Form formData = new GitLabApiForm()
                .withParam("name", group.getName())
                .withParam("path", group.getPath())
                .withParam("description", group.getDescription())
                .withParam("visibility", group.getVisibility())
                .withParam("lfs_enabled", group.getLfsEnabled())
                .withParam("request_access_enabled", group.getRequestAccessEnabled())
                .withParam("parent_id", isApiVersion(ApiVersion.V3) ? null : group.getParentId());
        Response response = put(Response.Status.OK, formData.asMap(), "groups", group.getId());
        return (response.readEntity(Group.class));
    }

    /**
     * Updates a project group. Available only for users who can create groups.
     *
     * <pre><code>GitLab Endpoint: PUT /groups</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param name the name of the group to add
     * @param path the path for the group
     * @param description (optional) - The group's description
     * @param visibility (optional) - The group's visibility. Can be private, internal, or public.
     * @param lfsEnabled (optional) - Enable/disable Large File Storage (LFS) for the projects in this group
     * @param requestAccessEnabled (optional) - Allow users to request member access
     * @param parentId (optional) - The parent group id for creating nested group
     * @return the updated Group instance
     * @throws GitLabApiException if any exception occurs
     */
    public Group updateGroup(Object groupIdOrPath, String name, String path, String description, Visibility visibility,
            Boolean lfsEnabled, Boolean requestAccessEnabled, Long parentId) throws GitLabApiException {

        Form formData = new GitLabApiForm()
                .withParam("name", name)
                .withParam("path", path)
                .withParam("description", description)
                .withParam("visibility", visibility)
                .withParam("lfs_enabled", lfsEnabled)
                .withParam("request_access_enabled", requestAccessEnabled)
                .withParam("parent_id", isApiVersion(ApiVersion.V3) ? null : parentId);
        Response response = put(Response.Status.OK, formData.asMap(), "groups", getGroupIdOrPath(groupIdOrPath));
        return (response.readEntity(Group.class));
    }

    /**
     * Creates a new project group. Available only for users who can create groups.
     *
     * <pre><code>GitLab Endpoint: POST /groups</code></pre>
     *
     * @param name the name of the group to add
     * @param path the path for the group
     * @param description (optional) - The group's description
     * @param membershipLock (optional, boolean) - Prevent adding new members to project membership within this group
     * @param shareWithGroupLock (optional, boolean) - Prevent sharing a project with another group within this group
     * @param visibility (optional) - The group's visibility. Can be private, internal, or public.
     * @param lfsEnabled (optional) - Enable/disable Large File Storage (LFS) for the projects in this group
     * @param requestAccessEnabled (optional) - Allow users to request member access.
     * @param parentId (optional) - The parent group id for creating nested group.
     * @param sharedRunnersMinutesLimit (optional) - (admin-only) Pipeline minutes quota for this group
     * @return the created Group instance
     * @throws GitLabApiException if any exception occurs
     * @deprecated  Will be removed in version 6.0, replaced by {@link #addGroup(String, String, String, Visibility,
     *      Boolean, Boolean, Long)}
     */
    @Deprecated
	public Group addGroup(String name, String path, String description, Boolean membershipLock,
            Boolean shareWithGroupLock, Visibility visibility, Boolean lfsEnabled, Boolean requestAccessEnabled,
            Long parentId, Integer sharedRunnersMinutesLimit) throws GitLabApiException {

        Form formData = new GitLabApiForm()
                .withParam("name", name)
                .withParam("path", path)
                .withParam("description", description)
                .withParam("membership_lock", membershipLock)
                .withParam("share_with_group_lock", shareWithGroupLock)
                .withParam("visibility", visibility)
                .withParam("lfs_enabled", lfsEnabled)
                .withParam("request_access_enabled", requestAccessEnabled)
                .withParam("parent_id", parentId)
                .withParam("shared_runners_minutes_limit", sharedRunnersMinutesLimit);
        Response response = post(Response.Status.CREATED, formData, "groups");
        return (response.readEntity(Group.class));
    }

    /**
     * Updates a project group. Available only for users who can create groups.
     *
     * <pre><code>GitLab Endpoint: PUT /groups</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param name the name of the group to add
     * @param path the path for the group
     * @param description (optional) - The group's description
     * @param membershipLock (optional, boolean) - Prevent adding new members to project membership within this group
     * @param shareWithGroupLock (optional, boolean) - Prevent sharing a project with another group within this group
     * @param visibility (optional) - The group's visibility. Can be private, internal, or public.
     * @param lfsEnabled (optional) - Enable/disable Large File Storage (LFS) for the projects in this group
     * @param requestAccessEnabled (optional) - Allow users to request member access
     * @param parentId (optional) - The parent group id for creating nested group
     * @param sharedRunnersMinutesLimit (optional) - (admin-only) Pipeline minutes quota for this group
     * @return the updated Group instance
     * @throws GitLabApiException if any exception occurs
     * @deprecated  Will be removed in version 6.0, replaced by {@link #updateGroup(Object, String, String, String,
     *      Visibility, Boolean, Boolean, Long)}
     */
    @Deprecated
	public Group updateGroup(Object groupIdOrPath, String name, String path, String description, Boolean membershipLock,
            Boolean shareWithGroupLock, Visibility visibility, Boolean lfsEnabled, Boolean requestAccessEnabled,
            Long parentId, Integer sharedRunnersMinutesLimit) throws GitLabApiException {

        Form formData = new GitLabApiForm()
                .withParam("name", name)
                .withParam("path", path)
                .withParam("description", description)
                .withParam("membership_lock", membershipLock)
                .withParam("share_with_group_lock", shareWithGroupLock)
                .withParam("visibility", visibility)
                .withParam("lfs_enabled", lfsEnabled)
                .withParam("request_access_enabled", requestAccessEnabled)
                .withParam("parent_id", parentId)
                .withParam("shared_runners_minutes_limit", sharedRunnersMinutesLimit);
        Response response = put(Response.Status.OK, formData.asMap(), "groups", getGroupIdOrPath(groupIdOrPath));
        return (response.readEntity(Group.class));
    }

    /**
     * Removes group with all projects inside.
     *
     * <pre><code>GitLab Endpoint: DELETE /groups/:id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteGroup(Object groupIdOrPath) throws GitLabApiException {
        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.NO_CONTENT);
        delete(expectedStatus, null, "groups", getGroupIdOrPath(groupIdOrPath));
    }

    /**
     * Get a list of group members viewable by the authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/members</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @return a list of group members viewable by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public List<Member> getMembers(Object groupIdOrPath) throws GitLabApiException {
        return (getMembers(groupIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a list of group members viewable by the authenticated user in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/members</code></pre>
     *
     *@param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param page the page to get
     * @param perPage the number of Member instances per page
     * @return a list of group members viewable by the authenticated user in the specified page range
     * @throws GitLabApiException if any exception occurs
     */
    public List<Member> getMembers(Object groupIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "groups", getGroupIdOrPath(groupIdOrPath), "members");
        return (response.readEntity(new GenericType<List<Member>>() {}));
    }

    /**
     * Get a Pager of group members viewable by the authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/members</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param itemsPerPage the number of Member instances that will be fetched per page
     * @return a list of group members viewable by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Member> getMembers(Object groupIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Member>(this, Member.class, itemsPerPage, null, "groups", getGroupIdOrPath(groupIdOrPath), "members"));
    }

    /**
     * Get a Stream of group members viewable by the authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/members</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @return a Stream of group members viewable by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Member> getMembersStream(Object groupIdOrPath) throws GitLabApiException {
        return (getMembers(groupIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get a group member viewable by the authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/members/:id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param userId the member ID of the member to get
     * @return a member viewable by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public Member getMember(Object groupIdOrPath, long userId) throws GitLabApiException {
	    return (getMember(groupIdOrPath, userId, false));
    }

    /**
     * Get a group member viewable by the authenticated user as an Optional instance.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/members/:id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param userId the member ID of the member to get
     * @return a member viewable by the authenticated user as an Optional instance
     */
    public Optional<Member> getOptionalMember(Object groupIdOrPath, long userId) {
        try {
            return (Optional.ofNullable(getMember(groupIdOrPath, userId, false)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Gets a group team member, optionally including inherited member.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/members/all/:user_id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param userId the user ID of the member
     * @param includeInherited if true will the member even if inherited thru an ancestor group
     * @return the member specified by the project ID/user ID pair
     * @throws GitLabApiException if any exception occurs
     */
    public Member getMember(Object groupIdOrPath, Long userId, Boolean includeInherited) throws GitLabApiException {
        Response response;
        if (includeInherited) {
            response = get(Response.Status.OK, null, "groups", getGroupIdOrPath(groupIdOrPath), "members", "all", userId);
        } else {
            response = get(Response.Status.OK, null, "groups", getGroupIdOrPath(groupIdOrPath), "members", userId);
        }
        return (response.readEntity(Member.class));
    }

    /**
     * Gets a group team member, optionally including inherited member.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/members/all/:user_id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param userId the user ID of the member
     * @param includeInherited if true will the member even if inherited thru an ancestor group
     * @return the member specified by the group ID/user ID pair as the value of an Optional
     */
    public Optional<Member> getOptionalMember(Object groupIdOrPath, Long userId, Boolean includeInherited)  {
        try {
            return (Optional.ofNullable(getMember(groupIdOrPath, userId, includeInherited)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Gets a list of group members viewable by the authenticated user, including inherited members
     * through ancestor groups. Returns multiple times the same user (with different member attributes)
     * when the user is a member of the group and of one or more ancestor group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/members/all</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @return a list of group members viewable by the authenticated user, including inherited members
     * through ancestor groups
     * @throws GitLabApiException if any exception occurs
     */
    public List<Member> getAllMembers(Object groupIdOrPath) throws GitLabApiException {
        return (getAllMembers(groupIdOrPath, null, null));
    }

    /**
     * Gets a list of group members viewable by the authenticated user, including inherited members
     * through ancestor groups. Returns multiple times the same user (with different member attributes)
     * when the user is a member of the group and of one or more ancestor group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/members/all</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param page the page to get
     * @param perPage the number of Member instances per page
     * @return a list of group members viewable by the authenticated user, including inherited members
     * through ancestor groups in the specified page range
     * @throws GitLabApiException if any exception occurs
     * @deprecated  Will be removed in version 6.0
     */
    @Deprecated
    public List<Member> getAllMembers(Object groupIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage),
                "groups", getGroupIdOrPath(groupIdOrPath), "members", "all");
        return (response.readEntity(new GenericType<List<Member>>() {}));
    }

    /**
     * Gets a Pager of group members viewable by the authenticated user, including inherited members
     * through ancestor groups. Returns multiple times the same user (with different member attributes)
     * when the user is a member of the group and of one or more ancestor group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/members/all</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param itemsPerPage the number of Member instances that will be fetched per page
     * @return a Pager of group members viewable by the authenticated user, including inherited members
     * through ancestor groups
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Member> getAllMembers(Object groupIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (getAllMembers(groupIdOrPath, null, null, itemsPerPage));
    }

    /**
     * Gets a Stream of group members viewable by the authenticated user, including inherited members
     * through ancestor groups. Returns multiple times the same user (with different member attributes)
     * when the user is a member of the group and of one or more ancestor group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/members/all</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @return a Stream of group members viewable by the authenticated user, including inherited members
     * through ancestor groups
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Member> getAllMembersStream(Object groupIdOrPath) throws GitLabApiException {
        return (getAllMembersStream(groupIdOrPath, null, null));
    }


    /**
     * Gets a list of group members viewable by the authenticated user, including inherited members
     * through ancestor groups. Returns multiple times the same user (with different member attributes)
     * when the user is a member of the group and of one or more ancestor group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/members/all</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param query a query string to search for members
     * @param userIds filter the results on the given user IDs
     * @return the group members viewable by the authenticated user, including inherited members through ancestor groups
     * @throws GitLabApiException if any exception occurs
     */
    public List<Member> getAllMembers(Object groupIdOrPath, String query, List<Long> userIds) throws GitLabApiException {
        return (getAllMembers(groupIdOrPath, query, userIds, getDefaultPerPage()).all());
    }

    /**
     * Gets a Pager of group members viewable by the authenticated user, including inherited members
     * through ancestor groups. Returns multiple times the same user (with different member attributes)
     * when the user is a member of the group and of one or more ancestor group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/members/all</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param query a query string to search for members
     * @param userIds filter the results on the given user IDs
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of the group members viewable by the authenticated user,
     * including inherited members through ancestor groups
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Member> getAllMembers(Object groupIdOrPath, String query, List<Long> userIds, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm form = new GitLabApiForm().withParam("query", query).withParam("user_ids", userIds);
        return (new Pager<Member>(this, Member.class, itemsPerPage, form.asMap(),
                "groups", getGroupIdOrPath(groupIdOrPath), "members", "all"));
    }

    /**
     * Gets a Stream of group members viewable by the authenticated user, including inherited members
     * through ancestor groups. Returns multiple times the same user (with different member attributes)
     * when the user is a member of the group and of one or more ancestor group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/members/all</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param query a query string to search for members
     * @param userIds filter the results on the given user IDs
     * @return a Stream of the group members viewable by the authenticated user,
     * including inherited members through ancestor groups
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Member> getAllMembersStream(Object groupIdOrPath, String query, List<Long> userIds) throws GitLabApiException {
        return (getAllMembers(groupIdOrPath, query, userIds, getDefaultPerPage()).stream());
    }

    /**
     * Adds a user to the list of group members.
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/members</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param userId the user ID of the member to add, required
     * @param accessLevel the access level for the new member, required
     * @return a Member instance for the added user
     * @throws GitLabApiException if any exception occurs
     */
    public Member addMember(Object groupIdOrPath, Long userId, Integer accessLevel) throws GitLabApiException {
        return (addMember(groupIdOrPath, userId, accessLevel, null));
    }

    /**
     * Adds a user to the list of group members.
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/members</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param userId the user ID of the member to add, required
     * @param accessLevel the access level for the new member, required
     * @return a Member instance for the added user
     * @throws GitLabApiException if any exception occurs
     */
    public Member addMember(Object groupIdOrPath, Long userId, AccessLevel accessLevel) throws GitLabApiException {
        return (addMember(groupIdOrPath, userId, accessLevel.toValue(), null));
    }

    /**
     * Adds a user to the list of group members.
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/members</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param userId the user ID of the member to add, required
     * @param accessLevel the access level for the new member, required
     * @param expiresAt the date the membership in the group will expire, optional
     * @return a Member instance for the added user
     * @throws GitLabApiException if any exception occurs
     */
    public Member addMember(Object groupIdOrPath, Long userId, AccessLevel accessLevel, Date expiresAt) throws GitLabApiException {
        return (addMember(groupIdOrPath, userId, accessLevel.toValue(), expiresAt));
    }

    /**
     * Adds a user to the list of group members.
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/members</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param userId the user ID of the member to add, required
     * @param accessLevel the access level for the new member, required
     * @param expiresAt the date the membership in the group will expire, optional
     * @return a Member instance for the added user
     * @throws GitLabApiException if any exception occurs
     */
    public Member addMember(Object groupIdOrPath, Long userId, Integer accessLevel, Date expiresAt) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("user_id", userId, true)
                .withParam("access_level", accessLevel, true)
                .withParam("expires_at",  expiresAt, false);
        Response response = post(Response.Status.CREATED, formData, "groups", getGroupIdOrPath(groupIdOrPath), "members");
        return (response.readEntity(Member.class));
    }

    /**
     * Updates a member of a group.
     *
     * <pre><code>GitLab Endpoint: PUT /groups/:groupId/members/:userId</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param userId the user ID of the member to update, required
     * @param accessLevel the new access level for the member, required
     * @return the updated member
     * @throws GitLabApiException if any exception occurs
     */
    public Member updateMember(Object groupIdOrPath, Long userId, Integer accessLevel) throws GitLabApiException {
        return (updateMember(groupIdOrPath, userId, accessLevel, null));
    }

    /**
     * Updates a member of a group.
     *
     * <pre><code>GitLab Endpoint: PUT /groups/:groupId/members/:userId</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param userId the user ID of the member to update, required
     * @param accessLevel the new access level for the member, required
     * @return the updated member
     * @throws GitLabApiException if any exception occurs
     */
    public Member updateMember(Object groupIdOrPath, Long userId, AccessLevel accessLevel) throws GitLabApiException {
        return (updateMember(groupIdOrPath, userId, accessLevel.toValue(), null));
    }

    /**
     * Updates a member of a group.
     *
     * <pre><code>GitLab Endpoint: PUT /groups/:groupId/members/:userId</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param userId the user ID of the member to update, required
     * @param accessLevel the new access level for the member, required
     * @param expiresAt the date the membership in the group will expire, optional
     * @return the updated member
     * @throws GitLabApiException if any exception occurs
     */
    public Member updateMember(Object groupIdOrPath, Long userId, AccessLevel accessLevel, Date expiresAt) throws GitLabApiException {
        return (updateMember(groupIdOrPath, userId, accessLevel.toValue(), expiresAt));
    }

    /**
     * Updates a member of a group.
     *
     * <pre><code>GitLab Endpoint: PUT /groups/:groupId/members/:userId</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param userId the user ID of the member to update, required
     * @param accessLevel the new access level for the member, required
     * @param expiresAt the date the membership in the group will expire, optional
     * @return the updated member
     * @throws GitLabApiException if any exception occurs
     */
    public Member updateMember(Object groupIdOrPath, Long userId, Integer accessLevel, Date expiresAt) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("access_level", accessLevel, true)
                .withParam("expires_at",  expiresAt, false);
        Response response = put(Response.Status.OK, formData.asMap(), "groups", getGroupIdOrPath(groupIdOrPath), "members", userId);
        return (response.readEntity(Member.class));
    }

    /**
     * Removes member from the group.
     *
     * <pre><code>GitLab Endpoint: DELETE /groups/:id/members/:user_id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param userId the user ID of the member to remove
     * @throws GitLabApiException if any exception occurs
     */
    public void removeMember(Object groupIdOrPath, Long userId) throws GitLabApiException {
        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.NO_CONTENT);
        delete(expectedStatus, null, "groups", getGroupIdOrPath(groupIdOrPath), "members", userId);
    }

    /**
     * Syncs the group with its linked LDAP group. Only available to group owners and administrators.
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/ldap_sync</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @throws GitLabApiException if any exception occurs
     */
    public void ldapSync(Object groupIdOrPath) throws GitLabApiException {
        post(Response.Status.NO_CONTENT, (Form)null, "groups", getGroupIdOrPath(groupIdOrPath), "ldap_sync");
    }

    /**
     * Adds an LDAP group link.
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/ldap_group_links</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param cn the CN of a LDAP group
     * @param groupAccess the minimum access level for members of the LDAP group
     * @param provider the LDAP provider for the LDAP group
     * @throws GitLabApiException if any exception occurs
     */
    public void addLdapGroupLink(Object groupIdOrPath, String cn, AccessLevel groupAccess, String provider) throws GitLabApiException {

        if (groupAccess == null) {
            throw new RuntimeException("groupAccess cannot be null or empty");
        }

        addLdapGroupLink(groupIdOrPath, cn, groupAccess.toValue(), provider);
    }

    /**
     * Adds an LDAP group link.
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/ldap_group_links</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param cn the CN of a LDAP group
     * @param groupAccess the minimum access level for members of the LDAP group
     * @param provider the LDAP provider for the LDAP group
     * @throws GitLabApiException if any exception occurs
     */
    public void addLdapGroupLink(Object groupIdOrPath, String cn, Integer groupAccess, String provider) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("cn",  cn, true)
                .withParam("group_access", groupAccess, true)
                .withParam("provider",  provider, true);
        post(Response.Status.CREATED, formData, "groups", getGroupIdOrPath(groupIdOrPath), "ldap_group_links");
    }

    /**
     * Deletes an LDAP group link.
     *
     * <pre><code>GitLab Endpoint: DELETE /groups/:id/ldap_group_links/:cn</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param cn the CN of the LDAP group link to delete
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteLdapGroupLink(Object groupIdOrPath, String cn) throws GitLabApiException {

        if (cn == null || cn.trim().isEmpty()) {
            throw new RuntimeException("cn cannot be null or empty");
        }

        delete(Response.Status.OK, null, "groups", getGroupIdOrPath(groupIdOrPath), "ldap_group_links", cn);
    }

    /**
     * Deletes an LDAP group link for a specific LDAP provider.
     *
     * <pre><code>GitLab Endpoint: DELETE /groups/:id/ldap_group_links/:provider/:cn</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param cn the CN of the LDAP group link to delete
     * @param provider the name of the LDAP provider
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteLdapGroupLink(Object groupIdOrPath, String cn, String provider) throws GitLabApiException {

        if (cn == null || cn.trim().isEmpty()) {
            throw new RuntimeException("cn cannot be null or empty");
        }

        if (provider == null || provider.trim().isEmpty()) {
            throw new RuntimeException("LDAP provider cannot be null or empty");
        }

        delete(Response.Status.OK, null, "groups", getGroupIdOrPath(groupIdOrPath), "ldap_group_links", provider, cn);
    }

    /**
     * Get list of a group’s variables.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/variables</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @return a list of variables belonging to the specified group
     * @throws GitLabApiException if any exception occurs
     */
    public List<Variable> getVariables(Object groupIdOrPath) throws GitLabApiException {
        return (getVariables(groupIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a list of variables for the specified group in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/variables</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param page the page to get
     * @param perPage the number of Variable instances per page
     * @return a list of variables belonging to the specified group in the specified page range
     * @throws GitLabApiException if any exception occurs
     */
    public List<Variable> getVariables(Object groupIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "groups", getGroupIdOrPath(groupIdOrPath), "variables");
        return (response.readEntity(new GenericType<List<Variable>>() {}));
    }

    /**
     * Get a Pager of variables belonging to the specified group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/variables</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param itemsPerPage the number of Variable instances that will be fetched per page
     * @return a Pager of variables belonging to the specified group
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Variable> getVariables(Object groupIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Variable>(this, Variable.class, itemsPerPage, null, "groups", getGroupIdOrPath(groupIdOrPath), "variables"));
    }

    /**
     * Get a Stream of variables belonging to the specified group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/variables</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @return a Stream of variables belonging to the specified group
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Variable> getVariablesStream(Object groupIdOrPath) throws GitLabApiException {
        return (getVariables(groupIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get the details of a group variable.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/variables/:key</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param key the key of an existing variable, required
     * @return the Variable instance for the specified group variable
     * @throws GitLabApiException if any exception occurs
     */
    public Variable getVariable(Object groupIdOrPath, String key) throws GitLabApiException {
      Response response = get(Response.Status.OK, null, "groups", getGroupIdOrPath(groupIdOrPath), "variables", key);
      return (response.readEntity(Variable.class));
    }

    /**
     * Get the details of a group variable as an Optional instance.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/variables/:key</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param key the key of an existing variable, required
     * @return the Variable for the specified group variable as an Optional instance
     */
    public Optional<Variable> getOptionalVariable(Object groupIdOrPath, String key) {
        try {
            return (Optional.ofNullable(getVariable(groupIdOrPath, key)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Create a new group variable.
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/variables</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param key the key of a variable; must have no more than 255 characters; only A-Z, a-z, 0-9, and _ are allowed, required
     * @param value the value for the variable, required
     * @param isProtected whether the variable is protected, optional
     * @return a Variable instance with the newly created variable
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Variable createVariable(Object groupIdOrPath, String key, String value, Boolean isProtected) throws GitLabApiException {

        return createVariable(groupIdOrPath, key, value, isProtected, false);
    }

    /**
     * Create a new group variable.
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/variables</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param key the key of a variable; must have no more than 255 characters; only A-Z, a-z, 0-9, and _ are allowed, required
     * @param value the value for the variable, required
     * @param isProtected whether the variable is protected, optional
     * @param masked whether the variable is masked, optional
     * @return a Variable instance with the newly created variable
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Variable createVariable(Object groupIdOrPath, String key, String value, Boolean isProtected, Boolean masked) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("key", key, true)
                .withParam("value", value, true)
                .withParam("masked", masked)
                .withParam("protected", isProtected);
        Response response = post(Response.Status.CREATED, formData, "groups", getGroupIdOrPath(groupIdOrPath), "variables");
        return (response.readEntity(Variable.class));
    }

    /**
     * Update a group variable.
     *
     * <pre><code>GitLab Endpoint: PUT /groups/:id/variables/:key</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param key the key of an existing variable, required
     * @param value the value for the variable, required
     * @param isProtected whether the variable is protected, optional
     * @return a Variable instance with the updated variable
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Variable updateVariable(Object groupIdOrPath, String key, String value, Boolean isProtected) throws GitLabApiException {

        return updateVariable(groupIdOrPath, key, value, isProtected, false);
    }

    /**
     * Update a group variable.
     *
     * <pre><code>GitLab Endpoint: PUT /groups/:id/variables/:key</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param key the key of an existing variable, required
     * @param value the value for the variable, required
     * @param isProtected whether the variable is protected, optional
     * @param masked whether the variable is masked, optional
     * @return a Variable instance with the updated variable
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Variable updateVariable(Object groupIdOrPath, String key, String value, Boolean isProtected, Boolean masked) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("value", value, true)
                .withParam("masked", masked)
                .withParam("protected", isProtected);
        Response response = putWithFormData(Response.Status.CREATED, formData, "groups", getGroupIdOrPath(groupIdOrPath), "variables", key);
        return (response.readEntity(Variable.class));
    }

    /**
     * Deletes a group variable.
     *
     * <pre><code>GitLab Endpoint: DELETE /groups/:id/variables/:key</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param key the key of an existing variable, required
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteVariable(Object groupIdOrPath, String key) throws GitLabApiException {
        delete(Response.Status.NO_CONTENT, null, "groups", getGroupIdOrPath(groupIdOrPath), "variables", key);
    }

    /**
     * Transfer a project to the Group namespace. Available only for admin users.
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/projects/:project_id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path, required
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return the transferred Project instance
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Project transferProject(Object groupIdOrPath, Object projectIdOrPath) throws GitLabApiException {
        Response response = post(Response.Status.CREATED, (Form)null, "groups",  getGroupIdOrPath(groupIdOrPath),
                "projects", getProjectIdOrPath(projectIdOrPath));
        return (response.readEntity(Project.class));
    }

    /**
     * Get a List of the group audit events viewable by Maintainer or an Owner of the group.
     *
     * <pre><code>GET /groups/:id/audit_events</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param created_after Group audit events created on or after the given time.
     * @param created_before Group audit events created on or before the given time.
     * @return a List of group Audit events
     * @throws GitLabApiException if any exception occurs
     */
    public List<AuditEvent> getAuditEvents(Object groupIdOrPath, Date created_after, Date created_before) throws GitLabApiException {
        return (getAuditEvents(groupIdOrPath, created_after, created_before, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of the group audit events viewable by Maintainer or an Owner of the group.
     *
     * <pre><code>GET /groups/:id/audit_events</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param created_after Group audit events created on or after the given time.
     * @param created_before Group audit events created on or before the given time.
     * @param itemsPerPage the number of Audit Event instances that will be fetched per page
     * @return a Pager of group Audit events
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<AuditEvent> getAuditEvents(Object groupIdOrPath, Date created_after, Date created_before, int itemsPerPage) throws GitLabApiException {
        Form form = new GitLabApiForm()
                .withParam("created_before", ISO8601.toString(created_after, false))
                .withParam("created_after", ISO8601.toString(created_before, false));
        return (new Pager<AuditEvent>(this, AuditEvent.class, itemsPerPage, form.asMap(),
                "groups", getGroupIdOrPath(groupIdOrPath), "audit_events"));
    }

    /**
     * Get a Stream of the group audit events viewable by Maintainer or an Owner of the group.
     *
     * <pre><code>GET /groups/:id/audit_events</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param created_after Group audit events created on or after the given time.
     * @param created_before Group audit events created on or before the given time.
     * @return a Stream of group Audit events
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<AuditEvent> getAuditEventsStream(Object groupIdOrPath, Date created_after, Date created_before) throws GitLabApiException {
        return (getAuditEvents(groupIdOrPath, created_after, created_before, getDefaultPerPage()).stream());
    }

    /**
     * Get a specific audit event of a group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/audit_events/:id_audit_event</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param auditEventId the auditEventId, required
     * @return the group Audit event
     * @throws GitLabApiException if any exception occurs
     */
    public AuditEvent getAuditEvent(Object groupIdOrPath, Long auditEventId) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "groups", getGroupIdOrPath(groupIdOrPath), "audit_events", auditEventId);
        return (response.readEntity(AuditEvent.class));
    }

    /**
     * Get a List of the group access requests viewable by the authenticated user.
     *
     * <pre><code>GET /group/:id/access_requests</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @return a List of project AccessRequest instances accessible by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public List<AccessRequest> getAccessRequests(Object groupIdOrPath) throws GitLabApiException {
        return (getAccessRequests(groupIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of the group access requests viewable by the authenticated user.
     *
     * <pre><code>GET /groups/:id/access_requests</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param itemsPerPage the number of AccessRequest instances that will be fetched per page
     * @return a Pager of group AccessRequest instances accessible by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<AccessRequest> getAccessRequests(Object groupIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<AccessRequest>(this, AccessRequest.class, itemsPerPage, null,
                "groups", getGroupIdOrPath(groupIdOrPath), "access_requests"));
    }

    /**
     * Get a Stream of the group access requests viewable by the authenticated user.
     *
     * <pre><code>GET /groups/:id/access_requests</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @return a Stream of group AccessRequest instances accessible by the authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<AccessRequest> getAccessRequestsStream(Object groupIdOrPath) throws GitLabApiException {
       return (getAccessRequests(groupIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Requests access for the authenticated user to the specified group.
     *
     * <pre><code>POST /groups/:id/access_requests</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @return the created AccessRequest instance
     * @throws GitLabApiException if any exception occurs
     */
    public AccessRequest requestAccess(Object groupIdOrPath) throws GitLabApiException {
        Response response = post(Response.Status.CREATED, (Form)null, "groups", getGroupIdOrPath(groupIdOrPath), "access_requests");
        return (response.readEntity(AccessRequest.class));
    }

    /**
     * Approve access for the specified user to the specified group.
     *
     * <pre><code>PUT /groups/:id/access_requests/:user_id/approve</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param userId the user ID to approve access for
     * @param accessLevel the access level the user is approved for, if null will be developer (30)
     * @return the approved AccessRequest instance
     * @throws GitLabApiException if any exception occurs
     */
    public AccessRequest approveAccessRequest(Object groupIdOrPath, Long userId, AccessLevel accessLevel) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("access_level", accessLevel);
        Response response = this.putWithFormData(Response.Status.CREATED, formData,
                "groups", getGroupIdOrPath(groupIdOrPath), "access_requests", userId, "approve");
        return (response.readEntity(AccessRequest.class));
    }

    /**
     * Deny access for the specified user to the specified group.
     *
     * <pre><code>DELETE /groups/:id/access_requests/:user_id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param userId the user ID to deny access for
     * @throws GitLabApiException if any exception occurs
     */
    public void denyAccessRequest(Object groupIdOrPath, Long userId) throws GitLabApiException {
        delete(Response.Status.NO_CONTENT, null,
                "groups", getGroupIdOrPath(groupIdOrPath), "access_requests", userId);
    }

    /**
     * Gets a list of a group’s badges and its group badges.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/badges</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @return a List of Badge instances for the specified group
     * @throws GitLabApiException if any exception occurs
     */
    public List<Badge> getBadges(Object groupIdOrPath) throws GitLabApiException {
    return getBadges(groupIdOrPath, null);
    }

    /**
     * Gets a list of a group’s badges, case-sensitively filtered on bagdeName if non-null.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/badges?name=:name</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param badgeName The name to filter on (case-sensitive), ignored if null.
     * @return All badges of the GitLab item, case insensitively filtered on name.
     * @throws GitLabApiException If any problem is encountered
     */
    public List<Badge> getBadges(Object groupIdOrPath, String badgeName) throws GitLabApiException {
    Form queryParam = new GitLabApiForm().withParam("name", badgeName);
    Response response = get(Response.Status.OK, queryParam.asMap(), "groups", getGroupIdOrPath(groupIdOrPath), "badges");
    return (response.readEntity(new GenericType<List<Badge>>() {}));
    }

    /**
     * Gets a badge of a group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/badges/:badge_id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param badgeId the ID of the badge to get
     * @return a Badge instance for the specified group/badge ID pair
     * @throws GitLabApiException if any exception occurs
     */
    public Badge getBadge(Object groupIdOrPath, Long badgeId) throws GitLabApiException {
	Response response = get(Response.Status.OK, null, "groups", getGroupIdOrPath(groupIdOrPath), "badges", badgeId);
	return (response.readEntity(Badge.class));
    }

    /**
     * Get an Optional instance with the value for the specified badge.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/badges/:badge_id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param badgeId the ID of the badge to get
     * @return an Optional instance with the specified badge as the value
     */
    public Optional<Badge> getOptionalBadge(Object groupIdOrPath, Long badgeId) {
	try {
	    return (Optional.ofNullable(getBadge(groupIdOrPath, badgeId)));
	} catch (GitLabApiException glae) {
	    return (GitLabApi.createOptionalFromException(glae));
	}
    }

    /**
     * Add a badge to a group.
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/badges</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param linkUrl the URL of the badge link
     * @param imageUrl the URL of the image link
     * @return a Badge instance for the added badge
     * @throws GitLabApiException if any exception occurs
     */
    public Badge addBadge(Object groupIdOrPath, String linkUrl, String imageUrl) throws GitLabApiException {
    return addBadge(groupIdOrPath, null, linkUrl, imageUrl);
    }

    /**
     * Add a badge to a group.
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/badges</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param name The name to give the badge (may be null)
     * @param linkUrl the URL of the badge link
     * @param imageUrl the URL of the image link
     * @return A Badge instance for the added badge
     * @throws GitLabApiException if any exception occurs
     */
    public Badge addBadge(Object groupIdOrPath, String name, String linkUrl, String imageUrl) throws GitLabApiException {
    GitLabApiForm formData = new GitLabApiForm()
        .withParam("name", name, false)
        .withParam("link_url", linkUrl, true)
        .withParam("image_url", imageUrl, true);
    Response response = post(Response.Status.OK, formData, "groups", getGroupIdOrPath(groupIdOrPath), "badges");
    return (response.readEntity(Badge.class));
    }

    /**
     * Edit a badge of a group.
     *
     * <pre><code>GitLab Endpoint: PUT /groups/:id/badges</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param badgeId the ID of the badge to get
     * @param linkUrl the URL of the badge link
     * @param imageUrl the URL of the image link
     * @return a Badge instance for the editted badge
     * @throws GitLabApiException if any exception occurs
     */
    public Badge editBadge(Object groupIdOrPath, Long badgeId, String linkUrl, String imageUrl) throws GitLabApiException {
    return (editBadge(groupIdOrPath, badgeId, null, linkUrl, imageUrl));
    }

    /**
     * Edit a badge of a group.
     *
     * <pre><code>GitLab Endpoint: PUT /groups/:id/badges</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param badgeId the ID of the badge to edit
     * @param name The name of the badge to edit (may be null)
     * @param linkUrl the URL of the badge link
     * @param imageUrl the URL of the image link
     * @return a Badge instance for the edited badge
     * @throws GitLabApiException if any exception occurs
     */
    public Badge editBadge(Object groupIdOrPath, Long badgeId, String name, String linkUrl, String imageUrl) throws GitLabApiException {
    GitLabApiForm formData = new GitLabApiForm()
        .withParam("name", name, false)
        .withParam("link_url", linkUrl, false)
        .withParam("image_url", imageUrl, false);
    Response response = putWithFormData(Response.Status.OK, formData, "groups", getGroupIdOrPath(groupIdOrPath), "badges", badgeId);
    return (response.readEntity(Badge.class));
    }

    /**
     * Remove a badge from a group.
     *
     * <pre><code>GitLab Endpoint: DELETE /groups/:id/badges/:badge_id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param badgeId the ID of the badge to remove
     * @throws GitLabApiException if any exception occurs
     */
    public void removeBadge(Object groupIdOrPath, Long badgeId) throws GitLabApiException {
	delete(Response.Status.NO_CONTENT, null, "groups", getGroupIdOrPath(groupIdOrPath), "badges", badgeId);
    }

    /**
     * Returns how the link_url and image_url final URLs would be after resolving the placeholder interpolation.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/badges/render</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param linkUrl the URL of the badge link
     * @param imageUrl the URL of the image link
     * @return a Badge instance for the rendered badge
     * @throws GitLabApiException if any exception occurs
     */
    public Badge previewBadge(Object groupIdOrPath,  String linkUrl, String imageUrl) throws GitLabApiException {
	GitLabApiForm formData = new GitLabApiForm()
		.withParam("link_url", linkUrl, true)
		.withParam("image_url", imageUrl, true);
	Response response = get(Response.Status.OK, formData.asMap(), "groups", getGroupIdOrPath(groupIdOrPath), "badges", "render");
	return (response.readEntity(Badge.class));
    }

    /**
     * Uploads and sets the project avatar for the specified group.
     *
     * <pre><code>GitLab Endpoint: PUT /groups/:id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param avatarFile the File instance of the avatar file to upload
     * @return the updated Group instance
     * @throws GitLabApiException if any exception occurs
     */
    public Group setGroupAvatar(Object groupIdOrPath, File avatarFile) throws GitLabApiException {
        Response response = putUpload(Response.Status.OK,
                "avatar", avatarFile, "groups", getGroupIdOrPath(groupIdOrPath));
        return (response.readEntity(Group.class));
    }

    /**
     * Share group with another group. Returns 200 and the group details on success.
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/share</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param shareWithGroupId the ID of the group to share with, required
     * @param groupAccess the access level to grant the group, required
     * @param expiresAt expiration date of the share, optional
     * @return a Group instance holding the details of the shared group
     * @throws GitLabApiException if any exception occurs
     */
    public Group shareGroup(Object groupIdOrPath, Long shareWithGroupId, AccessLevel groupAccess, Date expiresAt) throws GitLabApiException {
	GitLabApiForm formData = new GitLabApiForm()
		.withParam("group_id", shareWithGroupId, true)
		.withParam("group_access", groupAccess, true)
		.withParam("expires_at", expiresAt);
	Response response = post(Response.Status.OK, formData, "groups", getGroupIdOrPath(groupIdOrPath), "share");
	return (response.readEntity(Group.class));
    }

    /**
     * Unshare the group from another group.
     *
     * <pre><code>GitLab Endpoint: DELETE /groups/:id/share/:group_id</code></pre>
     *
     * @param groupIdOrPath the group ID, path of the group, or a Group instance holding the group ID or path
     * @param sharedWithGroupId the ID of the group to unshare with, required
     * @throws GitLabApiException if any exception occurs
     */
    public void unshareGroup(Object groupIdOrPath, Long sharedWithGroupId) throws GitLabApiException {
	delete(Response.Status.NO_CONTENT, null,
	        "groups", getGroupIdOrPath(groupIdOrPath), "share", sharedWithGroupId);
    }

    /**
     * Get all custom attributes for the specified group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/custom_attributes</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @return a list of group's CustomAttributes
     * @throws GitLabApiException if any exception occurs
     */
    public List<CustomAttribute> getCustomAttributes(final Object groupIdOrPath) throws GitLabApiException {
        return (getCustomAttributes(groupIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of custom attributes for the specified group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/custom_attributes</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param itemsPerPage the number of items per page
     * @return a Pager of group's custom attributes
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<CustomAttribute> getCustomAttributes(final Object groupIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<CustomAttribute>(this, CustomAttribute.class, itemsPerPage, null,
            "groups", getGroupIdOrPath(groupIdOrPath), "custom_attributes"));
    }

    /**
     * Get a Stream of all custom attributes for the specified group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/custom_attributes</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @return a Stream of group's custom attributes
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<CustomAttribute> getCustomAttributesStream(final Object groupIdOrPath) throws GitLabApiException {
        return (getCustomAttributes(groupIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get a single custom attribute for the specified group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/custom_attributes/:key</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param key the key for the custom attribute
     * @return a CustomAttribute instance for the specified key
     * @throws GitLabApiException if any exception occurs
     */
    public CustomAttribute getCustomAttribute(final Object groupIdOrPath, final String key) throws GitLabApiException {
        Response response = get(Response.Status.OK, null,
            "groups", getGroupIdOrPath(groupIdOrPath), "custom_attributes", key);
        return (response.readEntity(CustomAttribute.class));
    }

    /**
     * Get an Optional instance with the value for a single custom attribute for the specified group.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/custom_attributes/:key</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance, required
     * @param key the key for the custom attribute, required
     * @return an Optional instance with the value for a single custom attribute for the specified group
     */
    public Optional<CustomAttribute> geOptionalCustomAttribute(final Object groupIdOrPath, final String key) {
        try {
            return (Optional.ofNullable(getCustomAttribute(groupIdOrPath, key)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Set a custom attribute for the specified group. The attribute will be updated if it already exists,
     * or newly created otherwise.
     *
     * <pre><code>GitLab Endpoint: PUT /groups/:id/custom_attributes/:key</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param key the key for the custom attribute
     * @param value the value for the customAttribute
     * @return a CustomAttribute instance for the updated or created custom attribute
     * @throws GitLabApiException if any exception occurs
     */
    public CustomAttribute setCustomAttribute(final Object groupIdOrPath, final String key, final String value) throws GitLabApiException {

        if (Objects.isNull(key) || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Value cannot be null or empty");
        }

        GitLabApiForm formData = new GitLabApiForm().withParam("value", value);
        Response response = putWithFormData(Response.Status.OK, formData,
            "groups", getGroupIdOrPath(groupIdOrPath), "custom_attributes", key);
        return (response.readEntity(CustomAttribute.class));
    }

    /**
     * Delete a custom attribute for the specified group.
     *
     * <pre><code>GitLab Endpoint: DELETE /groups/:id/custom_attributes/:key</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param key the key of the custom attribute to delete
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteCustomAttribute(final Object groupIdOrPath, final String key) throws GitLabApiException {

        if (Objects.isNull(key) || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key can't be null or empty");
        }

        delete(Response.Status.OK, null, "groups", getGroupIdOrPath(groupIdOrPath), "custom_attributes", key);
    }
}
