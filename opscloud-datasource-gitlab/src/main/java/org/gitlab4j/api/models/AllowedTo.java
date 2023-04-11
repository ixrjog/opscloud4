package org.gitlab4j.api.models;

import org.gitlab4j.api.GitLabApiForm;

/**
 * This class is used by the ProtectedBranchesAPi to set up the
 * allowed_to_push, allowed_to_merge, and allowed_to_unprotect values.
 */
public class AllowedTo {

    private AccessLevel accessLevel;
    private Long userId;
    private Long groupId;

    public AllowedTo(AccessLevel accessLevel, Long userId, Long groupId) {
	this.accessLevel = accessLevel;
	this.userId = userId;
	this.groupId = groupId;
    }

    public AllowedTo withAccessLevel(AccessLevel accessLevel) {
	this.accessLevel = accessLevel;
	return (this);
    }

    public AllowedTo withUserId(Long userId) {
	this.userId = userId;
	return (this);
    }

    public AllowedTo withGroupId(Long groupId) {
	this.groupId = groupId;
	return (this);
    }

    public GitLabApiForm getForm(GitLabApiForm form, String allowedToName) {

	if (form == null) {
	    form = new GitLabApiForm();
	}

	return (form
		.withParam(allowedToName + "[][access_level]", accessLevel)
		.withParam(allowedToName + "[][user_id]", userId)
		.withParam(allowedToName + "[][group_id]", groupId));
    }
}
