package com.baiyi.opscloud.facade.sys.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.param.sys.EnvParam;
import com.baiyi.opscloud.facade.sys.EnvFacade;
import com.baiyi.opscloud.packer.sys.EnvPacker;
import com.baiyi.opscloud.service.sys.EnvService;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/25 4:33 下午
 * @Version 1.0
 */
@Service
public class EnvFacadeImpl implements EnvFacade {

    @Resource
    private EnvService envService;

    private Env toDO(EnvVO.Env env) {
        return BeanCopierUtil.copyProperties(env, Env.class);
    }

    @Override
    public void addEnv(EnvVO.Env env) {
        envService.add(toDO(env));
    }

    @Override
    public void updateEnv(EnvVO.Env env) {
        envService.update(toDO(env));
    }

    @Override
    public void deleteEnvById(Integer id) {
    }

    @Override
    public DataTable<EnvVO.Env> queryEnvPage(EnvParam.EnvPageQuery pageQuery) {
        DataTable<Env> table = envService.queryPageByParam(pageQuery);
        return new DataTable<>(EnvPacker.wrapVOList(table.getData()), table.getTotalNum());
    }


}
