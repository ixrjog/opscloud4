package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.todo.TodoConfigDO;
import com.sdg.cmdb.domain.todo.TodoDailyDO;
import com.sdg.cmdb.domain.todo.TodoDailyVO;
import com.sdg.cmdb.util.TimeUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by zxxiao on 2016/12/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class EmailServiceTest {

    @Resource
    private EmailService emailService;

    @Test
    public void testdoSendNewTodo() {
        UserDO sponsorUser = new UserDO();
        sponsorUser.setDisplayName("飞雪");

        UserDO userDO = new UserDO();
        userDO.setMail("xiaozh@net");
        userDO.setDisplayName("飞雪");

        TodoDailyDO dailyDO = new TodoDailyDO();
        TodoConfigDO levelOne = new TodoConfigDO();
        levelOne.setConfigName("测试一级类目");
        TodoConfigDO levelTwo = new TodoConfigDO();
        levelTwo.setConfigName("测试二级类目");
        TodoDailyVO dailyVO = new TodoDailyVO(dailyDO, levelOne, levelTwo);

        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

        dailyVO.setGmtCreate(dateFormat.format(new Date()));
        dailyVO.setGmtModify(dateFormat.format(new Date()));
        emailService.doSendUpdateTodo(userDO, dailyVO, sponsorUser);
    }

    @Test
    public void testdoSendNewTodo2() {
        UserDO sponsorUser = new UserDO();
        sponsorUser.setDisplayName("梁荐");

        UserDO userDO = new UserDO();
        userDO.setMail("@net");
        userDO.setDisplayName("梁荐");

        TodoDailyDO dailyDO = new TodoDailyDO();
        TodoConfigDO levelOne = new TodoConfigDO();
        levelOne.setConfigName("测试一级类目");
        TodoConfigDO levelTwo = new TodoConfigDO();
        levelTwo.setConfigName("测试二级类目");
        TodoDailyVO dailyVO = new TodoDailyVO(dailyDO, levelOne, levelTwo);
        emailService.doSendNewTodo(userDO, dailyVO, sponsorUser);
    }

}
