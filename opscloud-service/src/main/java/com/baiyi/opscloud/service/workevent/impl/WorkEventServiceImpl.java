package com.baiyi.opscloud.service.workevent.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.WorkEvent;
import com.baiyi.opscloud.domain.param.workevent.WorkEventParam;
import com.baiyi.opscloud.mapper.opscloud.WorkEventMapper;
import com.baiyi.opscloud.service.workevent.WorkEventService;
import com.baiyi.opscloud.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/8/5 4:22 PM
 * @Since 1.0
 */

@Service
@RequiredArgsConstructor
public class WorkEventServiceImpl implements WorkEventService {

    private final WorkEventMapper workEventMapper;

    @Override
    public void add(WorkEvent workEvent) {
        workEventMapper.insert(workEvent);
    }

    @Override
    public void deleteById(Integer id) {
        workEventMapper.deleteByPrimaryKey(id);
    }

    @Override
    public DataTable<WorkEvent> queryPageByParam(WorkEventParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(Application.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            criteria.andLike("comment", SQLUtil.toLike(pageQuery.getQueryName()));
        }
        if (-1 != pageQuery.getWorkRoleId()) {
            criteria.andEqualTo("workRoleId", pageQuery.getWorkRoleId());
        }
        example.setOrderByClause("create_time");
        List<WorkEvent> data = workEventMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }
}
