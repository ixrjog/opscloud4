package com.baiyi.opscloud.service.auth;

/**
 * @Author baiyi
 * @Date 2020/2/12 2:12 下午
 * @Version 1.0
 */
public interface OcAuthRoleResourceService {

    /**
     * 统计被引用的资源条目
     * @param resourceId
     * @return
     */
    int countByResourceId(int resourceId);
}
