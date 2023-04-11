package org.gitlab4j.api;

import org.gitlab4j.api.models.Deployment;
import org.gitlab4j.api.models.DeploymentFilter;
import org.gitlab4j.api.models.MergeRequest;

import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This class implements the client side API for the GitLab Deployments API calls.
 * See https://docs.gitlab.com/ee/api/deployments.html
 *
 */
public class DeploymentsApi extends AbstractApi {

    public DeploymentsApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Get a list of deployments for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deployments</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a list of Deployments
     * @throws GitLabApiException if any exception occurs
     */
    public List<Deployment> getProjectDeployments(Object projectIdOrPath) throws GitLabApiException {
        return (getProjectDeployments(projectIdOrPath, null, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of all deployments for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deployments</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of Deployments instances that will be fetched per page
     * @return a Pager of Deployment
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Deployment> getProjectDeployments(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (getProjectDeployments(projectIdOrPath, null, itemsPerPage));
    }

    /**
     * Get a Pager of all deployments for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deployments</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filter  {@link DeploymentFilter} a DeploymentFilter instance with the filter settings
     * @return a Pager of Deployment
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Deployment> getProjectDeployments(Object projectIdOrPath, DeploymentFilter filter) throws GitLabApiException {
        return (getProjectDeployments(projectIdOrPath, filter, getDefaultPerPage()));
    }

    /**
     * Get a Pager of all deployments for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deployments</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filter  {@link DeploymentFilter} a DeploymentFilter instance with the filter settings
     * @param itemsPerPage the number of Deployments instances that will be fetched per page
     * @return a Pager of Deployment
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Deployment> getProjectDeployments(Object projectIdOrPath, DeploymentFilter filter, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = (filter != null ? filter.getQueryParams() : new GitLabApiForm());
        return (new Pager<Deployment>(this, Deployment.class, itemsPerPage, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "deployments"));
    }

    /**
     * Get a Stream of all deployments for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deployments</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a list of Deployment
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Deployment> getProjectDeploymentsStream(Object projectIdOrPath) throws GitLabApiException {
        return (getProjectDeployments(projectIdOrPath, null, getDefaultPerPage()).stream());
    }

    /**
     * Get a Stream of all deployments for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deployments</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filter  {@link DeploymentFilter} a DeploymentFilter instance with the filter settings
     * @return a list of Deployment
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Deployment> getProjectDeploymentsStream(Object projectIdOrPath, DeploymentFilter filter) throws GitLabApiException {
        return (getProjectDeployments(projectIdOrPath, filter, getDefaultPerPage()).stream());
    }

    /**
     * Get a specific deployment.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deployments/:deployment_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param deploymentId the ID of a project's deployment
     * @return the specified Deployment instance
     * @throws GitLabApiException if any exception occurs
     */
    public Deployment getDeployment(Object projectIdOrPath, Long deploymentId) throws GitLabApiException {
        Response response = get(Response.Status.OK, getDefaultPerPageParam(),
                "projects", getProjectIdOrPath(projectIdOrPath), "deployments", deploymentId);
        return (response.readEntity(Deployment.class));
    }

    /**
     * Get a specific deployment as an Optional instance.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deployments/:deployment_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param deploymentId the ID of a project's deployment
     * @return the specified Deployment as an Optional instance
     */
    public Optional<Deployment> getOptionalDeployment(Object projectIdOrPath, Long deploymentId) {
        try {
            return (Optional.ofNullable(getDeployment(projectIdOrPath, deploymentId)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Creates a new deployment for a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/deployments</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param environment The name of the environment to create the deployment for, required
     * @param sha The SHA of the commit that is deployed, required
     * @param ref The name of the branch or tag that is deployed, required
     * @param tag A boolean that indicates if the deployed ref is a tag (true) or not (false), required
     * @param status The status to filter deployments by, required
     * @return a Deployment instance with info on the added deployment
     * @throws GitLabApiException if any exception occurs
     */
    public Deployment addDeployment(Object projectIdOrPath, String environment, String sha, String ref, Boolean tag, DeploymentStatus status) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("environment", environment, true)
                .withParam("sha", sha, true)
                .withParam("ref", ref, true)
                .withParam("tag", tag, true)
                .withParam("status", status, true);

        Response response = post(Response.Status.CREATED, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "deployments");
        return (response.readEntity(Deployment.class));
    }

    /**
     * Updates an existing project deploy key.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/deployments/:key_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param deploymentId The ID of the deployment to update, required
     * @param status The new status of the deployment, required
     * @return an updated Deployment instance
     * @throws GitLabApiException if any exception occurs
     */
    public Deployment updateDeployment(Object projectIdOrPath, Long deploymentId, DeploymentStatus status) throws GitLabApiException {

        if (deploymentId == null) {
            throw new RuntimeException("deploymentId cannot be null");
        }

        final Deployment deployment = new Deployment();
        deployment.setStatus(status);
        final Response response = put(Response.Status.OK, deployment,
                "projects", getProjectIdOrPath(projectIdOrPath), "deployments", deploymentId);

        return (response.readEntity(Deployment.class));
    }

    /**
     * Get a list of Merge Requests shipped with a given deployment.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deployments/:deployment_id/merge_requests</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param deploymentId The ID of the deployment to update, required
     * @return a list containing the MergeRequest instances shipped with a given deployment
     * @throws GitLabApiException GitLabApiException if any exception occurs during execution
     */
    public List<MergeRequest> getMergeRequests(Object projectIdOrPath, Long deploymentId) throws GitLabApiException {
        return (getMergeRequests(projectIdOrPath, deploymentId, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of Merge Requests shipped with a given deployment.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deployments/:deployment_id/merge_requests</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param deploymentId The ID of the deployment to update, required
     * @param itemsPerPage the number of Commit instances that will be fetched per page
     * @return a Pager containing the MergeRequest instances shipped with a given deployment
     * @throws GitLabApiException GitLabApiException if any exception occurs during execution
     */
    public Pager<MergeRequest> getMergeRequests(Object projectIdOrPath, Long deploymentId, int itemsPerPage) throws GitLabApiException {
    return (new Pager<MergeRequest>(this, MergeRequest.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "deployments", deploymentId, "merge_requests"));
    }

    /**
     * Get a Stream of Merge Requests shipped with a given deployment.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deployments/:deployment_id/merge_requests</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param deploymentId The ID of the deployment to update, required
     * @return a Stream containing the MergeRequest instances shipped with a given deployment
     * @throws GitLabApiException GitLabApiException if any exception occurs during execution
     */
    public Stream<MergeRequest> getMergeRequestsStream(Object projectIdOrPath, Long deploymentId) throws GitLabApiException {
        return (getMergeRequests(projectIdOrPath, deploymentId, getDefaultPerPage()).stream());
    }


}
