package com.baiyi.caesar.packer.datasource;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.packer.sys.CredentialPacker;
import com.baiyi.caesar.service.datasource.DsInstanceService;
import com.baiyi.caesar.util.ExtendUtil;
import com.baiyi.caesar.domain.vo.datasource.DsConfigVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/15 1:57 下午
 * @Version 1.0
 */
@Component
public class DsConfigPacker {

    @Resource
    private CredentialPacker credentialPacker;

    @Resource
    private DsInstanceService dsInstanceService;

    public List<DsConfigVO.DsConfig> wrapVOList(List<DatasourceConfig> data) {
        return BeanCopierUtil.copyListProperties(data, DsConfigVO.DsConfig.class);
    }

    public List<DsConfigVO.DsConfig> wrapVOList(List<DatasourceConfig> data, IExtend iExtend) {
        List<DsConfigVO.DsConfig> voList = wrapVOList(data);
        if (!ExtendUtil.isExtend(iExtend))
            return voList;

        return voList.stream().peek(e -> {
            credentialPacker.wrap(e);
            e.setIsRegistered(dsInstanceService.countByConfigId(e.getId()) > 0);
        }).collect(Collectors.toList());
    }

}
