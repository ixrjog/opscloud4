package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.param.user.UserSettingParam;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/5/17 1:08 下午
 * @Version 1.0
 */
public interface UserSettingFacade {

    Map<String, String> queryUserSettingBySettingGroup(String settingGroup);

    BusinessWrapper<Boolean> saveUserSettingBySettingGroup(UserSettingParam.UserSetting userSetting);
}
