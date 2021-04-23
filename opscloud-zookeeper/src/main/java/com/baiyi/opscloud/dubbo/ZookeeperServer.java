package com.baiyi.opscloud.dubbo;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/9 2:48 下午
 * @Version 1.0
 */
public interface ZookeeperServer {

    String getNodeData(String env, String path);

    List<String> getNodeChild(String env, String path);

    void closeClient(String env);
}
