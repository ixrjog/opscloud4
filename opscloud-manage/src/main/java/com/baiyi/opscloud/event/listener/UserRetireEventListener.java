package com.baiyi.opscloud.event.listener;

import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.facade.AuthBaseFacade;
import com.baiyi.opscloud.facade.aliyun.AliyunRAMFacade;
import com.baiyi.opscloud.facade.tencent.TencentExmailUserFacade;
import com.baiyi.opscloud.service.user.OcUserApiTokenService;
import com.baiyi.opscloud.service.user.OcUserToBeRetiredService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/30 10:52 上午
 * @Since 1.0
 */

@Slf4j
@Component
public class UserRetireEventListener {

    @Resource
    private OcUserApiTokenService ocUserApiTokenService;

    @Resource
    private AuthBaseFacade ocAuthFacade;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private AliyunRAMFacade aliyunRAMFacade;

    @Resource
    private TencentExmailUserFacade tencentExmailUserFacade;

    @Resource
    private OcUserToBeRetiredService ocUserToBeRetiredService;

    @Subscribe
    public void userApiTokenDel(OcUser ocUser) {
        log.info("吊销用户ApiToken,username:{}", ocUser.getUsername());
        ocUserApiTokenService.queryOcUserApiTokenByUsername(ocUser.getUsername()).forEach(e -> {
            e.setValid(false);
            ocUserApiTokenService.updateOcUserApiToken(e);
        });
    }

    @Subscribe
    public void userTokenDel(OcUser ocUser) {
        log.info("吊销用户Token,username:{}", ocUser.getUsername());
        ocAuthFacade.revokeUserToken(ocUser.getUsername());
        ocUser.setIsActive(false);
        ocUser.setLastLogin(new Date());
        ocUserService.updateOcUser(ocUser);
    }

    @Subscribe
    public void aliyunRAMDel(OcUser ocUser) {
        log.info("吊销用户阿里云RAM,username:{}", ocUser.getUsername());
        try {
            aliyunRAMFacade.delRAMUserByUsername(ocUser.getUsername());
        } catch (Exception e) {
            log.error("删除RAM账户错误，username={},error={}", ocUser.getUsername(), e.getMessage());
        }
    }

    @Subscribe
    public void tencentExmailDel(OcUser ocUser) {
        log.info("吊销用户腾讯企业邮箱,username:{}", ocUser.getUsername());
        tencentExmailUserFacade.deleteUser(ocUser);
    }

    @Subscribe
    public void clearToBeResigned(OcUser ocUser) {
        ocUserToBeRetiredService.delOcUserToBeRetiredByUserId(ocUser.getId());
    }

}
