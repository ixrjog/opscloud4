package org.gitlab4j.api.webhook;

import org.gitlab4j.api.models.Assignee;
import org.gitlab4j.api.models.Duration;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class EventMergeRequest {

    private Long assigneeId;
    private Long authorId;
    private String branchName;
    private Date createdAt;
    private String description;
    private Long id;
    private Long iid;
    private String mergeCommitSha;
    private String mergeStatus;
    private Long milestoneId;
    private Integer position;
    private Date lockedAt;
    private Long projectId;
    private String sourceBranch;
    private Long sourceProjectId;
    private String stCommits;
    private String stDiffs;
    private String state;
    private String targetBranch;
    private Long targetProjectId;
    private String title;
    private Date updatedAt;

    private EventProject source;
    private EventProject target;
    private EventCommit lastCommit;
    private Boolean workInProgress;
    private String url;
    private String action;
    private Assignee assignee;

    private Long updatedById;
    private String mergeError;
    private Map<String, String> mergeParams;
    private Boolean mergeWhenPipelineSucceeds;
    private Long mergeUserId;
    private Date deletedAt;
    private String inProgressMergeCommitSha;
    private Integer lockVersion;

    private Date last_editedAt;
    private Long lastEditedById;
    private Long headPipelineId;
    private Boolean refFetched;
    private Long mergeIid;
    private Integer totalTimeSpent;
    private Duration humanTotalTimeSpent;
    private Integer timeEstimate;
    private Duration humanTimeEstimate;
    private List<Long> assigneeIds;

    public Long getAssigneeId() {
        return this.assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Long getAuthorId() {
        return this.authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getBranchName() {
        return this.branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIid() {
        return this.iid;
    }

    public void setIid(Long iid) {
        this.iid = iid;
    }

    public String getMergeCommitSha() {
        return mergeCommitSha;
    }

    public void setMergeCommitSha(String mergeCommitSha) {
        this.mergeCommitSha = mergeCommitSha;
    }

    public String getMergeStatus() {
        return this.mergeStatus;
    }

    public void setMergeStatus(String mergeStatus) {
        this.mergeStatus = mergeStatus;
    }

    public Long getMilestoneId() {
        return this.milestoneId;
    }

    public void setMilestoneId(Long milestoneId) {
        this.milestoneId = milestoneId;
    }

    public Integer getPosition() {
        return this.position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Date getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(Date lockedAt) {
        this.lockedAt = lockedAt;
    }

    public Long getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getSourceBranch() {
        return this.sourceBranch;
    }

    public void setSourceBranch(String sourceBranch) {
        this.sourceBranch = sourceBranch;
    }

    public Long getSourceProjectId() {
        return this.sourceProjectId;
    }

    public void setSourceProjectId(Long sourceProjectId) {
        this.sourceProjectId = sourceProjectId;
    }

    public String getStCommits() {
        return this.stCommits;
    }

    public void setStCommits(String stCommits) {
        this.stCommits = stCommits;
    }

    public String getStDiffs() {
        return this.stDiffs;
    }

    public void setStDiffs(String stDiffs) {
        this.stDiffs = stDiffs;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTargetBranch() {
        return this.targetBranch;
    }

    public void setTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
    }

    public Long getTargetProjectId() {
        return this.targetProjectId;
    }

    public void setTargetProjectId(Long targetProjectId) {
        this.targetProjectId = targetProjectId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventProject getSource() {
        return source;
    }

    public void setSource(EventProject source) {
        this.source = source;
    }

    public EventProject getTarget() {
        return target;
    }

    public void setTarget(EventProject target) {
        this.target = target;
    }

    public EventCommit getLastCommit() {
        return lastCommit;
    }

    public void setLastCommit(EventCommit lastCommit) {
        this.lastCommit = lastCommit;
    }

    public Boolean getWorkInProgress() {
        return workInProgress;
    }

    public void setWorkInProgress(Boolean workInProgress) {
        this.workInProgress = workInProgress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    public Long getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Long updatedById) {
        this.updatedById = updatedById;
    }

    public String getMergeError() {
        return mergeError;
    }

    public void setMergeError(String mergeError) {
        this.mergeError = mergeError;
    }

    public Map<String, String> getMergeParams() {
        return mergeParams;
    }

    public void setMergeParams(Map<String, String> mergeParams) {
        this.mergeParams = mergeParams;
    }

    public Boolean getMergeWhenPipelineSucceeds() {
        return mergeWhenPipelineSucceeds;
    }

    public void setMergeWhenPipelineSucceeds(Boolean mergeWhenPipelineSucceeds) {
        this.mergeWhenPipelineSucceeds = mergeWhenPipelineSucceeds;
    }

    public Long getMergeUserId() {
        return mergeUserId;
    }

    public void setMergeUserId(Long mergeUserId) {
        this.mergeUserId = mergeUserId;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getInProgressMergeCommitSha() {
        return inProgressMergeCommitSha;
    }

    public void setInProgressMergeCommitSha(String inProgressMergeCommitSha) {
        this.inProgressMergeCommitSha = inProgressMergeCommitSha;
    }

    public Integer getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(Integer lockVersion) {
        this.lockVersion = lockVersion;
    }

    public Date getLast_editedAt() {
        return last_editedAt;
    }

    public void setLast_editedAt(Date last_editedAt) {
        this.last_editedAt = last_editedAt;
    }

    public Long getLastEditedById() {
        return lastEditedById;
    }

    public void setLastEditedById(Long lastEditedById) {
        this.lastEditedById = lastEditedById;
    }

    public Long getHeadPipelineId() {
        return headPipelineId;
    }

    public void setHeadPipelineId(Long headPipelineId) {
        this.headPipelineId = headPipelineId;
    }

    public Boolean getRefFetched() {
        return refFetched;
    }

    public void setRefFetched(Boolean refFetched) {
        this.refFetched = refFetched;
    }

    public Long getMergeIid() {
        return mergeIid;
    }

    public void setMergeIid(Long mergeIid) {
        this.mergeIid = mergeIid;
    }

    public Integer getTotalTimeSpent() {
        return totalTimeSpent;
    }

    public void setTotalTimeSpent(Integer totalTimeSpent) {
        this.totalTimeSpent = totalTimeSpent;
    }

    public Duration getHumanTotalTimeSpent() {
        return humanTotalTimeSpent;
    }

    public void setHumanTotalTimeSpent(Duration humanTotalTimeSpent) {
        this.humanTotalTimeSpent = humanTotalTimeSpent;
    }

    public Integer getTimeEstimate() {
        return timeEstimate;
    }

    public void setTimeEstimate(Integer timeEstimate) {
        this.timeEstimate = timeEstimate;
    }

    public Duration getHumanTimeEstimate() {
        return humanTimeEstimate;
    }

    public void setHumanTimeEstimate(Duration humanTimeEstimate) {
        this.humanTimeEstimate = humanTimeEstimate;
    }

    public List<Long> getAssigneeIds() {
        return assigneeIds;
    }

    public void setAssigneeIds(List<Long> assigneeIds) {
        this.assigneeIds = assigneeIds;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}