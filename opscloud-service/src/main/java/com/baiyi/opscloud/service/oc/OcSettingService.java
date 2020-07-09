package com.baiyi.opscloud.service.oc;

import com.baiyi.opscloud.domain.generator.opscloud.OcSetting;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/4 4:18 下午
 * @Version 1.0
 */
public interface OcSettingService {

    OcSetting queryOcSettingByName(String name);

    List<OcSetting> queryAll();

    void updateOcSetting(OcSetting ocSetting);
}
