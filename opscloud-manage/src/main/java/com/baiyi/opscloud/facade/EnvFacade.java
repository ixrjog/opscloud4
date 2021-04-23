package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.env.EnvParam;
import com.baiyi.opscloud.domain.vo.env.EnvVO;

/**
 * @Author baiyi
 * @Date 2020/2/21 5:28 下午
 * @Version 1.0
 */
public interface EnvFacade {

    DataTable<EnvVO.Env> queryEnvPage(EnvParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addEnv(EnvVO.Env env);

    BusinessWrapper<Boolean> updateEnv(EnvVO.Env env);

    BusinessWrapper<Boolean> deleteEnvById(int id);

    /**
     * 转envType
     * @param envName
     * @return
     */
    int convertEnvName(String envName);

}
