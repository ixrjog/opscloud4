package com.baiyi.opscloud.facade.sys;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.sys.EnvParam;
import com.baiyi.opscloud.domain.vo.env.EnvVO;

/**
 * @Author baiyi
 * @Date 2021/5/25 4:33 下午
 * @Version 1.0
 */
public interface EnvFacade {

    DataTable<EnvVO.Env> queryEnvPage(EnvParam.EnvPageQuery pageQuery);

    void addEnv(EnvParam.Env env);

    void updateEnv(EnvParam.Env env);

    void deleteEnvById(Integer id);

}