package com.sdg.cmdb.domain.ci.jenkins;

import java.io.Serializable;

public class Scm implements Serializable {
    private static final long serialVersionUID = -6708672705134388489L;

    private String url;

    private String branch;

    private String commit;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

}
