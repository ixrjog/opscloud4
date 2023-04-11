package org.gitlab4j.api;

import org.gitlab4j.api.models.Snippet;
import org.gitlab4j.api.models.Visibility;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This class provides an entry point to all the GitLab Snippets API project calls.
 */
public class SnippetsApi extends AbstractApi {

    public SnippetsApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Get a list of the authenticated user's snippets.
     *
     * <pre><code>GitLab Endpoint: GET /snippets</code></pre>
     *
     * @param downloadContent indicating whether to download the snippet content
     * @return a list of authenticated user's snippets
     * @throws GitLabApiException if any exception occurs
     */
    public List<Snippet> getSnippets(boolean downloadContent) throws GitLabApiException {

        Response response = get(Response.Status.OK, getDefaultPerPageParam(), "snippets");
        List<Snippet> snippets = (response.readEntity(new GenericType<List<Snippet>>(){}));

        if (downloadContent) {
            for (Snippet snippet : snippets) {
                snippet.setContent(getSnippetContent(snippet.getId()));
            }
        }

        return snippets;
    }

    /**
     * Get a list of the authenticated user's snippets.
     *
     * <pre><code>GitLab Endpoint: GET /snippets</code></pre>
     *
     * @return a list of authenticated user's snippets
     * @throws GitLabApiException if any exception occurs
     */
    public List<Snippet> getSnippets() throws GitLabApiException {
        return (getSnippets(getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of the authenticated user's snippets.
     *
     * <pre><code>GitLab Endpoint: GET /snippets</code></pre>
     *
     * @param itemsPerPage the number of snippets per page
     * @return the Pager of snippets
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Snippet> getSnippets(int itemsPerPage) throws GitLabApiException {
        return (new Pager<Snippet>(this, Snippet.class, itemsPerPage, null, "snippets"));
    }

    /**
     * Get a Stream of the authenticated user's snippets.
     *
     * <pre><code>GitLab Endpoint: GET /snippets</code></pre>
     *
     * @return a Stream of authenticated user's snippets
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Snippet> getSnippetsStream() throws GitLabApiException {
        return (getSnippets(getDefaultPerPage()).stream());
    }

    /**
     * Get the content of a Snippet.
     *
     * <pre><code>GitLab Endpoint: GET /snippets/:id/raw</code></pre>
     *
     * @param snippetId the snippet ID to remove
     * @return the content of snippet
     * @throws GitLabApiException if any exception occurs
     */
    public String getSnippetContent(Long snippetId) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "snippets", snippetId, "raw");
        return (response.readEntity(String.class));
    }

    /**
     * Get a specific Snippet.
     *
     * @param snippetId the snippet ID to get
     * @param downloadContent indicating whether to download the snippet content
     * @return the snippet with the given id
     * @throws GitLabApiException if any exception occurs
     */
    public Snippet getSnippet(Long snippetId, boolean downloadContent) throws GitLabApiException {

        if (snippetId == null) {
            throw new RuntimeException("snippetId can't be null");
        }

        Response response = get(Response.Status.OK, null, "snippets", snippetId);
        Snippet snippet = response.readEntity(Snippet.class);

        if (downloadContent) {
            snippet.setContent(getSnippetContent(snippet.getId()));
        }

        return snippet;
    }

    /**
     * Get a specific Snippet.
     *
     * @param snippetId the snippet ID to get
     * @return the snippet with the given id
     * @throws GitLabApiException if any exception occurs
     */
    public Snippet getSnippet(Long snippetId) throws GitLabApiException {
        return getSnippet(snippetId, false);
    }

    /**
     * Get a specific snippet as an Optional instance.
     *
     * <pre><code>GitLab Endpoint: GET /snippets/:snippet_id</code></pre>
     *
     * @param snippetId the ID of the snippet to get the Optional instance for
     * @return the specified Snippet as an Optional instance
     */
    public Optional<Snippet> getOptionalSnippet(Long snippetId) {
        return (getOptionalSnippet(snippetId, false));
    }

    /**
     * Get a specific snippet as an Optional instance.
     *
     * <pre><code>GitLab Endpoint: GET /snippets/:snippet_id</code></pre>
     *
     * @param snippetId the ID of the snippet to get the Optional instance for
     * @param downloadContent indicating whether to download the snippet content
     * @return the specified Snippet as an Optional instance
     */
    public Optional<Snippet> getOptionalSnippet(Long snippetId, boolean downloadContent) {
        try {
            return (Optional.ofNullable(getSnippet(snippetId, downloadContent)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Create a new Snippet.
     *
     * @param title the title of the snippet
     * @param fileName the file name of the snippet
     * @param content the content of the snippet
     * @return the created Snippet
     * @throws GitLabApiException if any exception occurs
     */
    public Snippet createSnippet(String title, String fileName, String content) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("title", title, true)
                .withParam("file_name", fileName, true)
                .withParam("content", content, true);
        Response response = post(Response.Status.CREATED, formData, "snippets");
        return (response.readEntity(Snippet.class));
    }

    /**
     * Create a new Snippet.
     *
     * @param title the title of the snippet
     * @param fileName the file name of the snippet
     * @param content the content of the snippet
     * @param visibility the visibility (Public, Internal, Private) of the snippet
     * @param description the description of the snippet
     * @return the created Snippet
     * @throws GitLabApiException if any exception occurs
     */
    public Snippet createSnippet(String title, String fileName, String content, Visibility visibility, String description) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("title", title, true)
                .withParam("file_name", fileName, true)
                .withParam("content", content, true)
                .withParam("visibility", visibility)
                .withParam("description", description);
        Response response = post(Response.Status.CREATED, formData, "snippets");
        return (response.readEntity(Snippet.class));
    }

    /**
     * Removes Snippet.
     *
     * <pre><code>GitLab Endpoint: DELETE /snippets/:id</code></pre>
     *
     * @param snippetId the snippet ID to remove
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteSnippet(Long snippetId) throws GitLabApiException {

        if (snippetId == null) {
            throw new RuntimeException("snippetId can't be null");
        }

        delete(Response.Status.NO_CONTENT, null, "snippets", snippetId);
    }
}
