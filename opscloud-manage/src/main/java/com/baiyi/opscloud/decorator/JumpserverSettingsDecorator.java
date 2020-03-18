package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.vo.jumpserver.JumpserverAssetsAdminuserVO;
import com.baiyi.opscloud.domain.vo.jumpserver.JumpserverAssetsSystemuserVO;
import com.baiyi.opscloud.domain.vo.jumpserver.JumpserverSettingsVO;
import com.baiyi.opscloud.service.jumpserver.AssetsAdminuserService;
import com.baiyi.opscloud.service.jumpserver.AssetsSystemuserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

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
    private RedisUtil redisUtil;

    public JumpserverSettingsVO.Settings decorator(JumpserverSettingsVO.Settings settings) {
        // 装饰资源组
        settings.setAssetsSystemusers(
                BeanCopierUtils.copyListProperties(assetsSystemuserService.queryAllAssetsSystemuser(), JumpserverAssetsSystemuserVO.AssetsSystemuser.class));
        settings.setAssetsAdminusers(
                BeanCopierUtils.copyListProperties(assetsAdminuserService.queryAllAssetsAdminuser(), JumpserverAssetsAdminuserVO.AssetsAdminuser.class));
        Map<String, String> settingsMap = (Map<String, String>) redisUtil.get(Global.JUMPSERVER_SETTINGS_KEY);
        if (settingsMap != null) {
            if(settingsMap.containsKey(Global.JUMPSERVER_ASSETS_ADMINUSER_ID_KEY))
                settings.setAssetsAdminuserId(settingsMap.get(Global.JUMPSERVER_ASSETS_ADMINUSER_ID_KEY));
            if(settingsMap.containsKey(Global.JUMPSERVER_ASSETS_SYSTEMUSER_ID_KEY))
                settings.setAssetsSystemuserId(settingsMap.get(Global.JUMPSERVER_ASSETS_SYSTEMUSER_ID_KEY));
            if(settingsMap.containsKey(Global.JUMPSERVER_ASSETS_ADMIN_SYSTEMUSER_ID_KEY))
                settings.setAssetsAdminSystemuserId(settingsMap.get(Global.JUMPSERVER_ASSETS_ADMIN_SYSTEMUSER_ID_KEY));
        }

        return settings;
    }


}
