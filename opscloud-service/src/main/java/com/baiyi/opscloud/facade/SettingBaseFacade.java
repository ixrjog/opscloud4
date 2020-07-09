package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.param.setting.SettingParam;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/6/4 5:09 下午
 * @Version 1.0
 */
public interface SettingBaseFacade {

    String querySetting(String name);

    void saveSetting(String name, String settingValue);

    Map<String, String> querySettingMap(String name);

    Map<String, String> querySettingMap();

    BusinessWrapper<Boolean> updateSetting(SettingParam.UpdateSettingParam updateSettingParam);
}
