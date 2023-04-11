package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.Constants.*;
import org.gitlab4j.api.GitLabApiForm;

import java.util.Date;
import java.util.List;

import static org.gitlab4j.api.Constants.MergeRequestScope.ALL;
import static org.gitlab4j.api.Constants.MergeRequestScope.ASSIGNED_TO_ME;

/**
 * This class is used to filter merge requests when getting lists of them.
 */
public class MergeRequestFilter {

    private Long projectId;
    private Long groupId;
    private List<Long> iids;
    private MergeRequestState state;
    private MergeRequestOrderBy orderBy;
    private SortOrder sort;
    private String milestone;
    private Boolean simpleView;
    private List<String> labels;
    private Date createdAfter;
    private Date createdBefore;
    private Date updatedAfter;
    private Date updatedBefore;
    private MergeRequestScope scope;

    /**
     * Filter MR by created by the given user id. Combine with scope=all or scope=assigned_to_me
     */
    private Long authorId;
    private Long assigneeId;
    private String myReactionEmoji;
    private String sourceBranch;
    private String targetBranch;
    private String search;
    private MergeRequestSearchIn in;
    private Boolean wip;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public MergeRequestFilter withProjectId(Long projectId) {
        this.projectId = projectId;
        return (this);
    }

    public List<Long> getIids() {
        return iids;
    }

    public void setIids(List<Long> iids) {
        this.iids = iids;
    }

    public MergeRequestFilter withIids(List<Long> iids) {
        this.iids = iids;
        return (this);
    }

    public MergeRequestState getState() {
        return state;
    }

    public void setState(MergeRequestState state) {
        this.state = state;
    }

    public MergeRequestFilter withState(MergeRequestState state) {
        this.state = state;
        return (this);
    }

    public MergeRequestOrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(MergeRequestOrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public MergeRequestFilter withOrderBy(MergeRequestOrderBy orderBy) {
        this.orderBy = orderBy;
        return (this);
    }

    public SortOrder getSort() {
        return sort;
    }

    public void setSort(SortOrder sort) {
        this.sort = sort;
    }

    public MergeRequestFilter withSort(SortOrder sort) {
        this.sort = sort;
        return (this);
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public MergeRequestFilter withMilestone(String milestone) {
        this.milestone = milestone;
        return (this);
    }

    public Boolean getSimpleView() {
        return simpleView;
    }

    public void setSimpleView(Boolean simpleView) {
        this.simpleView = simpleView;
    }

    public MergeRequestFilter withSimpleView(Boolean simpleView) {
        this.simpleView = simpleView;
        return (this);
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public MergeRequestFilter withLabels(List<String> labels) {
        this.labels = labels;
        return (this);
    }

    public Date getCreatedAfter() {
        return createdAfter;
    }

    public void setCreatedAfter(Date createdAfter) {
        this.createdAfter = createdAfter;
    }

    public MergeRequestFilter withCreatedAfter(Date createdAfter) {
        this.createdAfter = createdAfter;
        return (this);
    }

    public Date getCreatedBefore() {
        return createdBefore;
    }

    public void setCreatedBefore(Date createdBefore) {
        this.createdBefore = createdBefore;
    }

    public MergeRequestFilter withCreatedBefore(Date createdBefore) {
        this.createdBefore = createdBefore;
        return (this);
    }

    public Date getUpdatedAfter() {
        return updatedAfter;
    }

    public void setUpdatedAfter(Date updatedAfter) {
        this.updatedAfter = updatedAfter;
    }

    public MergeRequestFilter withUpdatedAfter(Date updatedAfter) {
        this.updatedAfter = updatedAfter;
        return (this);
    }

    public Date getUpdatedBefore() {
        return updatedBefore;
    }

    public void setUpdatedBefore(Date updatedBefore) {
        this.updatedBefore = updatedBefore;
    }

    public MergeRequestFilter withUpdatedBefore(Date updatedBefore) {
        this.updatedBefore = updatedBefore;
        return (this);
    }

    public MergeRequestScope getScope() {
        return scope;
    }

    public void setScope(MergeRequestScope scope) {
        this.scope = scope;
    }

    public MergeRequestFilter withScope(MergeRequestScope scope) {
        this.scope = scope;
        return (this);
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public MergeRequestFilter withAuthorId(Long authorId) {
        this.authorId = authorId;
        return (this);
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public MergeRequestFilter withAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
        return (this);
    }

    public String getMyReactionEmoji() {
        return myReactionEmoji;
    }

    public void setMyReactionEmoji(String myReactionEmoji) {
        this.myReactionEmoji = myReactionEmoji;
    }

    public MergeRequestFilter withMyReactionEmoji(String myReactionEmoji) {
        this.myReactionEmoji = myReactionEmoji;
        return (this);
    }

    public String getSourceBranch() {
        return sourceBranch;
    }

    public void setSourceBranch(String sourceBranch) {
        this.sourceBranch = sourceBranch;
    }

    public MergeRequestFilter withSourceBranch(String sourceBranch) {
        this.sourceBranch = sourceBranch;
        return (this);
    }

    public String getTargetBranch() {
        return targetBranch;
    }

    public void setTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
    }

    public MergeRequestFilter withTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
        return (this);
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public MergeRequestFilter withSearch(String search) {
        this.search = search;
        return (this);
    }

    public MergeRequestSearchIn getIn() {
        return in;
    }

    public void setIn(MergeRequestSearchIn in) {
        this.in = in;
    }

    public MergeRequestFilter withIn(MergeRequestSearchIn in) {
        this.in = in;
        return (this);
    }

    public Boolean getWip() {
        return wip;
    }

    public void setWip(Boolean wip) {
        this.wip = wip;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public MergeRequestFilter withGroupId(Long groupId) {
        this.groupId = groupId;
        return (this);
    }

    public MergeRequestFilter withWip(Boolean wip) {
        this.wip = wip;
        return (this);
    }

    @JsonIgnore
    public GitLabApiForm getQueryParams(int page, int perPage) {
        return (getQueryParams()
            .withParam(Constants.PAGE_PARAM, page)
            .withParam(Constants.PER_PAGE_PARAM, perPage));
    }

    @JsonIgnore
    public GitLabApiForm getQueryParams() {
        GitLabApiForm params = new GitLabApiForm()
                .withParam("iids", iids)
                .withParam("state", state)
                .withParam("order_by", orderBy)
                .withParam("sort", sort)
                .withParam("milestone", milestone)
                .withParam("view", (simpleView != null && simpleView ? "simple" : null))
                .withParam("labels", (labels != null ? String.join(",", labels) : null))
                .withParam("created_after", createdAfter)
                .withParam("created_before", createdBefore)
                .withParam("updated_after", updatedAfter)
                .withParam("updated_before", updatedBefore)
                .withParam("scope", scope)
                .withParam("assignee_id", assigneeId)
                .withParam("my_reaction_emoji", myReactionEmoji)
                .withParam("source_branch", sourceBranch)
                .withParam("target_branch", targetBranch)
                .withParam("search", search)
                .withParam("in", in)
                .withParam("wip", (wip == null ? null : wip ? "yes" : "no"));

        if (authorId != null && (scope == ALL || scope == ASSIGNED_TO_ME)) {
            params.withParam("author_id", authorId);
        }
        return params;
    }
}
