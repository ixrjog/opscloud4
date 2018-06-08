package com.sdg.cmdb.service;


import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.gitlab.GitlabWebHooksVO;

public interface GitlabService {

    BusinessWrapper<Boolean> webHooks(GitlabWebHooksVO webHooks);


    /**
     * tag 没有commit提交信息，无法获取email
     *
     * @param webHooks
     * @return
     */
    String acqEmailByWebHooks(GitlabWebHooksVO webHooks);
}
