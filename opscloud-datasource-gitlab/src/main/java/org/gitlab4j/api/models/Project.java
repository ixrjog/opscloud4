
package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.gitlab4j.api.Constants.AutoDevopsDeployStrategy;
import org.gitlab4j.api.Constants.BuildGitStrategy;
import org.gitlab4j.api.Constants.SquashOption;
import org.gitlab4j.api.ProjectLicense;
import org.gitlab4j.api.models.ImportStatus.Status;
import org.gitlab4j.api.utils.JacksonJson;
import org.gitlab4j.api.utils.JacksonJsonEnumHelper;

import java.util.Date;
import java.util.List;

public class Project {

    // Enum for the merge_method of the Project instance.
    public enum MergeMethod {

        MERGE, REBASE_MERGE, FF;

        private static JacksonJsonEnumHelper<MergeMethod> enumHelper = new JacksonJsonEnumHelper<>(MergeMethod.class);

        @JsonCreator
        public static MergeMethod forValue(String value) {
            return enumHelper.forValue(value);
        }

        @JsonValue
        public String toValue() {
            return (enumHelper.toString(this));
        }

        @Override
        public String toString() {
            return (enumHelper.toString(this));
        }
    }

    private Integer approvalsBeforeMerge;
    private Boolean archived;
    private String avatarUrl;
    private Boolean containerRegistryEnabled;
    private Date createdAt;
    private Long creatorId;
    private String defaultBranch;
    private String description;
    private Integer forksCount;
    private Project forkedFromProject;
    private String httpUrlToRepo;
    private Long id;
    private Boolean isPublic;
    private Boolean issuesEnabled;
    private Boolean jobsEnabled;
    private Date lastActivityAt;
    private Boolean lfsEnabled;
    private MergeMethod mergeMethod;
    private Boolean mergeRequestsEnabled;
    private String name;
    private Namespace namespace;
    private String nameWithNamespace;
    private Boolean onlyAllowMergeIfPipelineSucceeds;
    private Boolean allowMergeOnSkippedPipeline;
    private Boolean onlyAllowMergeIfAllDiscussionsAreResolved;
    private Integer openIssuesCount;
    private Owner owner;
    private String path;
    private String pathWithNamespace;
    private Permissions permissions;
    private Boolean publicJobs;
    private String repositoryStorage;
    private Boolean requestAccessEnabled;
    private String runnersToken;
    private Boolean sharedRunnersEnabled;
    private List<ProjectSharedGroup> sharedWithGroups;
    private Boolean snippetsEnabled;
    private String sshUrlToRepo;
    private Integer starCount;
    private List<String> tagList;
    private Integer visibilityLevel;
    private Visibility visibility;
    private Boolean wallEnabled;
    private String webUrl;
    private Boolean wikiEnabled;
    private Boolean printingMergeRequestLinkEnabled;
    private Boolean resolveOutdatedDiffDiscussions;
    private ProjectStatistics statistics;
    private Boolean initializeWithReadme;
    private Boolean packagesEnabled;
    private Boolean emptyRepo;
    private String licenseUrl;
    private ProjectLicense license;
    private List<CustomAttribute> customAttributes;
    private String buildCoverageRegex;
    private BuildGitStrategy buildGitStrategy;
    private String readmeUrl;
    private Boolean canCreateMergeRequestIn;
    private Status importStatus;
    private Integer ciDefaultGitDepth;
    private Boolean ciForwardDeploymentEnabled;
    private String ciConfigPath;
    private Boolean removeSourceBranchAfterMerge;
    private Boolean autoDevopsEnabled;
    private AutoDevopsDeployStrategy autoDevopsDeployStrategy;
    private Boolean autocloseReferencedIssues;
    private Boolean emailsDisabled;
    private String suggestionCommitMessage;
    private SquashOption squashOption;

    @JsonSerialize(using = JacksonJson.DateOnlySerializer.class)
    private Date markedForDeletionOn;

    public Integer getApprovalsBeforeMerge() {
        return approvalsBeforeMerge;
    }

    public void setApprovalsBeforeMerge(Integer approvalsBeforeMerge) {
        this.approvalsBeforeMerge = approvalsBeforeMerge;
    }

    public Project withApprovalsBeforeMerge(Integer approvalsBeforeMerge) {
        this.approvalsBeforeMerge = approvalsBeforeMerge;
        return (this);
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Boolean getContainerRegistryEnabled() {
        return containerRegistryEnabled;
    }

    public void setContainerRegistryEnabled(Boolean containerRegistryEnabled) {
        this.containerRegistryEnabled = containerRegistryEnabled;
    }

    public Project withContainerRegistryEnabled(boolean containerRegistryEnabled) {
        this.containerRegistryEnabled = containerRegistryEnabled;
        return (this);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public Project withDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
        return (this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Project withDescription(String description) {
        this.description = description;
        return (this);
    }

    public Integer getForksCount() {
        return forksCount;
    }

    public void setForksCount(Integer forksCount) {
        this.forksCount = forksCount;
    }

    public Project getForkedFromProject() {
        return forkedFromProject;
    }

    public void setForkedFromProject(Project forkedFromProject) {
        this.forkedFromProject = forkedFromProject;
    }

    public String getHttpUrlToRepo() {
        return httpUrlToRepo;
    }

    public void setHttpUrlToRepo(String httpUrlToRepo) {
        this.httpUrlToRepo = httpUrlToRepo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project withId(Long id) {
        this.id = id;
        return (this);
    }

    public Boolean getIssuesEnabled() {
        return issuesEnabled;
    }

    public void setIssuesEnabled(Boolean issuesEnabled) {
        this.issuesEnabled = issuesEnabled;
    }

    public Project withIssuesEnabled(boolean issuesEnabled) {
        this.issuesEnabled = issuesEnabled;
        return (this);
    }

    public Boolean getJobsEnabled() {
        return jobsEnabled;
    }

    public void setJobsEnabled(Boolean jobsEnabled) {
        this.jobsEnabled = jobsEnabled;
    }

    public Project withJobsEnabled(boolean jobsEnabled) {
        this.jobsEnabled = jobsEnabled;
        return (this);
    }

    public Date getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(Date lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public Boolean getLfsEnabled() {
        return lfsEnabled;
    }

    public void setLfsEnabled(Boolean lfsEnabled) {
        this.lfsEnabled = lfsEnabled;
    }

    public Project withLfsEnabled(Boolean lfsEnabled) {
        this.lfsEnabled = lfsEnabled;
        return (this);
    }

    public MergeMethod getMergeMethod() {
        return mergeMethod;
    }

    public void setMergeMethod(MergeMethod mergeMethod) {
        this.mergeMethod = mergeMethod;
    }

    public Project withMergeMethod(MergeMethod mergeMethod) {
        this.mergeMethod = mergeMethod;
        return (this);
    }

    public Boolean getMergeRequestsEnabled() {
        return mergeRequestsEnabled;
    }

    public void setMergeRequestsEnabled(Boolean mergeRequestsEnabled) {
        this.mergeRequestsEnabled = mergeRequestsEnabled;
    }

    public Project withMergeRequestsEnabled(boolean mergeRequestsEnabled) {
        this.mergeRequestsEnabled = mergeRequestsEnabled;
        return (this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project withName(String name) {
        this.name = name;
        return (this);
    }

    public Namespace getNamespace() {
        return namespace;
    }

    public void setNamespace(Namespace namespace) {
        this.namespace = namespace;
    }

    public Project withNamespace(Namespace namespace) {
        this.namespace = namespace;
        return (this);
    }

    public Project withNamespaceId(long namespaceId) {
        this.namespace = new Namespace();
        this.namespace.setId(namespaceId);
        return (this);
    }

    public String getNameWithNamespace() {
        return nameWithNamespace;
    }

    public void setNameWithNamespace(String nameWithNamespace) {
        this.nameWithNamespace = nameWithNamespace;
    }

    public Boolean getOnlyAllowMergeIfPipelineSucceeds() {
        return onlyAllowMergeIfPipelineSucceeds;
    }

    public void setOnlyAllowMergeIfPipelineSucceeds(Boolean onlyAllowMergeIfPipelineSucceeds) {
        this.onlyAllowMergeIfPipelineSucceeds = onlyAllowMergeIfPipelineSucceeds;
    }

    public Project withOnlyAllowMergeIfPipelineSucceeds(Boolean onlyAllowMergeIfPipelineSucceeds) {
        this.onlyAllowMergeIfPipelineSucceeds = onlyAllowMergeIfPipelineSucceeds;
        return (this);
    }

    public Boolean getAllowMergeOnSkippedPipeline() {
        return allowMergeOnSkippedPipeline;
    }

    public void setAllowMergeOnSkippedPipeline(Boolean allowMergeOnSkippedPipeline) {
        this.allowMergeOnSkippedPipeline = allowMergeOnSkippedPipeline;
    }

    public Project withAllowMergeOnSkippedPipeline(Boolean allowMergeOnSkippedPipeline) {
        this.allowMergeOnSkippedPipeline = allowMergeOnSkippedPipeline;
        return (this);
    }

    public Boolean getOnlyAllowMergeIfAllDiscussionsAreResolved() {
        return onlyAllowMergeIfAllDiscussionsAreResolved;
    }

    public void setOnlyAllowMergeIfAllDiscussionsAreResolved(Boolean onlyAllowMergeIfAllDiscussionsAreResolved) {
        this.onlyAllowMergeIfAllDiscussionsAreResolved = onlyAllowMergeIfAllDiscussionsAreResolved;
    }

    public Project withOnlyAllowMergeIfAllDiscussionsAreResolved(Boolean onlyAllowMergeIfAllDiscussionsAreResolved) {
        this.onlyAllowMergeIfAllDiscussionsAreResolved = onlyAllowMergeIfAllDiscussionsAreResolved;
        return (this);
    }

    public Integer getOpenIssuesCount() {
        return openIssuesCount;
    }

    public void setOpenIssuesCount(Integer openIssuesCount) {
        this.openIssuesCount = openIssuesCount;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Project withPath(String path) {
        this.path = path;
        return (this);
    }

    public String getPathWithNamespace() {
        return pathWithNamespace;
    }

    public void setPathWithNamespace(String pathWithNamespace) {
        this.pathWithNamespace = pathWithNamespace;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Project withPublic(Boolean isPublic) {
        this.isPublic = isPublic;
        return (this);
    }

    public Boolean getPublicJobs() {
        return publicJobs;
    }

    public void setPublicJobs(Boolean publicJobs) {
        this.publicJobs = publicJobs;
    }

    public Project withPublicJobs(boolean publicJobs) {
        this.publicJobs = publicJobs;
        return (this);
    }

    public String getRepositoryStorage() {
        return repositoryStorage;
    }

    public void setRepositoryStorage(String repositoryStorage) {
        this.repositoryStorage = repositoryStorage;
    }

    public Project withRepositoryStorage(String repositoryStorage) {
        this.repositoryStorage = repositoryStorage;
        return (this);
    }

    public Boolean getRequestAccessEnabled() {
        return requestAccessEnabled;
    }

    public void setRequestAccessEnabled(Boolean request_access_enabled) {
        this.requestAccessEnabled = request_access_enabled;
    }

    public Project withRequestAccessEnabled(boolean requestAccessEnabled) {
        this.requestAccessEnabled = requestAccessEnabled;
        return (this);
    }

    public String getRunnersToken() {
        return runnersToken;
    }

    public void setRunnersToken(String runnersToken) {
        this.runnersToken = runnersToken;
    }

    public Boolean getSharedRunnersEnabled() {
        return sharedRunnersEnabled;
    }

    public void setSharedRunnersEnabled(Boolean sharedRunnersEnabled) {
        this.sharedRunnersEnabled = sharedRunnersEnabled;
    }

    public List<ProjectSharedGroup> getSharedWithGroups() {
        return sharedWithGroups;
    }

    public void setSharedWithGroups(List<ProjectSharedGroup> sharedWithGroups) {
        this.sharedWithGroups = sharedWithGroups;
    }

    public Project withSharedRunnersEnabled(boolean sharedRunnersEnabled) {
        this.sharedRunnersEnabled = sharedRunnersEnabled;
        return (this);
    }

    public Boolean getSnippetsEnabled() {
        return snippetsEnabled;
    }

    public void setSnippetsEnabled(Boolean snippetsEnabled) {
        this.snippetsEnabled = snippetsEnabled;
    }

    public Project withSnippetsEnabled(boolean snippetsEnabled) {
        this.snippetsEnabled = snippetsEnabled;
        return (this);
    }

    public String getSshUrlToRepo() {
        return sshUrlToRepo;
    }

    public void setSshUrlToRepo(String sshUrlToRepo) {
        this.sshUrlToRepo = sshUrlToRepo;
    }

    public Integer getStarCount() {
        return starCount;
    }

    public void setStarCount(Integer starCount) {
        this.starCount = starCount;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public Project withTagList(List<String> tagList) {
        this.tagList = tagList;
        return (this);
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Project withVisibility(Visibility visibility) {
        this.visibility = visibility;
        return (this);
    }

    public Integer getVisibilityLevel() {
        return visibilityLevel;
    }

    public void setVisibilityLevel(Integer visibilityLevel) {
        this.visibilityLevel = visibilityLevel;
    }

    public Project withVisibilityLevel(Integer visibilityLevel) {
        this.visibilityLevel = visibilityLevel;
        return (this);
    }

    public Boolean getWallEnabled() {
        return wallEnabled;
    }

    public void setWallEnabled(Boolean wallEnabled) {
        this.wallEnabled = wallEnabled;
    }

    public Project withWallEnabled(Boolean wallEnabled) {
        this.wallEnabled = wallEnabled;
        return (this);
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public Project withWebUrl(String webUrl) {
        this.webUrl = webUrl;
        return (this);
    }

    public Boolean getWikiEnabled() {
        return wikiEnabled;
    }

    public void setWikiEnabled(Boolean wikiEnabled) {
        this.wikiEnabled = wikiEnabled;
    }

    public Project withWikiEnabled(boolean wikiEnabled) {
        this.wikiEnabled = wikiEnabled;
        return (this);
    }

    public Boolean getPrintingMergeRequestLinkEnabled() {
        return printingMergeRequestLinkEnabled;
    }

    public void setPrintingMergeRequestLinkEnabled(Boolean printingMergeRequestLinkEnabled) {
        this.printingMergeRequestLinkEnabled = printingMergeRequestLinkEnabled;
    }

    public Project withPrintingMergeRequestLinkEnabled(Boolean printingMergeRequestLinkEnabled) {
        this.printingMergeRequestLinkEnabled = printingMergeRequestLinkEnabled;
        return (this);
    }

    public Boolean getResolveOutdatedDiffDiscussions() {
        return resolveOutdatedDiffDiscussions;
    }

    public void setResolveOutdatedDiffDiscussions(Boolean resolveOutdatedDiffDiscussions) {
        this.resolveOutdatedDiffDiscussions = resolveOutdatedDiffDiscussions;
    }

    public Project withResolveOutdatedDiffDiscussions(boolean resolveOutdatedDiffDiscussions) {
        this.resolveOutdatedDiffDiscussions = resolveOutdatedDiffDiscussions;
        return (this);
    }

    public Boolean getInitializeWithReadme() {
        return initializeWithReadme;
    }

    public void setInitializeWithReadme(Boolean initializeWithReadme) {
        this.initializeWithReadme = initializeWithReadme;
    }

    public Project withInitializeWithReadme(boolean initializeWithReadme) {
        this.initializeWithReadme = initializeWithReadme;
        return (this);
    }

    public Boolean getPackagesEnabled() {
        return packagesEnabled;
    }

    public void setPackagesEnabled(Boolean packagesEnabled) {
        this.packagesEnabled = packagesEnabled;
    }

    public Project withPackagesEnabled(Boolean packagesEnabled) {
        this.packagesEnabled = packagesEnabled;
        return (this);
    }

    public ProjectStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(ProjectStatistics statistics) {
        this.statistics = statistics;
    }

    public Boolean getEmptyRepo() {
        return emptyRepo;
    }

    public void setEmptyRepo(Boolean emptyRepo) {
        this.emptyRepo = emptyRepo;
    }

    public Date getMarkedForDeletionOn() {
        return markedForDeletionOn;
    }

    public void setMarkedForDeletionOn(Date markedForDeletionOn) {
        this.markedForDeletionOn = markedForDeletionOn;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public ProjectLicense getLicense() {
        return license;
    }

    public void setLicense(ProjectLicense license) {
        this.license = license;
    }

    public List<CustomAttribute> getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(List<CustomAttribute> customAttributes) {
        this.customAttributes = customAttributes;
    }

    public static final boolean isValid(Project project) {
        return (project != null && project.getId() != null);
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }

    /**
     * Formats a fully qualified project path based on the provided namespace and project path.
     *
     * @param namespace the namespace, either a user name or group name
     * @param path the project path
     * @return a fully qualified project path based on the provided namespace and project path
     */
    public static final String getPathWithNammespace(String namespace, String path) {
        return (namespace.trim() + "/" + path.trim());
    }

    public String getBuildCoverageRegex() {
        return buildCoverageRegex;
    }

    public void setBuildCoverageRegex(String buildCoverageRegex) {
        this.buildCoverageRegex = buildCoverageRegex;
    }

    public Project withBuildCoverageRegex(String buildCoverageRegex) {
        this.buildCoverageRegex = buildCoverageRegex;
        return this;
    }

    public BuildGitStrategy getBuildGitStrategy() {
        return buildGitStrategy;
    }

    public void setBuildGitStrategy(BuildGitStrategy buildGitStrategy) {
        this.buildGitStrategy = buildGitStrategy;
    }

    public Project withBuildGitStrategy(BuildGitStrategy buildGitStrategy) {
        this.buildGitStrategy = buildGitStrategy;
        return this;
    }

    public String getReadmeUrl() {
        return readmeUrl;
    }

    public void setReadmeUrl(String readmeUrl) {
        this.readmeUrl = readmeUrl;
    }

    public Boolean getCanCreateMergeRequestIn() {
        return canCreateMergeRequestIn;
    }

    public void setCanCreateMergeRequestIn(Boolean canCreateMergeRequestIn) {
        this.canCreateMergeRequestIn = canCreateMergeRequestIn;
    }

    public Status getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(Status importStatus) {
        this.importStatus = importStatus;
    }

    public Integer getCiDefaultGitDepth() {
        return ciDefaultGitDepth;
    }

    public void setCiDefaultGitDepth(Integer ciDefaultGitDepth) {
        this.ciDefaultGitDepth = ciDefaultGitDepth;
    }

    public Boolean getCiForwardDeploymentEnabled() {
        return ciForwardDeploymentEnabled;
    }

    public void setCiForwardDeploymentEnabled(Boolean ciForwardDeploymentEnabled) {
        this.ciForwardDeploymentEnabled = ciForwardDeploymentEnabled;
    }

    public String getCiConfigPath() {
        return ciConfigPath;
    }

    public void setCiConfigPath(String ciConfigPath) {
        this.ciConfigPath = ciConfigPath;
    }

    public Boolean getRemoveSourceBranchAfterMerge() {
        return removeSourceBranchAfterMerge;
    }

    public void setRemoveSourceBranchAfterMerge(Boolean removeSourceBranchAfterMerge) {
        this.removeSourceBranchAfterMerge = removeSourceBranchAfterMerge;
    }

    public Project withRemoveSourceBranchAfterMerge(Boolean removeSourceBranchAfterMerge) {
        this.removeSourceBranchAfterMerge = removeSourceBranchAfterMerge;
        return this;
    }

    public Boolean getAutoDevopsEnabled() {
        return autoDevopsEnabled;
    }

    public void setAutoDevopsEnabled(Boolean autoDevopsEnabled) {
        this.autoDevopsEnabled = autoDevopsEnabled;
    }

    public AutoDevopsDeployStrategy getAutoDevopsDeployStrategy() {
        return autoDevopsDeployStrategy;
    }

    public void setAutoDevopsDeployStrategy(AutoDevopsDeployStrategy autoDevopsDeployStrategy) {
        this.autoDevopsDeployStrategy = autoDevopsDeployStrategy;
    }

    public Boolean getAutocloseReferencedIssues() {
        return autocloseReferencedIssues;
    }

    public void setAutocloseReferencedIssues(Boolean autocloseReferencedIssues) {
        this.autocloseReferencedIssues = autocloseReferencedIssues;
    }

    public Boolean getEmailsDisabled() {
        return emailsDisabled;
    }

    public void setEmailsDisabled(Boolean emailsDisabled) {
        this.emailsDisabled = emailsDisabled;
    }

    public Project withEmailsDisabled(Boolean emailsDisabled) {
        this.emailsDisabled = emailsDisabled;
        return this;
    }

    public String getSuggestionCommitMessage() {
        return this.suggestionCommitMessage;
    }

    public Project withSuggestionCommitMessage(String suggestionCommitMessage) {
        this.suggestionCommitMessage = suggestionCommitMessage;
        return this;
    }

    public void setSuggestionCommitMessage(String suggestionCommitMessage) {
        this.suggestionCommitMessage = suggestionCommitMessage;
    }

    public SquashOption getSquashOption() {
        return squashOption;
    }

    public void setSquashOption(SquashOption squashOption) {
        this.squashOption = squashOption;
    }

    public Project withSquashOption(SquashOption squashOption) {
        this.squashOption = squashOption;
        return this;
    }
}
