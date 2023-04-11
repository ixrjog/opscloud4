package org.gitlab4j.api;

import org.gitlab4j.api.models.ArtifactsFile;
import org.gitlab4j.api.models.Job;
import org.gitlab4j.api.models.JobAttributes;

import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This class provides an entry point to all the GitLab API job calls.
 */
public class JobApi extends AbstractApi implements Constants {

    public JobApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Get a list of jobs in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @return a list containing the jobs for the specified project ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Job> getJobs(Object projectIdOrPath) throws GitLabApiException {
        return (getJobs(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a list of jobs in a project in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path to get the jobs for
     * @param page the page to get
     * @param perPage the number of Job instances per page
     * @return a list containing the jobs for the specified project ID in the specified page range
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Job> getJobs(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "projects", getProjectIdOrPath(projectIdOrPath), "jobs");
        return (response.readEntity(new GenericType<List<Job>>() {}));
    }

    /**
     * Get a Pager of jobs in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path to get the jobs for
     * @param itemsPerPage the number of Job instances that will be fetched per page
     * @return a Pager containing the jobs for the specified project ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Pager<Job> getJobs(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Job>(this, Job.class, itemsPerPage, null, "projects", getProjectIdOrPath(projectIdOrPath), "jobs"));
    }

    /**
     * Get a Stream of jobs in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @return a Stream containing the jobs for the specified project ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Stream<Job> getJobsStream(Object projectIdOrPath) throws GitLabApiException {
        return (getJobs(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of jobs in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path to get the jobs for
     * @param scope the scope of jobs, one of: CREATED, PENDING, RUNNING, FAILED, SUCCESS, CANCELED, SKIPPED, MANUAL
     * @return a list containing the jobs for the specified project ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Job> getJobs(Object projectIdOrPath, JobScope scope) throws GitLabApiException {
        return (getJobs(projectIdOrPath, scope, getDefaultPerPage()).all());
    }

    /**
     * Get a list of jobs in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path to get the jobs for
     * @param scope the scope of jobs, one of: CREATED, PENDING, RUNNING, FAILED, SUCCESS, CANCELED, SKIPPED, MANUAL
     * @param itemsPerPage the number of Job instances that will be fetched per page
     * @return a list containing the jobs for the specified project ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Pager<Job> getJobs(Object projectIdOrPath, JobScope scope, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("scope", scope);
        return (new Pager<Job>(this, Job.class, itemsPerPage, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "jobs"));
    }

    /**
     * Get a Stream of jobs in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path to get the jobs for
     * @param scope the scope of jobs, one of: CREATED, PENDING, RUNNING, FAILED, SUCCESS, CANCELED, SKIPPED, MANUAL
     * @return a Stream containing the jobs for the specified project ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Stream<Job> getJobsStream(Object projectIdOrPath, JobScope scope) throws GitLabApiException {
        return (getJobs(projectIdOrPath, scope, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of jobs in a pipeline.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/pipelines/:pipeline_id/jobs</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path to get the pipelines for
     * @param pipelineId the pipeline ID to get the list of jobs for
     * @return a list containing the jobs for the specified project ID and pipeline ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Job> getJobsForPipeline(Object projectIdOrPath, long pipelineId) throws GitLabApiException {
        Response response = get(Response.Status.OK, getDefaultPerPageParam(),
                "projects", getProjectIdOrPath(projectIdOrPath), "pipelines", pipelineId, "jobs");
        return (response.readEntity(new GenericType<List<Job>>() {}));
    }

    /**
     * Get a list of jobs in a pipeline.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/pipelines/:pipeline_id/jobs</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path to get the pipelines for
     * @param pipelineId the pipeline ID to get the list of jobs for
     * @param scope the scope of jobs, one of: CREATED, PENDING, RUNNING, FAILED, SUCCESS, CANCELED, SKIPPED, MANUAL
     * @return a list containing the jobs for the specified project ID and pipeline ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Job> getJobsForPipeline(Object projectIdOrPath, long pipelineId, JobScope scope) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("scope", scope).withParam(PER_PAGE_PARAM, getDefaultPerPage());
        Response response = get(Response.Status.OK, formData.asMap(), "projects", getProjectIdOrPath(projectIdOrPath), "pipelines", pipelineId, "jobs");
        return (response.readEntity(new GenericType<List<Job>>() {}));
    }

    /**
     * Get a Pager of jobs in a pipeline.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/pipelines/:pipeline_id/jobs</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path to get the pipelines for
     * @param pipelineId      the pipeline ID to get the list of jobs for
     * @param itemsPerPage    the number of Job instances that will be fetched per page
     * @return a list containing the jobs for the specified project ID and pipeline ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Pager<Job> getJobsForPipeline(Object projectIdOrPath, long pipelineId, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Job>(this, Job.class, itemsPerPage, getDefaultPerPageParam(),
                "projects", getProjectIdOrPath(projectIdOrPath), "pipelines", pipelineId, "jobs"));
    }

    /**
     * Get a Stream of jobs in a pipeline.
     * <pre><code>GitLab Endpoint: GET /projects/:id/pipelines/:pipeline_id/jobs</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param pipelineId      the pipeline ID to get the list of jobs for
     * @return a Stream containing the jobs for the specified project ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Stream<Job> getJobsStream(Object projectIdOrPath, long pipelineId) throws GitLabApiException {
        return (getJobsForPipeline(projectIdOrPath, pipelineId, getDefaultPerPage()).stream());
    }

    /**
     * Get single job in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs/:job_id</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path to get the job for
     * @param jobId the job ID to get
     * @return a single job for the specified project ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Job getJob(Object projectIdOrPath, Long jobId) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "jobs", jobId);
        return (response.readEntity(Job.class));
    }

    /**
     * Get single job in a project as an Optional instance.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs/:job_id</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path to get the job for
     * @param jobId the job ID to get
     * @return a single job for the specified project ID as an Optional intance
     */
    public Optional<Job> getOptionalJob(Object projectIdOrPath, Long jobId) {
        try {
            return (Optional.ofNullable(getJob(projectIdOrPath, jobId)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Download the artifacts file from the given reference name and job provided the job finished successfully.
     * The file will be saved to the specified directory. If the file already exists in the directory it will
     * be overwritten.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs/artifacts/:ref_name/download?job=name</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param ref the ref from a repository
     * @param jobName the name of the job to download the artifacts for
     * @param directory the File instance of the directory to save the file to, if null will use "java.io.tmpdir"
     * @return a File instance pointing to the download of the specified artifacts file
     * @throws GitLabApiException if any exception occurs
     */
    public File downloadArtifactsFile(Object projectIdOrPath, String ref, String jobName, File directory) throws GitLabApiException {

        Form formData = new GitLabApiForm().withParam("job", jobName, true);
        Response response = getWithAccepts(Response.Status.OK, formData.asMap(), MediaType.MEDIA_TYPE_WILDCARD,
                "projects", getProjectIdOrPath(projectIdOrPath), "jobs", "artifacts", urlEncode(ref), "download");

        try {

            if (directory == null) {
                directory = new File(System.getProperty("java.io.tmpdir"));
            }

            String filename = jobName + "-artifacts.zip";
            File file = new File(directory, filename);

            InputStream in = response.readEntity(InputStream.class);
            Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return (file);

        } catch (IOException ioe) {
            throw new GitLabApiException(ioe);
        }
    }

    /**
     * Get an InputStream pointing to the artifacts file from the given reference name and job
     * provided the job finished successfully. The file will be saved to the specified directory.
     * If the file already exists in the directory it will be overwritten.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs/artifacts/:ref_name/download?job=name</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param ref the ref from a repository
     * @param jobName the name of the job to download the artifacts for
     * @return an InputStream to read the specified artifacts file from
     * @throws GitLabApiException if any exception occurs
     */
    public InputStream downloadArtifactsFile(Object projectIdOrPath, String ref, String jobName) throws GitLabApiException {
        Form formData = new GitLabApiForm().withParam("job", jobName, true);
        Response response = getWithAccepts(Response.Status.OK, formData.asMap(), MediaType.MEDIA_TYPE_WILDCARD,
                "projects", getProjectIdOrPath(projectIdOrPath), "jobs", "artifacts", urlEncode(ref), "download");
        return (response.readEntity(InputStream.class));
    }

    /**
     * Download the job artifacts file for the specified job ID.  The artifacts file will be saved in the
     * specified directory with the following name pattern: job-{jobid}-artifacts.zip.  If the file already
     * exists in the directory it will be overwritten.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs/:job_id/artifacts</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param jobId the job ID to get the artifacts for
     * @param directory the File instance of the directory to save the file to, if null will use "java.io.tmpdir"
     * @return a File instance pointing to the download of the specified job artifacts file
     * @throws GitLabApiException if any exception occurs
     */
    public File downloadArtifactsFile(Object projectIdOrPath, Long jobId, File directory) throws GitLabApiException {

        Response response = getWithAccepts(Response.Status.OK, null, MediaType.MEDIA_TYPE_WILDCARD,
                "projects", getProjectIdOrPath(projectIdOrPath), "jobs", jobId, "artifacts");
        try {

            if (directory == null) {
                directory = new File(System.getProperty("java.io.tmpdir"));
            }

            String filename = "job-" + jobId + "-artifacts.zip";
            File file = new File(directory, filename);

            InputStream in = response.readEntity(InputStream.class);
            Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return (file);

        } catch (IOException ioe) {
            throw new GitLabApiException(ioe);
        }
    }

    /**
     * Get an InputStream pointing to the job artifacts file for the specified job ID.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs/:job_id/artifacts</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param jobId the job ID to get the artifacts for
     * @return an InputStream to read the specified job artifacts file
     * @throws GitLabApiException if any exception occurs
     */
    public InputStream downloadArtifactsFile(Object projectIdOrPath, Long jobId) throws GitLabApiException {
        Response response = getWithAccepts(Response.Status.OK, null, MediaType.MEDIA_TYPE_WILDCARD,
                "projects", getProjectIdOrPath(projectIdOrPath), "jobs", jobId, "artifacts");
        return (response.readEntity(InputStream.class));
    }

    /**
     * Download a single artifact file from within the job's artifacts archive.
     *
     * Only a single file is going to be extracted from the archive and streamed to a client.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs/:job_id/artifacts/*artifact_path</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param jobId the unique job identifier
     * @param artifactsFile an ArtifactsFile instance for the artifact to download
     * @param directory the File instance of the directory to save the file to, if null will use "java.io.tmpdir"
     * @return a File instance pointing to the download of the specified artifacts file
     * @throws GitLabApiException if any exception occurs
     */
    public File downloadArtifactsFile(Object projectIdOrPath, Long jobId, ArtifactsFile artifactsFile, File directory) throws GitLabApiException {

        Response response = get(Response.Status.OK, getDefaultPerPageParam(),
                "projects", getProjectIdOrPath(projectIdOrPath), "jobs", jobId, "artifacts", artifactsFile.getFilename());
        try {

            if (directory == null) {
                directory = new File(System.getProperty("java.io.tmpdir"));
            }

            String filename = artifactsFile.getFilename();
            File file = new File(directory, filename);

            InputStream in = response.readEntity(InputStream.class);
            Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return (file);

        } catch (IOException ioe) {
            throw new GitLabApiException(ioe);
        }
    }

    /**
     * Download a single artifact file from within the job's artifacts archive.
     *
     * Only a single file is going to be extracted from the archive and streamed to a client.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs/:job_id/artifacts/*artifact_path</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param jobId the unique job identifier
     * @param artifactsFile an ArtifactsFile instance for the artifact to download
     * @return an InputStream to read the specified artifacts file from
     * @throws GitLabApiException if any exception occurs
     */
    public InputStream downloadArtifactsFile(Object projectIdOrPath, Long jobId, ArtifactsFile artifactsFile) throws GitLabApiException {
        Response response = get(Response.Status.OK, getDefaultPerPageParam(),
                "projects", getProjectIdOrPath(projectIdOrPath), "jobs", jobId, "artifacts", artifactsFile.getFilename());
        return (response.readEntity(InputStream.class));
    }

    /**
     * Download a single artifact file from within the job's artifacts archive.
     *
     * Only a single file is going to be extracted from the archive and streamed to a client.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs/:job_id/artifacts/*artifact_path</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param jobId the unique job identifier
     * @param artifactPath the Path to a file inside the artifacts archive
     * @param directory the File instance of the directory to save the file to, if null will use "java.io.tmpdir"
     * @return a File instance pointing to the download of the specified artifacts file
     * @throws GitLabApiException if any exception occurs
     */
    public File downloadSingleArtifactsFile(Object projectIdOrPath, Long jobId, Path artifactPath, File directory) throws GitLabApiException {

        String path = artifactPath.toString().replace("\\", "/");
        Response response = get(Response.Status.OK, getDefaultPerPageParam(),
                "projects", getProjectIdOrPath(projectIdOrPath), "jobs", jobId, "artifacts", path);
        try {

            if (directory == null) {
                directory = new File(System.getProperty("java.io.tmpdir"));
            }

            String filename = artifactPath.getFileName().toString();
            File file = new File(directory, filename);

            InputStream in = response.readEntity(InputStream.class);
            Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return (file);

        } catch (IOException ioe) {
            throw new GitLabApiException(ioe);
        }
    }

    /**
     * Download a single artifact file from within the job's artifacts archive.
     *
     * Only a single file is going to be extracted from the archive and streamed to a client.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs/:job_id/artifacts/*artifact_path</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param jobId the unique job identifier
     * @param artifactPath the Path to a file inside the artifacts archive
     * @return an InputStream to read the specified artifacts file from
     * @throws GitLabApiException if any exception occurs
     */
    public InputStream downloadSingleArtifactsFile(Object projectIdOrPath, Long jobId, Path artifactPath) throws GitLabApiException {
        String path = artifactPath.toString().replace("\\", "/");
        Response response = get(Response.Status.OK, getDefaultPerPageParam(),
                "projects", getProjectIdOrPath(projectIdOrPath), "jobs", jobId, "artifacts", path);
        return (response.readEntity(InputStream.class));
    }

    /**
     * Get a trace of a specific job of a project
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/jobs/:id/trace</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     *                        to get the specified job's trace for
     * @param jobId the job ID to get the trace for
     * @return a String containing the specified job's trace
     * @throws GitLabApiException if any exception occurs during execution
     */
     public String getTrace(Object projectIdOrPath, Long jobId) throws GitLabApiException {
        Response response = get(Response.Status.OK, getDefaultPerPageParam(),
                "projects", getProjectIdOrPath(projectIdOrPath), "jobs", jobId, "trace");
        return (response.readEntity(String.class));
     }

    /**
     * Cancel specified job in a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/jobs/:job_id/cancel</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param jobId the ID to cancel job
     * @return job instance which just canceled
     * @throws GitLabApiException if any exception occurs during execution
     * @deprecated replaced by {@link #cancelJob(Object, Long)}
     */
     @Deprecated
    public Job cancleJob(Object projectIdOrPath, Long jobId) throws GitLabApiException {
	return (cancelJob(projectIdOrPath, jobId));
    }

    /**
     * Cancel specified job in a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/jobs/:job_id/cancel</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param jobId the ID to cancel job
     * @return job instance which just canceled
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Job cancelJob(Object projectIdOrPath, Long jobId) throws GitLabApiException {
        GitLabApiForm formData = null;
        Response response = post(Response.Status.CREATED, formData, "projects", getProjectIdOrPath(projectIdOrPath), "jobs", jobId, "cancel");
        return (response.readEntity(Job.class));
    }

    /**
     * Retry specified job in a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/jobs/:job_id/retry</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param jobId the ID to retry job
     * @return job instance which just retried
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Job retryJob(Object projectIdOrPath, Long jobId) throws GitLabApiException {
        GitLabApiForm formData = null;
        Response response = post(Status.CREATED, formData, "projects", getProjectIdOrPath(projectIdOrPath), "jobs", jobId, "retry");
        return (response.readEntity(Job.class));
    }

    /**
     * Erase specified job in a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/jobs/:job_id/erase</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param jobId the ID to erase job
     * @return job instance which just erased
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Job eraseJob(Object projectIdOrPath, Long jobId) throws GitLabApiException {
        GitLabApiForm formData = null;
        Response response = post(Status.CREATED, formData, "projects", getProjectIdOrPath(projectIdOrPath), "jobs", jobId, "erase");
        return (response.readEntity(Job.class));
    }

    /**
     * Play specified job in a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/jobs/:job_id/play</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param jobId the ID to play job
     * @return job instance which just played
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Job playJob(Object projectIdOrPath, Long jobId) throws GitLabApiException {
      return playJob(projectIdOrPath, jobId, null);
    }

    /**
     * Play specified job with parameters in a project.
     *
     * <pre>
     * <code>GitLab Endpoint: POST /projects/:id/jobs/:job_id/play</code>
     * </pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID
     *                        or path
     * @param jobId           the ID to play job
     * @param jobAttributes   attributes for the played job
     * @return job instance which just played
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Job playJob(Object projectIdOrPath, Long jobId, JobAttributes jobAttributes)
        throws GitLabApiException {
      Response response;
      if (jobAttributes == null) {
        GitLabApiForm formData = null;
        response = post(Status.CREATED, formData, "projects",
            getProjectIdOrPath(projectIdOrPath), "jobs", jobId, "play");
      } else {
        response = post(Status.CREATED, jobAttributes, "projects",
            getProjectIdOrPath(projectIdOrPath), "jobs", jobId, "play");
      }
      return (response.readEntity(Job.class));
    }

    /**
     * Prevents artifacts from being deleted when expiration is set.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/jobs/:job_id/keep</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param jobId the ID to keep artifacts for
     * @return the Job instance that was just modified
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Job keepArtifacts(Object projectIdOrPath, Long jobId) throws GitLabApiException {
	GitLabApiForm formData = null;
        Response response = post(Status.OK, formData, "projects", getProjectIdOrPath(projectIdOrPath), "jobs", jobId, "keep");
        return (response.readEntity(Job.class));
    }

    /**
     * Delete artifacts of a job.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/jobs/:job_id/artifacts</code></pre>
     *
     * @param projectIdOrPath id, path of the project, or a Project instance holding the project ID or path
     * @param jobId the ID to delete artifacts for
     * @throws GitLabApiException if any exception occurs during execution
     */
    public void deleteArtifacts(Object projectIdOrPath, Long jobId) throws GitLabApiException {
        delete(Status.NO_CONTENT, null, "projects", getProjectIdOrPath(projectIdOrPath), "jobs", jobId, "artifacts");
    }
}
