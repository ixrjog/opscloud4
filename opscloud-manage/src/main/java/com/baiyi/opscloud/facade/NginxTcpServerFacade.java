package com.baiyi.opscloud.facade;

/**
 * @Author baiyi
 * @Date 2020/10/19 10:25 上午
 * @Version 1.0
 */
public interface NginxTcpServerFacade {

    String buildNginxDubboTcpServerByEnv(String env);

    void writeNginxDubboTcpServerConfByEnv(String env);
}
