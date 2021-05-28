package com.baiyi.caesar.facade.user.impl;

import com.baiyi.caesar.facade.user.UserPremissionFacade;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/27 2:38 下午
 * @Version 1.0
 */
@Service
public class UserPremissionFacadeImpl implements UserPremissionFacade {

    @Resource
    private UserPremissionService premissionService;


}
