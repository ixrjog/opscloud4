package com.baiyi.opscloud.server.decorator;

import com.baiyi.opscloud.common.base.SettingName;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.vo.jumpserver.JumpserverAssetsAdminuserVO;
import com.baiyi.opscloud.domain.vo.jumpserver.JumpserverAssetsSystemuserVO;
import com.baiyi.opscloud.domain.vo.jumpserver.JumpserverSettingsVO;
import com.baiyi.opscloud.facade.SettingBaseFacade;
import com.baiyi.opscloud.service.jumpserver.AssetsAdminuserService;
import com.baiyi.opscloud.service.jumpserver.AssetsSystemuserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/13 3:35 下午
 * @Version 1.0
 */
@Component
public class JumpserverSettingsDecorator {

    @Resource
    private AssetsSystemuserService assetsSystemuserService;

    @Resource
    private AssetsAdminuserService assetsAdminuserService;

    @Resource
    private SettingBaseFacade settingBaseFacade;

    public JumpserverSettingsVO.Settings decorator(JumpserverSettingsVO.Settings settings) {
        // 装饰资源组
        settings.setAssetsSystemusers(
                BeanCopierUtils.copyListProperties(assetsSystemuserService.queryAllAssetsSystemuser(), JumpserverAssetsSystemuserVO.AssetsSystemuser.class));
        settings.setAssetsAdminusers(
                BeanCopierUtils.copyListProperties(assetsAdminuserService.queryAllAssetsAdminuser(), JumpserverAssetsAdminuserVO.AssetsAdminuser.class));
        settings.setAssetsAdminuserId(settingBaseFacade.querySetting(SettingName.JUMPSERVER_ASSETS_ADMINUSER_ID)) ;
        settings.setAssetsSystemuserId(settingBaseFacade.querySetting(SettingName.JUMPSERVER_ASSETS_SYSTEMUSER_ID)) ;
        settings.setAssetsAdminSystemuserId(settingBaseFacade.querySetting(SettingName.JUMPSERVER_ASSETS_ADMIN_SYSTEMUSER_ID)) ;
        return settings;
    }


}
