package com.baiyi.caesar.service.sys.impl;

import com.baiyi.caesar.mapper.caesar.MenuMapper;
import com.baiyi.caesar.service.sys.MenuService;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/1 5:09 下午
 * @Version 1.0
 */
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuMapper menuMapper;
}
