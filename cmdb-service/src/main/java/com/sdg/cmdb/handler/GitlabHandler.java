package com.sdg.cmdb.handler;

import com.sdg.cmdb.plugin.gitlab.GitlabFactory;
import com.sdg.cmdb.plugin.ldap.LDAPFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GitlabHandler implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(GitlabHandler.class);

    @Resource
    private GitlabFactory gitlabFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        initGitlabInstance();
    }

    /**
     * 初始化ldap实例
     */
    private void initGitlabInstance() {

         try{
             gitlabFactory.buildApi();
         }catch (Exception e){
             e.printStackTrace();
         }

    }



}
