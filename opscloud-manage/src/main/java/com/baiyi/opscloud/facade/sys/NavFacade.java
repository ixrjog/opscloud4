package com.baiyi.opscloud.facade.sys;

import com.baiyi.opscloud.domain.vo.sys.NavVO;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/6/30 10:16 PM
 * @Since 1.0
 */
public interface NavFacade {

    List<NavVO.Nav> listActive();

}