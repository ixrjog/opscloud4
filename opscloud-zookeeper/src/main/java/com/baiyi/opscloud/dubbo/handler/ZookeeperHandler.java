package com.baiyi.opscloud.dubbo.handler;

import com.baiyi.opscloud.dubbo.config.ZookeeperConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author baiyi
 * @Date 2020/10/9 1:45 下午
 * @Version 1.0
 */

@Slf4j
@Component("ZookeeperConfig")
public class ZookeeperHandler {

    //会话超时时间
    private static final int sessionTimeoutMs = 3000;
    //连接超时时间
    private static final int connectionTimeoutMs = 5000;
    //会最大重试次数
    private static final int maxRetries = 5;
    //会初始休眠时间
    private static final int baseSleepTimeMs = 3000;

    private static CuratorFramework client = null;

    private static Map<String, CuratorFramework> context;

    @Resource
    private ZookeeperConfig zookeeperConfig;

//    @PostConstruct
//    public void init() {
//        context = new ConcurrentHashMap<>();
//        //重试策略，初试时间3秒，重试5次
//
//        zookeeperConfig.getClusters().forEach(e -> {
//            RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
//            client = CuratorFrameworkFactory.builder()
//                    .connectString(e.getAddress())
//                    .connectionTimeoutMs(connectionTimeoutMs)
//                    .sessionTimeoutMs(sessionTimeoutMs)
//                    .retryPolicy(retryPolicy)
//                    .build();
//            client.start();
//            context.put(e.getEnv(), client);
//        });
//        log.info("zookeeper 初始化完成...");
//    }

    private void init(String env) {
        if (context == null)
            context = new ConcurrentHashMap<>();
        zookeeperConfig.getClusters().forEach(e -> {
            if (e.getEnv().equals(env)) {
                RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
                client = CuratorFrameworkFactory.builder()
                        .connectString(e.getAddress())
                        .connectionTimeoutMs(connectionTimeoutMs)
                        .sessionTimeoutMs(sessionTimeoutMs)
                        .retryPolicy(retryPolicy)
                        .build();
                client.start();
                context.put(e.getEnv(), client);
            }
        });
    }

    public CuratorFramework getClient(String env) {
        init(env);
        return context.get(env);
    }

    public void closeClient(String env) {
        if (context.containsKey(env)) {
            CuratorFramework client = context.get(env);
            if (client != null) {
                context.remove(env);
                client.close();
            }
        }
    }

}
