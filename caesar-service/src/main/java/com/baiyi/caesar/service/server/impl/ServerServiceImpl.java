package com.baiyi.caesar.service.server.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.domain.param.server.ServerParam;
import com.baiyi.caesar.mapper.caesar.ServerMapper;
import com.baiyi.caesar.service.server.ServerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/24 9:39 上午
 * @Version 1.0
 */
@Service
public class ServerServiceImpl implements ServerService {

    @Resource
    private ServerMapper serverMapper;

    @Override
    public Server getById(Integer id) {
        return serverMapper.selectByPrimaryKey(id);
    }

    @Override
    public DataTable<Server> queryServerPage(ServerParam.ServerPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<Server> data = serverMapper.queryServerByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public void add(Server server) {
        serverMapper.insert(server);
    }

    @Override
    public void update(Server server) {
        serverMapper.updateByPrimaryKey(server);
    }

    @Override
    public Server getMaxSerialNumberServer(Integer serverGroupId, Integer envType) {
        Example example = new Example(Server.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupId", serverGroupId);
        criteria.andEqualTo("envType", envType);
        example.setOrderByClause("serial_number desc limit 1");
        return serverMapper.selectOneByExample(example);
    }

    @Override
    public int countByServerGroupId(Integer serverGroupId) {
        Example example = new Example(Server.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupId", serverGroupId);
        return serverMapper.selectCountByExample(example);
    }

    @Override
    public List<Server> queryByServerGroupId(Integer serverGroupId) {
        Example example = new Example(Server.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupId", serverGroupId);
        return serverMapper.selectByExample(example);
    }
}
