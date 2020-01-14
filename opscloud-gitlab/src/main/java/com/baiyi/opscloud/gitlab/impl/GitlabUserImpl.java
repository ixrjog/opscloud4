package com.baiyi.opscloud.gitlab.impl;

import com.baiyi.opscloud.gitlab.IGitlabUser;
import com.baiyi.opscloud.gitlab.factory.GitlabFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/14 4:22 下午
 * @Version 1.0
 */
@Component("GitlabUser")
public class GitlabUserImpl implements IGitlabUser {

    @Resource
    private GitlabFactory gitlabFactory;

    public static final String BRANCH_REF = "refs/heads/";

}
