package com.baiyi.caesar.facade.sys.impl;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.Env;
import com.baiyi.caesar.domain.param.sys.EnvParam;
import com.baiyi.caesar.facade.sys.EnvFacade;
import com.baiyi.caesar.packer.sys.EnvPacker;
import com.baiyi.caesar.service.sys.EnvService;
import com.baiyi.caesar.vo.env.EnvVO;
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
