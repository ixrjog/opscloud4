package com.baiyi.opscloud.facade.apollo.chain;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.base.AccessLevel;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.facade.apollo.handler.BaseApolloReleaseChainHandler;
import com.baiyi.opscloud.service.auth.AuthRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/11 17:18
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminReleasesChainHandler extends BaseApolloReleaseChainHandler {

    private final AuthRoleService authRoleService;

    @Override
    protected HttpResult<Boolean> handle(ApolloParam.ReleaseEvent releaseEvent, ApolloConfig apolloConfig) {
        if (!StringUtils.hasText(releaseEvent.getUsername())) {
            // 用户名不存在跳过
            return PASS_AND_DO_NEXT;
        }
        int accessLevel = authRoleService.getRoleAccessLevelByUsername(releaseEvent.getUsername());
        if (accessLevel >= AccessLevel.OPS.getLevel()) {
            return HttpResult.SUCCESS;
        }
        return PASS_AND_DO_NEXT;
    }

}