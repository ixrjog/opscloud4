package com.baiyi.opscloud.cloud.slb;

import com.baiyi.opscloud.domain.BusinessWrapper;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 3:40 下午
 * @Since 1.0
 */
public interface AliyunSLBACLCenter {

    BusinessWrapper<Boolean> syncSLBACL();

}
