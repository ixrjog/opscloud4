package com.baiyi.opscloud.facade.business.impl;

import com.baiyi.opscloud.facade.business.BusinessPropertyFacade;
import com.baiyi.opscloud.service.business.BusinessPropertyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/19 4:40 下午
 * @Version 1.0
 */
@Service
public class BusinessPropertyFacadeImpl implements BusinessPropertyFacade {

    @Resource
    private BusinessPropertyService businessPropertyService;

    public void add(){

    }

}
