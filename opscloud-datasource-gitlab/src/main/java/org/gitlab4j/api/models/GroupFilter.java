package org.gitlab4j.api.models;

import org.gitlab4j.api.Constants.GroupOrderBy;
import org.gitlab4j.api.Constants.SortOrder;
import org.gitlab4j.api.GitLabApiForm;

import java.util.List;

/**
 *  This class is used to filter Groups when getting lists of groups.
 */
public class GroupFilter {

    private List<Integer> skipGroups;
    private Boolean allAvailable;
    private String search;
    private GroupOrderBy orderBy;
    private SortOrder sort;
    private Boolean statistics;
    private Boolean withCustomAttributes;
    private Boolean owned;
    private AccessLevel accessLevel;
    private Boolean topLevelOnly;

    /**
     * Do not include the provided groups IDs.
     *
     * @param skipGroups List of group IDs to not include in the search
     * @return the reference to this GroupFilter instance
     */
    public GroupFilter withSkipGroups(List<Integer> skipGroups) {
        this.skipGroups = skipGroups;
        return (this);
    }

    /**
     * @deprecated this method contains a typo, use {@link #withAllAvailable(Boolean)} instead
     */
    @Deprecated
    public GroupFilter withAllAvailabley(Boolean allAvailable) {
        return withAllAvailable(allAvailable);
    }

    /**
     * Show all the groups you have access to (defaults to false for authenticated users, true for admin).
     * Attributes owned and min_access_level have precedence
     *
     * @param allAvailable if true show all available groups
     * @return the reference to this GroupFilter instance
     */
    public GroupFilter withAllAvailable(Boolean allAvailable) {
        this.allAvailable = allAvailable;
        return (this);
    }

    /**
     * Return list of groups matching the search criteria.
     *
     * @param search the search criteria
     * @return the reference to this GroupFilter instance
     */
    public GroupFilter withSearch(String search) {
        this.search = search;
        return (this);
    }

    /**
     * Return groups ordered by id, name, path, created_at, updated_at, or last_activity_at fields. Default is created_at.
     *
     * @param orderBy specifies what field to order by
     * @return the reference to this GroupFilter instance
     */
    public GroupFilter withOrderBy(GroupOrderBy orderBy) {
        this.orderBy = orderBy;
        return (this);
    }

    /**
     * Return groups sorted in asc or desc order. Default is desc.
     *
     * @param sort sort direction, ASC or DESC
     * @return the reference to this GroupFilter instance
     */
    public GroupFilter withSortOder(SortOrder sort) {
        this.sort = sort;
        return (this);
    }

    /**
     * Include group statistics (admins only).
     *
     * @param statistics if true, return statistics with the results
     * @return the reference to this GroupFilter instance
     */
    public GroupFilter withStatistics(Boolean statistics) {
        this.statistics = statistics;
        return (this);
    }

    /**
     *  Include custom attributes in response (admins only).
     *
     * @param withCustomAttributes if true, include custom attributes in the response
     * @return the reference to this GroupFilter instance
     */
    public GroupFilter withCustomAttributes(Boolean withCustomAttributes) {
        this.withCustomAttributes = withCustomAttributes;
        return (this);
    }

    /**
     * Limit by groups explicitly owned by the current user
     *
     * @param owned if true, limit to groups explicitly owned by the current user
     * @return the reference to this GroupFilter instance
     */
    public GroupFilter withOwned(Boolean owned) {
        this.owned = owned;
        return (this);
    }

    /**
     * Limit to groups where current user has at least this access level.
     *
     * @param accessLevel limit to groups where current user has at least this access level
     * @return the reference to this GroupFilter instance
     */
    public GroupFilter withMinAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
        return (this);
    }

    /**
     * Limit by groups which are top level groups
     *
     * @param topLevelOnly if true, limit to groups which are top level groups
     * @return the reference to this GroupFilter instance
     */
    public GroupFilter withTopLevelOnly(Boolean topLevelOnly) {
        this.topLevelOnly = topLevelOnly;
        return (this);
    }

    /**
     * Get the query params specified by this filter.
     *
     * @return a GitLabApiForm instance holding the query parameters for this GroupFilter instance
     */
    public GitLabApiForm getQueryParams() {
        return (new GitLabApiForm()
            .withParam("skip_groups", skipGroups)
            .withParam("all_available", allAvailable)
            .withParam("search", search)
            .withParam("order_by", orderBy)
            .withParam("sort", sort)
            .withParam("statistics", statistics)
            .withParam("with_custom_attributes", withCustomAttributes)
            .withParam("owned", owned)
            .withParam("min_access_level", accessLevel)
            .withParam("top_level_only", topLevelOnly)
        );
    }
}
