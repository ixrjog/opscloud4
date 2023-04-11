package org.gitlab4j.api.services;

import org.gitlab4j.api.GitLabApiForm;

public class BugzillaService extends NotificationService {

    /**
     * Get the form data for this service based on it's properties.
     *
     * @return the form data for this service based on it's properties
     */
    @Override
    public GitLabApiForm servicePropertiesForm() {
        GitLabApiForm formData = new GitLabApiForm()
            .withParam(DESCRIPTION_PROP, getDescription())
            .withParam(ISSUES_URL_PROP, getIssuesUrl(), true)
            .withParam(NEW_ISSUE_URL_PROP, getNewIssueUrl(), true)
            .withParam(PROJECT_URL_PROP, getProjectUrl(), true)
            .withParam(PUSH_EVENTS_PROP, getPushEvents())
            .withParam(TITLE_PROP, getTitle());
        return formData;
    }

    public String getNewIssueUrl() {
        return this.getProperty(NEW_ISSUE_URL_PROP);
    }

    public void setNewIssueUrl(String endpoint) {
        this.setProperty(NEW_ISSUE_URL_PROP, endpoint);
    }

    public BugzillaService withNewIssueUrl(String endpoint) {
        setNewIssueUrl(endpoint);
        return this;
    }

    public String getIssuesUrl() {
        return this.getProperty(ISSUES_URL_PROP);
    }

    public void setIssuesUrl(String endpoint) {
        this.setProperty(ISSUES_URL_PROP, endpoint);
    }

    public BugzillaService withIssuesUrl(String endpoint) {
        setIssuesUrl(endpoint);
        return this;
    }

    public String getProjectUrl() {
        return this.getProperty(PROJECT_URL_PROP);
    }

    public void setProjectUrl(String endpoint) {
        this.setProperty(PROJECT_URL_PROP, endpoint);
    }

    public BugzillaService withProjectUrl(String endpoint) {
        setProjectUrl(endpoint);
        return this;
    }

    public String getDescription() {
        return this.getProperty(DESCRIPTION_PROP);
    }

    public void setDescription(String description) {
        this.setProperty(DESCRIPTION_PROP, description);
    }

    public BugzillaService withDescription(String description) {
        setDescription(description);
        return this;
    }
}
