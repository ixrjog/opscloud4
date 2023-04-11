package org.gitlab4j.api;

import org.gitlab4j.api.models.ExportStatus;
import org.gitlab4j.api.models.ImportStatus;
import org.gitlab4j.api.models.Project;

import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Map;

/**
 * This class provides an entry point to all the GitLab API project import/export calls.
 * @see <a href="https://docs.gitlab.com/ee/api/project_import_export.html">Project import/export API at GitLab</a>
 */
public class ImportExportApi extends AbstractApi {

    public ImportExportApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Schedule an export.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/export</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @throws GitLabApiException if any exception occurs
     */
    public void scheduleExport(Object projectIdOrPath) throws GitLabApiException {
        scheduleExport(projectIdOrPath, null, null, null, null);
    }

    /**
     * Schedule an export.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/export</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param description overrides the project description, optional
     * @throws GitLabApiException if any exception occurs
     */
    public void scheduleExport(Object projectIdOrPath, String description) throws GitLabApiException {
        scheduleExport(projectIdOrPath, description, null, null, null);
    }

    /**
     * Schedule an export.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/export</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param description overrides the project description, optional
     * @param upload Mao that contains the information to upload the exported project to a web server
     * @param uploadUrl the URL to upload the project
     * @param uploadHttpMethod the HTTP method to upload the exported project.
     *                          Only PUT and POST methods allowed. Default is PUT
     * @throws GitLabApiException if any exception occurs
     */
    public void scheduleExport(Object projectIdOrPath, String description,
            Map<String, String> upload, String uploadUrl, String uploadHttpMethod) throws GitLabApiException {

        Form formData = new GitLabApiForm()
                .withParam("description", description)
                .withParam("upload", upload)
                .withParam("upload[url]", uploadUrl)
                .withParam("upload[http_method]", uploadHttpMethod);
        post(Response.Status.ACCEPTED, formData, "projects", getProjectIdOrPath(projectIdOrPath), "export");
    }

    /**
     * Get the status of export.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/export</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return an ExportStatus instance holding information on the export status
     * @throws GitLabApiException if any exception occurs
     */
    public ExportStatus getExportStatus(Object projectIdOrPath) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "export");
        return (response.readEntity(ExportStatus.class));
    }

    /**
     * Download the finished export.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/export/download</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param directory the File instance of the directory to save the export file to, if null will use "java.io.tmpdir"
     * @return a File instance pointing to the download of the project export file
     * @throws GitLabApiException if any exception occurs
     */
    public File downloadExport(Object projectIdOrPath, File directory) throws GitLabApiException {
        return downloadExport(projectIdOrPath, directory, null);
    }

    /**
     * Download the finished export.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/export/download</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param directory the File instance of the directory to save the export file to, if null will use "java.io.tmpdir"
     * @param filename Name to give to the downloaded file. If null then we try to get from Content-Disposition header
     *                 or to compute one from parameters
     * @return a File instance pointing to the download of the project export file
     * @throws GitLabApiException if any exception occurs
     */
    public File downloadExport(Object projectIdOrPath, File directory, String filename) throws GitLabApiException {

        Response response = getWithAccepts(Response.Status.OK, null, MediaType.MEDIA_TYPE_WILDCARD,
                "projects", getProjectIdOrPath(projectIdOrPath), "export", "download");

        if (directory == null) {
            directory = new File(System.getProperty("java.io.tmpdir"));
        }

        if (filename == null) {

            // No filename provided
            String disposition = response.getHeaderString("Content-Disposition");
            if (disposition == null) {

                // On GitLab.com the Content-Disposition returned is null
                String name = null;
                if (projectIdOrPath instanceof Project) {
                    name = ((Project) projectIdOrPath).getPathWithNamespace().replace('/', '_');
                } else if(projectIdOrPath instanceof String) {
                    name = (String)projectIdOrPath;
                } else if(projectIdOrPath instanceof Integer) {
                    name = "projectid-" + projectIdOrPath;
                }

                // template = "YYYY-MM-DD_HH-MM-SS_{name}_export.tar.gz"
                final String template = "%1$tY-%1$tm-%1$td_%1$tH-%1$tM-%1$tS_%2$s_export.tar.gz";
                filename = String.format(template,  new Date(), name);

            } else {
                filename = disposition.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
            }
        }

        try {

            File file = new File(directory, filename);
            InputStream in = response.readEntity(InputStream.class);
            Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return (file);

        } catch (IOException ioe) {
            throw new GitLabApiException(ioe);
        }
    }

    /**
     * <p>Import an exported project.  The following properties on the Project instance
     * are utilized in the creation of the new project:</p>
     *<ul>
     * <li>defaultBranch (optional) - master by default</li>
     * <li>description (optional) - short project description</li>
     * <li>visibility (optional) - Limit by visibility public, internal, or private</li>
     * <li>visibilityLevel (optional)</li>
     * <li>issuesEnabled (optional) - Enable issues for this project</li>
     * <li>mergeMethod (optional) - Set the merge method used</li>
     * <li>mergeRequestsEnabled (optional) - Enable merge requests for this project</li>
     * <li>wikiEnabled (optional) - Enable wiki for this project</li>
     * <li>snippetsEnabled (optional) - Enable snippets for this project</li>
     * <li>jobsEnabled (optional) - Enable jobs for this project</li>
     * <li>containerRegistryEnabled (optional) - Enable container registry for this project</li>
     * <li>sharedRunnersEnabled (optional) - Enable shared runners for this project</li>
     * <li>publicJobs (optional) - If true, jobs can be viewed by non-project-members</li>
     * <li>onlyAllowMergeIfPipelineSucceeds (optional) - Set whether merge requests can only be merged with successful jobs</li>
     * <li>onlyAllowMergeIfAllDiscussionsAreResolved (optional) - Set whether merge requests can only be merged when all the discussions are resolved</li>
     * <li>lfsEnabled (optional) - Enable LFS</li>
     * <li>requestAccessEnabled (optional) - Allow users to request member access</li>
     * <li>repositoryStorage (optional) - Which storage shard the repository is on. Available only to admins</li>
     * <li>approvalsBeforeMerge (optional) - How many approvers should approve merge request by default</li>
     * <li>printingMergeRequestLinkEnabled (optional) - Show link to create/view merge request when pushing from the command line</li>
     * <li>resolveOutdatedDiffDiscussions (optional) - Automatically resolve merge request diffs discussions on lines changed with a push</li>
     * <li>initialize_with_readme (optional) - Initialize project with README file</li>
     * <li>packagesEnabled (optional) - Enable or disable mvn packages repository feature</li>
     *</ul>
     * <pre><code>GitLab Endpoint: POST /projects/import</code></pre>
     *
     * @param namespaceIdOrPath the ID or path of the namespace that the project will be imported to. Defaults to the current user’s namespace
     * @param exportFile the project export file to be imported
     * @param path the name and path for the new project
     * @param overwrite if there is a project with the same path the import will overwrite it. Defaults to false
     * @param overrideParams overriding project params, supports all fields defined by the ProjectApi, optional
     * @return an Importstatus instance with info for the project being imported to
     * @throws GitLabApiException if any exception occurs
     */
    public ImportStatus startImport(Object namespaceIdOrPath, File exportFile, String path, Boolean overwrite, Project overrideParams) throws GitLabApiException {

        URL url;
        try {
            url = getApiClient().getApiUrl("projects", "import");
        } catch (IOException ioe) {
            throw new GitLabApiException(ioe);
        }

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("path", path, true)
                .withParam("namespace", namespaceIdOrPath)
                .withParam("overwrite", overwrite);

        if (overrideParams != null) {
            formData.withParam("default_branch", overrideParams.getDefaultBranch())
                .withParam("description", overrideParams.getDescription())
                .withParam("issues_enabled", overrideParams.getIssuesEnabled())
                .withParam("merge_method",  overrideParams.getMergeMethod())
                .withParam("merge_requests_enabled", overrideParams.getMergeRequestsEnabled())
                .withParam("jobs_enabled", overrideParams.getJobsEnabled())
                .withParam("wiki_enabled", overrideParams.getWikiEnabled())
                .withParam("container_registry_enabled", overrideParams.getContainerRegistryEnabled())
                .withParam("snippets_enabled", overrideParams.getSnippetsEnabled())
                .withParam("shared_runners_enabled", overrideParams.getSharedRunnersEnabled())
                .withParam("public_jobs", overrideParams.getPublicJobs())
                .withParam("visibility_level", overrideParams.getVisibilityLevel())
                .withParam("only_allow_merge_if_pipeline_succeeds", overrideParams.getOnlyAllowMergeIfPipelineSucceeds())
                .withParam("only_allow_merge_if_all_discussions_are_resolved", overrideParams.getOnlyAllowMergeIfAllDiscussionsAreResolved())
                .withParam("lfs_enabled", overrideParams.getLfsEnabled())
                .withParam("request_access_enabled", overrideParams.getRequestAccessEnabled())
                .withParam("repository_storage", overrideParams.getRepositoryStorage())
                .withParam("approvals_before_merge", overrideParams.getApprovalsBeforeMerge())
                .withParam("printing_merge_request_link_enabled", overrideParams.getPrintingMergeRequestLinkEnabled())
                .withParam("resolve_outdated_diff_discussions", overrideParams.getResolveOutdatedDiffDiscussions())
                .withParam("initialize_with_readme", overrideParams.getInitializeWithReadme())
                .withParam("packages_enabled", overrideParams.getPackagesEnabled())
                .withParam("build_git_strategy", overrideParams.getBuildGitStrategy())
                .withParam("build_coverage_regex", overrideParams.getBuildCoverageRegex())
                .withParam("squash_option", overrideParams.getSquashOption());
        }

        Response response = upload(Response.Status.CREATED, "file", exportFile, null, formData, url);
        return (response.readEntity(ImportStatus.class));
    }

    /**
     * Get the status of an import.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/import</code></pre>
     *
     * @param projectIdOrPath the new (imported) project identifier in the form of an Long(ID), String(path), or Project instance 
     * @return an ImportStatus instance holding information on the import status
     * @throws GitLabApiException if any exception occurs
     */
    public ImportStatus getImportStatus(Object projectIdOrPath) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "import");
        return (response.readEntity(ImportStatus.class));
    }
}
