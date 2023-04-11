package org.gitlab4j.api;

import org.gitlab4j.api.models.ExternalStatusCheck;
import org.gitlab4j.api.models.ExternalStatusCheckProtectedBranch;
import org.gitlab4j.api.models.ExternalStatusCheckResult;
import org.gitlab4j.api.models.ExternalStatusCheckStatus;
import org.gitlab4j.api.models.ExternalStatusCheckStatus.Status;

import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class implements the client side API for the GitLab external status checks.
 * See <a href="https://docs.gitlab.com/ee/api/status_checks.html">External Status Checks API</a> for more information.
 */
public class ExternalStatusCheckApi extends AbstractApi {

    public ExternalStatusCheckApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Gets a list of all external status checks for a given project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/external_status_checks</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @return a List of ExternalStatusCheck
     * @throws GitLabApiException if any exception occurs
     */
    public List<ExternalStatusCheck> getExternalStatusChecks(Object projectIdOrPath) throws GitLabApiException {
        return (getExternalStatusChecks(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Gets a Pager of all external status checks for a given project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/external_status_checks</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param itemsPerPage the number of ExternalStatusCheck instances that will be fetched per page
     * @return the Pager of ExternalStatusCheck instances
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<ExternalStatusCheck> getExternalStatusChecks(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<ExternalStatusCheck>(this, ExternalStatusCheck.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "external_status_checks"));
    }

    /**
     * Gets a Stream of all external status checks for a given project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/external_status_checks</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @return a Stream of ExternalStatusCheck
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<ExternalStatusCheck> getExternalStatusChecksStream(Object projectIdOrPath) throws GitLabApiException {
        return (getExternalStatusChecks(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Creates a new external status check.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/external_status_checks</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param name Display name of external status check (required)
     * @param externalUrl URL of external status check resource (optional)
     * @param protectedBranchIds IDs of protected branches to scope the rule by (optional)
     * @return an ExternalStatusCheck instance containing info on the newly created externalStatusCheck
     * @throws GitLabApiException if any exception occurs
     */
    public ExternalStatusCheck createExternalStatusCheck(Object projectIdOrPath, String name, String externalUrl, List<Long> protectedBranchIds) throws GitLabApiException {
        Form formData = new GitLabApiForm()
                .withParam("name", name, true)
                .withParam("external_url", externalUrl, true)
                .withParam("protected_branch_ids", protectedBranchIds);
        Response response = post(Response.Status.CREATED, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "external_status_checks");
        return (response.readEntity(ExternalStatusCheck.class));
    }

    /**
     * Creates a new external status check using the information contained in the provided ExternalStatusCheck instance. Only the following
     * fields from the ExternalStatusCheck instance are used:
     * <pre><code>
     *      name - Display name of external status check (required)
     *      external url - URL of external status check resource (required)
     *      protected branches - the id of the protected branches (optional)
     * </code></pre>
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/external_status_checks</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param externalStatusCheck the ExternalStatusCheck instance with information for the new external status check
     * @return an ExternalStatusCheck instance containing info on the newly created externalStatusCheck
     * @throws GitLabApiException if any exception occurs
     */
    public ExternalStatusCheck createExternalStatusCheck(Object projectIdOrPath, ExternalStatusCheck externalStatusCheck) throws GitLabApiException {
        List<Long> protectedBranchIds;
        if(externalStatusCheck.getProtectedBranches() == null) {
            protectedBranchIds = null;
        } else {
            protectedBranchIds = externalStatusCheck.getProtectedBranches().stream().map(ExternalStatusCheckProtectedBranch::getId).collect(Collectors.toList());
        }
        Form formData = new GitLabApiForm()
                .withParam("name", externalStatusCheck.getName(), true)
                .withParam("external_url", externalStatusCheck.getExternalUrl(), true)
                .withParam("protected_branch_ids", protectedBranchIds);
        Response response = post(Response.Status.CREATED, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "external_status_checks");
        return (response.readEntity(ExternalStatusCheck.class));
    }

    /**
     * Updates an existing external status check.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/external_status_checks/:check_id</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param checkId ID of an external status check to update (required)
     * @param name Display name of external status check (optional)
     * @param externalUrl URL of external status check resource (optional)
     * @param protectedBranchIds IDs of protected branches to scope the rule by (optional)
     * @return an ExternalStatusCheck instance containing info on the newly created ExternalStatusCheck
     * @throws GitLabApiException if any exception occurs
     */
    public ExternalStatusCheck updateExternalStatusCheck(Object projectIdOrPath, Long checkId, String name, String externalUrl, List<Long> protectedBranchIds) throws GitLabApiException {
        Form formData = new GitLabApiForm()
                .withParam("name", name)
                .withParam("external_url", externalUrl)
                .withParam("protected_branch_ids", protectedBranchIds);
        Response response = put(Response.Status.OK, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "external_status_checks", checkId);
        return (response.readEntity(ExternalStatusCheck.class));
    }

    /**
     * Updates an external status check using the information contained in the provided ExternalStatusCheck instance. Only the following
     * fields from the ExternalStatusCheck instance are used:
     * <pre><code>
     *      id - the id of the external status check (required)
     *      name - Display name of external status check (optional)
     *      external url - URL of external status check resource (optional)
     *      protected branches - the id of the protected branches (optional)
     * </code></pre>
     * <pre><code>GitLab Endpoint: PUT /projects/:id/external_status_checks/:check_id</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param externalStatusCheck the ExternalStatusCheck instance with update information
     * @return an ExternalStatusCheck instance containing info on the updated ExternalStatusCheck
     * @throws GitLabApiException if any exception occurs
     */
    public ExternalStatusCheck updateExternalStatusCheck(Object projectIdOrPath, ExternalStatusCheck externalStatusCheck) throws GitLabApiException {
        if (externalStatusCheck == null || externalStatusCheck.getId() == null) {
            throw new GitLabApiException("the specified external status check is null or has no id");
        }
        List<Long> protectedBranchIds = getProtectedBranchIds(externalStatusCheck);
        Form formData = new GitLabApiForm()
                .withParam("name", externalStatusCheck.getName())
                .withParam("external_url", externalStatusCheck.getExternalUrl())
                .withParam("protected_branch_ids", protectedBranchIds);
        Response response = put(Response.Status.OK, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "external_status_checks", externalStatusCheck.getId());
        return (response.readEntity(ExternalStatusCheck.class));
    }

    private List<Long> getProtectedBranchIds(ExternalStatusCheck externalStatusCheck) {
        if(externalStatusCheck.getProtectedBranches() == null) {
            return null;
        }
        return externalStatusCheck.getProtectedBranches().stream().map(ExternalStatusCheckProtectedBranch::getId).collect(Collectors.toList());
    }

    /**
     * Deletes an external status check.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/external_status_checks/:check_id</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param checkId ID of an external status check
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteExternalStatusCheck(Object projectIdOrPath, Long checkId) throws GitLabApiException {
        delete(Response.Status.NO_CONTENT, null, "projects", getProjectIdOrPath(projectIdOrPath), "external_status_checks", checkId);
    }

    /**
     * Gets a list of all statuses of the external status checks for a given merge request.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/merge_requests/:merge_request_iid/status_checks</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param mergeRequestIid the merge request IID to get the statuses
     * @return a List of ExternalStatusCheckStatus
     * @throws GitLabApiException if any exception occurs
     */
    public List<ExternalStatusCheckStatus> getExternalStatusCheckStatuses(Object projectIdOrPath, Long mergeRequestIid) throws GitLabApiException {
        return (getExternalStatusCheckStatuses(projectIdOrPath, mergeRequestIid, getDefaultPerPage()).all());
    }

    /**
     * Gets a Pager of all statuses of the external status checks for a given merge request.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/merge_requests/:merge_request_iid/status_checks</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param mergeRequestIid the merge request IID to get the statuses
     * @param itemsPerPage the number of ExternalStatusCheckStatus instances that will be fetched per page
     * @return the Pager of ExternalStatusCheckStatus instances
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<ExternalStatusCheckStatus> getExternalStatusCheckStatuses(Object projectIdOrPath, Long mergeRequestIid, int itemsPerPage) throws GitLabApiException {
        return (new Pager<ExternalStatusCheckStatus>(this, ExternalStatusCheckStatus.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "merge_requests", mergeRequestIid, "status_checks"));
    }

    /**
     * Gets a Stream of all statuses of the external status checks for a given merge request.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/merge_requests/:merge_request_iid/status_checks</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param mergeRequestIid the merge request IID to get the statuses
     * @return a Stream of ExternalStatusCheckStatus
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<ExternalStatusCheckStatus> getExternalStatusCheckStatusesStream(Object projectIdOrPath, Long mergeRequestIid) throws GitLabApiException {
        return (getExternalStatusCheckStatuses(projectIdOrPath, mergeRequestIid, getDefaultPerPage()).stream());
    }

    /**
     * Set the status of an external status check for a given merge request.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/merge_requests/:merge_request_iid/status_check_responses</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param mergeRequestIid the merge request IID to get the statuses
     * @param sha the commit SHA to set the status for (required)
     * @param externalStatusCheckId ID of an external status check (required)
     * @param status the status to set (optional)
     * @return an ExternalStatusCheckResult instance containing info on the newly created status
     * @throws GitLabApiException if any exception occurs
     */
    public ExternalStatusCheckResult setStatusOfExternalStatusCheck(Object projectIdOrPath, Long mergeRequestIid, String sha, Long externalStatusCheckId, Status status) throws GitLabApiException {
        Form formData = new GitLabApiForm()
                .withParam("sha", sha)
                .withParam("external_status_check_id", externalStatusCheckId)
                .withParam("status", status);
        Response response = post(Response.Status.CREATED, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "merge_requests", mergeRequestIid, "status_check_responses");
        return (response.readEntity(ExternalStatusCheckResult.class));
    }

    /**
     * Retry the specified failed external status check for a single merge request. Even though the merge request hasn’t changed, this endpoint resends the current state of merge request to the defined external service.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/merge_requests/:merge_request_iid/status_checks/:external_status_check_id/retry</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param mergeRequestIid the merge request IID to get the statuses
     * @param externalStatusCheckId ID of an external status check
     * @throws GitLabApiException if any exception occurs
     */
    public void retryExternalStatusCheck(Object projectIdOrPath, Long mergeRequestIid, Long externalStatusCheckId) throws GitLabApiException {
        post(Response.Status.ACCEPTED, (Form)null, "projects", getProjectIdOrPath(projectIdOrPath), "merge_requests", mergeRequestIid, "status_checks", externalStatusCheckId, "retry");
    }

}
