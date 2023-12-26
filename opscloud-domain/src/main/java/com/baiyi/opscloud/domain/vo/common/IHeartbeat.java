package com.baiyi.opscloud.domain.vo.common;

/**
 * @Author baiyi
 * @Date 2023/7/20 10:27
 * @Version 1.0
 */
public interface IHeartbeat {

    Boolean getIsLive();

    void setIsLive(Boolean isLive);

}