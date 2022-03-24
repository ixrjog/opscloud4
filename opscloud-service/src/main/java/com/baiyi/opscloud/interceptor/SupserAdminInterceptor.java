package com.baiyi.opscloud.interceptor;

import com.baiyi.opscloud.common.exception.auth.AuthRuntimeException;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTag;
import com.baiyi.opscloud.domain.generator.opscloud.Tag;
import com.baiyi.opscloud.facade.user.UserPermissionFacade;
import com.baiyi.opscloud.service.server.ServerService;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/3/24 13:57
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SupserAdminInterceptor {

    private final UserPermissionFacade userPermissionFacade;

    private final ServerService serverService;

    private final BusinessTagService bizTagService;

    private final TagService tagService;

    public void interceptLoginServer(int serverId) {
        Tag tag = tagService.getByTagKey(TagConstants.SUPER_ADMIN.getTag());
        // SA标签不存在
        if (tag == null) return;
        BusinessTag bizTag = BusinessTag.builder()
                .businessType(BusinessTypeEnum.SERVER.getType())
                .businessId(serverId)
                .tagId(tag.getId())
                .build();
        // 服务器未打SA标签
        if (bizTagService.countByBusinessTag(bizTag) == 0) return;
        int accessLevel = userPermissionFacade.getUserAccessLevel(SessionUtil.getUsername());
        if (accessLevel < 100) throw new AuthRuntimeException("越权访问: 业务资源只有{SUPER_SA}才能访问！");
        log.info("SA Interceptor: passed！");
    }

}
