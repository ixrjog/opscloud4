package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gitlab4j.api.Constants.IssueScope;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.GitLabApiForm;
import org.gitlab4j.api.utils.ISO8601;

import java.util.Date;
import java.util.List;

/**
 *  This class is used to filter issues when getting issue statistics. of them.
 */
public class IssuesStatisticsFilter {

    private List<String> labels;
    private String milestone;
    private IssueScope scope;
    private Long authorId;
    private Long assigneeId;
    private String myReactionEmoji;
    private List<Long> iids;
    private String search;
    private String in;
    private Date createdAfter;
    private Date createdBefore;
    private Date updatedAfter;
    private Date updatedBefore;
    private Boolean confidential;

    public IssuesStatisticsFilter withLabels(List<String> labels) {
        this.labels = labels;
        return (this);
    }

    public IssuesStatisticsFilter withIids(List<Long> iids) {
        this.iids = iids;
        return (this);
    }

    public IssuesStatisticsFilter withMilestone(String milestone) {
        this.milestone = milestone;
        return (this);
    }

    public IssuesStatisticsFilter withScope(IssueScope scope) {
        this.scope = scope;
        return (this);
    }

    public IssuesStatisticsFilter withAuthorId(Long authorId) {
        this.authorId = authorId;
        return (this);
    }

    public IssuesStatisticsFilter withAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
        return (this);
    }

    public IssuesStatisticsFilter withMyReactionEmoji(String myReactionEmoji) {
        this.myReactionEmoji = myReactionEmoji;
        return (this);
    }

    public IssuesStatisticsFilter withSearch(String search) {
        this.search = search;
        return (this);
    }

    public IssuesStatisticsFilter withIn(String in) {
        this.in = in;
        return (this);
    }

    public IssuesStatisticsFilter withCreatedAfter(Date createdAfter) {
        this.createdAfter = createdAfter;
        return (this);
    }

    public IssuesStatisticsFilter withCreatedBefore(Date createdBefore) {
        this.createdBefore = createdBefore;
        return (this);
    }

    public IssuesStatisticsFilter withUpdatedAfter(Date updatedAfter) {
        this.updatedAfter = updatedAfter;
        return (this);
    }

    public IssuesStatisticsFilter withUpdatedBefore(Date updatedBefore) {
        this.updatedBefore = updatedBefore;
        return (this);
    }

    public IssuesStatisticsFilter withConfidential(Boolean confidential) {
        this.confidential = confidential;
        return (this);
    }

    @JsonIgnore
    public GitLabApiForm getQueryParams() throws GitLabApiException  {

        return (new GitLabApiForm()
                .withParam("labels", (labels != null ? String.join(",", labels) : null))
                .withParam("iids", iids)
                .withParam("milestone", milestone)
                .withParam("scope", scope)
                .withParam("author_id", authorId)
                .withParam("assignee_id", assigneeId)
                .withParam("my_reaction_emoji", myReactionEmoji)
                .withParam("search", search)
                .withParam("in", in)
                .withParam("created_after", ISO8601.toString(createdAfter, false))
                .withParam("created_before", ISO8601.toString(createdBefore, false))
                .withParam("updated_after", ISO8601.toString(updatedAfter, false))
                .withParam("updated_before", ISO8601.toString(updatedBefore, false))
                .withParam("confidential", confidential));
    }
}
