package com.baiyi.opscloud.dubbo.impl;

import com.baiyi.opscloud.dubbo.ZookeeperServer;
import com.baiyi.opscloud.dubbo.handler.ZookeeperHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/9 2:48 下午
 * @Version 1.0
 */
@Slf4j
@Component("ZookeeperServer")
public class ZookeeperServerImpl implements ZookeeperServer {

    @Resource
    private ZookeeperHandler zookeeperHandler;

    @Override
    public String getNodeData(String env, String path) {
        CuratorFramework client = zookeeperHandler.getClient(env);
        try {
            // 数据读取和转换
            byte[] dataByte = client.getData().forPath(path);
            String data = new String(dataByte, "UTF-8");
            closeClient(env);
            if (StringUtils.isEmpty(data)) {
                return data;
            }
        } catch (Exception e) {
            log.error("getNodeData error...", e.getMessage());
            e.printStackTrace();
            closeClient(env);
        }
        return Strings.EMPTY;
    }

    @Override
    public List<String> getNodeChild(String env, String path) {
        try {
            CuratorFramework client = zookeeperHandler.getClient(env);
            return client.getChildren().forPath(path);
        } catch (Exception e) {
            log.error("getNodeChild error...", e.getMessage());
            e.printStackTrace();
            closeClient(env);
        }
        return Collections.emptyList();
    }

    @Override
    public void closeClient(String env) {
        zookeeperHandler.closeClient(env);
    }

}
