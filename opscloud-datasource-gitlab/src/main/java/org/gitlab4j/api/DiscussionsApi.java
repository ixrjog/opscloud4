package org.gitlab4j.api;

import org.gitlab4j.api.models.Discussion;
import org.gitlab4j.api.models.Note;
import org.gitlab4j.api.models.Position;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This class implements the client side API for the GitLab Discussions API.
 * See <a href="https://docs.gitlab.com/ee/api/discussions.html">Discussions API at GitLab</a> for more information.
 */
public class DiscussionsApi extends AbstractApi {

    public DiscussionsApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Get a list of all discussions for the specified issue.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of the issue
     * @return a list containing all the discussions for the specified issue
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Discussion> getIssueDiscussions(Object projectIdOrPath, Long issueIid) throws GitLabApiException {
        Pager<Discussion> pager = getIssueDiscussionsPager(projectIdOrPath, issueIid, getDefaultPerPage());
        return (pager.all());
    }

    /**
     * Get a list of discussions for the specified issue.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of the issue
     * @param maxItems the maximum number of Discussion instances to get, if &lt; 1 will fetch all Discussion instances for the issue
     * @return a list containing the discussions for the specified issue
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Discussion> getIssueDiscussions(Object projectIdOrPath, Long issueIid, int maxItems) throws GitLabApiException {
        if (maxItems < 1) {
            return (getIssueDiscussions(projectIdOrPath, issueIid));
        } else {
            Response response = get(Response.Status.OK, getPerPageQueryParam(maxItems),
                "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "discussions");
            return (response.readEntity(new GenericType<List<Discussion>>() {}));
        }
    }

    /**
     * Get a Pager of Discussion instances for the specified issue.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of the issue
     * @param itemsPerPage the number of Discussion instances that will be fetched per page
     * @return a Pager containing the Discussion instances for the specified issue
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Pager<Discussion> getIssueDiscussionsPager(Object projectIdOrPath, Long issueIid, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Discussion>(this, Discussion.class, itemsPerPage, null,
              "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "discussions"));
    }

    /**
     * Get a Stream of Discussion instances for the specified issue.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid the internal ID of the issue
     * @return a Stream instance containing the Discussion instances for the specified issue
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Stream<Discussion> getIssueDiscussionsStream(Object projectIdOrPath, Long issueIid) throws GitLabApiException {
        Pager<Discussion> pager = getIssueDiscussionsPager(projectIdOrPath, issueIid, getDefaultPerPage());
        return (pager.stream());
    }

    /**
     * Get a list of all discussions for the specified snippet.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/snippets/:snippet_id/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param snippetId the ID of the snippet
     * @return a list containing all the discussions for the specified snippet
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Discussion> getSnippetDiscussions(Object projectIdOrPath, Long snippetId) throws GitLabApiException {
        Pager<Discussion> pager = getSnippetDiscussionsPager(projectIdOrPath, snippetId, getDefaultPerPage());
        return (pager.all());
    }

    /**
     * Get a list of discussions for the specified snippet.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/snippets/:snippet_id/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param snippetId the ID of the snippet
     * @param maxItems the maximum number of Discussion instances to get, if &lt; 1 will fetch all Discussion instances for the snippet
     * @return a list containing the discussions for the specified snippet
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Discussion> getSnippetDiscussions(Object projectIdOrPath, Long snippetId, int maxItems) throws GitLabApiException {
        if (maxItems < 1) {
            return (getSnippetDiscussions(projectIdOrPath, snippetId));
        } else {
            Response response = get(Response.Status.OK, getPerPageQueryParam(maxItems),
                "projects", getProjectIdOrPath(projectIdOrPath), "snippets", snippetId, "discussions");
            return (response.readEntity(new GenericType<List<Discussion>>() {}));
        }
    }

    /**
     * Get a Pager of Discussion instances for the specified snippet.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/snippets/:snippet_id/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param snippetId the ID of the snippet
     * @param itemsPerPage the number of Discussion instances that will be fetched per page
     * @return a Pager containing the Discussion instances for the specified snippet
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Pager<Discussion> getSnippetDiscussionsPager(Object projectIdOrPath, Long snippetId, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Discussion>(this, Discussion.class, itemsPerPage, null,
              "projects", getProjectIdOrPath(projectIdOrPath), "snippets", snippetId, "discussions"));
    }

    /**
     * Get a Stream of Discussion instances for the specified snippet.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/snippets/:snippet_id/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param snippetId the ID of the snippet
     * @return a Stream instance containing the Discussion instances for the specified snippet
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Stream<Discussion> getSnippetDiscussionsStream(Object projectIdOrPath, Long snippetId) throws GitLabApiException {
        Pager<Discussion> pager = getSnippetDiscussionsPager(projectIdOrPath, snippetId, getDefaultPerPage());
        return (pager.stream());
    }


    /**
     * Get a list of all discussions for the specified epic.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/epics/:epic_id/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param epicId the internal ID of the epic
     * @return a list containing all the discussions for the specified epic
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Discussion> getEpicDiscussions(Object projectIdOrPath, Long epicId) throws GitLabApiException {
        Pager<Discussion> pager = getEpicDiscussionsPager(projectIdOrPath, epicId, getDefaultPerPage());
        return (pager.all());
    }

    /**
     * Get a list of discussions for the specified epic.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/epics/:epic_id/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param epicId the internal ID of the epic
     * @param maxItems the maximum number of Discussion instances to get, if &lt; 1 will fetch all Discussion instances for the epic
     * @return a list containing the discussions for the specified epic
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Discussion> getEpicDiscussions(Object projectIdOrPath, Long epicId, int maxItems) throws GitLabApiException {
        if (maxItems < 1) {
            return (getEpicDiscussions(projectIdOrPath, epicId));
        } else {
            Response response = get(Response.Status.OK, getPerPageQueryParam(maxItems),
                "projects", getProjectIdOrPath(projectIdOrPath), "epics", epicId, "discussions");
            return (response.readEntity(new GenericType<List<Discussion>>() {}));
        }
    }

    /**
     * Get a Pager of Discussion instances for the specified epic.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/epics/:epic_id/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param epicId the internal ID of the epic
     * @param itemsPerPage the number of Discussion instances that will be fetched per page
     * @return a Pager containing the Discussion instances for the specified epic
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Pager<Discussion> getEpicDiscussionsPager(Object projectIdOrPath, Long epicId, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Discussion>(this, Discussion.class, itemsPerPage, null,
              "projects", getProjectIdOrPath(projectIdOrPath), "epics", epicId, "discussions"));
    }

    /**
     * Get a Stream of Discussion instances for the specified epic.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/epics/:epic_id/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param epicId the internal ID of the epic
     * @return a Stream instance containing the Discussion instances for the specified epic
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Stream<Discussion> getEpicDiscussionsStream(Object projectIdOrPath, Long epicId) throws GitLabApiException {
        Pager<Discussion> pager = getEpicDiscussionsPager(projectIdOrPath, epicId, getDefaultPerPage());
        return (pager.stream());
    }

    /**
     * Get a list of all discussions for the specified merge request.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/merge_requests/:merge_request_iid/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param mergeRequestIid the internal ID of the merge request
     * @return a list containing all the discussions for the specified merge request
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Discussion> getMergeRequestDiscussions(Object projectIdOrPath, Long mergeRequestIid) throws GitLabApiException {
        Pager<Discussion> pager = getMergeRequestDiscussionsPager(projectIdOrPath, mergeRequestIid, getDefaultPerPage());
        return (pager.all());
    }

    /**
     * Get a list of discussions for the specified merge request.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/merge_requests/:merge_request_iid/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param mergeRequestIid the internal ID of the merge request
     * @param maxItems the maximum number of Discussion instances to get, if &lt; 1 will fetch all Discussion instances for the merge request
     * @return a list containing the discussions for the specified merge request
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Discussion> getMergeRequestDiscussions(Object projectIdOrPath, Long mergeRequestIid, int maxItems) throws GitLabApiException {
        if (maxItems < 1) {
            return (getMergeRequestDiscussions(projectIdOrPath, mergeRequestIid));
        } else {
            Response response = get(Response.Status.OK, getPerPageQueryParam(maxItems),
                "projects", getProjectIdOrPath(projectIdOrPath), "merge_requests", mergeRequestIid, "discussions");
            return (response.readEntity(new GenericType<List<Discussion>>() {}));
        }
    }

    /**
     * Get a Pager of Discussion instances for the specified merge request.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/merge_requests/:merge_request_iid/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param mergeRequestIid the internal ID of the merge request
     * @param itemsPerPage the number of Discussion instances that will be fetched per page
     * @return a Pager containing the Discussion instances for the specified merge request
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Pager<Discussion> getMergeRequestDiscussionsPager(Object projectIdOrPath, Long mergeRequestIid, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Discussion>(this, Discussion.class, itemsPerPage, null,
              "projects", getProjectIdOrPath(projectIdOrPath), "merge_requests", mergeRequestIid, "discussions"));
    }

    /**
     * Get a Stream of Discussion instances for the specified merge request.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/issues/:issue_iid/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param mergeRequestIid the internal ID of the merge request
     * @return a Stream instance containing the Discussion instances for the specified issue
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Stream<Discussion> getMergeRequestDiscussionsStream(Object projectIdOrPath, Long mergeRequestIid) throws GitLabApiException {
        Pager<Discussion> pager = getMergeRequestDiscussionsPager(projectIdOrPath, mergeRequestIid, getDefaultPerPage());
        return (pager.stream());
    }

    /**
     * Creates a new discussion to a single project merge request. This is similar to creating
     * a note but other comments (replies) can be added to it later.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/merge_requests/:merge_request_iid/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param mergeRequestIid mergeRequestIid the internal ID of the merge request
     * @param body the content of a discussion
     * @param createdAt date the discussion was created (requires admin or project/group owner rights)
     * @param positionHash position when creating a diff note
     * @param position a Position instance holding the position attributes
     * @return a Discussion instance containing the newly created discussion
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Discussion createMergeRequestDiscussion(Object projectIdOrPath, Long mergeRequestIid,
            String body, Date createdAt, String positionHash, Position position) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("body", body, true)
                .withParam("created_at", createdAt)
                .withParam("position", positionHash);

        if (position != null) {
            formData.withParam("position[base_sha]", position.getBaseSha(), true)
                .withParam("position[start_sha]", position.getStartSha(), true)
                .withParam("position[head_sha]", position.getHeadSha(), true)
                .withParam("position[position_type]", position.getPositionType(), true)
                .withParam("position[new_path]", position.getNewPath())
                .withParam("position[new_line]", position.getNewLine())
                .withParam("position[old_path]", position.getOldPath())
                .withParam("position[old_line]", position.getOldLine())
                .withParam("position[width]", position.getWidth())
                .withParam("position[height]", position.getHeight())
                .withParam("position[x]", position.getX())
                .withParam("position[y]", position.getY());
        }

        Response response = post(Response.Status.CREATED, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "merge_requests", mergeRequestIid, "discussions");
        return (response.readEntity(Discussion.class));
    }

    /**
     * Resolve or unresolve whole discussion of a merge request.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/merge_requests/:merge_request_iid/discussions/:discussion_id</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param mergeRequestIid mergeRequestIid the internal ID of the merge request
     * @param discussionId the ID of a discussion
     * @param resolved resolve/unresolve the discussion
     * @return the updated DIscussion instance
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Discussion resolveMergeRequestDiscussion(Object projectIdOrPath, Long mergeRequestIid,
            String discussionId, Boolean resolved)  throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("resolved", resolved, true);
        Response response = put(Response.Status.OK, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "merge_requests", mergeRequestIid, "discussions", discussionId);
        return (response.readEntity(Discussion.class));
    }

    /**
     * Deletes an existing discussion note of a merge request.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/merge_requests/:merge_request_iid/discussions/:discussion_id/notes/:note_id</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param mergeRequestIid mergeRequestIid the internal ID of the merge request
     * @param discussionId the ID of a discussion
     * @param noteId the note ID to delete
     * @throws GitLabApiException if any exception occurs during execution
     */
    public void deleteMergeRequestDiscussionNote(Object projectIdOrPath, Long mergeRequestIid,
            String discussionId, Long noteId)  throws GitLabApiException {
        delete(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath),
                "merge_requests", mergeRequestIid, "discussions", discussionId, "notes", noteId);
    }

    /**
     * Get a list of all discussions for the specified commit.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/commits/:commit_sha/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param commitSha the SHA of the commit to get discussions for
     * @return a list containing all the discussions for the specified commit
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Discussion> getCommitDiscussions(Object projectIdOrPath, String commitSha) throws GitLabApiException {
        Pager<Discussion> pager = getCommitDiscussionsPager(projectIdOrPath, commitSha, getDefaultPerPage());
        return (pager.all());
    }

    /**
     * Get a list of discussions for the specified commit.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/commits/:commit_sha/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param commitSha the SHA of the commit to get discussions for
     * @param maxItems the maximum number of Discussion instances to get, if &lt; 1 will fetch all Discussion instances for the commit
     * @return a list containing the discussions for the specified commit
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Discussion> getCommitDiscussions(Object projectIdOrPath, String commitSha, int maxItems) throws GitLabApiException {
        if (maxItems < 1) {
            return (getCommitDiscussions(projectIdOrPath, commitSha));
        } else {
            Response response = get(Response.Status.OK, getPerPageQueryParam(maxItems),
                "projects", getProjectIdOrPath(projectIdOrPath), "repository", "commits", commitSha, "discussions");
            return (response.readEntity(new GenericType<List<Discussion>>() {}));
        }
    }

    /**
     * Get a Pager of Discussion instances for the specified commit.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/commits/:commit_sha/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param commitSha the SHA of the commit to get discussions for
     * @param itemsPerPage the number of Discussion instances that will be fetched per page
     * @return a Pager containing the Discussion instances for the specified commit
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Pager<Discussion> getCommitDiscussionsPager(Object projectIdOrPath, String commitSha, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Discussion>(this, Discussion.class, itemsPerPage, null,
              "projects", getProjectIdOrPath(projectIdOrPath), "repository", "commits", commitSha, "discussions"));
    }

    /**
     * Get a Stream of Discussion instances for the specified commit.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/commits/:commit_sha/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param commitSha the SHA of the commit to get discussions for
     * @return a Stream instance containing the Discussion instances for the specified commit
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Stream<Discussion> getCommitDiscussionsStream(Object projectIdOrPath, String commitSha) throws GitLabApiException {
        Pager<Discussion> pager = getCommitDiscussionsPager(projectIdOrPath, commitSha, getDefaultPerPage());
        return (pager.stream());
    }

    /**
     * Get a single discussion for the specified commit.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/commits/:commit_sha/discussions/:discussion_id</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param commitSha the SHA of the commit to get discussions for
     * @param discussionId the ID of the discussion
     * @return the Discussion instance specified by discussionId for the specified commit
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Discussion getCommitDiscussion(Object projectIdOrPath, String commitSha, String discussionId) throws GitLabApiException {
        Response response = get(Response.Status.OK, null,
            "projects", getProjectIdOrPath(projectIdOrPath), "repository", "commits", commitSha, "discussions", discussionId);
        return (response.readEntity(Discussion.class));
    }

    /**
     * Get an Optional instance of a single discussion for the specified commit.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/commits/:commit_sha/discussions/:discussion_id</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param commitSha the SHA of the commit to get discussions for
     * @param discussionId the ID of the discussion
     * @return an Optional instance with the specified Discussion instance as a value
     */
    public Optional<Discussion> getOptionalCommitDiscussion(Object projectIdOrPath, String commitSha, String discussionId) {
        try {
            return (Optional.ofNullable(getCommitDiscussion(projectIdOrPath, commitSha, discussionId)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Creates a new discussion to a single project commit. This is similar to creating
     * a note but other comments (replies) can be added to it later.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/repository/commits/:commit_sha/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param commitSha the commit SHA to create the discussion for
     * @param body the content of a discussion
     * @param createdAt date the discussion was created (requires admin or project/group owner rights)
     * @param positionHash position when creating a diff note
     * @param position a Position instance holding the position attributes
     * @return a Discussion instance containing the newly created discussion
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Discussion createCommitDiscussion(Object projectIdOrPath, String commitSha,
            String body, Date createdAt, String positionHash, Position position) throws GitLabApiException {

        if (position == null) {
            throw new GitLabApiException("position instance can not be null");
        }

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("body", body, true)
                .withParam("created_at", createdAt)
                .withParam("position", positionHash)
                .withParam("position[base_sha]", position.getBaseSha(), true)
                .withParam("position[start_sha]", position.getStartSha(), true)
                .withParam("position[head_sha]", position.getHeadSha(), true)
                .withParam("position[position_type]", position.getPositionType(), true)
                .withParam("position[new_path]", position.getNewPath())
                .withParam("position[new_line]", position.getNewLine())
                .withParam("position[old_path]", position.getOldPath())
                .withParam("position[old_line]", position.getOldLine())
                .withParam("position[width]", position.getWidth())
                .withParam("position[height]", position.getHeight())
                .withParam("position[x]", position.getX())
                .withParam("position[y]", position.getY());

        Response response = post(Response.Status.CREATED, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "repository", "commits", commitSha, "discussions");
        return (response.readEntity(Discussion.class));
    }

    /**
     * Adds a note to an existing commit discussion.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/repository/commits/:commit_sha/discussions/:discussion_id/notes</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param commitSha the commit SHA to create the discussion for
     * @param discussionId the ID of a discussion
     * @param body the content of a discussion
     * @param createdAt date the discussion was created (requires admin or project/group owner rights)
     * @return a Note instance containing the newly created discussion note
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Note addCommitDiscussionNote(Object projectIdOrPath, String commitSha, String discussionId,
            String body, Date createdAt) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("body", body, true)
                .withParam("created_at", createdAt);

        Response response = post(Response.Status.CREATED, formData,
                "projects", getProjectIdOrPath(projectIdOrPath),
                "repository", "commits", commitSha, "discussions", discussionId, "notes");
        return (response.readEntity(Note.class));
    }

    /**
     * Modify an existing discussion note of a commit.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/repository/commits/:commit_sha/discussions/:discussion_id/notes</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param commitSha the commit SHA to delete the discussion from
     * @param discussionId the ID of a discussion
     * @param noteId the note ID to modify
     * @param body the content of a discussion
     * @return a Note instance containing the updated discussion note
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Note modifyCommitDiscussionNote(Object projectIdOrPath,
            String commitSha, String discussionId, Long noteId, String body) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm().withParam("body", body, true);
        Response response = this.putWithFormData(Response.Status.OK, formData,
                "projects", getProjectIdOrPath(projectIdOrPath),
                "repository", "commits", commitSha, "discussions", discussionId, "notes", noteId);
        return (response.readEntity(Note.class));
    }

    /**
     * Resolve or unresolve an existing discussion note of a commit.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/repository/commits/:commit_sha/discussions/:discussion_id/notes</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param commitSha the commit SHA to delete the discussion from
     * @param discussionId the ID of a discussion
     * @param noteId the note ID to resolve or unresolve
     * @param resolved if true will resolve the note, false will unresolve the note
     * @return a Note instance containing the updated discussion note
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Note resolveCommitDiscussionNote(Object projectIdOrPath,
            String commitSha, String discussionId, Long noteId, Boolean resolved) throws GitLabApiException {

        GitLabApiForm queryParams = new GitLabApiForm().withParam("resolved", resolved);
        Response response = this.put(Response.Status.OK, queryParams.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath),
                "repository", "commits", commitSha, "discussions", discussionId, "notes", noteId);
        return (response.readEntity(Note.class));
    }

    /**
     * Deletes an existing discussion note of a commit.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/repository/commits/:commit_sha/discussions/:discussion_id/notes/:note_id</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param commitSha the commit SHA to delete the discussion from
     * @param discussionId the ID of a discussion
     * @param noteId the note ID to delete
     * @throws GitLabApiException if any exception occurs during execution
     */
    public void deleteCommitDiscussionNote(Object projectIdOrPath, String commitSha,
            String discussionId, Long noteId)  throws GitLabApiException {
        delete(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath),
                "repository", "commits", commitSha, "discussions", discussionId, "notes", noteId);
    }

    /**
     * Adds a new note to the thread. This can also create a thread from a single comment.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/merge_requests/:merge_request_iid/discussions/:discussion_id/notes</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param mergeRequestIid mergeRequestIid the internal ID of the merge request
     * @param discussionId the ID of a discussion
     * @param body the content of a discussion
     * @param createdAt date the discussion was created (requires admin or project/group owner rights)
     * @return a Note instance containing the newly created discussion note
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Note addMergeRequestThreadNote(Object projectIdOrPath, Long mergeRequestIid,
	    String discussionId, String body, Date createdAt) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("body", body, true)
                .withParam("created_at", createdAt);
        Response response = post(Response.Status.CREATED, formData,
                "projects", getProjectIdOrPath(projectIdOrPath),
                "merge_requests", mergeRequestIid, "discussions", discussionId, "notes");
        return (response.readEntity(Note.class));
    }

    /**
     * Modify or resolve an existing thread note of a merge request.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/merge_requests/:merge_request_iid/discussions/:discussion_id/notes/:note_id</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param mergeRequestIid mergeRequestIid the internal ID of the merge request
     * @param discussionId the ID of a discussion
     * @param noteId the note ID to modify
     * @param body the content of a discussion
     * @param resolved if true will resolve the note, false will unresolve the note
     * @return a Note instance containing the updated discussion note
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Note modifyMergeRequestThreadNote(Object projectIdOrPath, Long mergeRequestIid,
	    String discussionId, Long noteId, String body, Boolean resolved) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm().withParam("body", body).withParam("resolved", resolved);
        Response response = this.putWithFormData(Response.Status.OK, formData,
               "projects", getProjectIdOrPath(projectIdOrPath),
               "merge_requests", mergeRequestIid, "discussions", discussionId, "notes", noteId);
        return (response.readEntity(Note.class));
    }

    /**
     * Deletes an existing thread note of a merge request.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/merge_requests/:merge_request_iid/discussions/:discussion_id/notes/:note_id</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param mergeRequestIid mergeRequestIid the internal ID of the merge request
     * @param discussionId the ID of a discussion
     * @param noteId the note ID to delete
     * @throws GitLabApiException if any exception occurs during execution
     */
    public void deleteMergeRequestThreadNote(Object projectIdOrPath, Long mergeRequestIid,
	    String discussionId, Long noteId)  throws GitLabApiException {
        delete(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath),
                "merge_requests", mergeRequestIid, "discussions", discussionId, "notes", noteId);
    }

    /**
     * Creates a new thread to a single project issue. This is similar to creating a note but other comments (replies) can be added to it later.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/issues/:issue_iid/discussions</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid The IID of an issue
     * @param body the content of the discussion
     * @param createdAt (optional) date the discussion was created (requires admin or project/group owner rights)
     * @return a Discussion instance containing the newly created discussion
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Discussion createIssueDiscussion(Object projectIdOrPath, Long issueIid, String body, Date createdAt) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("body", body, true)
                .withParam("created_at", createdAt);
        Response response = post(Response.Status.CREATED, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "discussions");
        return (response.readEntity(Discussion.class));
    }

    /**
     * Adds a new note to the thread.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/issues/:issue_iid/discussions/:discussion_id/notes</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid The IID of an issue
     * @param discussionId the id of discussion
     * @param body the content of the note
     * @param createdAt (optional) date the discussion was created (requires admin or project/group owner rights)
     * @return a Note instance containing the newly created note
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Note addIssueThreadNote(Object projectIdOrPath, Long issueIid,
                                   String discussionId, String body, Date createdAt) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("body", body, true)
                .withParam("created_at", createdAt);
        Response response = post(Response.Status.CREATED, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "discussions", discussionId, "notes");
        return (response.readEntity(Note.class));
    }

    /**
     * Modify existing thread note of an issue.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/issues/:issue_iid/discussions/:discussion_id/notes/:note_id</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid The IID of an issue
     * @param discussionId the id of discussion
     * @param noteId the id of the note
     * @param body the content of the note
     * @return a Note instance containing the modified note
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Note modifyIssueThreadNote(Object projectIdOrPath, Long issueIid,
                                      String discussionId, Long noteId, String body) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("body", body, true);
        Response response = putWithFormData(Response.Status.OK, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "discussions", discussionId, "notes", noteId);
        return (response.readEntity(Note.class));
    }

    /**
     * Deletes an existing thread note of an issue.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/issues/:issue_iid/discussions/:discussion_id/notes/:note_id</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param issueIid The IID of an issue
     * @param discussionId the id of discussion
     * @param noteId the id of the note
     * @throws GitLabApiException if any exception occurs during execution
     */
    public void deleteIssueThreadNote(Object projectIdOrPath, Long issueIid,
                                      String discussionId, Long noteId) throws GitLabApiException {
        delete(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "issues", issueIid, "discussions", discussionId, "notes", noteId);
    }

}
