package com.baiyi.opscloud.facade.gitlab.impl;

import com.baiyi.opscloud.caesar.CaesarGitlabAPI;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.param.gitlab.GitlabGroupParam;
import com.baiyi.opscloud.domain.vo.gitlab.GitlabGroupVO;
import com.baiyi.opscloud.domain.vo.gitlab.GitlabInstanceVO;
import com.baiyi.opscloud.facade.gitlab.GitlabFacade;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/2/23 2:02 下午
 * @Since 1.0
 */

@Component
public class GitlabFacadeImpl implements GitlabFacade {

    @Resource
    private CaesarGitlabAPI caesarGitlabAPI;

    @Override
    public DataTable<GitlabInstanceVO.Instance> queryGitlabInstance(String queryName) {
        try {
            return caesarGitlabAPI.queryGitlabInstance(queryName);
        } catch (IOException e) {
            return DataTable.EMPTY;
        }
    }

    @Override
    public DataTable<GitlabGroupVO.Group> queryGitlabGroupPage(GitlabGroupParam.GitlabGroupPageQuery query) {
        try {
            return caesarGitlabAPI.queryGitlabGroupPage(query);
        } catch (IOException e) {
            return DataTable.EMPTY;
        }
    }

    @Override
    public BusinessWrapper<Boolean> addGitlabGroupMember(GitlabGroupParam.AddMember addMember) {
        try {
            return caesarGitlabAPI.addGitlabGroupMember(addMember);
        } catch (IOException e) {
            return new BusinessWrapper<>(ErrorEnum.GITLAB_ADD_GROUP_MEMBER);
        }

    }
}
