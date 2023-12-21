package com.baiyi.opscloud.facade.sys.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Nav;
import com.baiyi.opscloud.domain.vo.sys.NavVO;
import com.baiyi.opscloud.facade.sys.NavFacade;
import com.baiyi.opscloud.service.sys.NavService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/6/30 10:18 PM
 * @Since 1.0
 */

@Service
@RequiredArgsConstructor
public class NavFacadeImpl implements NavFacade {

    private final NavService navService;

    @Override
    public List<NavVO.Nav> listActive() {
        List<Nav> navList = navService.listActive();
        return BeanCopierUtil.copyListProperties(navList, NavVO.Nav.class);
    }

}