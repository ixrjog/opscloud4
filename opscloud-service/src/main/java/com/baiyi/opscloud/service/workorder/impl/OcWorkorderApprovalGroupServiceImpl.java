package com.baiyi.opscloud.service.workorder.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderApprovalGroup;
import com.baiyi.opscloud.mapper.opscloud.OcWorkorderApprovalGroupMapper;
import com.baiyi.opscloud.service.workorder.OcWorkorderApprovalGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/6 2:33 下午
 * @Version 1.0
 */
@Service
public class OcWorkorderApprovalGroupServiceImpl implements OcWorkorderApprovalGroupService {

    @Resource
    private OcWorkorderApprovalGroupMapper ocWorkorderApprovalGroupMapper;

    @Override
    public OcWorkorderApprovalGroup queryOcWorkorderApprovalGroupById(int id) {
        return ocWorkorderApprovalGroupMapper.selectByPrimaryKey(id);

    }

}
