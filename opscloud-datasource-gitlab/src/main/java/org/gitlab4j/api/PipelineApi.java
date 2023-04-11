package org.gitlab4j.api;

import org.gitlab4j.api.models.*;

import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * <p>This class provides an entry point to all the GitLab API pipeline related calls.
 * For more information on the Pipeline APIs see:</p>
 *
 * <a href="https://docs.gitlab.com/ee/api/pipelines.html">Pipelines API</a>
 * <a href="https://docs.gitlab.com/ee/api/pipeline_schedules.html">Pipeline Schedules API</a>
 * <a href="https://docs.gitlab.com/ee/api/pipeline_triggers.html">Pipeline Triggers API</a>
 */
public class PipelineApi extends AbstractApi implements Constants {

    public PipelineApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Get a list of pipelines in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/pipelines</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a list containing the pipelines for the specified project ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Pipeline> getPipelines(Object projectIdOrPath) throws GitLabApiException {
        return (getPipelines(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a list of pipelines in a project in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/pipelines</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param page the page to get
     * @param perPage the number of Pipeline instances per page
     * @return a list containing the pipelines for the specified project ID in the specified page range
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Pipeline> getPipelines(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage),
                "projects", getProjectIdOrPath(projectIdOrPath), "pipelines");
        return (response.readEntity(new GenericType<List<Pipeline>>() {}));
    }

    /**
     * Get a Pager of pipelines in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/pipelines</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of Pipeline instances that will be fetched per page
     * @return a Pager containing the pipelines for the specified project ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Pager<Pipeline> getPipelines(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Pipeline>(this, Pipeline.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "pipelines"));
    }

    /**
     * Get a Stream of pipelines in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/pipelines</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a Stream containing the pipelines for the specified project ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Stream<Pipeline> getPipelinesStream(Object projectIdOrPath) throws GitLabApiException {
        return (getPipelines(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of pipelines in a project filtered with the provided {@link PipelineFilter}.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/pipelines</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filter a PipelineFilter instance used to filter the results
     * @return a list containing the pipelines for the specified project ID and matching the provided filter
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Pipeline> getPipelines(Object projectIdOrPath, PipelineFilter filter) throws GitLabApiException {
        return (getPipelines(projectIdOrPath, filter, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of pipelines in a project filtered with the provided {@link PipelineFilter}.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/pipelines</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filter a PipelineFilter instance used to filter the results
     * @param itemsPerPage the number of Pipeline instances that will be fetched per page
     * @return a Pager containing the pipelines for the specified project ID and matching the provided filter
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Pager<Pipeline> getPipelines(Object projectIdOrPath, PipelineFilter filter, int itemsPerPage) throws GitLabApiException {
	GitLabApiForm formData = (filter != null ? filter.getQueryParams() : new GitLabApiForm());
	return (new Pager<Pipeline>(this, Pipeline.class, itemsPerPage, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "pipelines"));
    }

    /**
     * Get a Stream of pipelines in a project filtered with the provided {@link PipelineFilter}.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/pipelines</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filter a PipelineFilter instance used to filter the results
     * @return a Stream containing the pipelines for the specified project ID and matching the provided filter
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Stream<Pipeline> getPipelinesStream(Object projectIdOrPath, PipelineFilter filter) throws GitLabApiException {
        return (getPipelines(projectIdOrPath, filter, getDefaultPerPage()).stream());
    }

    /**
     * Get a list of pipelines in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/pipelines</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param scope the scope of pipelines, one of: RUNNING, PENDING, FINISHED, BRANCHES, TAGS
     * @param status the status of pipelines, one of: RUNNING, PENDING, SUCCESS, FAILED, CANCELED, SKIPPED
     * @param ref the ref of pipelines
     * @param yamlErrors returns pipelines with invalid configurations
     * @param name the name of the user who triggered pipelines
     * @param username the username of the user who triggered pipelines
     * @param orderBy order pipelines by ID, STATUS, REF, USER_ID (default: ID)
     * @param sort sort pipelines in ASC or DESC order (default: DESC)
     * @return a list containing the pipelines for the specified project ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Pipeline> getPipelines(Object projectIdOrPath, PipelineScope scope, PipelineStatus status, String ref, boolean yamlErrors,
            String name, String username, PipelineOrderBy orderBy, SortOrder sort) throws GitLabApiException {

        return(getPipelines(projectIdOrPath, scope, status, ref, yamlErrors,
            name, username, orderBy, sort, getDefaultPerPage()).all());
    }

    /**
     * Get a list of pipelines in a project in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/pipelines</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param scope the scope of pipelines, one of: RUNNING, PENDING, FINISHED, BRANCHES, TAGS
     * @param status the status of pipelines, one of: RUNNING, PENDING, SUCCESS, FAILED, CANCELED, SKIPPED
     * @param ref the ref of pipelines
     * @param yamlErrors returns pipelines with invalid configurations
     * @param name the name of the user who triggered pipelines
     * @param username the username of the user who triggered pipelines
     * @param orderBy order pipelines by ID, STATUS, REF, USER_ID (default: ID)
     * @param sort sort pipelines in ASC or DESC order (default: DESC)
     * @param page the page to get
     * @param perPage the number of Pipeline instances per page
     * @return a list containing the pipelines for the specified project ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public List<Pipeline> getPipelines(Object projectIdOrPath, PipelineScope scope, PipelineStatus status, String ref, boolean yamlErrors,
            String name, String username, PipelineOrderBy orderBy, SortOrder sort, int page, int perPage) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("scope", scope)
                .withParam("status", status)
                .withParam("ref", ref)
                .withParam("yaml_errors", yamlErrors)
                .withParam("name", name)
                .withParam("username", username)
                .withParam("order_by", orderBy)
                .withParam("sort", sort)
                .withParam("page", page)
                .withParam(PER_PAGE_PARAM, perPage);

        Response response = get(Response.Status.OK, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "pipelines");
        return (response.readEntity(new GenericType<List<Pipeline>>() {}));
    }

    /**
     * Get a Stream of pipelines in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/pipelines</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param scope the scope of pipelines, one of: RUNNING, PENDING, FINISHED, BRANCHES, TAGS
     * @param status the status of pipelines, one of: RUNNING, PENDING, SUCCESS, FAILED, CANCELED, SKIPPED
     * @param ref the ref of pipelines
     * @param yamlErrors returns pipelines with invalid configurations
     * @param name the name of the user who triggered pipelines
     * @param username the username of the user who triggered pipelines
     * @param orderBy order pipelines by ID, STATUS, REF, USER_ID (default: ID)
     * @param sort sort pipelines in ASC or DESC order (default: DESC)
     * @return a Stream containing the pipelines for the specified project ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Stream<Pipeline> getPipelinesStream(Object projectIdOrPath, PipelineScope scope, PipelineStatus status,
            String ref, boolean yamlErrors, String name, String username, PipelineOrderBy orderBy, SortOrder sort) throws GitLabApiException {

        return(getPipelines(projectIdOrPath, scope, status, ref, yamlErrors,
            name, username, orderBy, sort, getDefaultPerPage()).stream());
   }

    /**
     * Get a Pager of pipelines in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/pipelines</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param scope the scope of pipelines, one of: RUNNING, PENDING, FINISHED, BRANCHES, TAGS
     * @param status the status of pipelines, one of: RUNNING, PENDING, SUCCESS, FAILED, CANCELED, SKIPPED
     * @param ref the ref of pipelines
     * @param yamlErrors returns pipelines with invalid configurations
     * @param name the name of the user who triggered pipelines
     * @param username the username of the user who triggered pipelines
     * @param orderBy order pipelines by ID, STATUS, REF, USER_ID (default: ID)
     * @param sort sort pipelines in ASC or DESC order (default: DESC)
     * @param itemsPerPage the number of Pipeline instances that will be fetched per page
     * @return a list containing the pipelines for the specified project ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Pager<Pipeline> getPipelines(Object projectIdOrPath, PipelineScope scope, PipelineStatus status, String ref, boolean yamlErrors,
            String name, String username, PipelineOrderBy orderBy, SortOrder sort, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("scope", scope)
                .withParam("status", status)
                .withParam("ref", (ref != null ? urlEncode(ref) : null))
                .withParam("yaml_errors", yamlErrors)
                .withParam("name", name)
                .withParam("username", username)
                .withParam("order_by", orderBy)
                .withParam("sort", sort);

        return (new Pager<Pipeline>(this, Pipeline.class, itemsPerPage, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "pipelines"));
    }

    /**
     * Get single pipelines in a project.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/pipelines/:pipeline_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param pipelineId the pipeline ID to get
     * @return a single pipelines for the specified project ID
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Pipeline getPipeline(Object projectIdOrPath, long pipelineId) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "pipelines", pipelineId);
        return (response.readEntity(Pipeline.class));
    }

    /**
     * Create a pipelines in a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/pipeline</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param ref reference to commit
     * @return a Pipeline instance with the newly created pipeline info
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Pipeline createPipeline(Object projectIdOrPath, String ref) throws GitLabApiException {
        return (createPipeline(projectIdOrPath, ref, Variable.convertMapToList(null)));
    }

    /**
     * Create a pipelines in a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/pipeline</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param ref reference to commit
     * @param variables a Map containing the variables available in the pipeline
     * @return a Pipeline instance with the newly created pipeline info
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Pipeline createPipeline(Object projectIdOrPath, String ref, Map<String, String> variables) throws GitLabApiException {
        return (createPipeline(projectIdOrPath, ref, Variable.convertMapToList(variables)));
    }

    /**
     * Create a pipelines in a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/pipeline</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param ref reference to commit
     * @param variables a Map containing the variables available in the pipeline
     * @return a Pipeline instance with the newly created pipeline info
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Pipeline createPipeline(Object projectIdOrPath, String ref, List<Variable> variables) throws GitLabApiException {

        if (ref == null || ref.trim().isEmpty()) {
            throw new GitLabApiException("ref cannot be null or empty");
        }

        if (variables == null || variables.isEmpty()) {
            GitLabApiForm formData = new GitLabApiForm().withParam("ref", ref, true);
            Response response = post(Response.Status.CREATED, formData, "projects", getProjectIdOrPath(projectIdOrPath), "pipeline");
            return (response.readEntity(Pipeline.class));
        }

        // The create pipeline REST API expects the variable data in an unusual format, this
        // class is used to create the JSON for the POST data.
        class CreatePipelineForm {
            @SuppressWarnings("unused")
            public String ref;
            @SuppressWarnings("unused")
            public List<Variable> variables;
            CreatePipelineForm(String ref, List<Variable> variables) {
                this.ref = ref;
                this.variables = variables;
            }
        }

        CreatePipelineForm pipelineForm = new CreatePipelineForm(ref, variables);
        Response response = post(Response.Status.CREATED, pipelineForm, "projects", getProjectIdOrPath(projectIdOrPath), "pipeline");
        return (response.readEntity(Pipeline.class));
    }

    /**
     * Delete a pipeline from a project.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/pipelines/:pipeline_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param pipelineId the pipeline ID to delete
     * @throws GitLabApiException if any exception occurs during execution
     */
    public void deletePipeline(Object projectIdOrPath, long pipelineId) throws GitLabApiException {
       delete(Response.Status.ACCEPTED, null, "projects", getProjectIdOrPath(projectIdOrPath), "pipelines", pipelineId);
    }

    /**
     * Retry a job in specified pipelines in a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/pipelines/:pipeline_id/retry</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param pipelineId the pipeline ID to retry a job from
     * @return pipeline instance which just retried
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Pipeline retryPipelineJob(Object projectIdOrPath, long pipelineId) throws GitLabApiException {
        GitLabApiForm formData = null;
        Response response = post(Response.Status.OK, formData, "projects", getProjectIdOrPath(projectIdOrPath), "pipelines", pipelineId, "retry");
        return (response.readEntity(Pipeline.class));
    }

    /**
     * Cancel jobs of specified pipelines in a project.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/pipelines/:pipeline_id/cancel</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param pipelineId the pipeline ID to cancel jobs
     * @return pipeline instance which just canceled
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Pipeline cancelPipelineJobs(Object projectIdOrPath, long pipelineId) throws GitLabApiException {
        GitLabApiForm formData = null;
        Response response = post(Response.Status.OK, formData, "projects", getProjectIdOrPath(projectIdOrPath), "pipelines", pipelineId, "cancel");
        return (response.readEntity(Pipeline.class));
    }

    /**
     * Get a list of the project pipeline_schedules for the specified project.
     *
     * <pre><code>GET /projects/:id/pipeline_schedules</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a list of pipeline schedules for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public List<PipelineSchedule> getPipelineSchedules(Object projectIdOrPath) throws GitLabApiException {
        return (getPipelineSchedules(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get list of project pipeline schedules in the specified page range.
     *
     * <pre><code>GET /projects/:id/pipeline_schedules</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param page the page to get
     * @param perPage the number of PipelineSchedule instances per page
     * @return a list of project pipeline_schedules for the specified project in the specified page range
     * @throws GitLabApiException if any exception occurs
     */
    public List<PipelineSchedule> getPipelineSchedules(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "projects", getProjectIdOrPath(projectIdOrPath), "pipeline_schedules");
        return (response.readEntity(new GenericType<List<PipelineSchedule>>() {}));
    }

    /**
     * Get Pager of project pipeline schedule.
     *
     * <pre><code>GET /projects/:id/pipeline_schedule</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param itemsPerPage the number of PipelineSchedule instances that will be fetched per page
     * @return a Pager of project pipeline_schedules for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<PipelineSchedule> getPipelineSchedules(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<PipelineSchedule>(this, PipelineSchedule.class, itemsPerPage, null, "projects", getProjectIdOrPath(projectIdOrPath), "pipeline_schedules"));
    }

    /**
     * Get a Stream of the project pipeline schedule for the specified project.
     *
     * <pre><code>GET /projects/:id/pipeline_schedule</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a Stream of project pipeline schedules for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<PipelineSchedule> getPipelineSchedulesStream(Object projectIdOrPath) throws GitLabApiException {
        return (getPipelineSchedules(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get a specific pipeline schedule for project.
     *
     * <pre><code>GET /projects/:id/pipeline_schedules/:pipeline_schedule_id</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param pipelineScheduleId the ID of the pipeline schedule to get
     * @return the project PipelineSchedule
     * @throws GitLabApiException if any exception occurs
     */
    public PipelineSchedule getPipelineSchedule(Object projectIdOrPath, Long pipelineScheduleId) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "pipeline_schedules", pipelineScheduleId);
        return (response.readEntity(PipelineSchedule.class));
    }

    /**
     * Get a specific pipeline schedule for project as an Optional instance.
     *
     * <pre><code>GET /projects/:id/pipeline_schedules/:pipeline_schedule_id</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param pipelineScheduleId the ID of the hook to get
     * @return the project PipelineSchedule as an Optional instance
     */
    public Optional<PipelineSchedule> getOptionalPipelineSchedule (Object projectIdOrPath, Long pipelineScheduleId) {
        try {
            return (Optional.ofNullable(getPipelineSchedule(projectIdOrPath, pipelineScheduleId)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * create a pipeline schedule for a project.
     *
     * <pre><code>POST /projects/:id/pipeline_schedules</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param pipelineSchedule a PipelineSchedule instance to create
     * @return the added PipelineSchedule instance
     * @throws GitLabApiException if any exception occurs
     */
    public PipelineSchedule createPipelineSchedule(Object projectIdOrPath, PipelineSchedule pipelineSchedule)
            throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("description", pipelineSchedule.getDescription(), true)
                .withParam("ref", pipelineSchedule.getRef(), true)
                .withParam("cron", pipelineSchedule.getCron(), true)
                .withParam("cron_timezone", pipelineSchedule.getCronTimezone(), false)
                .withParam("active", pipelineSchedule.getActive(), false);
        Response response = post(Response.Status.CREATED, formData, "projects", getProjectIdOrPath(projectIdOrPath), "pipeline_schedules");
        return (response.readEntity(PipelineSchedule.class));
    }

    /**
     * Deletes a pipeline schedule from the project.
     *
     * <pre><code>DELETE /projects/:id/pipeline_schedules/:pipeline_schedule_id</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param pipelineScheduleId the project schedule ID to delete
     * @throws GitLabApiException if any exception occurs
     */
    public void deletePipelineSchedule(Object projectIdOrPath, Long pipelineScheduleId) throws GitLabApiException {
        Response.Status expectedStatus = (isApiVersion(GitLabApi.ApiVersion.V3) ? Response.Status.OK : Response.Status.NO_CONTENT);
        delete(expectedStatus, null, "projects", getProjectIdOrPath(projectIdOrPath), "pipeline_schedules", pipelineScheduleId);
    }

    /**
     * Modifies a pipeline schedule for project.
     *
     * <pre><code>PUT /projects/:id/pipeline_schedules/:pipeline_schedule_id</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param pipelineSchedule the pipelineSchedule instance that contains the pipelineSchedule info to modify
     * @return the modified project schedule
     * @throws GitLabApiException if any exception occurs
     */
    public PipelineSchedule updatePipelineSchedule(Object projectIdOrPath,PipelineSchedule pipelineSchedule) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("description", pipelineSchedule.getDescription(), false)
                .withParam("ref", pipelineSchedule.getRef(), false)
                .withParam("cron", pipelineSchedule.getCron(), false)
                .withParam("cron_timezone", pipelineSchedule.getCronTimezone(), false)
                .withParam("active", pipelineSchedule.getActive(), false);

        Response response = put(Response.Status.OK, formData.asMap(), "projects", getProjectIdOrPath(projectIdOrPath), "pipeline_schedules", pipelineSchedule.getId());
        return (response.readEntity(PipelineSchedule.class));
    }

    /**
     * Update the owner of the pipeline schedule of a project.
     *
     * <pre><code>POST /projects/:id/pipeline_schedules/:pipeline_schedule_id/take_ownership</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param pipelineScheduleId the pipelineSchedule instance id that ownership has to be taken of
     * @return the modified project schedule
     * @throws GitLabApiException if any exception occurs
     */
    public PipelineSchedule takeOwnershipPipelineSchedule(Object projectIdOrPath, Long pipelineScheduleId) throws GitLabApiException {

        Response response = post(Response.Status.OK, "", "projects", getProjectIdOrPath(projectIdOrPath),  "pipeline_schedules", pipelineScheduleId, "take_ownership");
        return (response.readEntity(PipelineSchedule.class));
    }

    /**
     * Trigger a new scheduled pipeline, which runs immediately.
     *
     * <pre><code>POST /projects/:id/pipeline_schedules/:pipeline_schedule_id/play</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param pipelineScheduleId the pipelineSchedule instance id which should run immediately
     * @throws GitLabApiException if any exception occurs during execution
     */
    public void playPipelineSchedule(Object projectIdOrPath, Long pipelineScheduleId) throws GitLabApiException {
        post(Response.Status.CREATED, (Form)null, "projects", getProjectIdOrPath(projectIdOrPath),  "pipeline_schedules", pipelineScheduleId, "play");
    }

    /**
     * Create a pipeline schedule variable.
     *
     * <pre><code>GitLab Endpoint: POST /projects/:id/pipeline_schedules/:pipeline_schedule_id/variables</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param pipelineScheduleId the pipelineSchedule ID
     * @param key the key of a variable; must have no more than 255 characters; only A-Z, a-z, 0-9, and _ are allowed
     * @param value the value for the variable
     * @return a Pipeline instance with the newly created pipeline schedule variable
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Variable createPipelineScheduleVariable(Object projectIdOrPath, Long pipelineScheduleId,
            String key, String value) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("key", key, true)
                .withParam("value", value, true);
        Response response = post(Response.Status.CREATED, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "pipeline_schedules", pipelineScheduleId, "variables");
        return (response.readEntity(Variable.class));
    }

    /**
     * Update a pipeline schedule variable.
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/pipeline_schedules/:pipeline_schedule_id/variables/:key</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param pipelineScheduleId the pipelineSchedule ID
     * @param key the key of an existing pipeline schedule variable
     * @param value the new value for the variable
     * @return a Pipeline instance with the updated variable
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Variable updatePipelineScheduleVariable(Object projectIdOrPath, Long pipelineScheduleId,
            String key, String value) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm().withParam("value", value, true);
        Response response = this.putWithFormData(Response.Status.CREATED, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "pipeline_schedules", pipelineScheduleId, "variables", key);
        return (response.readEntity(Variable.class));
    }

    /**
     * Deletes a pipeline schedule variable.
     *
     * <pre><code>DELETE /projects/:id/pipeline_schedules/:pipeline_schedule_id/variables/:key</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param pipelineScheduleId the pipeline schedule ID
     * @param key the key of an existing pipeline schedule variable
     * @throws GitLabApiException if any exception occurs
     */
    public void deletePipelineScheduleVariable(Object projectIdOrPath, Long pipelineScheduleId, String key) throws GitLabApiException {
        Response.Status expectedStatus = (isApiVersion(GitLabApi.ApiVersion.V3) ? Response.Status.OK : Response.Status.NO_CONTENT);
        delete(expectedStatus, null, "projects", getProjectIdOrPath(projectIdOrPath),
                "pipeline_schedules", pipelineScheduleId, "variables", key);
    }

    /**
     * Get a list of the project pipeline triggers for the specified project.
     *
     * <pre><code>GET /projects/:id/triggers</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a list of pipeline triggers for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public List<Trigger> getPipelineTriggers(Object projectIdOrPath) throws GitLabApiException {
        return (getPipelineTriggers(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get list of project pipeline triggers in the specified page range.
     *
     * <pre><code>GET /projects/:id/triggers</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param page the page to get
     * @param perPage the number of Trigger instances per page
     * @return a list of project pipeline triggers for the specified project in the specified page range
     * @throws GitLabApiException if any exception occurs
     */
    public List<Trigger> getPipelineTriggers(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage), "projects", getProjectIdOrPath(projectIdOrPath), "triggers");
        return (response.readEntity(new GenericType<List<Trigger>>() {}));
    }

    /**
     * Get Pager of project pipeline triggers.
     *
     * <pre><code>GET /projects/:id/pipeline_schedule</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param itemsPerPage the number of Project instances that will be fetched per page
     * @return a Pager of project pipeline triggers for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Trigger> getPipelineTriggers(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Trigger>(this, Trigger.class, itemsPerPage, null, "projects", getProjectIdOrPath(projectIdOrPath), "triggers"));
    }

    /**
     * Get a Stream of the project pipeline triggers for the specified project.
     *
     * <pre><code>GET /projects/:id/pipeline_schedule</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @return a Stream of project pipeline triggers for the specified project
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Trigger> getPipelineTriggersStream(Object projectIdOrPath) throws GitLabApiException {
        return (getPipelineTriggers(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get a specific pipeline schedule for project.
     *
     * <pre><code>GET /projects/:id/triggers/:trigger_id</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param triggerId the ID of the trigger to get
     * @return the project pipeline trigger
     * @throws GitLabApiException if any exception occurs
     */
    public Trigger getPipelineTrigger(Object projectIdOrPath, Long triggerId) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "triggers", triggerId);
        return (response.readEntity(Trigger.class));
    }

    /**
     * Get a specific pipeline trigger for project as an Optional instance.
     *
     * <pre><code>GET /projects/:id/triggers/:trigger_id</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param triggerId the ID of the trigger to get
     * @return the project pipeline trigger as an Optional instance
     */
    public Optional<Trigger> getOptionalPipelineTrigger(Object projectIdOrPath, Long triggerId) {
        try {
            return (Optional.ofNullable(getPipelineTrigger(projectIdOrPath, triggerId)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Create a pipeline trigger for a project.
     *
     * <pre><code>POST /projects/:id/triggers</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param description the trigger description
     * @return the created Trigger instance
     * @throws GitLabApiException if any exception occurs
     */
    public Trigger createPipelineTrigger(Object projectIdOrPath, String description) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("description", description, true);
        Response response = post(Response.Status.CREATED, formData, "projects", getProjectIdOrPath(projectIdOrPath), "triggers");
        return (response.readEntity(Trigger.class));
    }

    /**
     * Updates a pipeline trigger for project.
     *
     * <pre><code>PUT /projects/:id/triggers/:trigger_id</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param triggerId the trigger ID to update
     * @param description the new trigger description
     * @return the updated Trigger instance
     * @throws GitLabApiException if any exception occurs
     */
    public Trigger updatePipelineTrigger(Object projectIdOrPath, Long triggerId, String description) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("description", description, false);
        Response response = put(Response.Status.OK, formData.asMap(), "projects", getProjectIdOrPath(projectIdOrPath), "triggers", triggerId);
        return (response.readEntity(Trigger.class));
    }

    /**
     * Deletes a pipeline trigger from the project.
     *
     * <pre><code>DELETE /projects/:id/triggers/:trigger_id</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param triggerId the project trigger ID to delete
     * @throws GitLabApiException if any exception occurs
     */
    public void deletePipelineTrigger(Object projectIdOrPath, Long triggerId) throws GitLabApiException {
        delete(Response.Status.NO_CONTENT, null, "projects", getProjectIdOrPath(projectIdOrPath), "triggers", triggerId);
    }

    /**
     * Take ownership of a pipeline trigger for project.
     *
     * <pre><code>PUT /projects/:id/triggers/:trigger_id/take_ownership</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param triggerId the trigger ID to take opwnership of
     * @return the updated Trigger instance
     * @throws GitLabApiException if any exception occurs
     */
    public Trigger takeOwnewrshipOfPipelineTrigger(Object projectIdOrPath, Long triggerId) throws GitLabApiException {
        Response response = put(Response.Status.OK, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "triggers", triggerId, "take_ownership");
        return (response.readEntity(Trigger.class));
    }

    /**
     * Trigger a pipeline for a project.
     *
     * <pre><code>POST /projects/:id/trigger/pipeline</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param trigger the Trigger instance holding the trigger token
     * @param ref the ref that the pipeline is to be triggered for
     * @param variables a List of variables to be passed with the trigger
     * @return a Pipeline instance holding information on the triggered pipeline
     * @throws GitLabApiException if any exception occurs
     */
    public Pipeline triggerPipeline(Object projectIdOrPath, Trigger trigger, String ref, List<Variable> variables) throws GitLabApiException {

        if (trigger == null) {
            throw new GitLabApiException("trigger cannot be null");
        }

        return (triggerPipeline(projectIdOrPath, trigger.getToken(), ref, variables));
    }

    /**
     * Trigger a pipeline for a project.
     *
     * <pre><code>POST /projects/:id/trigger/pipeline</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param token the trigger token
     * @param ref the ref that the pipeline is to be triggered for
     * @param variables a List of variables to be passed with the trigger
     * @return a Pipeline instance holding information on the triggered pipeline
     * @throws GitLabApiException if any exception occurs
     */
    public Pipeline triggerPipeline(Object projectIdOrPath, String token, String ref, List<Variable> variables) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("token", token, true)
                .withParam("ref",  ref, true)
                .withParam(variables);
        Response response = post(Response.Status.CREATED, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "trigger", "pipeline");
        return (response.readEntity(Pipeline.class));
    }

    /**
     * Get List of variables of a pipeline.
     *
     * <pre><code>GET /projects/:id/pipelines/:pipeline_id/variables</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param pipelineId the pipeline ID
     * @return a List of pipeline variables
     * @throws GitLabApiException if any exception occurs
     */
    public List<Variable> getPipelineVariables(Object projectIdOrPath, Long pipelineId) throws GitLabApiException {
        return (getPipelineVariables(projectIdOrPath, pipelineId, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of variables of a pipeline.
     *
     * <pre><code>GET /projects/:id/pipelines/:pipeline_id/variables</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param pipelineId the pipeline ID
     * @param itemsPerPage the number of Pipeline instances that will be fetched per page
     * @return a Pager of pipeline variables
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Variable> getPipelineVariables(Object projectIdOrPath, Long pipelineId, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Variable>(this, Variable.class, itemsPerPage, null,
                "projects",  getProjectIdOrPath(projectIdOrPath), "pipelines", pipelineId, "variables"));
    }

    /**
     * Get a Stream of variables of a pipeline as a Stream.
     *
     * <pre><code>GET /projects/:id/pipelines/:pipeline_id/variables</code></pre>
     *
     * @param projectIdOrPath projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance, required
     * @param pipelineId the pipeline ID
     * @return a Stream of pipeline variables
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Variable> getPipelineVariablesStream(Object projectIdOrPath, Long pipelineId) throws GitLabApiException {
        return (getPipelineVariables(projectIdOrPath, pipelineId, getDefaultPerPage()).stream());
    }
}
