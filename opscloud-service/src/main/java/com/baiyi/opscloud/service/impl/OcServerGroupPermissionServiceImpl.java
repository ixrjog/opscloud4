package com.baiyi.opscloud.service.impl;

import com.baiyi.opscloud.domain.generator.OcServerGroupPermission;
import com.baiyi.opscloud.mapper.OcServerGroupPermissionMapper;
import com.baiyi.opscloud.service.OcServerGroupPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/8 9:37 上午
 * @Version 1.0
 */
@Service
public class OcServerGroupPermissionServiceImpl implements OcServerGroupPermissionService {

    @Resource
    private OcServerGroupPermissionMapper ocServerGroupPermissionMapper;

    @Override
    public void addOcServerGroupPermission(OcServerGroupPermission ocServerGroupPermission) {
        ocServerGroupPermissionMapper.insert(ocServerGroupPermission);
    }

    @Override
    public void delOcServerGroupPermission(int id) {
        ocServerGroupPermissionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<OcServerGroupPermission> queryUserServerGroupPermission(int userId) {
        return ocServerGroupPermissionMapper.queryUserServerGroupPermission(userId);
    }


}
