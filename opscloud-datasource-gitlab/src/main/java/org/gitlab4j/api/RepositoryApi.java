package org.gitlab4j.api;

import org.gitlab4j.api.GitLabApi.ApiVersion;
import org.gitlab4j.api.models.*;
import org.gitlab4j.api.utils.FileUtils;

import jakarta.ws.rs.core.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * <p>This class provides an entry point to all the GitLab API repository calls.
 * For more information on the repository APIs see:</p>
 *
 * <a href="https://docs.gitlab.com/ce/api/repositories.html">Repositories API</a>
 * <a href="https://docs.gitlab.com/ce/api/branches.html">Branches API</a>
 */
public class RepositoryApi extends AbstractApi {

    public RepositoryApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Get a list of repository branches from a project, sorted by name alphabetically.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/branches</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return the list of repository branches for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public List<Branch> getBranches(Object projectIdOrPath) throws GitLabApiException {
        return getBranches(projectIdOrPath, null, getDefaultPerPage()).all();
    }

    /**
     * Get a list of repository branches from a project, sorted by name alphabetically.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/branches</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param page the page to get
     * @param perPage the number of Branch instances per page
     * @return the list of repository branches for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public List<Branch> getBranches(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "projects",
                getProjectIdOrPath(projectIdOrPath), "repository", "branches");
        return (response.readEntity(new GenericType<List<Branch>>() {}));
    }

    /**
     * Get a Pager of repository branches from a project, sorted by name alphabetically.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/branches</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return the list of repository branches for the specified project ID
     *
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Branch> getBranches(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return getBranches(projectIdOrPath, null, itemsPerPage);
    }

    /**
     * Get a Stream of repository branches from a project, sorted by name alphabetically.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/branches</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a Stream of repository branches for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Branch> getBranchesStream(Object projectIdOrPath) throws GitLabApiException {
        return getBranches(projectIdOrPath, null, getDefaultPerPage()).stream();
    }

    /**
     * Get a single project repository branch.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/branches/:branch</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param branchName the name of the branch to get
     * @return the branch info for the specified project ID/branch name pair
     * @throws GitLabApiException if any exception occurs
     */
    public Branch getBranch(Object projectIdOrPath, String branchName) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects",
                getProjectIdOrPath(projectIdOrPath), "repository", "branches", urlEncode(branchName));
        return (response.readEntity(Branch.class));
    }

    /**
     * Get a List of repository branches from a project, sorted by name alphabetically, filter by the search term.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/branches?search=:search</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param search the branch name search term
     * @return the List of repository branches for the specified project ID and search term
     * @throws GitLabApiException if any exception occurs
     */
    public List<Branch> getBranches(Object projectIdOrPath, String search) throws GitLabApiException {
        return (getBranches(projectIdOrPath, search, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of repository branches from a project, sorted by name alphabetically, filter by the search term.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/branches?search=:search</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param search the branch name search term
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return the list of repository branches for the specified project ID and search term
     *
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Branch> getBranches(Object projectIdOrPath, String search, int itemsPerPage) throws GitLabApiException {
        MultivaluedMap<String, String> queryParams = ( search == null ? null :
            new GitLabApiForm().withParam("search", urlEncode(search)).asMap() );

        return (new Pager<Branch>(this, Branch.class, itemsPerPage, queryParams, "projects",
                getProjectIdOrPath(projectIdOrPath), "repository", "branches"));
    }

    /**
     * Get a Stream of repository branches from a project, sorted by name alphabetically, filter by the search term.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/branches?search=:search</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param search the branch name search term
     * @return the Stream of repository branches for the specified project ID and search term
     *
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Branch> getBranchesStream(Object projectIdOrPath, String search) throws GitLabApiException {
        return (getBranches(projectIdOrPath, search, getDefaultPerPage()).stream());
    }

    /**
     * Get an Optional instance with the value for the specific repository branch.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/branches/:branch</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param branchName the name of the branch to get
     * @return an Optional instance with the info for the specified project ID/branch name pair as the value
     * @throws GitLabApiException if any exception occurs
     */
    public Optional<Branch> getOptionalBranch(Object projectIdOrPath, String branchName) throws GitLabApiException {
        try {
            return (Optional.ofNullable(getBranch(projectIdOrPath, branchName)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Creates a branch for the project. Support as of version 6.8.x
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/repository/branches</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param branchName the name of the branch to create
     * @param ref Source to create the branch from, can be an existing branch, tag or commit SHA
     * @return the branch info for the created branch
     * @throws GitLabApiException if any exception occurs
     */
    public Branch createBranch(Object projectIdOrPath, String branchName, String ref) throws GitLabApiException {

        Form formData = new GitLabApiForm()
                .withParam(isApiVersion(ApiVersion.V3) ? "branch_name" : "branch", branchName, true)
                .withParam("ref", ref, true);
        Response response = post(Response.Status.CREATED, formData.asMap(), "projects",
                getProjectIdOrPath(projectIdOrPath), "repository", "branches");
        return (response.readEntity(Branch.class));
    }

    /**
     * Delete a single project repository branch.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/repository/branches/:branch</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param branchName the name of the branch to delete
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteBranch(Object projectIdOrPath, String branchName) throws GitLabApiException {
        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.NO_CONTENT);
        delete(expectedStatus, null, "projects",
                getProjectIdOrPath(projectIdOrPath), "repository", "branches", urlEncode(branchName));
    }

    /**
     * Protects a single project repository branch. This is an idempotent function,
     * protecting an already protected repository branch will not produce an error.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/repository/branches/:branch/protect</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param branchName the name of the branch to protect
     * @return the branch info for the protected branch
     * @throws GitLabApiException if any exception occurs
     */
    public Branch protectBranch(Object projectIdOrPath, String branchName) throws GitLabApiException {
        Response response = put(Response.Status.OK, null, "projects",
                getProjectIdOrPath(projectIdOrPath), "repository", "branches", urlEncode(branchName), "protect");
        return (response.readEntity(Branch.class));
    }

    /**
     * Unprotects a single project repository branch. This is an idempotent function, unprotecting an
     * already unprotected repository branch will not produce an error.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/repository/branches/:branch/unprotect</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param branchName the name of the branch to un-protect
     * @return the branch info for the unprotected branch
     * @throws GitLabApiException if any exception occurs
     */
    public Branch unprotectBranch(Object projectIdOrPath, String branchName) throws GitLabApiException {
        Response response = put(Response.Status.OK, null, "projects",
                getProjectIdOrPath(projectIdOrPath), "repository", "branches", urlEncode(branchName), "unprotect");
        return (response.readEntity(Branch.class));
    }

    /**
     * Get a list of repository files and directories in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/tree</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a tree with the root directories and files of a project
     * @throws GitLabApiException if any exception occurs
     */
    public List<TreeItem> getTree(Object projectIdOrPath) throws GitLabApiException {
        return (getTree(projectIdOrPath, "/", "master"));
    }

    /**
     * Get a Pager of repository files and directories in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/tree</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager containing a tree with the root directories and files of a project
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<TreeItem> getTree(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (getTree(projectIdOrPath, "/", "master", false, itemsPerPage));
    }

    /**
     * Get a Stream of repository files and directories in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/tree</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a Stream containing a tree with the root directories and files of a project
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<TreeItem> getTreeStream(Object projectIdOrPath) throws GitLabApiException {
        return (getTreeStream(projectIdOrPath, "/", "master"));
    }

    /**
     * Get a list of repository files and directories in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/tree</code></pre>
     *
     * id (required) - The ID of a project
     * path (optional) - The path inside repository. Used to get content of subdirectories
     * ref_name (optional) - The name of a repository branch or tag or if not given the default branch
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filePath the path inside repository, used to get content of subdirectories
     * @param refName the name of a repository branch or tag or if not given the default branch
     * @return a tree with the directories and files of a project
     * @throws GitLabApiException if any exception occurs
     */
    public List<TreeItem> getTree(Object projectIdOrPath, String filePath, String refName) throws GitLabApiException {
        return (getTree(projectIdOrPath, filePath, refName, false));
    }

    /**
     * Get a Pager of repository files and directories in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/tree</code></pre>
     *
     * id (required) - The ID of a project
     * path (optional) - The path inside repository. Used to get content of subdirectories
     * ref_name (optional) - The name of a repository branch or tag or if not given the default branch
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filePath the path inside repository, used to get content of subdirectories
     * @param refName the name of a repository branch or tag or if not given the default branch
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager containing a tree with the directories and files of a project
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<TreeItem> getTree(Object projectIdOrPath, String filePath, String refName, int itemsPerPage) throws GitLabApiException {
        return (getTree(projectIdOrPath, filePath, refName, false, itemsPerPage));
    }

    /**
     * Get a Stream of repository files and directories in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/tree</code></pre>
     *
     * id (required) - The ID of a project
     * path (optional) - The path inside repository. Used to get content of subdirectories
     * ref_name (optional) - The name of a repository branch or tag or if not given the default branch
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filePath the path inside repository, used to get content of subdirectories
     * @param refName the name of a repository branch or tag or if not given the default branch
     * @return a Stream containing a tree with the directories and files of a project
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<TreeItem> getTreeStream(Object projectIdOrPath, String filePath, String refName) throws GitLabApiException {
        return (getTreeStream(projectIdOrPath, filePath, refName, false));
    }

    /**
     * Get a list of repository files and directories in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/tree</code></pre>
     *
     * id (required) - The ID of a project
     * path (optional) - The path inside repository. Used to get contend of subdirectories
     * ref_name (optional) - The name of a repository branch or tag or if not given the default branch
     * recursive (optional) - Boolean value used to get a recursive tree (false by default)
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filePath the path inside repository, used to get content of subdirectories
     * @param refName the name of a repository branch or tag or if not given the default branch
     * @param recursive flag to get a recursive tree or not
     * @return a tree with the directories and files of a project
     * @throws GitLabApiException if any exception occurs
     */
    public List<TreeItem> getTree(Object projectIdOrPath, String filePath, String refName, Boolean recursive) throws GitLabApiException {
        return (getTree(projectIdOrPath, filePath, refName, recursive, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of repository files and directories in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/tree</code></pre>
     *
     * id (required) - The ID of a project
     * path (optional) - The path inside repository. Used to get contend of subdirectories
     * ref_name (optional) - The name of a repository branch or tag or if not given the default branch
     * recursive (optional) - Boolean value used to get a recursive tree (false by default)
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filePath the path inside repository, used to get content of subdirectories
     * @param refName the name of a repository branch or tag or if not given the default branch
     * @param recursive flag to get a recursive tree or not
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a tree with the directories and files of a project
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<TreeItem> getTree(Object projectIdOrPath, String filePath, String refName, Boolean recursive, int itemsPerPage) throws GitLabApiException {
        Form formData = new GitLabApiForm()
                .withParam("id", getProjectIdOrPath(projectIdOrPath), true)
                .withParam("path", filePath, false)
                .withParam(isApiVersion(ApiVersion.V3) ? "ref_name" : "ref", (refName != null ? urlEncode(refName) : null), false)
                .withParam("recursive", recursive, false);
        return (new Pager<TreeItem>(this, TreeItem.class, itemsPerPage, formData.asMap(), "projects",
                getProjectIdOrPath(projectIdOrPath), "repository", "tree"));
    }

    /**
     * Get a Stream of repository files and directories in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/tree</code></pre>
     *
     * id (required) - The ID of a project
     * path (optional) - The path inside repository. Used to get contend of subdirectories
     * ref_name (optional) - The name of a repository branch or tag or if not given the default branch
     * recursive (optional) - Boolean value used to get a recursive tree (false by default)
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filePath the path inside repository, used to get content of subdirectories
     * @param refName the name of a repository branch or tag or if not given the default branch
     * @param recursive flag to get a recursive tree or not
     * @return a Stream containing a tree with the directories and files of a project
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<TreeItem> getTreeStream(Object projectIdOrPath, String filePath, String refName, Boolean recursive) throws GitLabApiException {
        return (getTree(projectIdOrPath, filePath, refName, recursive, getDefaultPerPage()).stream());
    }

    /**
     * Get the raw file contents for a blob by blob SHA.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/raw_blobs/:sha</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param sha the SHA of the file to get the contents for
     * @return the raw file contents for the blob on an InputStream
     * @throws GitLabApiException if any exception occurs
     */
    public InputStream getRawBlobContent(Object projectIdOrPath, String sha) throws GitLabApiException {
        Response response = getWithAccepts(Response.Status.OK, null, MediaType.MEDIA_TYPE_WILDCARD,
                "projects", getProjectIdOrPath(projectIdOrPath), "repository", "blobs", sha, "raw");
        return (response.readEntity(InputStream.class));
    }

    /**
     * Get an archive of the complete repository by SHA (optional).
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/archive</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param sha the SHA of the archive to get
     * @return an input stream that can be used to save as a file
     * or to read the content of the archive
     * @throws GitLabApiException if any exception occurs
     */
    public InputStream getRepositoryArchive(Object projectIdOrPath, String sha) throws GitLabApiException {
        Form formData = new GitLabApiForm().withParam("sha", sha);
        Response response = getWithAccepts(Response.Status.OK, formData.asMap(), MediaType.WILDCARD,
                "projects", getProjectIdOrPath(projectIdOrPath), "repository", "archive");
        return (response.readEntity(InputStream.class));
    }

    /**
     * Get an archive of the complete repository by SHA (optional).
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/archive</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param sha the SHA of the archive to get
     * @param format The archive format, defaults to "tar.gz" if null
     * @return an input stream that can be used to save as a file or to read the content of the archive
     * @throws GitLabApiException if format is not a valid archive format or any exception occurs
     */
    public InputStream getRepositoryArchive(Object projectIdOrPath, String sha, String format) throws GitLabApiException {
        ArchiveFormat archiveFormat = ArchiveFormat.forValue(format);
        return (getRepositoryArchive(projectIdOrPath, sha, archiveFormat));
    }

    /**
     * Get an archive of the complete repository by SHA (optional).
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/archive</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param sha the SHA of the archive to get
     * @param format The archive format, defaults to TAR_GZ if null
     * @return an input stream that can be used to save as a file or to read the content of the archive
     * @throws GitLabApiException if any exception occurs
     */
    public InputStream getRepositoryArchive(Object projectIdOrPath, String sha, ArchiveFormat format) throws GitLabApiException {

        if (format == null) {
            format = ArchiveFormat.TAR_GZ;
        }

        /*
         * Gitlab-ce has a bug when you try to download file archives with format by using "&format=zip(or tar... etc.)",
         * there is a solution to request .../archive.:format instead of .../archive?format=:format.
         *
         * Issue:  https://gitlab.com/gitlab-org/gitlab-ce/issues/45992
         *         https://gitlab.com/gitlab-com/support-forum/issues/3067
         */
        Form formData = new GitLabApiForm().withParam("sha", sha);
        Response response = getWithAccepts(Response.Status.OK, formData.asMap(), MediaType.WILDCARD,
                "projects", getProjectIdOrPath(projectIdOrPath), "repository", "archive" + "." + format.toString());
        return (response.readEntity(InputStream.class));
    }

    /**
     * Get an archive of the complete repository by SHA (optional) and saves to the specified directory.
     * If the archive already exists in the directory it will be overwritten.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/archive</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param sha the SHA of the archive to get
     * @param directory the File instance of the directory to save the archive to, if null will use "java.io.tmpdir"
     * @return a File instance pointing to the downloaded instance
     * @throws GitLabApiException if any exception occurs
     */
    public File getRepositoryArchive(Object projectIdOrPath, String sha, File directory) throws GitLabApiException {

        Form formData = new GitLabApiForm().withParam("sha", sha);
        Response response = getWithAccepts(Response.Status.OK, formData.asMap(), MediaType.WILDCARD,
                "projects", getProjectIdOrPath(projectIdOrPath), "repository", "archive");

        try {

            if (directory == null) {
                directory = new File(System.getProperty("java.io.tmpdir"));
            }

            String filename = FileUtils.getFilenameFromContentDisposition(response);
            File file = new File(directory, filename);

            InputStream in = response.readEntity(InputStream.class);
            Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return (file);

        } catch (IOException ioe) {
            throw new GitLabApiException(ioe);
        }
    }

    /**
     * Get an archive of the complete repository by SHA (optional) and saves to the specified directory.
     * If the archive already exists in the directory it will be overwritten.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/archive</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param sha the SHA of the archive to get
     * @param directory the File instance of the directory to save the archive to, if null will use "java.io.tmpdir"
     * @param format The archive format, defaults to "tar.gz" if null
     * @return a File instance pointing to the downloaded instance
     * @throws GitLabApiException if format is not a valid archive format or any exception occurs
     */
    public File getRepositoryArchive(Object projectIdOrPath, String sha, File directory, String format) throws GitLabApiException {
        ArchiveFormat archiveFormat = ArchiveFormat.forValue(format);
        return (getRepositoryArchive(projectIdOrPath, sha, directory, archiveFormat));
    }

    /**
     * Get an archive of the complete repository by SHA (optional) and saves to the specified directory.
     * If the archive already exists in the directory it will be overwritten.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/archive</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param sha the SHA of the archive to get
     * @param directory the File instance of the directory to save the archive to, if null will use "java.io.tmpdir"
     * @param format The archive format, defaults to TAR_GZ if null
     * @return a File instance pointing to the downloaded instance
     * @throws GitLabApiException if any exception occurs
     */
    public File getRepositoryArchive(Object projectIdOrPath, String sha, File directory, ArchiveFormat format) throws GitLabApiException {

        if (format == null) {
            format = ArchiveFormat.TAR_GZ;
        }

        /*
         * Gitlab-ce has a bug when you try to download file archives with format by using "&format=zip(or tar... etc.)",
         * there is a solution to request .../archive.:format instead of .../archive?format=:format.
         *
         * Issue:  https://gitlab.com/gitlab-org/gitlab-ce/issues/45992
         *         https://gitlab.com/gitlab-com/support-forum/issues/3067
         */
        Form formData = new GitLabApiForm().withParam("sha", sha);
        Response response = getWithAccepts(Response.Status.OK, formData.asMap(), MediaType.WILDCARD,
                "projects", getProjectIdOrPath(projectIdOrPath), "repository", "archive" + "." + format.toString());

        try {

            if (directory == null) {
                directory = new File(System.getProperty("java.io.tmpdir"));
            }

            String filename = FileUtils.getFilenameFromContentDisposition(response);
            File file = new File(directory, filename);

            InputStream in = response.readEntity(InputStream.class);
            Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return (file);

        } catch (IOException ioe) {
            throw new GitLabApiException(ioe);
        }
    }

    /**
     * Compare branches, tags or commits. This can be accessed without authentication
     * if the repository is publicly accessible.
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param from the commit SHA or branch name
     * @param to the commit SHA or branch name
     * @param straight specifies the comparison method, true for direct comparison between from and to (from..to),
     *          false to compare using merge base (from…to)’.
     * @return a CompareResults containing the results of the comparison
     * @throws GitLabApiException if any exception occurs
     */
    public CompareResults compare(Object projectIdOrPath, String from, String to, boolean straight) throws GitLabApiException {
        Form formData = new GitLabApiForm()
                .withParam("from", from, true)
                .withParam("to", to, true)
                .withParam("straight", straight);
        Response response = get(Response.Status.OK, formData.asMap(), "projects",
                getProjectIdOrPath(projectIdOrPath), "repository", "compare");
        return (response.readEntity(CompareResults.class));
    }

    /**
     * Compare branches, tags or commits. This can be accessed without authentication
     * if the repository is publicly accessible.
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param from the commit SHA or branch name
     * @param to the commit SHA or branch name
     * @return a CompareResults containing the results of the comparison
     * @throws GitLabApiException if any exception occurs
     */
    public CompareResults compare(Object projectIdOrPath, String from, String to) throws GitLabApiException {
        return (compare(projectIdOrPath, from, to, false));
    }

    /**
     * Get a list of contributors from a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/contributors</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a List containing the contributors for the specified project ID
     * @throws GitLabApiException if any exception occurs
     */
    public List<Contributor> getContributors(Object projectIdOrPath) throws GitLabApiException {
        return (getContributors(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a list of contributors from a project and in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/contributors</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param page the page to get
     * @param perPage the number of projects per page
     * @return a List containing the contributors for the specified project ID
     * @throws GitLabApiException if any exception occurs
     */
    public List<Contributor> getContributors(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage),
                "projects", getProjectIdOrPath(projectIdOrPath), "repository", "contributors");
        return (response.readEntity(new GenericType<List<Contributor>>() { }));
    }

    /**
     * Get a list of contributors from a project and in the specified page range, sorted by specified param.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/contributors</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param page the page to get
     * @param perPage the number of projects per page
     * @param orderBy (optional param) returns contributors ordered by NAME, EMAIL, or COMMITS. Default is COMMITS
     * @param sortOrder (optional param) returns contributors sorted in ASC or DESC order. Default is ASC
     * @return a List containing the contributors for the specified project ID
     * @throws GitLabApiException if any exception occurs
     */
    public List<Contributor> getContributors(Object projectIdOrPath, int page, int perPage, ContributorOrderBy orderBy, SortOrder sortOrder) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam(PAGE_PARAM, page).withParam(PER_PAGE_PARAM, perPage);
        if (sortOrder != null) {
            formData.withParam("sort", sortOrder, false);
        }

        if (orderBy != null) {
            formData.withParam("order_by", orderBy, false);
        }

        Response response = get(Response.Status.OK, formData.asMap(),
            "projects", getProjectIdOrPath(projectIdOrPath), "repository", "contributors");
        return (response.readEntity(new GenericType<List<Contributor>>() { }));
    }

    /**
     * Get a Pager of contributors from a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/contributors</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager containing the contributors for the specified project ID
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Contributor> getContributors(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return new Pager<Contributor>(this, Contributor.class, itemsPerPage, null, "projects",
                getProjectIdOrPath(projectIdOrPath), "repository", "contributors");
    }

    /**
     * Get a list of contributors from a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/contributors</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a List containing the contributors for the specified project ID
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Contributor> getContributorsStream(Object projectIdOrPath) throws GitLabApiException {
        return (getContributors(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get the common ancestor for 2 or more refs (commit SHAs, branch names or tags).
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/merge_base</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param refs a List of 2 or more refs (commit SHAs, branch names or tags)
     * @return the Commit instance containing the common ancestor
     * @throws GitLabApiException if any exception occurs
     */
    public Commit getMergeBase(Object projectIdOrPath, List<String> refs) throws GitLabApiException {

	if (refs == null || refs.size() < 2) {
	    throw new RuntimeException("refs must conatin at least 2 refs");
	}

	List<String> encodedRefs = new ArrayList<>(refs.size());
	for (String ref : refs) {
	    encodedRefs.add(urlEncode(ref));
	}

        GitLabApiForm queryParams = new GitLabApiForm().withParam("refs", encodedRefs, true);
        Response response = get(Response.Status.OK, queryParams.asMap(), "projects",
                getProjectIdOrPath(projectIdOrPath), "repository", "merge_base");
        return (response.readEntity(Commit.class));
    }

    /**
     * Get an Optional instance with the value of the common ancestor
     * for 2 or more refs (commit SHAs, branch names or tags).
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/repository/merge_base</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param refs a List of 2 or more refs (commit SHAs, branch names or tags)
     * @return an Optional instance with the Commit instance containing the common ancestor as the value
     * @throws GitLabApiException if any exception occurs
     */
    public Optional<Commit> getOptionalMergeBase(Object projectIdOrPath, List<String> refs) throws GitLabApiException {
        try {
            return (Optional.ofNullable(getMergeBase(projectIdOrPath, refs)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * <p>Delete all branches that are merged into the project’s default branch.</p>
     * NOTE: Protected branches will not be deleted as part of this operation.
     *
     * <pre><code>GitLab Endpoint: /projects/:id/repository/merged_branches</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteMergedBranches(Object projectIdOrPath) throws GitLabApiException {
        delete(Response.Status.NO_CONTENT, null, "projects",
                getProjectIdOrPath(projectIdOrPath), "repository", "merged_branches");
    }

    /**
     * Generate changelog data based on commits in a repository.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/repository/changelog</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param version         the version to generate the changelog for
     * @throws GitLabApiException if any exception occurs
     */
    public void generateChangelog(Object projectIdOrPath, String version) throws GitLabApiException {
        generateChangelog(projectIdOrPath, new ChangelogPayload(version));
    }

    /**
     * Generate changelog data based on commits in a repository.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/repository/changelog</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param payload         the payload to generate the changelog for
     * @throws GitLabApiException if any exception occurs
     */
    public void generateChangelog(Object projectIdOrPath, ChangelogPayload payload) throws GitLabApiException {
        post(Response.Status.OK, payload.getFormData(), "projects",
                getProjectIdOrPath(projectIdOrPath), "repository", "changelog");
    }

}
