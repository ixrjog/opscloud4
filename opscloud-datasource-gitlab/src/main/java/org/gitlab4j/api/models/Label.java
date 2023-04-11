package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gitlab4j.api.GitLabApiForm;
import org.gitlab4j.api.utils.JacksonJson;

public class Label {

    private Long id;
    private String name;
    private String color;
    private String description;
    private Integer openIssuesCount;
    private Integer closedIssuesCount;
    private Integer openMergeRequestsCount;
    private Boolean subscribed;
    private Integer priority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Label withName(String name) {
        this.name = name;
        return (this);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Label withColor(String color) {
        this.color = color;
        return (this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Label withDescription(String description) {
        this.description = description;
        return (this);
    }

    public Integer getOpenIssuesCount() {
        return openIssuesCount;
    }

    public void setOpenIssuesCount(Integer openIssuesCount) {
        this.openIssuesCount = openIssuesCount;
    }

    public Integer getClosedIssuesCount() {
        return closedIssuesCount;
    }

    public void setClosedIssuesCount(Integer closedIssuesCount) {
        this.closedIssuesCount = closedIssuesCount;
    }

    public Integer getOpenMergeRequestsCount() {
        return openMergeRequestsCount;
    }

    public void setOpenMergeRequestsCount(Integer openMergeRequestsCount) {
        this.openMergeRequestsCount = openMergeRequestsCount;
    }

    public Boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Label withPriority(Integer priority) {
        this.priority = priority;
        return (this);
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }

    /**
     * Get the form params specified by this instance.
     *
     * @param isCreate set to true if the params are for a create label call, false for an update
     * @return a GitLabApiForm instance holding the form parameters for this LabelParams instance
     */
    @JsonIgnore
    public GitLabApiForm getForm(boolean isCreate) {
	GitLabApiForm form = new GitLabApiForm()
            .withParam("description", description)
            .withParam("color", color, isCreate)
            .withParam("priority", priority);

	if (isCreate) {
	    form.withParam("name", name, true);
	} else {
	    form.withParam("new_name", name);
	}

	return (form);
    }
}
