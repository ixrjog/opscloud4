package org.gitlab4j.api.webhook;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public abstract class AbstractPushEvent {

    private String eventName;

    private String after;
    private String before;
    private String ref;
    private String checkoutSha;

    private Long userId;
    private String userName;
    private String userUsername;
    private String userEmail;
    private String userAvatar;

    private Long projectId;
    private EventProject project;
    private EventRepository repository;
    private List<EventCommit> commits;
    private Integer totalCommitsCount;

    private String requestUrl;
    private String requestQueryString;
    private String requestSecretToken;

    public String getEventName() {
        return (eventName);
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getCheckoutSha() {
        return checkoutSha;
    }

    public void setCheckoutSha(String checkoutSha) {
        this.checkoutSha = checkoutSha;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public EventProject getProject() {
        return project;
    }

    public void setProject(EventProject project) {
        this.project = project;
    }

    public EventRepository getRepository() {
        return repository;
    }

    public void setRepository(EventRepository repository) {
        this.repository = repository;
    }

    public List<EventCommit> getCommits() {
        return commits;
    }

    public void setCommits(List<EventCommit> commits) {
        this.commits = commits;
    }

    public Integer getTotalCommitsCount() {
        return totalCommitsCount;
    }

    public void setTotalCommitsCount(Integer totalCommitsCount) {
        this.totalCommitsCount = totalCommitsCount;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    @JsonIgnore
    public String getRequestUrl() {
        return (requestUrl);
    }

    public void setRequestQueryString(String requestQueryString) {
        this.requestQueryString = requestQueryString;
    }

    @JsonIgnore
    public String getRequestQueryString() {
        return (requestQueryString);
    }


    public void setRequestSecretToken(String secretToken) {
        this.requestSecretToken = secretToken;
    }

    @JsonIgnore
    public String getRequestSecretToken() {
        return (requestSecretToken);
    }

    /**
     * Gets the branch name from the ref. Will return null if the ref does not start with "refs/heads/".
     *
     * @return the branch name from the ref
     */
    @JsonIgnore
    public String getBranch() {

        String ref = getRef();
        if (ref == null || ref.trim().length() == 0) {
            return (null);
        }

        ref = ref.trim();
        int refsHeadsIndex = ref.indexOf(REFS_HEADS);
        if (refsHeadsIndex != 0) {
            return (null);
        }

        return (ref.substring(REFS_HEADS.length()));
    }

    private static final String REFS_HEADS = "refs/heads/";
}
