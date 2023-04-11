package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gitlab4j.api.GitLabApiForm;

public class ProjectApprovalsConfig {

    private Integer approvalsBeforeMerge;
    private Boolean resetApprovalsOnPush;
    private Boolean disableOverridingApproversPerMergeRequest;
    private Boolean mergeRequestsAuthorApproval;
    private Boolean mergeRequestsDisableCommittersApproval;

    public Integer getApprovalsBeforeMerge() {
        return approvalsBeforeMerge;
    }

    public void setApprovalsBeforeMerge(Integer approvalsBeforeMerge) {
        this.approvalsBeforeMerge = approvalsBeforeMerge;
    }

    public ProjectApprovalsConfig withApprovalsBeforeMerge(Integer approvalsBeforeMerge) {
        this.approvalsBeforeMerge = approvalsBeforeMerge;
        return (this);
   }

    public Boolean getResetApprovalsOnPush() {
        return resetApprovalsOnPush;
    }

    public void setResetApprovalsOnPush(Boolean resetApprovalsOnPush) {
        this.resetApprovalsOnPush = resetApprovalsOnPush;
    }

    public ProjectApprovalsConfig withResetApprovalsOnPush(Boolean resetApprovalsOnPush) {
        this.resetApprovalsOnPush = resetApprovalsOnPush;
        return (this);
    }

    public Boolean getDisableOverridingApproversPerMergeRequest() {
        return disableOverridingApproversPerMergeRequest;
    }

    public void setDisableOverridingApproversPerMergeRequest(Boolean disableOverridingApproversPerMergeRequest) {
        this.disableOverridingApproversPerMergeRequest = disableOverridingApproversPerMergeRequest;
    }

    public ProjectApprovalsConfig withDisableOverridingApproversPerMergeRequest(Boolean disableOverridingApproversPerMergeRequest) {
        this.disableOverridingApproversPerMergeRequest = disableOverridingApproversPerMergeRequest;
        return (this);
    }

    public Boolean getMergeRequestsAuthorApproval() {
        return mergeRequestsAuthorApproval;
    }

    public void setMergeRequestsAuthorApproval(Boolean mergeRequestsAuthorApproval) {
        this.mergeRequestsAuthorApproval = mergeRequestsAuthorApproval;
    }

    public ProjectApprovalsConfig withMergeRequestsAuthorApproval(Boolean mergeRequestsAuthorApproval) {
        this.mergeRequestsAuthorApproval = mergeRequestsAuthorApproval;
        return (this);
     }

    public Boolean getMergeRequestsDisableCommittersApproval() {
        return mergeRequestsDisableCommittersApproval;
    }

    public void setMergeRequestsDisableCommittersApproval(Boolean mergeRequestsDisableCommittersApproval) {
        this.mergeRequestsDisableCommittersApproval = mergeRequestsDisableCommittersApproval;
    }

    public ProjectApprovalsConfig withMergeRequestsDisableCommittersApproval(Boolean mergeRequestsDisableCommittersApproval) {
        this.mergeRequestsDisableCommittersApproval = mergeRequestsDisableCommittersApproval;
        return (this);
    }

    /**
     * Get the form params specified by this instance.
     *
     * @return a GitLabApiForm instance holding the form parameters for this ProjectApprovalsConfig instance
     */
    @JsonIgnore
    public GitLabApiForm getForm() {
	return new GitLabApiForm()
            .withParam("approvals_before_merge", approvalsBeforeMerge)
            .withParam("reset_approvals_on_push", resetApprovalsOnPush)
            .withParam("disable_overriding_approvers_per_merge_request", disableOverridingApproversPerMergeRequest)
            .withParam("merge_requests_author_approval", mergeRequestsAuthorApproval)
            .withParam("merge_requests_disable_committers_approval", mergeRequestsDisableCommittersApproval);
    }
}
