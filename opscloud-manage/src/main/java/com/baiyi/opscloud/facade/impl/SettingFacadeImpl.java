package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.domain.generator.opscloud.OcSetting;
import com.baiyi.opscloud.facade.SettingFacade;
import com.baiyi.opscloud.service.oc.OcSettingService;
import com.google.common.collect.Maps;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/6/4 5:09 下午
 * @Version 1.0
 */
@Service
public class SettingFacadeImpl implements SettingFacade {

    @Resource
    private OcSettingService ocSettingService;

    @Override
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_ANSIBLE_CACHE_REPO)
    public String querySetting(String name) {
        OcSetting ocSetting = ocSettingService.queryOcSettingByName(name);
        if (ocSetting == null) return null;
        return ocSetting.getSettingValue();
    }

    @Override
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_ANSIBLE_CACHE_REPO)
    public Map<String, String> querySettingMap(String name) {
        Map<String, String> settingMap = Maps.newHashMap();
        OcSetting ocSetting = ocSettingService.queryOcSettingByName(name);
        if (ocSetting == null) return settingMap;
        settingMap.put(ocSetting.getName(), ocSetting.getSettingValue());
        return settingMap;
    }

}
