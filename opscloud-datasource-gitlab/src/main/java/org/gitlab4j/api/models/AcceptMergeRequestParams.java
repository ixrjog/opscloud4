package org.gitlab4j.api.models;

import org.gitlab4j.api.GitLabApiForm;

public class AcceptMergeRequestParams {

    private String mergeCommitMessage;
    private Boolean mergeWhenPipelineSucceeds;
    private String sha;
    private Boolean shouldRemoveSourceBranch;
    private Boolean squash;
    private String squashCommitMessage;

    /**
     * Custom merge commit message.
     *
     * @param mergeCommitMessage Custom merge commit message
     * @return The reference to this AcceptMergeRequestParams instance.
     */
    public AcceptMergeRequestParams withMergeCommitMessage(String mergeCommitMessage) {
        this.mergeCommitMessage = mergeCommitMessage;
        return this;
    }

    /**
     * If {@code true} the MR is merged when the pipeline succeeds.
     *
     * @param mergeWhenPipelineSucceeds If {@code true} the MR is merged when the pipeline succeeds.
     * @return The reference to this AcceptMergeRequestParams instance.
     */
    public AcceptMergeRequestParams withMergeWhenPipelineSucceeds(Boolean mergeWhenPipelineSucceeds) {
        this.mergeWhenPipelineSucceeds = mergeWhenPipelineSucceeds;
        return this;
    }

    /**
     * If present, then this SHA must match the HEAD of the source branch, otherwise the merge will fail.
     *
     * @param sha If present, then this SHA must match the HEAD of the source branch, otherwise the merge will fail.
     * @return The reference to this AcceptMergeRequestParams instance.
     */
    public AcceptMergeRequestParams withSha(String sha) {
        this.sha = sha;
        return this;
    }

    /**
     * If {@code true} removes the source branch.
     *
     * @param shouldRemoveSourceBranch If {@code true} removes the source branch.
     * @return The reference to this AcceptMergeRequestParams instance.
     */
    public AcceptMergeRequestParams withShouldRemoveSourceBranch(Boolean shouldRemoveSourceBranch) {
        this.shouldRemoveSourceBranch = shouldRemoveSourceBranch;
        return this;
    }

    /**
     * If {@code true} the commits will be squashed into a single commit on merge.
     *
     * @param squash If {@code true} the commits will be squashed into a single commit on merge.
     * @return The reference to this AcceptMergeRequestParams instance.
     */
    public AcceptMergeRequestParams withSquash(Boolean squash) {
        this.squash = squash;
        return this;
    }

    /**
     * Custom squash commit message.
     *
     * @param squashCommitMessage Custom squash commit message.
     * @return The reference to this AcceptMergeRequestParams instance.
     */
    public AcceptMergeRequestParams withSquashCommitMessage(String squashCommitMessage) {
        this.squashCommitMessage = squashCommitMessage;
        return this;
    }

    /**
     * Get the form params specified by this instance.
     *
     * @return a GitLabApiForm instance holding the form parameters for this AcceptMergeRequestParams instance
     */
    public GitLabApiForm getForm() {
        return new GitLabApiForm()
                .withParam("merge_commit_message", mergeCommitMessage)
                .withParam("merge_when_pipeline_succeeds", mergeWhenPipelineSucceeds)
                .withParam("sha", sha)
                .withParam("should_remove_source_branch", shouldRemoveSourceBranch)
                .withParam("squash", squash)
                .withParam("squash_commit_message", squashCommitMessage);
    }
}
