package com.baiyi.opscloud.packer;

import com.baiyi.opscloud.common.annotation.BizDocWrapper;
import com.baiyi.opscloud.common.annotation.EnvWrapper;
import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @Author baiyi
 * @Date 2022/2/17 5:23 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ServerPackerDelegate {

    private final BusinessAssetRelationService businessAssetRelationService;

    @EnvWrapper
    @TagsWrapper
    @BizDocWrapper
    public void wrap(ServerVO.Server server, IExtend iExtend) {
        if (iExtend.getExtend()) {
            server.setAssetFlag(!CollectionUtils.isEmpty(businessAssetRelationService.queryBusinessRelations(server)));
        }
    }

}