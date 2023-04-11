package org.gitlab4j.api.models;

/**
 * This class models the object for a repository contributor.
 * See <a href="https://docs.gitlab.com/ee/api/repositories.html#contributors">Contributors at GitLab</a>.
 */
public class Contributor extends AbstractUser<Contributor> {

    private Integer commits;
    private Integer additions;
    private Integer deletions;

    public Integer getCommits() {
        return commits;
    }

    public void setCommits(Integer commits) {
        this.commits = commits;
    }

    public Integer getAdditions() {
        return additions;
    }

    public void setAdditions(Integer additions) {
        this.additions = additions;
    }

    public Integer getDeletions() {
        return deletions;
    }

    public void setDeletions(Integer deletions) {
        this.deletions = deletions;
    }
}