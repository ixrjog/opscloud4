package com.baiyi.opscloud.facade.user;

import com.baiyi.opscloud.domain.vo.user.UserUIVO;

/**
 * @Author baiyi
 * @Date 2021/6/4 5:32 下午
 * @Version 1.0
 */
public interface UserUIFacade {

    UserUIVO.UIInfo buildUIInfo();
}
