package com.baiyi.caesar.facade.sys;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.sys.EnvParam;
import com.baiyi.caesar.vo.env.EnvVO;

/**
 * @Author baiyi
 * @Date 2021/5/25 4:33 下午
 * @Version 1.0
 */
public interface EnvFacade {

    DataTable<EnvVO.Env> queryEnvPage(EnvParam.EnvPageQuery pageQuery);

    void addEnv(EnvVO.Env env);

    void updateEnv(EnvVO.Env env);

    void deleteEnvById(Integer id);
}
