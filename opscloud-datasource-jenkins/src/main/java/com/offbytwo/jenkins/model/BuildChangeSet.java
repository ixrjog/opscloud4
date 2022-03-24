/**
 *
 */
package com.offbytwo.jenkins.model;

import java.util.List;

/**
 * Build ChangeSet.
 *
 * @author Karl Heinz Marbaise
 *
 */
public class BuildChangeSet {
    private List<BuildChangeSetItem> items;

    /**
     * The SCM system by which this changeset is being built. {@code git},
     * {@code svn} etc.
     */
    // TODO: Think about it if its possible to use an enum type
    private String kind;

    /**
     * @param items {@link BuildChangeSet}
     */
    public BuildChangeSet setItems(List<BuildChangeSetItem> items) {
        this.items = items;
        return this;
    }

    /**
     * @return list of {@link BuildChangeSet}
     */
    public List<BuildChangeSetItem> getItems() {
        return items;
    }

    /**
     * @return the kind
     */
    public String getKind() {
        return kind;
    }

    /**
     * @param kind the kind of (usually svn, git).
     */
    public BuildChangeSet setKind(String kind) {
        this.kind = kind;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((items == null) ? 0 : items.hashCode());
        result = prime * result + ((kind == null) ? 0 : kind.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BuildChangeSet other = (BuildChangeSet) obj;
        if (items == null) {
            if (other.items != null)
                return false;
        } else if (!items.equals(other.items))
            return false;
        if (kind == null) {
            if (other.kind != null)
                return false;
        } else if (!kind.equals(other.kind))
            return false;
        return true;
    }

}
