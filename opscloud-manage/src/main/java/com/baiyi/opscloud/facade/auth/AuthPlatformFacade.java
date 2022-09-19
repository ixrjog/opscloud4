package com.baiyi.opscloud.facade.auth;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.auth.AuthPlatformParam;
import com.baiyi.opscloud.domain.vo.auth.AuthPlatformVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/9/19 09:32
 * @Version 1.0
 */
public interface AuthPlatformFacade {

    List<AuthPlatformVO.Platform> getPlatformOptions();

    DataTable<AuthPlatformVO.AuthPlatformLog> queryAuthPlatformLog(AuthPlatformParam.AuthPlatformLogPageQuery pageQuery);

}
