package com.baiyi.opscloud.facade.user;

import com.baiyi.opscloud.domain.vo.user.UserProfileVO;

/**
 * @Author baiyi
 * @Date 2023/2/2 15:23
 * @Version 1.0
 */
public interface UserProfileFacade {

    UserProfileVO.Profiles getProfiles();

}
