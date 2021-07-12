package com.baiyi.opscloud.facade.application.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.facade.application.ApplicationFacade;
import com.baiyi.opscloud.packer.application.ApplicationPacker;
import com.baiyi.opscloud.service.application.ApplicationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/7/12 12:58 下午
 * @Version 1.0
 */
@Service
public class ApplicationFacadeImpl implements ApplicationFacade {

    @Resource
    private ApplicationService applicationService;

    @Resource
    private ApplicationPacker applicationPacker;

    @Override
    public DataTable<ApplicationVO.Application> queryApplicationPage(ApplicationParam.ApplicationPageQuery pageQuery) {
        DataTable<Application> table = applicationService.queryPageByParam(pageQuery);
        return new DataTable<>(applicationPacker.wrapVOList(table.getData(), pageQuery), table.getTotalNum());
    }
}
