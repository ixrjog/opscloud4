package com.baiyi.opscloud.service.server.impl;

import com.baiyi.opscloud.common.annotation.EventPublisher;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.types.EventActionTypeEnum;
import com.baiyi.opscloud.factory.business.base.AbstractBusinessService;
import com.baiyi.opscloud.mapper.opscloud.ServerGroupMapper;
import com.baiyi.opscloud.service.server.ServerGroupService;
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
@BusinessType(BusinessTypeEnum.SERVERGROUP)
@Service
public class ServerGroupServiceImpl extends AbstractBusinessService<ServerGroup> implements ServerGroupService {

    @Resource
    private ServerGroupMapper serverGroupMapper;

    @Override
    public ServerGroup getById(Integer id) {
        return serverGroupMapper.selectByPrimaryKey(id);
    }

    @Override
    public ServerGroup getByName(String name) {
        Example example = new Example(ServerGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        return serverGroupMapper.selectOneByExample(example);
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.CREATE)
    public void add(ServerGroup serverGroup) {
        serverGroupMapper.insert(serverGroup);
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.UPDATE)
    public void update(ServerGroup serverGroup) {
        serverGroupMapper.updateByPrimaryKey(serverGroup);
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.DELETE)
    public void delete(ServerGroup serverGroup) {
        serverGroupMapper.deleteByPrimaryKey(serverGroup.getId());
    }

    @Override
    public Integer countByServerGroupTypeId(Integer serverGroupTypeId) {
        Example example = new Example(ServerGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupTypeId", serverGroupTypeId);
        return serverGroupMapper.selectCountByExample(example);
    }

    @Override
    public DataTable<ServerGroup> queryPageByParam(ServerGroupParam.ServerGroupPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<ServerGroup> data = serverGroupMapper.queryServerGroupByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public DataTable<ServerGroup> queryPageByParam(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<ServerGroup> data = serverGroupMapper.queryUserPermissionServerGroupByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<ServerGroup> queryUserServerGroupTreeByParam(ServerGroupParam.UserServerTreeQuery queryParam) {
        return serverGroupMapper.queryUserServerGroupTreeByParam(queryParam);
    }
}
