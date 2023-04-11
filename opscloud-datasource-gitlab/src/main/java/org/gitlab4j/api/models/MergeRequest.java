package org.gitlab4j.api.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;
import java.util.List;

public class MergeRequest {

    private Boolean allowCollaboration;
    private Boolean allowMaintainerToPush;
    private Integer approvalsBeforeMerge;
    private Assignee assignee;
    private List<Assignee> assignees;
    private List<Reviewer> reviewers;
    private Author author;
    private Boolean blockingDiscussionsResolved;
    private List<Diff> changes;
    private String changesCount;
    private Date closedAt;
    private Participant closedBy;
    private Date createdAt;
    private String description;
    private Boolean discussionLocked;
    private Integer divergedCommitsCount;
    private Integer downvotes;
    private Boolean forceRemoveSourceBranch;
    private Boolean hasConflicts;
    private Long id;
    private Long iid;
    private List<String> labels;
    private Date latestBuildFinishedAt;
    private Date latestBuildStartedAt;
    private String mergeCommitSha;
    private String squashCommitSha;
    /**
     * @deprecated since 15.6, use {@link #detailedMergeStatus} instead.
     * see https://docs.gitlab.com/ee/update/deprecations.html#merge_status-api-field
     */
    @Deprecated
    private String mergeStatus;
    private String detailedMergeStatus;
    private Date mergedAt;
    /**
     * @deprecated since 14.7, use {@link #mergeUser} instead.
     * see https://docs.gitlab.com/ee/update/deprecations.html#merged_by-api-field
     */
    private Participant mergedBy;
    private Participant mergeUser;
    private Boolean mergeWhenPipelineSucceeds;
    private String mergeError;
    private Milestone milestone;
    private Pipeline pipeline;
    private Long projectId;
    private String sha;
    private Boolean shouldRemoveSourceBranch;
    private String sourceBranch;
    private Long sourceProjectId;
    private Boolean squash;
    private String state;
    private Boolean subscribed;
    private String targetBranch;
    private Long targetProjectId;
    private TaskCompletionStatus taskCompletionStatus;
    private References references;
    private TimeStats timeStats;
    private String title;
    private Date updatedAt;
    private Integer upvotes;
    private Integer userNotesCount;
    private String webUrl;
    private Boolean workInProgress;
    private DiffRef diffRefs;
    private Boolean rebaseInProgress;

    // The approval fields will only be available when listing approvals, approving  or unapproving a merge reuest.
    private Integer approvalsRequired;
    private Integer approvalsLeft;

    @JsonSerialize(using = JacksonJson.UserListSerializer.class)
    @JsonDeserialize(using = JacksonJson.UserListDeserializer.class)
    private List<User> approvedBy;

    public Boolean getAllowCollaboration() {
        return allowCollaboration;
    }

    public void setAllowCollaboration(Boolean allowCollaboration) {
        this.allowCollaboration = allowCollaboration;
    }

    public Boolean getAllowMaintainerToPush() {
        return allowMaintainerToPush;
    }

    public void setAllowMaintainerToPush(Boolean allowMaintainerToPush) {
        this.allowMaintainerToPush = allowMaintainerToPush;
    }

    public Integer getApprovalsBeforeMerge() {
        return approvalsBeforeMerge;
    }

    public void setApprovalsBeforeMerge(Integer approvalsBeforeMerge) {
        this.approvalsBeforeMerge = approvalsBeforeMerge;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    public List<Assignee> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<Assignee> assignees) {
        this.assignees = assignees;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Boolean getBlockingDiscussionsResolved() {
        return blockingDiscussionsResolved;
    }

    public void setBlockingDiscussionsResolved(Boolean blockingDiscussionsResolved) {
        this.blockingDiscussionsResolved = blockingDiscussionsResolved;
    }

    public List<Diff> getChanges() {
        return changes;
    }

    public void setChanges(List<Diff> changes) {
        this.changes = changes;
    }

    public String getChangesCount() {
        return changesCount;
    }

    public void setChangesCount(String changesCount) {
        this.changesCount = changesCount;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }

    public Participant getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(Participant closedBy) {
        this.closedBy = closedBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDiscussionLocked() {
        return discussionLocked;
    }

    public void setDiscussionLocked(Boolean discussionLocked) {
        this.discussionLocked = discussionLocked;
    }

    public Integer getDivergedCommitsCount() {
        return divergedCommitsCount;
    }

    public void setDivergedCommitsCount(Integer divergedCommitsCount) {
        this.divergedCommitsCount = divergedCommitsCount;
    }

    public Integer getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(Integer downvotes) {
        this.downvotes = downvotes;
    }

    public Boolean getForceRemoveSourceBranch() {
        return forceRemoveSourceBranch;
    }

    public void setForceRemoveSourceBranch(Boolean forceRemoveSourceBranch) {
        this.forceRemoveSourceBranch = forceRemoveSourceBranch;
    }

    public Boolean getHasConflicts() {
        return hasConflicts;
    }

    public void setHasConflicts(Boolean hasConflicts) {
        this.hasConflicts = hasConflicts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIid() {
        return iid;
    }

    public void setIid(Long iid) {
        this.iid = iid;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public Date getLatestBuildFinishedAt() {
        return latestBuildFinishedAt;
    }

    public void setLatestBuildFinishedAt(Date latestBuildFinishedAt) {
        this.latestBuildFinishedAt = latestBuildFinishedAt;
    }

    public Date getLatestBuildStartedAt() {
        return latestBuildStartedAt;
    }

    public void setLatestBuildStartedAt(Date latestBuildStartedAt) {
        this.latestBuildStartedAt = latestBuildStartedAt;
    }

    public String getMergeCommitSha() {
        return mergeCommitSha;
    }

    public void setMergeCommitSha(String mergeCommitSha) {
        this.mergeCommitSha = mergeCommitSha;
    }

    public String getSquashCommitSha() {
        return squashCommitSha;
    }

    public void setSquashCommitSha(String squashCommitSha) {
        this.squashCommitSha = squashCommitSha;
    }

    /**
     * @deprecated since 15.6, use {@link #getDetailedMergeStatus()} instead.
     */
    @Deprecated
    public String getMergeStatus() {
        return mergeStatus;
    }

    /**
     * @deprecated since 15.6, use {@link #setDetailedMergeStatus(String)} instead.
     */
    @Deprecated
    public void setMergeStatus(String mergeStatus) {
        this.mergeStatus = mergeStatus;
    }

    public String getDetailedMergeStatus() {
        return detailedMergeStatus;
    }

    public void setDetailedMergeStatus(String detailedMergeStatus) {
        this.detailedMergeStatus = detailedMergeStatus;
    }

    public Date getMergedAt() {
        return mergedAt;
    }

    public void setMergedAt(Date mergedAt) {
        this.mergedAt = mergedAt;
    }

    /**
     * @deprecated since 14.7, use {@link #getMergeUser()} instead.
     * see https://docs.gitlab.com/ee/update/deprecations.html#merged_by-api-field
     */
    @Deprecated
    public Participant getMergedBy() {
        return mergedBy;
    }

    /**
     * @deprecated since 14.7, use {@link #setMergeUser(Participant)} instead.
     * see https://docs.gitlab.com/ee/update/deprecations.html#merged_by-api-field
     */
    @Deprecated
    public void setMergedBy(Participant mergedBy) {
        this.mergedBy = mergedBy;
    }

    public Participant getMergeUser() {
        return mergeUser;
    }

    public void setMergeUser(Participant mergeUser) {
        this.mergeUser = mergeUser;
    }

    public Boolean getMergeWhenPipelineSucceeds() {
        return mergeWhenPipelineSucceeds;
    }

    public void setMergeWhenPipelineSucceeds(Boolean mergeWhenPipelineSucceeds) {
        this.mergeWhenPipelineSucceeds = mergeWhenPipelineSucceeds;
    }

    public String getMergeError() {
        return mergeError;
    }

    public void setMergeError(String mergeError) {
        this.mergeError = mergeError;
    }

    public Milestone getMilestone() {
        return milestone;
    }

    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }

    public Pipeline getPipeline() {
        return pipeline;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public Boolean getShouldRemoveSourceBranch() {
        return shouldRemoveSourceBranch;
    }

    public void setShouldRemoveSourceBranch(Boolean shouldRemoveSourceBranch) {
        this.shouldRemoveSourceBranch = shouldRemoveSourceBranch;
    }

    public String getSourceBranch() {
        return sourceBranch;
    }

    public void setSourceBranch(String sourceBranch) {
        this.sourceBranch = sourceBranch;
    }

    public Long getSourceProjectId() {
        return sourceProjectId;
    }

    public void setSourceProjectId(Long sourceProjectId) {
        this.sourceProjectId = sourceProjectId;
    }

    public Boolean getSquash() {
        return squash;
    }

    public void setSquash(Boolean squash) {
        this.squash = squash;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }

    public String getTargetBranch() {
        return targetBranch;
    }

    public void setTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
    }

    public Long getTargetProjectId() {
        return targetProjectId;
    }

    public void setTargetProjectId(Long targetProjectId) {
        this.targetProjectId = targetProjectId;
    }

    public TaskCompletionStatus getTaskCompletionStatus() {
        return taskCompletionStatus;
    }

    public void setTaskCompletionStatus(TaskCompletionStatus taskCompletionStatus) {
        this.taskCompletionStatus = taskCompletionStatus;
    }

    public References getReferences() {
        return references;
    }

    public void setReferences(References references) {
        this.references = references;
    }

    public TimeStats getTimeStats() {
        return timeStats;
    }

    public void setTimeStats(TimeStats timeStats) {
        this.timeStats = timeStats;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(Integer upvotes) {
        this.upvotes = upvotes;
    }

    public Integer getUserNotesCount() {
        return userNotesCount;
    }

    public void setUserNotesCount(Integer userNotesCount) {
        this.userNotesCount = userNotesCount;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public Boolean getWorkInProgress() {
        return workInProgress;
    }

    public void setWorkInProgress(Boolean workInProgress) {
        this.workInProgress = workInProgress;
    }

    /**
     * Get the number of approvals required for the merge request.
     *
     * NOTE: This property will only be used when listing, approiving, or unapproving a merge request.
     *
     * @return the number of approvals required for the merge request
     */
    public Integer getApprovalsRequired() {
        return approvalsRequired;
    }

    /**
     * Set the number of approvals required for the merge request.
     *
     * NOTE: This property will only be used when listing, approiving, or unapproving a merge request.
     *
     * @param approvalsRequired the number of approvals required for the merge request
     */
    public void setApprovalsRequired(Integer approvalsRequired) {
        this.approvalsRequired = approvalsRequired;
    }

    /**
     * Get the number of approvals left for the merge request.
     *
     * NOTE: This property will only be used when listing, approiving, or unapproving a merge request.
     *
     * @return the number of approvals left for the merge request
     */
    public Integer getApprovalsLeft() {
        return approvalsLeft;
    }

    /**
     * Set the number of approvals missing for the merge request.
     *
     * NOTE: This property will only be used when listing, approiving, or unapproving a merge request.
     *
     * @param approvalsLeft the number of approvals missing for the merge request
     */
    public void setApprovalsLeft(Integer approvalsLeft) {
        this.approvalsLeft = approvalsLeft;
    }

    /**
     * Get the list of users that have approved the merge request.
     *
     * NOTE: This property will only be used when listing, approiving, or unapproving a merge request.
     *
     * @return the list of users that have approved the merge request
     */
    public List<User> getApprovedBy() {
        return approvedBy;
    }

    /**
     * Set the list of users that have approved the merge request.
     *
     * NOTE: This property will only be used when listing, approiving, or unapproving a merge request.
     *
     * @param approvedBy the list of users that have approved the merge request
     */
    public void setApprovedBy(List<User> approvedBy) {
        this.approvedBy = approvedBy;
    }

    public DiffRef getDiffRefs() {
        return diffRefs;
    }

    public void setDiffRefs(final DiffRef diffRefs) {
        this.diffRefs = diffRefs;
    }

    public Boolean getRebaseInProgress() {
        return rebaseInProgress;
    }

    public void setRebaseInProgress(Boolean rebaseInProgress) {
        this.rebaseInProgress = rebaseInProgress;
    }

    public static final boolean isValid(MergeRequest mergeRequest) {
        return (mergeRequest != null && mergeRequest.getId() != null);
    }

    public List<Reviewer> getReviewers() {
        return reviewers;
    }

    public void setReviewers(List<Reviewer> reviewers) {
        this.reviewers = reviewers;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
