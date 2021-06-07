package com.baiyi.caesar.facade.user.impl;

import com.baiyi.caesar.domain.vo.sys.MenuVO;
import com.baiyi.caesar.domain.vo.user.UserUIVO;
import com.baiyi.caesar.facade.sys.MenuFacade;
import com.baiyi.caesar.facade.user.UserUIFacade;
import com.baiyi.caesar.service.auth.AuthResourceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/4 5:32 下午
 * @Version 1.0
 */
@Service
public class UserUIFacadeImpl implements UserUIFacade {

    @Resource
    private MenuFacade menuFacade;

    @Resource
    private AuthResourceService authResourceService;


    private void dd(){


    }



    @Override
    public UserUIVO.UIInfo buildUIInfo() {

        List<MenuVO.Menu> menuInfo = menuFacade.queryMyMenu();

        return UserUIVO.UIInfo.builder()
                .menuInfo(menuInfo)
                .build();
    }
}
