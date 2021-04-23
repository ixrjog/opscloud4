package com.baiyi.opscloud.facade.helpdesk.impl;

import com.baiyi.opscloud.convert.helpdesk.HelpdeskReportConvert;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcHelpdeskReport;
import com.baiyi.opscloud.domain.param.helpdesk.HelpdeskReportParam;
import com.baiyi.opscloud.domain.vo.helpdesk.HelpdeskReportVO;
import com.baiyi.opscloud.facade.helpdesk.HelpdeskFacade;
import com.baiyi.opscloud.service.helpdesk.OcHelpdeskReportService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/4 3:22 下午
 * @Since 1.0
 */

@Component("HelpdeskFacade")
public class HelpdeskFacadeImpl implements HelpdeskFacade {

    @Resource
    private OcHelpdeskReportService ocHelpdeskReportService;

    @Override
    public DataTable<HelpdeskReportVO.HelpdeskReport> helpdeskReportPage(HelpdeskReportParam.PageQuery pageQuery) {
        DataTable<OcHelpdeskReport> table = ocHelpdeskReportService.OcHelpdeskReportPage(pageQuery);
        List<HelpdeskReportVO.HelpdeskReport> page = HelpdeskReportConvert.toVOList(table.getData());
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> saveHelpdeskReport(HelpdeskReportParam.SaveHelpdeskReport param) {
        try {
            ocHelpdeskReportService.addOcHelpdeskReportList(param.getHelpdeskReportList());
            return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorEnum.HELPDESK_SAVE_FAIL);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delHelpdeskReport(int id) {
        ocHelpdeskReportService.deleteHelpdeskReport(id);
        return BusinessWrapper.SUCCESS;
    }
}
