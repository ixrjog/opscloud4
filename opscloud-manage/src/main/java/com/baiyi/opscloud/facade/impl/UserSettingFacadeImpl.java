package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.bo.UserSettingBO;
import com.baiyi.opscloud.common.base.UserSettingGroup;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserSetting;
import com.baiyi.opscloud.domain.param.user.UserSettingParam;
import com.baiyi.opscloud.facade.UserFacade;
import com.baiyi.opscloud.facade.UserSettingFacade;
import com.baiyi.opscloud.service.user.OcUserSettingService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/5/17 1:08 下午
 * @Version 1.0
 */
@Service
public class UserSettingFacadeImpl implements UserSettingFacade, InitializingBean {

    public static Map<String, Map<String, String>> settingGroupMap = Maps.newHashMap();

    @Resource
    private OcUserSettingService ocUserSettingService;

    @Resource
    private UserFacade userFacade;

    @Override
    public Map<String, String> queryUserSettingBySettingGroup(String settingGroup) {
        Map<String, String> xtermSettingGroup = settingGroupMap.get(settingGroup);
        List<OcUserSetting> settings = ocUserSettingService.queryOcUserSettingBySettingGroup(userFacade.getOcUserBySession().getId(), settingGroup);
        if (settings != null && !settings.isEmpty())
            xtermSettingGroup.putAll(convert(settings));
        return xtermSettingGroup;
    }

    @Override
    public BusinessWrapper<Boolean> saveUserSettingBySettingGroup(UserSettingParam.UserSetting userSetting) {
        OcUser ocUser = userFacade.getOcUserBySession();
        // 当前用户配置
        List<OcUserSetting> settings = ocUserSettingService.queryOcUserSettingBySettingGroup(ocUser.getId(), userSetting.getSettingGroup());

        userSetting.getSettingMap().keySet().forEach(k -> {
            String settingValue = userSetting.getSettingMap().get(k);
            OcUserSetting ocUserSetting = getOcUserSettingByName(k, settings);
            if (ocUserSetting != null) {
                ocUserSetting.setSettingValue(settingValue);
                ocUserSettingService.updateOcUserSetting(ocUserSetting);
            } else {
                UserSettingBO userSettingBO = UserSettingBO.builder()
                        .settingGroup(userSetting.getSettingGroup())
                        .userId(ocUser.getId())
                        .username(ocUser.getUsername())
                        .name(k)
                        .settingValue(settingValue)
                        .build();
                ocUserSettingService.addOcUserSetting(BeanCopierUtils.copyProperties(userSettingBO, OcUserSetting.class));
            }
        });
        return BusinessWrapper.SUCCESS;
    }

    private OcUserSetting getOcUserSettingByName(String name, List<OcUserSetting> settings) {
        List<OcUserSetting> list = settings.stream().filter(e -> e.getName().equals(name)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(list))
            return list.get(0);
        return null;
    }

    private Map<String, String> convert(List<OcUserSetting> settings) {
        return settings.stream().collect(Collectors.toMap(OcUserSetting::getName, OcUserSetting::getSettingValue, (k1, k2) -> k1));
    }


    @Override
    public void afterPropertiesSet() {
        Map<String, String> xtermSettingGroup = Maps.newHashMap();
        xtermSettingGroup.put("XTERM_FOREGROUND", "#FFFFFF"); // 字体
        xtermSettingGroup.put("XTERM_BACKGROUND", "#5b5d66");  // 背景色
        xtermSettingGroup.put("XTERM_ROWS", "30");  // 终端行数
        xtermSettingGroup.put("XTERM_LAYOUT", "0");  // 终端窗口布局
        settingGroupMap.put(UserSettingGroup.XTERM.getName(), xtermSettingGroup);
    }


}
