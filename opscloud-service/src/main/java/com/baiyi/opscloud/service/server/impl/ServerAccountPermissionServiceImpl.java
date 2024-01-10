package com.baiyi.opscloud.service.server.impl;

import com.baiyi.opscloud.domain.generator.opscloud.ServerAccountPermission;
import com.baiyi.opscloud.mapper.ServerAccountPermissionMapper;
import com.baiyi.opscloud.service.server.ServerAccountPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/26 2:18 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class ServerAccountPermissionServiceImpl implements ServerAccountPermissionService {

    private final ServerAccountPermissionMapper accountPermissionMapper;

    @Override
    public void add(ServerAccountPermission permission) {
        accountPermissionMapper.insert(permission);
    }

    @Override
    public void deleteById(Integer id) {
        accountPermissionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<ServerAccountPermission> queryByServerId(Integer serverId) {
        Example example = new Example(ServerAccountPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverId", serverId);
        return accountPermissionMapper.selectByExample(example);
    }

    @Override
    public int countByServerAccountId(Integer serverAccountId) {
        Example example = new Example(ServerAccountPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverAccountId", serverAccountId);
        return accountPermissionMapper.selectCountByExample(example);
    }

}