package com.baiyi.opscloud.service.serverChange.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;
import com.baiyi.opscloud.mapper.opscloud.OcServerChangeTaskFlowMapper;
import com.baiyi.opscloud.service.serverChange.OcServerChangeTaskFlowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/28 1:24 下午
 * @Version 1.0
 */
@Service
public class OcServerChangeTaskFlowServiceImpl implements OcServerChangeTaskFlowService {

    @Resource
    private OcServerChangeTaskFlowMapper ocServerChangeTaskFlowMapper;

    @Override
    public void addOcServerChangeTaskFlow(OcServerChangeTaskFlow ocServerChangeTaskFlow) {
        ocServerChangeTaskFlowMapper.insert(ocServerChangeTaskFlow);
    }

    @Override
    public void updateOcServerChangeTaskFlow(OcServerChangeTaskFlow ocServerChangeTaskFlow) {
        ocServerChangeTaskFlowMapper.updateByPrimaryKey(ocServerChangeTaskFlow);
    }
}
