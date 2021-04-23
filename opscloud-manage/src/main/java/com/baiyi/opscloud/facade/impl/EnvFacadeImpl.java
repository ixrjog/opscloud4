package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcEnv;
import com.baiyi.opscloud.domain.param.env.EnvParam;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.facade.EnvFacade;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.baiyi.opscloud.service.server.OcServerService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/21 5:29 下午
 * @Version 1.0
 */
@Service
public class EnvFacadeImpl implements EnvFacade {

    @Resource
    private OcEnvService ocEnvService;

    @Resource
    private OcServerService ocServerService;

    public static final boolean ACTION_ADD = true;
    public static final boolean ACTION_UPDATE = false;

    @Override
    public DataTable<EnvVO.Env> queryEnvPage(EnvParam.PageQuery pageQuery) {
        DataTable<OcEnv> table = ocEnvService.queryOcEnvByParam(pageQuery);
        List<EnvVO.Env> page = BeanCopierUtils.copyListProperties(table.getData(), EnvVO.Env.class);
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> addEnv(EnvVO.Env env) {
        return saveEnv(env, ACTION_ADD);
    }

    @Override
    public BusinessWrapper<Boolean> updateEnv(EnvVO.Env env) {
        return saveEnv(env, ACTION_UPDATE);
    }

    private BusinessWrapper<Boolean> saveEnv(EnvVO.Env env, boolean action) {
        OcEnv checkOcEnvName = ocEnvService.queryOcEnvByName(env.getEnvName());
        OcEnv ocEnv = BeanCopierUtils.copyProperties(env, OcEnv.class);
        // 对象存在 && 新增
        if (checkOcEnvName != null && action)
            return new BusinessWrapper<>(ErrorEnum.ENV_NAME_ALREADY_EXIST);
        if (action) {
            ocEnvService.addOcEnv(ocEnv);
        } else {
            ocEnvService.updateOcEnv(ocEnv);
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteEnvById(int id) {
        OcEnv ocEnv = ocEnvService.queryOcEnvById(id);
        if (ocEnv == null)
            return new BusinessWrapper<>(ErrorEnum.ENV_NOT_EXIST);
        // 判断是否位基本数据
        if (ocEnv.getEnvType() == 0)
            new BusinessWrapper<>(ErrorEnum.ENV_IS_DEFAULT);
        // 判断server绑定的资源
        int count = ocServerService.countByEnvType(ocEnv.getEnvType());
        if (count == 0) {
            ocEnvService.deleteOcEnvById(id);
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.ENV_HAS_USED);
        }
    }

    @Override
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_ENV_PEPO)
    public int convertEnvName(String envName) {
        OcEnv ocEnv = ocEnvService.queryOcEnvByName(envName);
        return ocEnv == null ? 0 : ocEnv.getEnvType();
    }
}
