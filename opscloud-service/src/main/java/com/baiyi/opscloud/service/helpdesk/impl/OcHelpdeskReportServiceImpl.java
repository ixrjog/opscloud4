package com.baiyi.opscloud.service.helpdesk.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcHelpdeskReport;
import com.baiyi.opscloud.domain.param.helpdesk.HelpdeskReportParam;
import com.baiyi.opscloud.domain.vo.dashboard.HeplDeskGroupByType;
import com.baiyi.opscloud.domain.vo.dashboard.HeplDeskGroupByWeek;
import com.baiyi.opscloud.mapper.opscloud.OcHelpdeskReportMapper;
import com.baiyi.opscloud.service.helpdesk.OcHelpdeskReportService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/11/23 11:24 上午
 * @Version 1.0
 */
@Service
public class OcHelpdeskReportServiceImpl implements OcHelpdeskReportService {

    @Resource
    private OcHelpdeskReportMapper ocHelpdeskReportMapper;

    @Override
    public List<HeplDeskGroupByWeek> queryHelpdeskGroupByWeek() {
        return ocHelpdeskReportMapper.queryHelpdeskGroupByWeek();
    }

    @Override
    public List<HeplDeskGroupByType> queryHelpdeskGroupByType() {
        return ocHelpdeskReportMapper.queryHelpdeskGroupByType();
    }

    @Override
    public DataTable<OcHelpdeskReport> OcHelpdeskReportPage(HelpdeskReportParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcHelpdeskReport> helpdeskReportList;
        Example example = new Example(OcHelpdeskReport.class);
        Example.Criteria criteria = example.createCriteria();
        if (!pageQuery.getHelpdeskType().equals(-1))
            criteria.andEqualTo("helpdeskType", pageQuery.getHelpdeskType());
        example.setOrderByClause("helpdesk_time DESC");
        helpdeskReportList = ocHelpdeskReportMapper.selectByExample(example);
        return new DataTable<>(helpdeskReportList, page.getTotal());
    }

    @Override
    public void addOcHelpdeskReportList(List<OcHelpdeskReport> ocHelpdeskReportList) {
        ocHelpdeskReportMapper.insertList(ocHelpdeskReportList);
    }

    @Override
    public void deleteHelpdeskReport(int id) {
        ocHelpdeskReportMapper.deleteByPrimaryKey(id);
    }
}
