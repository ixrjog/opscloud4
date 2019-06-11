package com.sdg.cmdb.domain.gitlab;

import com.sdg.cmdb.domain.auth.UserVO;
import lombok.Data;
import org.gitlab.api.models.GitlabBranch;
import org.gitlab.api.models.GitlabProjectMember;
import org.gitlab.api.models.GitlabTag;

import java.io.Serializable;
import java.util.List;

@Data
public class GitlabProjectVO extends GitlabProjectDO implements Serializable {
    private static final long serialVersionUID = 7684730167181501379L;

    private UserVO owner;

    List<GitlabTag> tagList;

    List<GitlabBranch> branchList;

    private List<GitlabProjectMember> memberList;

    public GitlabProjectVO(GitlabProjectDO gitlabProjectDO, UserVO owner, List<GitlabBranch> branchList, List<GitlabTag> tagList) {
        setId(gitlabProjectDO.getId());
        setProjectId(gitlabProjectDO.getProjectId());
        setName(gitlabProjectDO.getName());
        setNameWithNamespace(gitlabProjectDO.getNameWithNamespace());
        setDescription(gitlabProjectDO.getDescription());
        setDefaultBranch(gitlabProjectDO.getDefaultBranch());
        setOwnerId(gitlabProjectDO.getOwnerId());
        setOwnerUsername(gitlabProjectDO.getOwnerUsername());
        setSshUrl(gitlabProjectDO.getSshUrl());
        setWebUrl(gitlabProjectDO.getWebUrl());
        setHttpUrl(gitlabProjectDO.getHttpUrl());
        setCreateedAt(gitlabProjectDO.getCreateedAt());
        setLastActivityAt(gitlabProjectDO.getLastActivityAt());
        this.owner = owner;
        this.branchList =branchList;
        this.tagList =tagList;
    }

    public GitlabProjectVO() {
    }

}
