package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.auth.UserDO;
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


    @Test
    public void test() {
        List<UserDO> userDOList = userDao.getAllUser();

        for (UserDO u : userDOList) {
            System.err.println(u.getDisplayName() + ":" + u.getPwd() + ":" + u.getId());


        }


    }


}



