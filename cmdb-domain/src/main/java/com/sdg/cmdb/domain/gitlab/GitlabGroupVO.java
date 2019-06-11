package com.sdg.cmdb.domain.gitlab;

import lombok.Data;
import org.gitlab.api.models.GitlabGroupMember;
import org.gitlab.api.models.GitlabProject;

import java.io.Serializable;
import java.util.List;

@Data
public class GitlabGroupVO extends GitlabGroupDO implements Serializable {
    private static final long serialVersionUID = 9127786862690772737L;

    private List<GitlabGroupMember> memberList;
    private List<GitlabProject> projectList;


}
