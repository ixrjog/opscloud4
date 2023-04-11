package org.gitlab4j.api;

import org.gitlab4j.api.models.Label;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This class provides an entry point to all the GitLab API project and group label calls.
 *
 * @see <a href="https://docs.gitlab.com/ce/api/labels.html">Labels API at GitLab</a>
 * @see <a href="https://docs.gitlab.com/ce/api/group_labels.html">Group Labels API at GitLab</a>
 */
public class LabelsApi extends AbstractApi {

    public LabelsApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Get all labels of the specified project.
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a list of project's labels
     * @throws GitLabApiException if any exception occurs
     */
    public List<Label> getProjectLabels(Object projectIdOrPath) throws GitLabApiException {
        return (getProjectLabels(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of all labels of the specified project.
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of items per page
     * @return a list of project's labels in the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Label> getProjectLabels(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Label>(this, Label.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "labels"));
    }

    /**
     * Get a Stream of all labels of the specified project.
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a Stream of project's labels
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Label> getProjectLabelsStream(Object projectIdOrPath) throws GitLabApiException {
        return (getProjectLabels(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get a single project label.
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param labelIdOrName the label in the form of an Long(ID), String(name), or Label instance
     * @return a Label instance holding the information for the group label
     * @throws GitLabApiException if any exception occurs
     */
    public Label getProjectLabel(Object projectIdOrPath, Object labelIdOrName) throws GitLabApiException {
        Response response = get(Response.Status.OK, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "labels", getLabelIdOrName(labelIdOrName));
        return (response.readEntity(Label.class));
    }

    /**
     * Get a single project label as the value of an Optional.
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param labelIdOrName the label in the form of an Long(ID), String(name), or Label instance
     * @return a Optional instance with a Label instance as its value
     * @throws GitLabApiException if any exception occurs
     */
    public Optional<Label> getOptionalProjectLabel(Object projectIdOrPath, Object labelIdOrName) throws GitLabApiException {
        try {
            return (Optional.ofNullable(getProjectLabel(projectIdOrPath, labelIdOrName)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Create a project label.  A Label instance is used to set the label properties.
     * withXXX() methods are provided to set the properties of the label to create:
     * <pre><code>
     *   // name and color properties are required
     *   Label labelProperties = new Label()
     *          .withName("a-pink-project-label")
     *          .withColor("pink")
     *          .withDescription("A new pink project label")
     *       .withPriority(10);
     *   gitLabApi.getLabelsApi().createProjectLabel(projectId, labelProperties);
     * </code></pre>
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/labels</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param labelProperties a Label instance holding the properties for the new group label
     * @return the created Label instance
     * @throws GitLabApiException if any exception occurs
     */
    public Label createProjectLabel(Object projectIdOrPath, Label labelProperties) throws GitLabApiException {
        GitLabApiForm formData = labelProperties.getForm(true);
        Response response = post(Response.Status.CREATED, formData, "projects", getProjectIdOrPath(projectIdOrPath), "labels");
        return (response.readEntity(Label.class));
    }

    /**
     * Update the specified project label. The name, color, and description can be updated.
     * A Label instance is used to set the properties of the label to update,
     * withXXX() methods are provided to set the properties to update:
     * <pre><code>
     *   Label labelUpdates = new Label()
     *        .withName("a-new-name")
     *        .withColor("red")
     *        .withDescription("A red group label");
     *   gitLabApi.getLabelsApi().updateGroupLabel(projectId, labelId, labelUpdates);
     * </code></pre>
     *
     * <pre><code>GitLab Endpoint: PUT /projects/:id/labels/:label_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param labelIdOrName the label in the form of an Long(ID), String(name), or Label instance
     * @param labelConfig a Label instance holding the label properties to update
     * @return the updated Label instance
     * @throws GitLabApiException if any exception occurs
     */
    public Label updateProjectLabel(Object projectIdOrPath, Object labelIdOrName, Label labelConfig) throws GitLabApiException {
        GitLabApiForm formData = labelConfig.getForm(false);
        Response response = putWithFormData(Response.Status.OK, formData,
                "projects", getProjectIdOrPath(projectIdOrPath), "labels", getLabelIdOrName(labelIdOrName));
        return (response.readEntity(Label.class));
    }

    /**
     * Delete the specified project label.
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param labelIdOrName the label in the form of an Long(ID), String(name), or Label instance
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteProjectLabel(Object projectIdOrPath, Object labelIdOrName) throws GitLabApiException {
        delete(Response.Status.OK, null, "projects", getProjectIdOrPath(projectIdOrPath), "labels", getLabelIdOrName(labelIdOrName));
    }

    /**
     * Subscribe a specified project label.
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param labelIdOrName the label in the form of an Long(ID), String(name), or Label instance
     * @return HttpStatusCode 503
     * @throws GitLabApiException if any exception occurs
     */
    public Label subscribeProjectLabel(Object projectIdOrPath, Object labelIdOrName) throws GitLabApiException {
        Response response = post(Response.Status.NOT_MODIFIED, getDefaultPerPageParam(),
                "projects", getProjectIdOrPath(projectIdOrPath), "labels", getLabelIdOrName(labelIdOrName), "subscribe");
        return (response.readEntity(Label.class));
    }

    /**
     * Unsubscribe a specified project label.
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param labelIdOrName the label in the form of an Long(ID), String(name), or Label instance
     * @return HttpStatusCode 503
     * @throws GitLabApiException if any exception occurs
     */
    public Label unsubscribeProjectLabel(Object projectIdOrPath, Object labelIdOrName) throws GitLabApiException {
        Response response = post(Response.Status.NOT_MODIFIED, getDefaultPerPageParam(),
                "projects", getProjectIdOrPath(projectIdOrPath), "labels", getLabelIdOrName(labelIdOrName), "unsubscribe");
        return (response.readEntity(Label.class));
    }

    /**
     * Get all labels of the specified group.
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @return a list of group's labels
     * @throws GitLabApiException if any exception occurs
     */
    public List<Label> getGroupLabels(Object groupIdOrPath) throws GitLabApiException {
        return (getGroupLabels(groupIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a Pager of all labels of the specified group.
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param itemsPerPage the number of items per page
     * @return a list of group's labels in the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Label> getGroupLabels(Object groupIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Label>(this, Label.class, itemsPerPage, null,
                "groups", getGroupIdOrPath(groupIdOrPath), "labels"));
    }

    /**
     * Get a Stream of all labels of the specified group.
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @return a Stream of group's labels
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Label> getGroupLabelsStream(Object groupIdOrPath) throws GitLabApiException {
        return (getGroupLabels(groupIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get a single group label.
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param labelIdOrName the label in the form of an Long(ID), String(name), or Label instance
     * @return a Label instance holding the information for the group label
     * @throws GitLabApiException if any exception occurs
     */
    public Label getGroupLabel(Object groupIdOrPath, Object labelIdOrName) throws GitLabApiException {
        Response response = get(Response.Status.OK, null,
                "groups", getGroupIdOrPath(groupIdOrPath), "labels", getLabelIdOrName(labelIdOrName));
        return (response.readEntity(Label.class));
    }

    /**
     * Get a single group label as the value of an Optional.
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param labelIdOrName the label in the form of an Long(ID), String(name), or Label instance
     * @return a Optional instance with a Label instance as its value
     * @throws GitLabApiException if any exception occurs
     */
    public Optional<Label> getOptionalGroupLabel(Object groupIdOrPath, Object labelIdOrName) throws GitLabApiException {
        try {
            return (Optional.ofNullable(getGroupLabel(groupIdOrPath, labelIdOrName)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Create a group label.  A Label instance is used to set the label properties.
     * withXXX() methods are provided to set the properties of the label to create:
     * <pre><code>
     *   Label labelProperties = new Label()
     *       .withName("a-name")
     *       .withColor("green")
     *       .withDescription("A new green group label");
     *   gitLabApi.getLabelsApi().createGroupLabel(projectId, labelProperties);
     * </code></pre>
     *
     * <pre><code>GitLab Endpoint: POST /groups/:id/labels</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param labelProperties a Label instance holding the properties for the new group label
     * @return the created Label instance
     * @throws GitLabApiException if any exception occurs
     */
    public Label createGroupLabel(Object groupIdOrPath, Label labelProperties) throws GitLabApiException {
        GitLabApiForm formData = labelProperties.getForm(true);
        Response response = post(Response.Status.CREATED, formData, "groups", getGroupIdOrPath(groupIdOrPath), "labels");
        return (response.readEntity(Label.class));
    }

    /**
     * Update the specified label. The name, color, and description can be updated.
     * A Label instance is used to set the properties of the label to update,
     * withXXX() methods are provided to set the properties to update:
     * <pre><code>
     *   Label labelUpdates = new Label()
     *       .withName("a-new-name")
     *       .withColor("red")
     *       .withDescription("A red group label");
     *   gitLabApi.getLabelsApi().updateGroupLabel(projectId, labelId, labelUpdates);
     * </code></pre>
     *
     * <pre><code>GitLab Endpoint: PUT /groups/:id/labels/:label_id</code></pre>
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param labelIdOrName the label in the form of an Long(ID), String(name), or Label instance
     * @param labelConfig a Label instance holding the label properties to update
     * @return the updated Label instance
     * @throws GitLabApiException if any exception occurs
     */
    public Label updateGroupLabel(Object groupIdOrPath, Object labelIdOrName, Label labelConfig) throws GitLabApiException {
        GitLabApiForm formData = labelConfig.getForm(false);
        Response response = putWithFormData(Response.Status.OK, formData,
                "groups", getGroupIdOrPath(groupIdOrPath), "labels", getLabelIdOrName(labelIdOrName));
        return (response.readEntity(Label.class));
    }

    /**
     * Delete the specified label
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param labelIdOrName the label in the form of an Long(ID), String(name), or Label instance
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteGroupLabel(Object groupIdOrPath, Object labelIdOrName) throws GitLabApiException {
        delete(Response.Status.OK, null, "groups", getGroupIdOrPath(groupIdOrPath), "labels", getLabelIdOrName(labelIdOrName));
    }

    /**
     * Subscribe a specified group label.
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param labelIdOrName the label in the form of an Long(ID), String(name), or Label instance
     * @return HttpStatusCode 503
     * @throws GitLabApiException if any exception occurs
     */
    public Label subscribeGroupLabel(Object groupIdOrPath, Object labelIdOrName) throws GitLabApiException {
        Response response = post(Response.Status.NOT_MODIFIED, getDefaultPerPageParam(),
                "groups", getGroupIdOrPath(groupIdOrPath), "labels", getLabelIdOrName(labelIdOrName), "subscribe");
        return (response.readEntity(Label.class));
    }

    /**
     * Unsubscribe a specified group label.
     *
     * @param groupIdOrPath the group in the form of an Long(ID), String(path), or Group instance
     * @param labelIdOrName the label in the form of an Long(ID), String(name), or Label instance
     * @return HttpStatusCode 503
     * @throws GitLabApiException if any exception occurs
     */
    public Label unsubscribeGroupLabel(Object groupIdOrPath, Object labelIdOrName) throws GitLabApiException {
        Response response = post(Response.Status.NOT_MODIFIED, getDefaultPerPageParam(),
                "groups", getGroupIdOrPath(groupIdOrPath), "labels", getLabelIdOrName(labelIdOrName), "unsubscribe");
        return (response.readEntity(Label.class));
    }


    /**
     * Get all labels of the specified project.
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a list of project's labels
     * @throws GitLabApiException if any exception occurs
     * @deprecated Replaced by the {@link #getProjectLabels(Object)} method.
     */
    @Deprecated
    public List<Label> getLabels(Object projectIdOrPath) throws GitLabApiException {
        return (getLabels(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get all labels of the specified project to using the specified page and per page setting
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param page the page to get
     * @param perPage the number of items per page
     * @return a list of project's labels in the specified range
     * @throws GitLabApiException if any exception occurs
     * @deprecated Will be removed in the next major release (6.0.0)
     */
    @Deprecated
    public List<Label> getLabels(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(jakarta.ws.rs.core.Response.Status.OK, getPageQueryParams(page, perPage),
                "projects", getProjectIdOrPath(projectIdOrPath), "labels");
        return (response.readEntity(new GenericType<List<Label>>() {}));
    }

    /**
     * Get a Pager of all labels of the specified project.
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of items per page
     * @return a list of project's labels in the specified range
     * @throws GitLabApiException if any exception occurs
     * @deprecated Replaced by the {@link #getProjectLabels(Object, int)} method.
     */
    @Deprecated
    public Pager<Label> getLabels(Object projectIdOrPath, int itemsPerPage) throws GitLabApiException {
        return (new Pager<Label>(this, Label.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "labels"));
    }

    /**
     * Get a Stream of all labels of the specified project.
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a Stream of project's labels
     * @throws GitLabApiException if any exception occurs
     * @deprecated Replaced by the {@link #getProjectLabelsStream(Object)} method.
     */
    @Deprecated
    public Stream<Label> getLabelsStream(Object projectIdOrPath) throws GitLabApiException {
        return (getLabels(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Create a label
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param name        the name for the label
     * @param color       the color for the label
     * @param description the description for the label
     * @return the created Label instance
     * @throws GitLabApiException if any exception occurs
     * @deprecated Replaced by the {@link #createProjectLabel(Object, Label)} method.
     */
    @Deprecated
    public Label createLabel(Object projectIdOrPath, String name, String color, String description) throws GitLabApiException {
        return (createLabel(projectIdOrPath, name, color, description, null));
    }

    /**
     * Create a label
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param name the name for the label
     * @param color the color for the label
     * @return the created Label instance
     * @throws GitLabApiException if any exception occurs
     * @deprecated Replaced by the {@link #createProjectLabel(Object, Label)} method.
     */
    @Deprecated
    public Label createLabel(Object projectIdOrPath, String name, String color) throws GitLabApiException {
        return (createLabel(projectIdOrPath, name, color, null, null));
    }

    /**
     * Create a label
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param name the name for the label
     * @param color the color for the label
     * @param priority the priority for the label
     * @return the created Label instance
     * @throws GitLabApiException if any exception occurs
     * @deprecated Replaced by the {@link #createProjectLabel(Object, Label)} method.
     */
    @Deprecated
    public Label createLabel(Object projectIdOrPath, String name, String color, Integer priority) throws GitLabApiException {
        return (createLabel(projectIdOrPath, name, color, null, priority));
    }

    /**
     *  Create a label
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param name the name for the label
     * @param color the color for the label
     * @param description the description for the label
     * @param priority the priority for the label
     * @return the created Label instance
     * @throws GitLabApiException if any exception occurs
     * @deprecated Replaced by the {@link #createProjectLabel(Object, Label)} method.
     */
    @Deprecated
    public Label createLabel(Object projectIdOrPath, String name, String color, String description, Integer priority) throws GitLabApiException {
        Label labelProperties = new Label()
                .withName(name)
                .withColor(color)
                .withDescription(description)
                .withPriority(priority);
        return (createProjectLabel(projectIdOrPath, labelProperties));
    }

    /**
     * Update the specified label
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param name the name for the label
     * @param newName the new name for the label
     * @param description the description for the label
     * @param priority the priority for the label
     * @return the modified Label instance
     * @throws GitLabApiException if any exception occurs
     * @deprecated @deprecated Replaced by the {@link #updateProjectLabel(Object, Object, Label)} method.
     */
    @Deprecated
    public Label updateLabelName(Object projectIdOrPath, String name, String newName, String description, Integer priority) throws GitLabApiException {
        return (updateLabel(projectIdOrPath, name, newName, null, description, priority));
    }

    /**
     * Update the specified label
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param name the name for the label
     * @param color the color for the label
     * @param description the description for the label
     * @param priority the priority for the label
     * @return the modified Label instance
     * @throws GitLabApiException if any exception occurs
     * @deprecated @deprecated Replaced by the {@link #updateProjectLabel(Object, Object, Label)} method.
     */
    @Deprecated
    public Label updateLabelColor(Object projectIdOrPath, String name, String color, String description, Integer priority) throws GitLabApiException {
        return (updateLabel(projectIdOrPath, name, null, color, description, priority));
    }

    /**
     * Update the specified label
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param name the name for the label
     * @param newName the new name for the label
     * @param color the color for the label
     * @param description the description for the label
     * @param priority the priority for the label
     * @return the modified Label instance
     * @throws GitLabApiException if any exception occurs
     * @deprecated @deprecated Replaced by the {@link #updateProjectLabel(Object, Object, Label)} method.
     */
    @Deprecated
    public Label updateLabel(Object projectIdOrPath, String name, String newName, String color, String description, Integer priority) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("name", name, true)
                .withParam("new_name", newName)
                .withParam("color", color)
                .withParam("description", description)
                .withParam("priority", priority);
        Response response = put(Response.Status.OK, formData.asMap(),
                "projects", getProjectIdOrPath(projectIdOrPath), "labels");
        return (response.readEntity(Label.class));
    }

    /**
     * Delete the specified label
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param name the name for the label
     * @throws GitLabApiException if any exception occurs
     * @deprecated Replaced by the {@link #deleteProjectLabel(Object, Object)} method.
     */
    @Deprecated
    public void deleteLabel(Object projectIdOrPath, String name) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("name", name, true);
        delete(Response.Status.OK, formData.asMap(), "projects", getProjectIdOrPath(projectIdOrPath), "labels");
    }

    /**
     * Subscribe a specified label
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param labelId the label ID
     * @return HttpStatusCode 503
     * @throws GitLabApiException if any exception occurs
     * @deprecated Replaced by the {@link #subscribeProjectLabel(Object, Object)} method.
     */
    @Deprecated
    public Label subscribeLabel(Object projectIdOrPath, Long labelId) throws GitLabApiException {
        return (subscribeProjectLabel(projectIdOrPath, labelId));
    }

    /**
     * Unsubscribe a specified label
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param labelId the label ID
     * @return HttpStatusCode 503
     * @throws GitLabApiException if any exception occurs
     * @deprecated Replaced by the {@link #unsubscribeProjectLabel(Object, Object)} method.
     */
    @Deprecated
    public Label unsubscribeLabel(Object projectIdOrPath, Long labelId) throws GitLabApiException {
        return (unsubscribeProjectLabel(projectIdOrPath, labelId));
    }
}
