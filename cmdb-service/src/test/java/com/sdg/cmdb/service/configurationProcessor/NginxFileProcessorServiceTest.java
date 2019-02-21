package com.sdg.cmdb.service.configurationProcessor;


import com.sdg.cmdb.dao.cmdb.NginxDao;
<<<<<<< HEAD
import com.sdg.cmdb.domain.nginx.EnvFileDO;
import com.sdg.cmdb.domain.nginx.VhostDO;
import com.sdg.cmdb.domain.nginx.VhostEnvDO;
=======
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.nginx.EnvFileDO;
import com.sdg.cmdb.domain.nginx.VhostDO;
import com.sdg.cmdb.domain.nginx.VhostEnvDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
>>>>>>> develop
import com.sdg.cmdb.util.PathUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class NginxFileProcessorServiceTest {

<<<<<<< HEAD

=======
>>>>>>> develop
    @Resource
    private NginxFileProcessorService nginxFileProcessorService;

    @Resource
    private NginxDao nginxDao;

<<<<<<< HEAD
    @Test
    public void testPath() {
        System.err.println(PathUtils.getPath("/data/www/", "/conf/", "/aaa.conf"));

    }


    @Test
    public void test() {

        // VhostDO vhostDO, VhostEnvDO vhostEnvDO, EnvFileDO envFileDO
        // www.qianou.com
        VhostDO vhostDO = nginxDao.getVhost(1);

=======
    @Resource
    private ServerGroupDao serverGroupDao;

    @Test
    public void testPath() {
        System.err.println(PathUtils.getPath("/data/www/", "/conf/", "/aaa.conf"));
    }

    @Test
    public void testDelCache(){
      List<ServerGroupDO> groupList =  serverGroupDao.queryServerGroup();
      for(ServerGroupDO serverGroupDO:groupList)
          nginxFileProcessorService.delCache(serverGroupDO);
    }

    @Test
    public void testGetConfig() {
        VhostDO vhostDO = nginxDao.getVhost(25);
>>>>>>> develop
        List<VhostEnvDO> envs = nginxDao.queryVhostEnvByVhostId(vhostDO.getId());
        for (VhostEnvDO vhostEnvDO : envs) {
            List<EnvFileDO> files = nginxDao.queryEnvFileByEnvId(vhostEnvDO.getId());
            for(EnvFileDO file:files){
                System.err.println(nginxFileProcessorService.getFile(vhostDO,vhostEnvDO,file));
<<<<<<< HEAD

=======
>>>>>>> develop
                System.err.println("-------------------");
                System.err.println(nginxFileProcessorService.getFilePath(vhostEnvDO,file));
            }

<<<<<<< HEAD

        }


=======
        }
>>>>>>> develop
    }


}
