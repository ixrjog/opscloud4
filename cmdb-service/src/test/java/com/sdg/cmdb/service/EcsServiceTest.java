package com.sdg.cmdb.service;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.domain.server.EcsServerDO;
import com.sdg.cmdb.domain.server.EcsServerVO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.service.impl.EcsServiceImpl;
import com.sdg.cmdb.util.TimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by liangjian on 2016/12/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class EcsServiceTest {

    @Resource
    private EcsService ecsService;

    @Resource
    private EcsServiceImpl ecsServiceImpl;

    @Test
    public void testEcsGetAll() {
        List<EcsServerDO> servers = ecsServiceImpl.ecsGetAll();
        for (EcsServerDO ecs : servers) {
            if (ecs.getInsideIp().equals("192.168.101.92"))
                System.err.println(ecs);
        }
    }

    @Test
    public void testEcsSyncByAliyun() {
       ecsServiceImpl.ecsSyncByAliyun();

    }



    @Resource
    private ServerDao serverDao;

    @Test
    public void testDescribeInstance() {
        DescribeInstancesResponse.Instance instance = ecsServiceImpl.describeInstance(EcsServiceImpl.regionIdCnHangzhou, "i-bp1hf2fotcwwj3lp7nir");
        System.err.println(JSON.toJSONString(instance));
    }

    @Test
    public void test() {
        List<EcsServerDO> servers = ecsServiceImpl.acqServerExclude();
        for (EcsServerDO ecs : servers) {
            System.err.println(ecs);
        }
    }

    @Test
    public void testStatistics() {
        System.err.println(ecsService.statistics());
    }

    @Test
    public void testEcsRefresh() {
        ecsService.ecsRefresh();
    }

    @Test
    public void testUpdateIp() {
        EcsServerDO ecsServerDO = serverDao.queryEcsByInsideIp("10.10.10.10");
        ServerDO serverDO = serverDao.queryServerByInsideIp("10.10.10.10");
        ecsServiceImpl.updateServerPublicIp(serverDO, ecsServerDO);
    }

    @Test
    public void testQueryDisks() {

        List<DescribeDisksResponse.Disk> disks = ecsService.queryDisks(null, "i-bp16", false);
        for (DescribeDisksResponse.Disk disk : disks) {
            System.err.println("DiskId:" + disk.getDiskId());
            System.err.println("Category:" + disk.getCategory());
            System.err.println("Type:" + disk.getType());
            System.err.println("Size:" + disk.getSize());
        }
    }

    @Test
    public void testCreateTime() {
        String str = "2017-04-20T06:21Z";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        try {
            Date date = format.parse(str);
            SimpleDateFormat fmt;
            fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String now = fmt.format(new Date());
            System.err.println(now);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试计算过期日期
     */
    @Test
    public void testExpiredDay() {
        // 2019-04-12 16:00:00
        EcsServerDO ecs = serverDao.queryEcsByInsideIp("192.168.10.74");
        System.err.println(ecs);
        try {
            long et = TimeUtils.dateToStamp(ecs.getExpiredTime());
            Date d = new Date();

            System.err.println(et - d.getTime());

            System.err.println((et - d.getTime()) / TimeUtils.dayTime);

        } catch (Exception e) {

        }

    }


    @Test
    public void testQuery() {
        //// TODO 查询ECS实例
        DescribeInstancesResponse.Instance i = ecsService.query(null, "i-bp1hf2fotcwwj3lp7nir");
        System.err.println(JSON.toJSONString(i).toString());
    }

    @Test
    public void testEcsGet() {
        System.err.println(ecsService.ecsGet(null, "i-bp16lhh4wo6rt3db54lm"));

    }

    @Test
    public void testEcsProperty() {
        EcsServerDO ecsServerDO = serverDao.queryEcsByInstanceId("i-bp1");

        EcsServerVO ecsServerVO = new EcsServerVO(ecsServerDO);

        ecsService.invokeEcsServerVO(ecsServerVO);

        System.err.println(ecsServerVO);
    }


    @Test
    public void testEcsSync() {

        // 测试同步阿里云
        // ecsService.ecsSync(0);
        //测试同步金融云
        ecsService.ecsSync(0);
    }


}
