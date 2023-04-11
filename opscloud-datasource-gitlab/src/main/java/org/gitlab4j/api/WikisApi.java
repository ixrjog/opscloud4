/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Greg Messner <greg@messners.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.gitlab4j.api;

import org.gitlab4j.api.models.WikiAttachment;
import org.gitlab4j.api.models.WikiPage;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This class implements the client side API for the GitLab Wikis API.
 * See <a href="https://docs.gitlab.com/ce/api/wikis.html">Wikis API at GitLab</a> for more information.
 */
public class WikisApi extends AbstractApi {

    public WikisApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Get a list of pages in project wiki.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/wikis</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a list of pages in the project's wiki
     * @throws GitLabApiException if any exception occurs
     */
    public List<WikiPage> getPages(Object projectIdOrPath) throws GitLabApiException {
        return (getPages(projectIdOrPath, false, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of pages in project wiki.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/wikis</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of WikiPage instances that will be fetched per page
     * @return a Pager of pages in project's wiki for the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<WikiPage> getPages(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
	return (getPages(projectIdOrPath, false, itemsPerPage));
    }

    /**
     * Get a Stream of pages in project wiki.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/wikis</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a Pager of pages in project's wiki for the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<WikiPage> getPagesStream(Object projectIdOrPath) throws GitLabApiException {
	return (getPages(projectIdOrPath, false, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of pages in project wiki for the specified page.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/wikis</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param page the page to get
     * @param perPage the number of wiki-pages per page
     * @return a list of pages in project's wiki for the specified range
     * @throws GitLabApiException if any exception occurs
     * @deprecated Will be removed in a future release, use {@link #getPages(Object, boolean, int)}
     */
    public List<WikiPage> getPages(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage),
                "projects", getProjectIdOrPath(projectIdOrPath), "wikis");
        return response.readEntity(new GenericType<List<WikiPage>>() {});
    }

    /**
     * Get a List of pages in project wiki.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/wikis</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param withContent if true the results will include the pages content
     * @return a List of pages in project's wiki for the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public List<WikiPage> getPages(Object projectIdOrPath, boolean withContent) throws GitLabApiException {
	return (getPages(projectIdOrPath, withContent, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of pages in project wiki.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/wikis</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param withContent if true the results will include the pages content
     * @param itemsPerPage the number of WikiPage instances that will be fetched per page
     * @return a Pager of pages in project's wiki for the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<WikiPage> getPages(Object projectIdOrPath, boolean withContent, int itemsPerPage) throws GitLabApiException {
	GitLabApiForm formData = new GitLabApiForm().withParam("with_content", (withContent ? 1 : 0));
	return (new Pager<WikiPage>(this, WikiPage.class, itemsPerPage, formData.asMap(), 
            "projects", getProjectIdOrPath(projectIdOrPath), "wikis"));
    }

    /**
     * Get a Stream of pages in project wiki.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/wikis</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param withContent if true the results will include the pages content
     * @return a Stream of pages in project's wiki for the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<WikiPage> getPagesStream(Object projectIdOrPath, boolean withContent) throws GitLabApiException {
	return (getPages(projectIdOrPath, withContent, getDefaultPerPage()).stream());
    }

    /**
     * Get a single page of project wiki.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/wikis/:slug</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param slug the slug of the project's wiki page
     * @return the specified project Snippet
     * @throws GitLabApiException if any exception occurs
     */
    public WikiPage getPage(Object projectIdOrPath, String slug) throws GitLabApiException {
        Response response = get(Response.Status.OK, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "wikis", slug);
        return (response.readEntity(WikiPage.class));
    }

    /**
     * Get a single page of project wiki as an Optional instance.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/wikis/:slug</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param slug the slug of the project's wiki page
     * @return the specified project Snippet as an Optional instance
     */
    public Optional<WikiPage> getOptionalPage(Object projectIdOrPath, String slug) {
        try {
            return (Optional.ofNullable(getPage(projectIdOrPath, slug)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Creates a new project wiki page. The user must have permission to create new wiki page.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/wikis</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param title the title of a snippet, required
     * @param content the content of a wiki page, required
     * @return a WikiPage instance with info on the created page
     * @throws GitLabApiException if any exception occurs
     */
    public WikiPage createPage(Object projectIdOrPath, String title, String content) throws GitLabApiException {
        // one of title or content is required
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("title", title)
                .withParam("content", content);

        Response response = post(Response.Status.CREATED, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "wikis");
        return (response.readEntity(WikiPage.class));
    }

    /**
     * Updates an existing project wiki page. The user must have permission to change an existing wiki page.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/wikis/:slug</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param slug the slug of the project's wiki page, required
     * @param title the title of a snippet, optional
     * @param content the content of a page, optional. Either title or content must be supplied.
     * @return a WikiPage instance with info on the updated page
     * @throws GitLabApiException if any exception occurs
     */
    public WikiPage updatePage(Object projectIdOrPath, String slug, String title, String content) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("title", title)
                .withParam("slug", slug, true)
                .withParam("content", content);

        Response response = put(Response.Status.OK, formData.asMap(), "projects", getProjectIdOrPath(projectIdOrPath), "wikis", slug);
        return (response.readEntity(WikiPage.class));
    }

    /**
     * Deletes an existing project wiki page. This is an idempotent function and deleting a non-existent page does
     * not cause an error.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/wikis/:slug</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param slug the slug of the project's wiki page
     * @throws GitLabApiException if any exception occurs
     */
    public void deletePage(Object projectIdOrPath, String slug) throws GitLabApiException {
        delete(Response.Status.NO_CONTENT, null, "projects", getProjectIdOrPath(projectIdOrPath), "wikis", slug);
    }

    /**
     * Uploads a file to the attachment folder inside the wiki’s repository. The attachment folder is the uploads folder.
     *
     * <pre><code>POST /projects/:id/wikis/attachments</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param fileToUpload the File instance of the file to upload, required
     * @return a FileUpload instance with information on the just uploaded file
     * @throws GitLabApiException if any exception occurs
     */
    public WikiAttachment uploadAttachment(Object projectIdOrPath, File fileToUpload) throws GitLabApiException {
        return (uploadAttachment(projectIdOrPath, fileToUpload, null));
    }

    /**
     * Uploads a file to the attachment folder inside the wiki’s repository. The attachment folder is the uploads folder.
     *
     * <pre><code>POST /projects/:id/wikis/attachments</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param fileToUpload the File instance of the file to upload, required
     * @param branch the name of the branch, defaults to the wiki repository default branch, optional
     * @return a FileUpload instance with information on the just uploaded file
     * @throws GitLabApiException if any exception occurs
     */
    public WikiAttachment uploadAttachment(Object projectIdOrPath, File fileToUpload, String branch) throws GitLabApiException {

        URL url;
        try {
            url = getApiClient().getApiUrl("projects", getProjectIdOrPath(projectIdOrPath), "wikis", "attachments");
        } catch (IOException ioe) {
            throw new GitLabApiException(ioe);
        }

        GitLabApiForm formData = new GitLabApiForm().withParam("branch", branch);
        Response response = upload(Response.Status.CREATED, "file", fileToUpload, null, formData, url);
        return (response.readEntity(WikiAttachment.class));
    }
}
