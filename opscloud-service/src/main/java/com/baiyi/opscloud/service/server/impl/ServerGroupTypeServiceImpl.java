package com.baiyi.opscloud.service.server.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroupType;
import com.baiyi.opscloud.domain.param.server.ServerGroupTypeParam;
import com.baiyi.opscloud.mapper.ServerGroupTypeMapper;
import com.baiyi.opscloud.service.server.ServerGroupTypeService;
import com.baiyi.opscloud.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/24 10:44 上午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class ServerGroupTypeServiceImpl implements ServerGroupTypeService {

    private final ServerGroupTypeMapper serverGroupTypeMapper;

    @Override
    public DataTable<ServerGroupType> queryPageByParam(ServerGroupTypeParam.ServerGroupTypePageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(ServerGroupType.class);
        if (StringUtils.isNotBlank(pageQuery.getName())) {
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("name", SQLUtil.toLike(pageQuery.getName()));
        }
        example.setOrderByClause("create_time");
        List<ServerGroupType> data = serverGroupTypeMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public void update(ServerGroupType serverGroupType) {
        serverGroupTypeMapper.updateByPrimaryKey(serverGroupType);
    }

    @Override
    public void add(ServerGroupType serverGroupType) {
        serverGroupTypeMapper.insert(serverGroupType);
    }

    @Override
    public void deleteById(Integer id) {
        serverGroupTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public ServerGroupType getById(Integer id) {
        return serverGroupTypeMapper.selectByPrimaryKey(id);
    }

    @Override
    public ServerGroupType getByName(String name) {
        Example example = new Example(ServerGroupType.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("name", name);
        return serverGroupTypeMapper.selectOneByExample(example);
    }

}