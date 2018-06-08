package com.sdg.cmdb.service;

import com.sdg.cmdb.dao.cmdb.ConfigDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.config.ConfigPropertyDO;
import com.sdg.cmdb.domain.config.ConfigPropertyGroupDO;
import com.sdg.cmdb.domain.config.ServerGroupPropertiesDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by zxxiao on 2016/10/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class PropertyServerGroupInitTest {

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private ConfigDao configDao;

    @Test
    public void addData() {

              }


    private void addConfigProprty(String groupName,String configProperty,String propertyValue){
        //String configPropertyGroupName="Tomcat" ;
        String configPropertyGroupName="InterfaceCI" ;

        if (propertyValue.equals("-")) {
            System.err.println(groupName+configProperty+"值为空");
            return;
        }
        System.err.println(groupName+configProperty+propertyValue);
        // group_trade
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName(groupName);
        if (serverGroupDO == null) System.err.println(groupName+"服务器组不存在");
        // Tomcat&JDK
        ConfigPropertyGroupDO propertyGroupDO = configDao.getConfigPropertyGroupByName(configPropertyGroupName);
        // tomcat.app.name
        ConfigPropertyDO propertyDO = configDao.getConfigPropertyByName(configProperty);
        //下面的代码,就是设置好各种值,然后存储数据库
        ServerGroupPropertiesDO groupPropertiesDO = new ServerGroupPropertiesDO();
        groupPropertiesDO.setPropertyGroupId(propertyGroupDO.getId());
        groupPropertiesDO.setGroupId(serverGroupDO.getId());
        groupPropertiesDO.setPropertyId(propertyDO.getId());
        groupPropertiesDO.setPropertyValue(propertyValue);
        configDao.addServerPropertyData(groupPropertiesDO);
    }


    private void addConfigProprty(String groupName,String configPropertyGroup,String configProperty,String propertyValue){
        // group_trade
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName(groupName);
        // Tomcat&JDK
        ConfigPropertyGroupDO propertyGroupDO = configDao.getConfigPropertyGroupByName(configPropertyGroup);
        // tomcat.app.name
        ConfigPropertyDO propertyDO = configDao.getConfigPropertyByName(configProperty);
        //下面的代码,就是设置好各种值,然后存储数据库
        ServerGroupPropertiesDO groupPropertiesDO = new ServerGroupPropertiesDO();
        groupPropertiesDO.setPropertyGroupId(propertyGroupDO.getId());
        groupPropertiesDO.setGroupId(serverGroupDO.getId());
        groupPropertiesDO.setPropertyId(propertyDO.getId());
        groupPropertiesDO.setPropertyValue(propertyValue);
        configDao.addServerPropertyData(groupPropertiesDO);

    }


}
