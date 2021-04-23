package com.baiyi.opscloud.facade.gitlab;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.gitlab.GitlabGroupParam;
import com.baiyi.opscloud.domain.vo.gitlab.GitlabGroupVO;
import com.baiyi.opscloud.domain.vo.gitlab.GitlabInstanceVO;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/2/23 2:02 下午
 * @Since 1.0
 */
public interface GitlabFacade {

    DataTable<GitlabInstanceVO.Instance> queryGitlabInstance(String queryName);

    DataTable<GitlabGroupVO.Group> queryGitlabGroupPage(GitlabGroupParam.GitlabGroupPageQuery query);

    BusinessWrapper<Boolean> addGitlabGroupMember(GitlabGroupParam.AddMember addMember);
}
