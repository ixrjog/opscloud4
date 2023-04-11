
package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

public class Branch {

    private Commit commit;
    private Boolean developersCanMerge;
    private Boolean developersCanPush;
    private Boolean merged;
    private String name;
    private Boolean isProtected;
    private Boolean isDefault;
    private Boolean canPush;
    private String webUrl;

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public Boolean getDevelopersCanMerge() {
        return developersCanMerge;
    }

    public void setDevelopersCanMerge(Boolean developersCanMerge) {
        this.developersCanMerge = developersCanMerge;
    }

    public Boolean getDevelopersCanPush() {
        return developersCanPush;
    }

    public void setDevelopersCanPush(Boolean developersCanPush) {
        this.developersCanPush = developersCanPush;
    }

    public Boolean getMerged() {
        return merged;
    }

    public void setMerged(Boolean merged) {
        this.merged = merged;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getProtected() {
        return isProtected;
    }

    public void setProtected(Boolean isProtected) {
        this.isProtected = isProtected;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getCanPush() {
        return canPush;
    }

    public void setCanPush(Boolean canPush) {
        this.canPush = canPush;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public static final boolean isValid(Branch branch) {
        return (branch != null && branch.getName() != null);
    }


    public Branch withCommit(Commit commit) {
        this.commit = commit;
        return this;
    }

    public Branch withDevelopersCanMerge(Boolean developersCanMerge) {
        this.developersCanMerge = developersCanMerge;
        return this;
    }

    public Branch withDevelopersCanPush(Boolean developersCanPush) {
        this.developersCanPush = developersCanPush;
        return this;
    }

    /**
     * Set the merged attribute
     * @param merged
     * @deprecated Use {@link #withMerged(Boolean)} instead
     * @return Current branch instance
     */
    @Deprecated
    public Branch withDerged(Boolean merged) {
        this.merged = merged;
        return this;
    }

    public Branch withMerged(Boolean merged) {
        this.merged = merged;
        return this;
    }

    public Branch withName(String name) {
        this.name = name;
        return this;
    }

    public Branch withIsProtected(Boolean isProtected) {
        this.isProtected = isProtected;
        return this;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
