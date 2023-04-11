
package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.List;

public class CompareResults {

    private Commit commit;
    private List<Commit> commits;;
    private List<Diff> diffs;
    private Boolean compareTimeout;
    private Boolean compareSameRef;

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }

    public List<Diff> getDiffs() {
        return diffs;
    }

    public void setDiffs(List<Diff> diffs) {
        this.diffs = diffs;
    }

    public Boolean getCompareTimeout() {
        return compareTimeout;
    }

    public void setCompareTimeout(Boolean compareTimeout) {
        this.compareTimeout = compareTimeout;
    }

    public Boolean getCompareSameRef() {
        return compareSameRef;
    }

    public void setCompareSameRef(Boolean compareSameRef) {
        this.compareSameRef = compareSameRef;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
