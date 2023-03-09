package com.baiyi.opscloud.packer.datasource;

import com.baiyi.opscloud.common.util.ExtendUtil;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.datasource.DsConfigVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.packer.sys.CredentialPacker;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/5/15 1:57 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class DsConfigPacker implements IWrapper<DsConfigVO.DsConfig> {

    private final CredentialPacker credentialPacker;

    private final DsInstanceService dsInstanceService;

    @Override
    public void wrap(DsConfigVO.DsConfig dsConfig, IExtend iExtend) {
        if (!ExtendUtil.isExtend(iExtend)) {
            return;
        }
        credentialPacker.wrap(dsConfig);
        dsConfig.setIsRegistered(dsInstanceService.countByConfigId(dsConfig.getId()) > 0);
    }

}
