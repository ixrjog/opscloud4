package org.gitlab4j.api;

import org.gitlab4j.api.models.DeployToken;

import jakarta.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class implements the client side API for the GitLab Deploy Tokens API calls.
 * See https://docs.gitlab.com/ee/api/deploy_tokens.html
 *
 * Since GitLab 12.9
 *
 */
public class DeployTokensApi extends AbstractApi {

    public DeployTokensApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /* ************************************************************************************************
     * Global Deploy Token API
     */

    /**
     * Get a list of all deploy tokens across the GitLab instance. This endpoint requires admin access.
     *
     * <pre><code>GitLab Endpoint: GET /deploy_tokens</code></pre>
     *
     * @return a list of DeployToken
     * @throws GitLabApiException if any exception occurs
     */
    public List<DeployToken> getDeployTokens() throws GitLabApiException {
        return (getDeployTokens(getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of all deploy tokens across all projects of the GitLab instance. This method requires admin access.
     *
     * <pre><code>GitLab Endpoint: GET /deploy_tokens</code></pre>
     *
     * @param itemsPerPage the number of DeployToken instances that will be fetched per page
     * @return a Pager of DeployToken
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<DeployToken> getDeployTokens(int itemsPerPage) throws GitLabApiException {
        return (new Pager<>(this, DeployToken.class, itemsPerPage, null, "deploy_tokens"));
    }

    /**
     * Get a Stream of all deploy tokens across all projects of the GitLab instance. This method requires admin access.
     *
     * <pre><code>GitLab Endpoint: GET /deploy_tokens</code></pre>
     *
     * @return a list of DeployToken
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<DeployToken> getDeployTokensStream() throws GitLabApiException {
        return (getDeployTokens(getDefaultPerPage()).stream());
    }

    /* ************************************************************************************************
     * Projects Deploy Token API
     */

    /**
     * Get a list of the deploy tokens for the specified project. This method requires admin access.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deploy_tokens</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a list of DeployToken
     * @throws GitLabApiException if any exception occurs
     */
    public List<DeployToken> getProjectDeployTokens(Object projectIdOrPath) throws GitLabApiException {
        return (getProjectDeployTokens(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of the deploy tokens for the specified project. This method requires admin access.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deploy_tokens</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance@param projectId the ID of the project
     * @param itemsPerPage the number of DeployToken instances that will be fetched per page
     * @return a Pager of DeployToken
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<DeployToken> getProjectDeployTokens(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<>(this, DeployToken.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "deploy_tokens"));
    }

    /**
     * Get a list of the deploy tokens for the specified project. This method requires admin access.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deploy_tokens</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a list of DeployToken
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<DeployToken> getProjectDeployTokensStream(Object projectIdOrPath) throws GitLabApiException {
        return (getProjectDeployTokens(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Creates a new deploy token for a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/deploy_tokens</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param name the new deploy token’s name, required
     * @param expiresAt expiration date for the deploy token. Currently documented as not required but api fails if not provided. Does not expire if no value is provided.
     * @param username the username for deploy token. Currently documented as not required but api fails if not provided. Default is gitlab+deploy-token-{n}
     * @param scopes indicates the deploy token scopes. Must be at least one of {@link DeployTokenScope}.
     * @return an DeployToken instance with info on the added deploy token
     * @throws GitLabApiException if any exception occurs
     */
    public DeployToken addProjectDeployToken(Object projectIdOrPath, String name, Date expiresAt, String username, List<DeployTokenScope> scopes) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("name", name, true)
                .withParam("expires_at", expiresAt, true) // Currently documented as not required but api fails if not provided
                .withParam("username",  username, true)// Currently documented as not required but api fails if not provided
                .withParam("scopes", scopes, true);
        Response response = post(Response.Status.CREATED, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "deploy_tokens");
        return (response.readEntity(DeployToken.class));
    }

    /**
     * Removes a deploy token from the group.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/deploy_tokens/:token_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param tokenId the ID of the deploy token to delete
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteProjectDeployToken(Object projectIdOrPath, Long tokenId) throws GitLabApiException {

        if (tokenId == null) {
            throw new RuntimeException("tokenId cannot be null");
        }

        delete(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "deploy_tokens", tokenId);
    }

    /* ************************************************************************************************
     * Groups Deploy Token API
     */

    /**
     * Get a list of the deploy tokens for the specified group. This method requires admin access.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/deploy_tokens</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @return a list of DeployToken
     * @throws GitLabApiException if any exception occurs
     */
    public List<DeployToken> getGroupDeployTokens(Object groupIdOrPath) throws GitLabApiException {
        return (getGroupDeployTokens(groupIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of the deploy tokens for the specified group. This method requires admin access.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/deploy_tokens</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance@param groupId the ID of the group
     * @param itemsPerPage the number of DeployToken instances that will be fetched per page
     * @return a Pager of DeployToken
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<DeployToken> getGroupDeployTokens(Object groupIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<>(this, DeployToken.class, itemsPerPage, null,
                "groups", getGroupIdOrPath(groupIdOrPath), "deploy_tokens"));
    }

    /**
     * Get a list of the deploy tokens for the specified group. This method requires admin access.
     *
     * <pre><code>GitLab Endpoint: GET /groups/:id/deploy_tokens</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @return a list of DeployToken
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<DeployToken> getGroupDeployTokensStream(Object groupIdOrPath) throws GitLabApiException {
        return (getGroupDeployTokens(groupIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Creates a new deploy token for a group.
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/deploy_tokens</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param name the new deploy token’s name, required
     * @param expiresAt expiration date for the deploy token. Currently documented as not required but api fails if not provided. Does not expire if no value is provided.
     * @param username the username for deploy token. Currently documented as not required but api fails if not provided. Default is gitlab+deploy-token-{n}
     * @param scopes indicates the deploy token scopes. Must be at least one of {@link DeployTokenScope}.
     * @return an DeployToken instance with info on the added deploy token
     * @throws GitLabApiException if any exception occurs
     */
    public DeployToken addGroupDeployToken(Object groupIdOrPath, String name, Date expiresAt, String username, List<DeployTokenScope> scopes) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("name", name, true)
                .withParam("expires_at", expiresAt, true) // Currently documented as not required but api fails if not provided
                .withParam("username",  username, true)// Currently documented as not required but api fails if not provided
                .withParam("scopes", scopes, true);
        Response response = post(Response.Status.CREATED, formData,
                "groups", getGroupIdOrPath(groupIdOrPath), "deploy_tokens");
        return (response.readEntity(DeployToken.class));
    }

    /**
     * Removes a deploy token from the group.
     *
     * <pre><code>GitLab Endpoint: DELETE /groups/:id/deploy_tokens/:token_id</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param tokenId the ID of the deploy token to delete
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteGroupDeployToken(Object groupIdOrPath, Long tokenId) throws GitLabApiException {

        if (tokenId == null) {
            throw new RuntimeException("tokenId cannot be null");
        }

        delete(Response.Status.OK, null, "groups", getGroupIdOrPath(groupIdOrPath), "deploy_tokens", tokenId);
    }

}
