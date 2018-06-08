package com.sdg.cmdb.service.impl;


import com.sdg.cmdb.domain.gitlab.RefsVO;
import com.sdg.cmdb.service.GitService;
import com.sdg.cmdb.service.GitlabService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class GitServiceImplTest {


    @Resource
    private GitService gitService;

    @Resource
    private GitServiceImpl gitServiceImpl;


    @Test
    public void testString() {

        String s = "vuePro";

        System.err.println(s.toLowerCase());

    }

    @Test
    public void testRepoByGitlab() {
        //System.err.println(gitService.doScanRepo("http://gitlab.51xianqu.com/xq_ios/lianhua.git")) ;
        // git@gitlab.51xianqu.com:xq_ios/xq_jianhuo.git
        try {
            // /yhgj/Carthage/Build/Mac/RxSwift.framework/Versions/Current
            // gitServiceImpl.delDir(new File("/data/www/temp/xq_ios/yhgj"));
            //  gitServiceImpl.delDir(new File("/data/www/temp/xq_ios/yhgj"));

            gitService.getRefList(GitServiceImpl.GITLAB_REPOSITORY, "xq_ios", "lianhua");


        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.err.println(gitService.doScanRepo("git@gitlab.51xianqu.com:xq_ios/lianhua.git ")) ;
    }


    @Test
    public void testRepoByStash() {
        // http://liangjian@stash.51xianqu.net/scm/trade/origin.git
        //
        try {
          //  RefsVO refsVO = gitService.getRefList(GitServiceImpl.STASH_REPOSITORY, "real", "realtime-dataspout");
            RefsVO refsVO = gitService.getRefList(GitServiceImpl.STASH_REPOSITORY, "kaweb", "kaweb");
            System.err.println(refsVO);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
