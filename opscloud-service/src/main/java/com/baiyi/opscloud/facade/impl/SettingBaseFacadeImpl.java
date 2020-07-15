package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcSetting;
import com.baiyi.opscloud.domain.param.setting.SettingParam;
import com.baiyi.opscloud.facade.SettingBaseFacade;
import com.baiyi.opscloud.service.oc.OcSettingService;
import com.google.common.collect.Maps;
import org.springframework.cache.annotation.CacheEvict;
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
public class SettingBaseFacadeImpl implements SettingBaseFacade {

    @Resource
    private OcSettingService ocSettingService;

    @Override
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_SETTING_CACHE_REPO)
    public String querySetting(String name) {
        OcSetting ocSetting = ocSettingService.queryOcSettingByName(name);
        if (ocSetting == null) return null;
        return ocSetting.getSettingValue();
    }

    @Override
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_SETTING_CACHE_REPO, key = "#name")
    public Map<String, String> querySettingMap(String name) {
        Map<String, String> settingMap = Maps.newHashMap();
        OcSetting ocSetting = ocSettingService.queryOcSettingByName(name);
        if (ocSetting == null) return settingMap;
        settingMap.put(ocSetting.getName(), ocSetting.getSettingValue());
        return settingMap;
    }

    @Override
    public Map<String, String> querySettingMap() {
        Map<String, String> settingMap = Maps.newHashMap();
        ocSettingService.queryAll().forEach(e -> settingMap.put(e.getName(), e.getSettingValue()));
        return settingMap;
    }

    @Override
    public BusinessWrapper<Boolean> updateSetting(SettingParam.UpdateSettingParam updateSettingParam) {
        OcSetting ocSetting = ocSettingService.queryOcSettingByName(updateSettingParam.getName());
        if (ocSetting == null) return new BusinessWrapper<>(10000, "配置项不存在");
        ocSetting.setSettingValue(updateSettingParam.getSettingValue());
        ocSettingService.updateOcSetting(ocSetting);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    @CacheEvict(cacheNames = CachingConfig.CACHE_NAME_SETTING_CACHE_REPO, key = "#name", beforeInvocation = true)
    public void saveSetting(String name, String settingValue) {
        OcSetting ocSetting = ocSettingService.queryOcSettingByName(name);
        if (ocSetting == null) return;
        ocSetting.setSettingValue(settingValue);
        ocSettingService.updateOcSetting(ocSetting);
    }

}
