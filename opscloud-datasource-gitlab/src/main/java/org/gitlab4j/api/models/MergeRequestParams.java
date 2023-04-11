package org.gitlab4j.api.models;

import org.gitlab4j.api.Constants.StateEvent;
import org.gitlab4j.api.GitLabApiForm;

import java.util.Arrays;
import java.util.List;

/**
 * This class provides the form parameters for creating and updating merge requests.
 */
public class MergeRequestParams {

    private String sourceBranch;
    private String targetBranch;
    private String title;
    private Long assigneeId;
    private List<Long> assigneeIds;
    private List<Long> reviewerIds;
    private Long milestoneId;
    private List<String> labels;
    private String description;
    private Long targetProjectId;
    private StateEvent stateEvent;
    private Boolean removeSourceBranch;
    private Boolean squash;
    private Boolean discussionLocked;
    private Boolean allowCollaboration;
    private Integer approvalsBeforeMerge;

    /**
     * Set the source branch. This is for merge request creation only.
     *
     * @param sourceBranch the sourceBranch to set
     * @return the reference to this MergeRequestParams instance
     */
    public MergeRequestParams withSourceBranch(String sourceBranch) {
	this.sourceBranch = sourceBranch;
	return (this);
    }

    /**
     * Set the target branch.
     *
     * @param targetBranch the targetBranch to set
     * @return the reference to this MergeRequestParams instance
     */
    public MergeRequestParams withTargetBranch(String targetBranch) {
	this.targetBranch = targetBranch;
	return (this);
    }

    /**
     * Set the title of the merge request.
     *
     * @param title the title to set
     * @return the reference to this MergeRequestParams instance
     */
    public MergeRequestParams withTitle(String title) {
	this.title = title;
	return (this);
    }

    /**
     * Set the assignee user ID.
     *
     * @param assigneeId the assigneeId to set
     * @return the reference to this MergeRequestParams instance
     */
    public MergeRequestParams withAssigneeId(Long assigneeId) {
	this.assigneeId = assigneeId;
	return (this);
    }

    /**
     * The ID of the user(s) to assign the merge request to. Set to 0 or provide
     * an empty value to unassign all assignees.
     *
     * @param assigneeIds the assigneeIds to set
     * @return the reference to this MergeRequestParams instance
     */
    public MergeRequestParams withAssigneeIds(List<Long> assigneeIds) {
	this.assigneeIds = assigneeIds;
	return (this);
    }

    /**
     * The ID of the user(s) to assign to the review of the merge request. Set to 0 or provide
     * an empty value to unassign all reviewers.
     *
     * @param reviewerIds the reviewerIds to set
     * @return the reference to this MergeRequestParams instance
     */
    public MergeRequestParams withReviewerIds(List<Long> reviewerIds) {
        this.reviewerIds = reviewerIds;
        return (this);
    }

    /**
     * Set the milestone ID field value.
     *
     * @param milestoneId the milestoneId to set
     * @return the reference to this MergeRequestParams instance
     */
    public MergeRequestParams withMilestoneId(Long milestoneId) {
	this.milestoneId = milestoneId;
	return (this);
    }

    /**
     * Set the labels for the merge request.
     *
     * @param labels the List of labels to set
     * @return the reference to this MergeRequestParams instance
     */
    public MergeRequestParams withLabels(List<String> labels) {
	this.labels = labels;
	return (this);
    }

    /**
     * Set the labels for the merge request.
     *
     * @param labels the array of labels to set
     * @return the reference to this MergeRequestParams instance
     */
    public MergeRequestParams withLabels(String[] labels) {
	this.labels = (labels != null ? Arrays.asList(labels) : null);
	return (this);
    }

    /**
     * Set the description of the merge request. Limited to 1,048,576 characters.
     *
     * @param description the description to set
     * @return the reference to this MergeRequestParams instance
     */
    public MergeRequestParams withDescription(String description) {
	this.description = description;
	return (this);
    }

    /**
     * Set the target project ID. This is for merge request creation only.
     *
     * @param targetProjectId the targetProjectId to set
     * @return the reference to this MergeRequestParams instance
     */
    public MergeRequestParams withTargetProjectId(Long targetProjectId) {
	this.targetProjectId = targetProjectId;
	return (this);
    }

    /**
     * New state (close/reopen). This is for merge request updates only.
     *
     * @param stateEvent the stateEvent to set
     * @return the reference to this MergeRequestParams instance
     */
    public MergeRequestParams withStateEvent(StateEvent stateEvent) {
	this.stateEvent = stateEvent;
	return (this);
    }

    /**
     * Flag indicating if a merge request should remove the source branch when merging.
     *
     * @param removeSourceBranch the removeSourceBranch to set
     * @return the reference to this MergeRequestParams instance
     */
    public MergeRequestParams withRemoveSourceBranch(Boolean removeSourceBranch) {
	this.removeSourceBranch = removeSourceBranch;
	return (this);
    }

    /**
     * Squash commits into a single commit when merging.
     *
     * @param squash the squash to set
     * @return the reference to this MergeRequestParams instance
     */
    public MergeRequestParams withSquash(Boolean squash) {
	this.squash = squash;
	return (this);
    }

    /**
     * Flag indicating if the merge request’s discussion is locked. If the discussion is locked only
     * project members can add, edit or resolve comments. This is for merge request updates only.
     *
     * @param discussionLocked the discussionLocked to set
     * @return the reference to this MergeRequestParams instance
     */
    public MergeRequestParams withDiscussionLocked(Boolean discussionLocked) {
	this.discussionLocked = discussionLocked;
	return (this);
    }

    /**
     * Allow commits from members who can merge to the target branch.
     *
     * @param allowCollaboration the allowCollaboration to set
     * @return the reference to this MergeRequestParams instance
     */
    public MergeRequestParams withAllowCollaboration(Boolean allowCollaboration) {
	this.allowCollaboration = allowCollaboration;
	return (this);
    }

    /**
     * Set the approvals_before_merge field value. This is for merge request creation only.
     *
     * @param approvalsBeforeMerge the approvalsBeforeMerge to set
     * @return the reference to this MergeRequestParams instance
     */
    public MergeRequestParams withApprovalsBeforeMerge(Integer approvalsBeforeMerge) {
	this.approvalsBeforeMerge = approvalsBeforeMerge;
	return (this);
    }

    /**
     * Get the form params specified by this instance.
     *
     * @param isCreate set to true if this is for a create merge request API call,
     *                 set to false if this is for an update merge request
     * @return a GitLabApiForm instance holding the form parameters for this MergeRequestParams instance
     */
    public GitLabApiForm getForm(boolean isCreate) {

	GitLabApiForm form = new GitLabApiForm()
            .withParam("target_branch", targetBranch, isCreate)
            .withParam("title", title, isCreate)
            .withParam("assignee_id", assigneeId)
            .withParam("assignee_ids", assigneeIds)
            .withParam("reviewer_ids", reviewerIds)
            .withParam("milestone_id", milestoneId)
            .withParam("labels", (labels != null ? String.join(",", labels) : null))
            .withParam("description", description)
            .withParam("remove_source_branch", removeSourceBranch)
            .withParam("squash", squash)
            .withParam("allow_collaboration", allowCollaboration);

	if (isCreate) {
	    form.withParam("source_branch", sourceBranch, true)
		.withParam("target_project_id", targetProjectId)
		.withParam("approvals_before_merge", approvalsBeforeMerge);
	} else {
	    form.withParam("state_event", stateEvent)
	        .withParam("discussion_locked", discussionLocked);
	}

	return (form);
    }
}
