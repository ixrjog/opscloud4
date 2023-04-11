package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.List;

public class ApprovalRule {

    private Long id;
    private String name;
    private String ruleType;
    private List<User> eligibleApprovers;
    private Integer approvalsRequired;
    private ApprovalRule sourceRule;
    private List<User> users;
    private List<Group> groups;
    private Boolean containsHiddenGroups;
    private List<User> approvedBy;
    private Boolean approved;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getRuleType() {
	return ruleType;
    }

    public void setRuleType(String ruleType) {
	this.ruleType = ruleType;
    }

    public List<User> getEligibleApprovers() {
	return eligibleApprovers;
    }

    public void setEligibleApprovers(List<User> eligibleApprovers) {
	this.eligibleApprovers = eligibleApprovers;
    }

    public Integer getApprovalsRequired() {
	return approvalsRequired;
    }

    public void setApprovalsRequired(Integer approvalsRequired) {
	this.approvalsRequired = approvalsRequired;
    }

    public ApprovalRule getSourceRule() {
	return sourceRule;
    }

    public void setSourceRule(ApprovalRule sourceRule) {
	this.sourceRule = sourceRule;
    }

    public List<User> getUsers() {
	return users;
    }

    public void setUsers(List<User> users) {
	this.users = users;
    }

    public List<Group> getGroups() {
	return groups;
    }

    public void setGroups(List<Group> groups) {
	this.groups = groups;
    }

    public Boolean getContainsHiddenGroups() {
	return containsHiddenGroups;
    }

    public void setContainsHiddenGroups(Boolean containsHiddenGroups) {
	this.containsHiddenGroups = containsHiddenGroups;
    }

    public List<User> getApprovedBy() {
	return approvedBy;
    }

    public void setApprovedBy(List<User> approvedBy) {
	this.approvedBy = approvedBy;
    }

    public Boolean getApproved() {
	return approved;
    }

    public void setApproved(Boolean approved) {
	this.approved = approved;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
