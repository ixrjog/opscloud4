package com.baiyi.caesar.service.server.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.ServerGroup;
import com.baiyi.caesar.domain.param.server.ServerGroupParam;
import com.baiyi.caesar.mapper.caesar.ServerGroupMapper;
import com.baiyi.caesar.service.server.ServerGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/24 1:01 下午
 * @Version 1.0
 */
@Service
public class ServerGroupServiceImpl implements ServerGroupService {

    @Resource
    private ServerGroupMapper serverGroupMapper;

    @Override
    public ServerGroup getById(Integer id) {
        return serverGroupMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(ServerGroup serverGroup) {
        serverGroupMapper.insert(serverGroup);
    }

    @Override
    public void update(ServerGroup serverGroup) {
        serverGroupMapper.updateByPrimaryKey(serverGroup);
    }

    @Override
    public Integer countByServerGroupTypeId(Integer serverGroupTypeId) {
        Example example = new Example(ServerGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupTypeId", serverGroupTypeId);
        return serverGroupMapper.selectCountByExample(example);
    }

    @Override
    public DataTable<ServerGroup> queryServerGroupPage(ServerGroupParam.ServerGroupPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<ServerGroup> data = serverGroupMapper.queryServerGroupByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }
}
