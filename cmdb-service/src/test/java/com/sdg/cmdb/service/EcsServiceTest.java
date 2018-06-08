package com.sdg.cmdb.service;

import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.domain.server.EcsServerDO;
import com.sdg.cmdb.domain.server.EcsServerVO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.service.impl.EcsServiceImpl;
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


    @Resource
    private ServerDao serverDao;

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

        List<DescribeDisksResponse.Disk> disks = ecsService.queryDisks(null, "i-bp16",false);
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

    @Test
    public void testQuery() {
        //// TODO 查询ECS实例
        DescribeInstancesResponse.Instance i = ecsService.query(null, "i-bp1gu");


        System.err.println("NetworkType:" + i.getInstanceNetworkType());
        if (i.getInstanceNetworkType().equals("vpc")) {
            System.err.println("PrivateIpAddress:" + i.getVpcAttributes().getPrivateIpAddress().get(0));
        } else {
            System.err.println("InnerIpAddress:" + i.getInnerIpAddress().get(0));
        }
        if (i.getPublicIpAddress().size() != 0) {
            System.err.println(i.getPublicIpAddress().get(0));
        }
        System.err.println("SecurityGroupId:" + i.getSecurityGroupIds().get(0).toString());
        System.err.println("VSwitchId:" + i.getVpcAttributes().getVSwitchId());
        System.err.println("vpcId:" + i.getVpcAttributes().getVpcId());


        System.err.println(i.getStatus().getStringValue());
    }

    @Test
    public void testEcsGet() {
        System.err.println(ecsService.ecsGet(null, "i-bp1g"));

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
