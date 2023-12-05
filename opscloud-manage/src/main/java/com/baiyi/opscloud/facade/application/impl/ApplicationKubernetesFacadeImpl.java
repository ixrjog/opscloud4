package com.baiyi.opscloud.facade.application.impl;

import com.baiyi.opscloud.common.base.AccessLevel;
import com.baiyi.opscloud.common.exception.auth.AuthenticationException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.facade.application.ApplicationKubernetesFacade;
import com.baiyi.opscloud.packer.application.ApplicationKubernetesPacker;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.auth.AuthRoleService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/3/27 17:20
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@BusinessType(BusinessTypeEnum.APPLICATION)
public class ApplicationKubernetesFacadeImpl implements ApplicationKubernetesFacade {

    private final ApplicationKubernetesPacker applicationKubernetesPacker;

    private final UserService userService;

    private final AuthRoleService authRoleService;

    private final UserPermissionService userPermissionService;

    private final ApplicationService applicationService;

    @Override
    public ApplicationVO.Kubernetes getApplicationKubernetes(ApplicationParam.GetApplicationKubernetes getApplicationKubernetes, String username) {
        // 鉴权
        if (!isAdmin(username)) {
            int userId = userService.getByUsername(username).getId();
            UserPermission query = UserPermission.builder()
                    .businessType(BusinessTypeEnum.APPLICATION.getType())
                    .businessId(getApplicationKubernetes.getApplicationId())
                    .userId(userId)
                    .build();
            if (userPermissionService.getByUniqueKey(query) == null) {
                throw new AuthenticationException(ErrorEnum.AUTHENTICATION_FAILURE);
            }
        }
        Application application = applicationService.getById(getApplicationKubernetes.getApplicationId());
        ApplicationVO.Kubernetes vo = BeanCopierUtil.copyProperties(application, ApplicationVO.Kubernetes.class);
        applicationKubernetesPacker.wrap(vo, getApplicationKubernetes.getEnvType());
        return vo;
    }

    /**
     * OPS角色以上即认定为系统管理员
     *
     * @return
     */
    private boolean isAdmin(String username) {
        int accessLevel = authRoleService.getRoleAccessLevelByUsername(username);
        return accessLevel >= AccessLevel.OPS.getLevel();
    }

}
