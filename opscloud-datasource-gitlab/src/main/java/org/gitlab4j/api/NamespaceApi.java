package org.gitlab4j.api;

import org.gitlab4j.api.models.Namespace;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class implements the client side API for the GitLab namespace calls.
 */
public class NamespaceApi extends AbstractApi {

    public NamespaceApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Get a list of the namespaces of the authenticated user. If the user is an administrator,
     * a list of all namespaces in the GitLab instance is created.
     *
     * <pre><code>GitLab Endpoint: GET /namespaces</code></pre>
     *
     * @return a List of Namespace instances
     * @throws GitLabApiException if any exception occurs
     */
    public List<Namespace> getNamespaces() throws GitLabApiException {
        return (getNamespaces(getDefaultPerPage()).all());
    }

    /**
     * Get a list of the namespaces of the authenticated user. If the user is an administrator,
     * a list of all namespaces in the GitLab instance is returned.
     *
     * <pre><code>GitLab Endpoint: GET /namespaces</code></pre>
     *
     * @param page the page to get
     * @param perPage the number of Namespace instances per page
     * @return a List of Namespace instances in the specified page range
     * @throws GitLabApiException if any exception occurs
     */
    public List<Namespace> getNamespaces(int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "namespaces");
        return (response.readEntity(new GenericType<List<Namespace>>() {}));
    }

    /**
     * Get a Pager of the namespaces of the authenticated user. If the user is an administrator,
     * a Pager of all namespaces in the GitLab instance is returned.
     *
     * <pre><code>GitLab Endpoint: GET /namespaces</code></pre>
     *
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of Namespace instances
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Namespace> getNamespaces(int itemsPerPage) throws GitLabApiException {
        return (new Pager<Namespace>(this, Namespace.class, itemsPerPage, null, "namespaces"));
    }

    /**
     * Get a Stream of the namespaces of the authenticated user. If the user is an administrator,
     * a Stream of all namespaces in the GitLab instance is returned.
     *
     * <pre><code>GitLab Endpoint: GET /namespaces</code></pre>
     *
     * @return a Stream of Namespace instances
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Namespace> getNamespacesStream() throws GitLabApiException {
        return (getNamespaces(getDefaultPerPage()).stream());
    }

    /**
     * Get all namespaces that match a string in their name or path.
     *
     * <pre><code>GitLab Endpoint: GET /namespaces?search=:query</code></pre>
     *
     * @param query the search string
     * @return the Namespace List with the matching namespaces
     * @throws GitLabApiException if any exception occurs
     */
    public List<Namespace> findNamespaces(String query) throws GitLabApiException {
        return (findNamespaces(query, getDefaultPerPage()).all());
    }

    /**
     * Get all namespaces that match a string in their name or path in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /namespaces?search=:query</code></pre>
     *
     * @param query the search string
     * @param page the page to get
     * @param perPage the number of Namespace instances per page
     * @return the Namespace List with the matching namespaces
     * @throws GitLabApiException if any exception occurs
     */
    public List<Namespace> findNamespaces(String query, int page, int perPage) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("search", query, true).withParam(PAGE_PARAM,  page).withParam(PER_PAGE_PARAM,  perPage);
        Response response = get(Response.Status.OK, formData.asMap(), "namespaces");
        return (response.readEntity(new GenericType<List<Namespace>>() {}));
    }

    /**
     * Get a Pager of all namespaces that match a string in their name or path.
     *
     * <pre><code>GitLab Endpoint: GET /namespaces?search=:query</code></pre>
     *
     * @param query the search string
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of Namespace instances with the matching namespaces
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Namespace> findNamespaces(String query, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("search", query, true);
        return (new Pager<Namespace>(this, Namespace.class, itemsPerPage, formData.asMap(), "namespaces"));
    }

    /**
     * Get all namespaces that match a string in their name or path as a Stream.
     *
     * <pre><code>GitLab Endpoint: GET /namespaces?search=:query</code></pre>
     *
     * @param query the search string
     * @return a Stream with the matching namespaces
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Namespace> findNamespacesStream(String query) throws GitLabApiException {
        return (findNamespaces(query, getDefaultPerPage()).stream());
    }
}
