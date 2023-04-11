package org.gitlab4j.api.models;

import org.gitlab4j.api.GitLabApiForm;

import java.util.List;

/**
 *  This class is used to filter Groups when getting lists of groups for a specified project.
 */
public class ProjectGroupsFilter {

    private String search;
    private AccessLevel sharedMinAccessLevel;
    private Boolean sharedVisibleOnly;
    private List<Long> skipGroups;
    private Boolean withShared;

    /**
     * Search for specific groups.
     *
     * @param search the search criteria
     * @return the reference to this ProjectGroupsFilter instance
     */
    public ProjectGroupsFilter withSearch(String search) {
        this.search = search;
        return (this);
    }

    /**
     * Limit to shared groups with at least this role.
     *
     * @param sharedMinAccessLevel the minimal role
     * @return the reference to this ProjectGroupsFilter instance
     */
    public ProjectGroupsFilter withSharedMinAccessLevel(AccessLevel sharedMinAccessLevel) {
        this.sharedMinAccessLevel = sharedMinAccessLevel;
        return (this);
    }

    /**
     * Limit to shared groups user has access to.
     *
     * @param sharedVisibleOnly if true limit to the shared groups user has access to.
     * @return the reference to this ProjectGroupsFilter instance
     */
    public ProjectGroupsFilter withSharedVisibleOnly(Boolean sharedVisibleOnly) {
        this.sharedVisibleOnly = sharedVisibleOnly;
        return (this);
    }

    /**
     * Do not include the provided groups IDs.
     *
     * @param skipGroups List of group IDs to not include in the search
     * @return the reference to this ProjectGroupsFilter instance
     */
    public ProjectGroupsFilter withSkipGroups(List<Long> skipGroups) {
        this.skipGroups = skipGroups;
        return (this);
    }

    /**
     * Include projects shared with this group.
     *
     * @param withShared if true include projects shared with this group.
     * @return the reference to this ProjectGroupsFilter instance
     */
    public ProjectGroupsFilter withWithShared(Boolean withShared) {
        this.withShared = withShared;
        return (this);
    }

    /**
     * Get the query params specified by this filter.
     *
     * @return a GitLabApiForm instance holding the query parameters for this ProjectGroupsFilter instance
     */
    public GitLabApiForm getQueryParams() {
        return (new GitLabApiForm()
            .withParam("search", search)
            .withParam("shared_min_access_level", sharedMinAccessLevel)
            .withParam("shared_visible_only", sharedVisibleOnly)
            .withParam("skip_groups", skipGroups)
            .withParam("with_shared", withShared)
        );
    }
}
