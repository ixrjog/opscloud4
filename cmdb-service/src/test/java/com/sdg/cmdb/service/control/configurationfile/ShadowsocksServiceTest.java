package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.ss.SsVO;
import com.sdg.cmdb.service.configurationProcessor.ShadowsocksFileProcessorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by liangjian on 2016/12/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ShadowsocksServiceTest {
    @Resource
    protected UserDao userDao;


    @Resource
    protected ShadowsocksFileProcessorService sss;


    @Test
    public void test() {
        UserDO userDO = userDao.getUserByName("baiyi");
        List<SsVO> ss= sss.getSsByUser(userDO);
        for(SsVO s:ss){
            System.err.println(s);
        }

    }



}



