package org.gitlab4j.api;

import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.AllowedTo;
import org.gitlab4j.api.models.ProtectedBranch;

import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This class provides an entry point to all the Protected Branches API calls.
 * @see <a href="https://docs.gitlab.com/ee/api/protected_branches.html">Protected branches API at GitLab</a>
 */
public class ProtectedBranchesApi extends AbstractApi {

    public ProtectedBranchesApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Gets a list of protected branches from a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/protected_branches</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return the list of protected branches for the project
     * @throws GitLabApiException if any exception occurs
     */
    public List<ProtectedBranch> getProtectedBranches(Object projectIdOrPath) throws GitLabApiException {
        return (getProtectedBranches(projectIdOrPath, this.getDefaultPerPage()).all());
    }

    /**
     * Gets a Pager of protected branches from a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/protected_branches</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of instances that will be fetched per page
     * @return the Pager of protected branches for the project
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<ProtectedBranch> getProtectedBranches(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<ProtectedBranch>(this, ProtectedBranch.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "protected_branches"));
    }

    /**
     * Gets a Stream of protected branches from a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/protected_branches</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return the Stream of protected branches for the project
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<ProtectedBranch> getProtectedBranchesStream(Object projectIdOrPath) throws GitLabApiException {
        return (getProtectedBranches(projectIdOrPath, this.getDefaultPerPage()).stream());
    }

    /**
     * Get a single protected branch or wildcard protected branch.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/protected_branches/:branch_name</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param branchName the name of the branch or wildcard
     * @return a ProtectedBranch instance with info on the protected branch
     * @throws GitLabApiException if any exception occurs
     */
    public ProtectedBranch getProtectedBranch(Object projectIdOrPath, String branchName) throws GitLabApiException {
        Response response = get(Response.Status.OK, null,
                "projects", this.getProjectIdOrPath(projectIdOrPath), "protected_branches", urlEncode(branchName));
        return (response.readEntity(ProtectedBranch.class));
    }

    /**
     * Get an Optional instance with the value for the specific protected branch.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/protected_branches/:branch_name</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param branchName the name of the branch or wildcard
     * @return an Optional instance with the specified protected branch as a value
     */
    public Optional<ProtectedBranch> getOptionalProtectedBranch(Object projectIdOrPath, String branchName) {
        try {
            return (Optional.ofNullable(getProtectedBranch(projectIdOrPath, branchName)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Unprotects the given protected branch or wildcard protected branch.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/protected_branches/:name</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param branchName the name of the branch to un-protect, can be a wildcard
     * @throws GitLabApiException if any exception occurs
     */
    public void unprotectBranch(Object projectIdOrPath, String branchName) throws GitLabApiException {
        delete(Response.Status.NO_CONTENT, null, "projects", getProjectIdOrPath(projectIdOrPath), "protected_branches", urlEncode(branchName));
    }

    /**
     * Protects a single repository branch or several project repository branches
     * using a wildcard protected branch.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/protected_branches</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param branchName the name of the branch to protect, can be a wildcard
     * @return the branch info for the protected branch
     * @throws GitLabApiException if any exception occurs
     */
    public ProtectedBranch protectBranch(Object projectIdOrPath, String branchName) throws GitLabApiException {
        return protectBranch(projectIdOrPath, branchName, AccessLevel.MAINTAINER, AccessLevel.MAINTAINER);
    }

    /**
     * Protects a single repository branch or several project repository branches using a wildcard protected branch.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/protected_branches</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param branchName the name of the branch to protect, can be a wildcard
     * @param pushAccessLevel Access levels allowed to push (defaults: 40, maintainer access level)
     * @param mergeAccessLevel Access levels allowed to merge (defaults: 40, maintainer access level)
     * @return the branch info for the protected branch
     * @throws GitLabApiException if any exception occurs
     */
    public ProtectedBranch protectBranch(Object projectIdOrPath, String branchName, AccessLevel pushAccessLevel, AccessLevel mergeAccessLevel) throws GitLabApiException {
        return (protectBranch(projectIdOrPath, branchName, pushAccessLevel, mergeAccessLevel, null, null));
    }

    /**
     * Protects a single repository branch or several project repository branches using a wildcard protected branch.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/protected_branches</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param branchName the name of the branch to protect, can be a wildcard
     * @param pushAccessLevel access levels allowed to push (defaults: 40, maintainer access level)
     * @param mergeAccessLevel access levels allowed to merge (defaults: 40, maintainer access level)
     * @param unprotectAccessLevel access levels allowed to unprotect (defaults: 40, maintainer access level)
     * @param codeOwnerApprovalRequired prevent pushes to this branch if it matches an item in the CODEOWNERS file. (defaults: false)
     * @return the branch info for the protected branch
     * @throws GitLabApiException if any exception occurs
     */
    public ProtectedBranch protectBranch(Object projectIdOrPath, String branchName,
            AccessLevel pushAccessLevel, AccessLevel mergeAccessLevel, AccessLevel unprotectAccessLevel,
            Boolean codeOwnerApprovalRequired) throws GitLabApiException {
        Form formData = new GitLabApiForm()
                .withParam("name", branchName, true)
                .withParam("push_access_level", pushAccessLevel)
                .withParam("merge_access_level", mergeAccessLevel)
                .withParam("unprotect_access_level", unprotectAccessLevel)
                .withParam("code_owner_approval_required", codeOwnerApprovalRequired);
        Response response = post(Response.Status.CREATED, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "protected_branches");
        return (response.readEntity(ProtectedBranch.class));
    }

    /**
     * Protects a single repository branch or several project repository branches using a wildcard protected branch.
     *
     * <p>NOTE: This method is only available to GitLab Starter, Bronze, or higher.</p>
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/protected_branches</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param branchName the name of the branch to protect, can be a wildcard
     * @param allowedToPushUserId user ID allowed to push, can be null
     * @param allowedToMergeUserId user ID allowed to merge, can be null
     * @param allowedToUnprotectUserId user ID allowed to unprotect, can be null
     * @param codeOwnerApprovalRequired prevent pushes to this branch if it matches an item in the CODEOWNERS file. (defaults: false)
     * @return the branch info for the protected branch
     * @throws GitLabApiException if any exception occurs
     */
    public ProtectedBranch protectBranch(Object projectIdOrPath, String branchName,
            Integer allowedToPushUserId, Integer allowedToMergeUserId, Integer allowedToUnprotectUserId,
            Boolean codeOwnerApprovalRequired) throws GitLabApiException {

        Form formData = new GitLabApiForm()
                .withParam("name", branchName, true)
                .withParam("allowed_to_push[][user_id]", allowedToPushUserId)
                .withParam("allowed_to_merge[][user_id]", allowedToMergeUserId)
                .withParam("allowed_to_unprotect[][user_id]", allowedToUnprotectUserId)
                .withParam("code_owner_approval_required", codeOwnerApprovalRequired);
        Response response = post(Response.Status.CREATED, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "protected_branches");
        return (response.readEntity(ProtectedBranch.class));
    }

    /**
     * Protects a single repository branch or several project repository branches using a wildcard protected branch.
     *
     * <p>NOTE: This method is only available to GitLab Starter, Bronze, or higher.</p>
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/protected_branches</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param branchName the name of the branch to protect, can be a wildcard
     * @param allowedToPush an AllowedTo instance holding the configuration for "allowed_to_push"
     * @param allowedToMerge an AllowedTo instance holding the configuration for "allowed_to_merge"
     * @param allowedToUnprotect an AllowedTo instance holding the configuration for "allowed_to_unprotect" be null
     * @param codeOwnerApprovalRequired prevent pushes to this branch if it matches an item in the CODEOWNERS file. (defaults: false)
     * @return the branch info for the protected branch
     * @throws GitLabApiException if any exception occurs
     */
    public ProtectedBranch protectBranch(Object projectIdOrPath, String branchName,
            AllowedTo allowedToPush, AllowedTo allowedToMerge, AllowedTo allowedToUnprotect,
            Boolean codeOwnerApprovalRequired) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("name", branchName, true)
                .withParam("code_owner_approval_required", codeOwnerApprovalRequired);

        if (allowedToPush != null) {
            allowedToPush.getForm(formData, "allowed_to_push");
        }
        if (allowedToMerge != null) {
            allowedToMerge.getForm(formData, "allowed_to_merge");
        }
        if (allowedToUnprotect != null) {
            allowedToUnprotect.getForm(formData, "allowed_to_unprotect");
        }

        Response response = post(Response.Status.CREATED, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "protected_branches");
        return (response.readEntity(ProtectedBranch.class));
    }

    /**
     * Sets the code_owner_approval_required flag on the specified protected branch.
     *
     * <p>NOTE: This method is only available in GitLab Premium or higher.</p>
     *
     * <pre><code>GitLab Endpoint: PATCH /projects/:id/protected_branches/:branch_name?code_owner_approval_required=true</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param branchName the name of the branch to protect, can be a wildcard
     * @param codeOwnerApprovalRequired prevent pushes to this branch if it matches an item in the CODEOWNERS file.
     * @return the branch info for the protected branch
     * @throws GitLabApiException if any exception occurs
     */
    public ProtectedBranch setCodeOwnerApprovalRequired(Object projectIdOrPath, String branchName,
            Boolean codeOwnerApprovalRequired) throws GitLabApiException {
        Form formData = new GitLabApiForm()
                .withParam("code_owner_approval_required", codeOwnerApprovalRequired);

        Response response = patch(Response.Status.OK, formData.asMap(),
                "projects", this.getProjectIdOrPath(projectIdOrPath),
                "protected_branches", urlEncode(branchName));
        return (response.readEntity(ProtectedBranch.class));
    }
}
