package com.baiyi.opscloud.facade.user.impl;

import com.baiyi.opscloud.domain.vo.sys.MenuVO;
import com.baiyi.opscloud.domain.vo.user.UIVO;
import com.baiyi.opscloud.facade.sys.MenuFacade;
import com.baiyi.opscloud.facade.user.UserUIFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/4 5:32 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserUIFacadeImpl implements UserUIFacade {

    private final MenuFacade menuFacade;

    @Override
    public UIVO.UIInfo buildUIInfo() {
        List<MenuVO.Menu> menuInfo = menuFacade.queryMyMenu();
        return UIVO.UIInfo.builder()
                .menuInfo(menuInfo)
                .build();
    }

}