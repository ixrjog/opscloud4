package com.baiyi.opscloud.leo.handler.deploy.base;

/**
 * @Author baiyi
 * @Date 2022/12/13 16:25
 * @Version 1.0
 */
public interface IDeployStrategy extends IDeployStep {

    String getDeployType();

}