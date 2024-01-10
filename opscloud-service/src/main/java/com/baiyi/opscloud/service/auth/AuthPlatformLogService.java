package com.baiyi.opscloud.service.auth;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AuthPlatformLog;
import com.baiyi.opscloud.domain.param.auth.AuthPlatformParam;

/**
 * @Author baiyi
 * @Date 2022/8/22 14:46
 * @Version 1.0
 */
public interface AuthPlatformLogService {

    void add(AuthPlatformLog authPlatformLog);

    DataTable<AuthPlatformLog> queryPageByParam(AuthPlatformParam.AuthPlatformLogPageQuery pageQuery);

}