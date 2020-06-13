package com.baiyi.opscloud.facade;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/6/4 5:09 下午
 * @Version 1.0
 */
public interface SettingFacade {

    String querySetting(String name);

    Map<String, String> querySettingMap(String name);
}
