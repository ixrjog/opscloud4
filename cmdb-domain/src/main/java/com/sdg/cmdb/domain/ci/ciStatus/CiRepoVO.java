package com.sdg.cmdb.domain.ci.ciStatus;

import java.io.Serializable;

public class CiRepoVO implements Serializable {
    private static final long serialVersionUID = -400835060094115602L;

    private String repo;

    private int cnt;

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
