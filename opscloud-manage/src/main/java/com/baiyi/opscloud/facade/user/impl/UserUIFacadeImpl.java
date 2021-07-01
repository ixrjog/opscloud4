package com.baiyi.opscloud.facade.user.impl;

import com.baiyi.opscloud.domain.vo.sys.MenuVO;
import com.baiyi.opscloud.domain.vo.user.UserUIVO;
import com.baiyi.opscloud.facade.sys.MenuFacade;
import com.baiyi.opscloud.facade.user.UserUIFacade;
import com.baiyi.opscloud.service.auth.AuthResourceService;
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
