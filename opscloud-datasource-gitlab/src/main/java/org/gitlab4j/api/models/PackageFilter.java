package org.gitlab4j.api.models;

import org.gitlab4j.api.Constants.PackageOrderBy;
import org.gitlab4j.api.Constants.PackageStatus;
import org.gitlab4j.api.Constants.SortOrder;
import org.gitlab4j.api.GitLabApiForm;

/**
 *  This class is used to filter Projects when getting lists of projects for a specified group.
 */
public class PackageFilter {

    private Boolean excludeSubgroups;
    private PackageOrderBy orderBy;
    private SortOrder sort;
    private PackageType packageType;
    private String packageName;
    private Boolean includeVersionless;
    private PackageStatus status;

    /**
     * Exclude Subgroups.
     *
     * @param excludeSubgroups if true, packages from projects from subgroups are not listed.
     * @return the reference to this ProjectFilter instance
     */
    public PackageFilter withExcludeSubgroups(Boolean excludeSubgroups) {
        this.excludeSubgroups = excludeSubgroups;
        return (this);
    }

    /**
     * Return projects ordered by created_at, name, version, type, or project_path
     *
     * @param orderBy specifies what field to order by
     * @return the reference to this ProjectFilter instance
     */
    public PackageFilter withOrderBy(PackageOrderBy orderBy) {
        this.orderBy = orderBy;
        return (this);
    }

    /**
     * Return projects sorted in asc or desc order. Default is desc.
     *
     * @param sort sort direction, ASC or DESC
     * @return the reference to this ProjectFilter instance
     */
    public PackageFilter withSortOder(SortOrder sort) {
        this.sort = sort;
        return (this);
    }

    /**
     * Filter the returned packages by type.
     *
     * @param packageType One of conan, maven, npm, pypi, composer, nuget, helm, generic or golang
     * @return the reference to this ProjectFilter instance
     */
    public PackageFilter withPackageType(PackageType packageType) {
        this.packageType = packageType;
        return (this);
    }

    /**
     * Filter the project packages with a fuzzy search by name
     *
     * @param packageName
     * @return the reference to this ProjectFilter instance
     */
    public PackageFilter withPackageName(String packageName) {
        this.packageName = packageName;
        return (this);
    }

    /**
     * @param includeVersionless if true, versionless packages are included in the response
     * @return the reference to this ProjectFilter instance
     */
    public PackageFilter withIncludeVersionless(Boolean includeVersionless) {
        this.includeVersionless = includeVersionless;
        return (this);
    }

    /**
     * Filter the returned packages by status.
     *
     * @param status One of default (default), hidden, or processing
     * @return the reference to this ProjectFilter instance
     */
    public PackageFilter withStatus(PackageStatus status) {
        this.status = status;
        return (this);
    }

    /**
     * Get the query params specified by this filter.
     *
     * @return a GitLabApiForm instance holding the query parameters for this ProjectFilter instance
     */
    public GitLabApiForm getQueryParams() {
        return (new GitLabApiForm()
            .withParam("order_by", orderBy)
            .withParam("sort", sort)
            .withParam("exclude_subgroups", excludeSubgroups)
            .withParam("package_type", packageType)
            .withParam("package_name", packageName)
            .withParam("include_versionless", includeVersionless)
            .withParam("status", status)
        );
    }
}
