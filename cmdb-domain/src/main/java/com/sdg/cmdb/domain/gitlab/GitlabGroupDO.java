package com.sdg.cmdb.domain.gitlab;


import lombok.Data;
import org.gitlab.api.models.GitlabGroup;

import java.io.Serializable;

@Data
public class GitlabGroupDO implements Serializable {
    private static final long serialVersionUID = -3130689329680922396L;

    private long id;
    private int groupId;
    private String name;
    private String description;
    private String visibility;
    private boolean lfsEnabled;
    private boolean requestAccessEnabled;
    private String webUrl;

    public GitlabGroupDO() {
    }

    public GitlabGroupDO(GitlabGroup gitlabGroup) {
        this.groupId = gitlabGroup.getId();
        this.name = gitlabGroup.getName();
        this.description = gitlabGroup.getDescription();
        this.visibility = gitlabGroup.getVisibility().toString();
        this.lfsEnabled = gitlabGroup.isLfsEnabled();
        this.requestAccessEnabled = gitlabGroup.isRequestAccessEnabled();
        this.webUrl = gitlabGroup.getWebUrl();
    }

    public GitlabGroupDO(long id, GitlabGroup gitlabGroup) {
        this.id = id;
        this.groupId = gitlabGroup.getId();
        this.name = gitlabGroup.getName();
        this.description = gitlabGroup.getDescription();
        this.visibility = gitlabGroup.getVisibility().toString();
        this.lfsEnabled = gitlabGroup.isLfsEnabled();
        this.requestAccessEnabled = gitlabGroup.isRequestAccessEnabled();
        this.webUrl = gitlabGroup.getWebUrl();
    }
}
