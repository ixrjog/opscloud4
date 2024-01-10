package com.baiyi.opscloud.service.workevent.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.WorkEvent;
import com.baiyi.opscloud.domain.param.workevent.WorkEventParam;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import com.baiyi.opscloud.mapper.WorkEventMapper;
import com.baiyi.opscloud.service.workevent.WorkEventService;
import com.baiyi.opscloud.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
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
    public void addList(List<WorkEvent> workEventList) {
        workEventMapper.insertList(workEventList);
    }

    @Override
    public void add(WorkEvent workEvent) {
        workEventMapper.insert(workEvent);
    }

    @Override
    public void update(WorkEvent workEvent) {
        workEventMapper.updateByPrimaryKey(workEvent);
    }

    @Override
    public void deleteById(Integer id) {
        workEventMapper.deleteByPrimaryKey(id);
    }

    @Override
    public WorkEvent getById(Integer id) {
        return workEventMapper.selectByPrimaryKey(id);
    }

    @Override
    public DataTable<WorkEvent> queryPageByParam(WorkEventParam.WorkEventPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        return new DataTable<>(workEventMapper.queryPageByParam(pageQuery),page.getTotal());
    }

    @Deprecated
    public DataTable<WorkEvent> queryPageByParam2(WorkEventParam.WorkEventPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(WorkEvent.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            criteria.andLike("comment", SQLUtil.toLike(pageQuery.getQueryName()));
        }
        if (!pageQuery.getWorkRoleId().equals(-1)) {
            criteria.andEqualTo("workRoleId", pageQuery.getWorkRoleId());
        }
        if (!CollectionUtils.isEmpty(pageQuery.getWorkItemIdList())) {
            criteria.andIn("workItemId", pageQuery.getWorkItemIdList());
        }
        if (StringUtils.isNotBlank(pageQuery.getUsername())) {
            criteria.andEqualTo("username", pageQuery.getUsername());
        }
        if (!ObjectUtils.isEmpty(pageQuery.getWorkEventStartTime()) && !ObjectUtils.isEmpty(pageQuery.getWorkEventEndTime())) {
            criteria.andBetween("workEventTime", pageQuery.getWorkEventStartTime(), pageQuery.getWorkEventEndTime());
        }
        example.setOrderByClause("work_event_time desc, id desc");
        List<WorkEvent> data = workEventMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<ReportVO.Report> queryWeek(Integer workRoleId) {
        return workEventMapper.queryWeek(workRoleId);
    }

    @Override
    public List<ReportVO.Report> queryWeekByItem(Integer workRoleId, Integer workItemId) {
        return workEventMapper.queryWeekByItem(workRoleId, workItemId);
    }

    @Override
    public List<ReportVO.Report> getWorkEventItemReport(Integer workRoleId) {
        return workEventMapper.getWorkEventItemReport(workRoleId);
    }

    @Override
    public List<ReportVO.CommonReport> getWorkEventTimeReport() {
        return workEventMapper.getWorkEventTimeReport();
    }

    @Override
    public List<ReportVO.CommonReport> getWorkEventInterceptReport() {
        return workEventMapper.getWorkEventInterceptReport();
    }

    @Override
    public List<ReportVO.CommonReport> getWorkEventSolveReport() {
        return workEventMapper.getWorkEventSolveReport();
    }

    @Override
    public List<ReportVO.CommonReport> getWorkEventFaultReport() {
        return workEventMapper.getWorkEventFaultReport();
    }

}