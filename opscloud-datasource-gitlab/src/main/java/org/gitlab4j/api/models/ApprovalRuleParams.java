package org.gitlab4j.api.models;

import org.gitlab4j.api.GitLabApiForm;

import java.util.List;

public class ApprovalRuleParams {

    private String name;
    private Integer approvalsRequired;
    private List<Long> userIds;
    private List<Long> groupIds;

    public ApprovalRuleParams withName(String name) {
	this.name = name;
	return (this);
    }

    public ApprovalRuleParams withApprovalsRequired(Integer approvalsRequired) {
	this.approvalsRequired = approvalsRequired;
	return (this);
    }

    public ApprovalRuleParams withUserIds(List<Long> userIds) {
	this.userIds = userIds;
	return (this);
    }

    public ApprovalRuleParams withGroupIds(List<Long> groupIds) {
	this.groupIds = groupIds;
	return (this);
    }

    /**
     * Get the form params specified by this instance.
     *
     * @return a GitLabApiForm instance holding the form parameters for this ApprovalRuleParams instance
     */
    public GitLabApiForm getForm() {
	return new GitLabApiForm()
            .withParam("name", name)
            .withParam("approvals_required", approvalsRequired, true)
            .withParam("user_ids", userIds)
            .withParam("group_ids", groupIds);
    }
}
