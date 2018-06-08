package com.sdg.cmdb.domain.gitlab;

import java.io.Serializable;
import java.util.List;

public class RefsVO implements Serializable {

    List<String> branches;

    List<String> tags;

    public RefsVO() {

    }

    public RefsVO(List<String> branches, List<String> tags) {
        this.branches = branches;
        this.tags = tags;
    }

    public List<String> getBranches() {
        return branches;
    }

    public void setBranches(List<String> branches) {
        this.branches = branches;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        String result;
        result = "RefsVO{" +
                "branches={";

        if (branches != null)
            for (String branche : branches) {
                result += branche + ",";
            }
        result += "} , tags={";
        if (tags != null)
            for (String tag : tags) {
                result += tag + ",";
            }

        result += "}}";
        return result;
    }
}
