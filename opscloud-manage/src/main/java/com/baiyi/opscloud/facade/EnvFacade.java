package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.env.EnvParam;
import com.baiyi.opscloud.domain.vo.env.OcEnvVO;

/**
 * @Author baiyi
 * @Date 2020/2/21 5:28 下午
 * @Version 1.0
 */
public interface EnvFacade {

    DataTable<OcEnvVO.Env> queryEnvPage(EnvParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addEnv(OcEnvVO.Env env);

    BusinessWrapper<Boolean> updateEnv(OcEnvVO.Env env);

    BusinessWrapper<Boolean> deleteEnvById(int id);
}
