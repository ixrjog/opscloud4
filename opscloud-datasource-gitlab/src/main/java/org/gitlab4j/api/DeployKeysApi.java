package org.gitlab4j.api;

import org.gitlab4j.api.models.DeployKey;

import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This class implements the client side API for the GitLab Deploy Keys API calls.
 */
public class DeployKeysApi extends AbstractApi {

    public DeployKeysApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Get a list of all deploy keys across all projects of the GitLab instance. This method requires admin access.
     *
     * <pre><code>GitLab Endpoint: GET /deploy_keys</code></pre>
     *
     * @return a list of DeployKey
     * @throws GitLabApiException if any exception occurs
     */
    public List<DeployKey> getDeployKeys() throws GitLabApiException {
        return (getDeployKeys(getDefaultPerPage()).all());
    }

    /**
     * Get a list of all deploy keys across all projects of the GitLab instance using the specified page and per page settings. 
     * This method requires admin access.
     *
     * <pre><code>GitLab Endpoint: GET /deploy_keys</code></pre>
     *
     * @param page the page to get
     * @param perPage the number of deploy keys per page
     * @return the list of DeployKey in the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public List<DeployKey> getDeployKeys(int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "deploy_keys");
        return (response.readEntity(new GenericType<List<DeployKey>>() {}));
    }

    /**
     * Get a Pager of all deploy keys across all projects of the GitLab instance. This method requires admin access.
     *
     * <pre><code>GitLab Endpoint: GET /deploy_keys</code></pre>
     *
     * @param itemsPerPage the number of DeployKey instances that will be fetched per page
     * @return a Pager of DeployKey
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<DeployKey> getDeployKeys(int itemsPerPage) throws GitLabApiException {
        return (new Pager<DeployKey>(this, DeployKey.class, itemsPerPage, null, "deploy_keys"));
    }

    /**
     * Get a Stream of all deploy keys across all projects of the GitLab instance. This method requires admin access.
     *
     * <pre><code>GitLab Endpoint: GET /deploy_keys</code></pre>
     *
     * @return a list of DeployKey
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<DeployKey> getDeployKeysStream() throws GitLabApiException {
        return (getDeployKeys(getDefaultPerPage()).stream());
    }

    /**
     * Get a list of the deploy keys for the specified project. This method requires admin access.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deploy_keys</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a list of DeployKey
     * @throws GitLabApiException if any exception occurs
     */
    public List<DeployKey> getProjectDeployKeys(Object projectIdOrPath) throws GitLabApiException {
        return (getProjectDeployKeys(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a list of the deploy keys for the specified project using the specified page and per page settings. 
     * This method requires admin access.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deploy_keys</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param page the page to get
     * @param perPage the number of deploy keys per page
     * @return the list of DeployKey in the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public List<DeployKey> getProjectDeployKeys(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage),
                "projects", getProjectIdOrPath(projectIdOrPath), "deploy_keys");
        return (response.readEntity(new GenericType<List<DeployKey>>() {}));
    }

    /**
     * Get a Pager of the deploy keys for the specified project. This method requires admin access.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deploy_keys</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance@param projectId the ID of the project
     * @param itemsPerPage the number of DeployKey instances that will be fetched per page
     * @return a Pager of DeployKey
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<DeployKey> getProjectDeployKeys(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<DeployKey>(this, DeployKey.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "deploy_keys"));
    }

    /**
     * Get a list of the deploy keys for the specified project. This method requires admin access.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deploy_keys</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a list of DeployKey
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<DeployKey> getProjectDeployKeysStream(Object projectIdOrPath) throws GitLabApiException {
        return (getProjectDeployKeys(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get a single deploy key for the specified project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deploy_keys/:key_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param keyId the ID of the deploy key to delete
     * @return the DeployKey instance for the specified project ID and key ID
     * @throws GitLabApiException if any exception occurs
     */
    public DeployKey getDeployKey(Object projectIdOrPath, Long keyId) throws GitLabApiException {

        if (keyId == null) {
            throw new RuntimeException("keyId cannot be null");
        }

        Response response = get(Response.Status.OK, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "deploy_keys", keyId);
        return (response.readEntity(DeployKey.class));
    }

    /**
     * Get a single deploy key for the specified project as an Optional instance.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/deploy_keys/:key_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param keyId the ID of the deploy key to delete
     * @return the DeployKey for the specified project ID and key ID as an Optional instance
     */
    public Optional<DeployKey> getOptionalDeployKey(Object projectIdOrPath, Long keyId) {
        try {
            return (Optional.ofNullable(getDeployKey(projectIdOrPath, keyId)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Creates a new deploy key for a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/deploy_keys</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param title the new deploy key's title, required
     * @param key the new deploy key, required
     * @param canPush can deploy key push to the project's repository, optional
     * @return an DeployKey instance with info on the added deploy key
     * @throws GitLabApiException if any exception occurs
     */
    public DeployKey addDeployKey(Object projectIdOrPath, String title, String key, Boolean canPush) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("title", title, true)
                .withParam("key", key, true)
                .withParam("can_push",  canPush);
        Response response = post(Response.Status.CREATED, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "deploy_keys");
        return (response.readEntity(DeployKey.class));
    }

    /**
     * Updates an existing project deploy key.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/deploy_keys/:key_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param deployKeyId the ID of the deploy key to update, required
     * @param title the title for the deploy key, optional
     * @param canPush can deploy key push to the project's repository, optional
     * @return an updated DeployKey instance
     * @throws GitLabApiException if any exception occurs
     */
    public DeployKey updateDeployKey(Object projectIdOrPath, Long deployKeyId, String title, Boolean canPush) throws GitLabApiException {

        if (deployKeyId == null) {
            throw new RuntimeException("deployKeyId cannot be null");
        }

        final DeployKey key = new DeployKey();
        key.setCanPush(canPush);
        key.setTitle(title);
        final Response response = put(Response.Status.OK, key,
                "projects", getProjectIdOrPath(projectIdOrPath), "deploy_keys", deployKeyId);

        return (response.readEntity(DeployKey.class));
    }

    /**
     * Removes a deploy key from the project. If the deploy key is used only for this project,it will be deleted from the system.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/deploy_keys/:key_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param keyId the ID of the deploy key to delete
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteDeployKey(Object projectIdOrPath, Long keyId) throws GitLabApiException {

        if (keyId == null) {
            throw new RuntimeException("keyId cannot be null");
        }

        delete(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "deploy_keys", keyId);
    }

    /**
     * Enables a deploy key for a project so this can be used. Returns the enabled key when successful.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/deploy_keys/:key_id/enable</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param keyId the ID of the deploy key to enable
     * @return an DeployKey instance with info on the enabled deploy key
     * @throws GitLabApiException if any exception occurs
     */
    public DeployKey enableDeployKey(Object projectIdOrPath, Long keyId) throws GitLabApiException {

        if (keyId == null) {
            throw new RuntimeException("keyId cannot be null");
        }

        Response response = post(Response.Status.CREATED, (Form)null,
                "projects", getProjectIdOrPath(projectIdOrPath), "deploy_keys", keyId, "enable");
        return (response.readEntity(DeployKey.class));
    }
}
