package com.baiyi.opscloud.service.task.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.ServerTask;
import com.baiyi.opscloud.domain.param.task.ServerTaskParam;
import com.baiyi.opscloud.mapper.ServerTaskMapper;
import com.baiyi.opscloud.service.task.ServerTaskService;
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
 * @Date 2021/9/18 5:19 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class ServerTaskServiceImpl implements ServerTaskService {

    private final ServerTaskMapper serverTaskMapper;

    @Override
    public DataTable<ServerTask> queryServerTaskPage(ServerTaskParam.ServerTaskPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(ServerTask.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isBlank(pageQuery.getQueryName())) {
            String likeName = SQLUtil.toLike(pageQuery.getQueryName());
            criteria.orLike("taskUuid", likeName)
                    .orLike("username", likeName);
        }
        if (pageQuery.getFinalized() != null) {
            Example.Criteria criteria2 = example.createCriteria();
            criteria2.andEqualTo("finalized", pageQuery.getFinalized());
            example.and(criteria2);
        }
        example.setOrderByClause("create_time desc");
        List<ServerTask> data = serverTaskMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public void add(ServerTask serverTask) {
        serverTaskMapper.insert(serverTask);
    }

    @Override
    public void update(ServerTask serverTask) {
        serverTaskMapper.updateByPrimaryKey(serverTask);
    }

}