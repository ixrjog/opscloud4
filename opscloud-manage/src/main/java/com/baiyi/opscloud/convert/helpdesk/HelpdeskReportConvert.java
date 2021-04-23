package com.baiyi.opscloud.convert.helpdesk;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcHelpdeskReport;
import com.baiyi.opscloud.domain.vo.helpdesk.HelpdeskReportVO;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/4 3:00 下午
 * @Since 1.0
 */
public class HelpdeskReportConvert {

    private static HelpdeskReportVO.HelpdeskReport toVO(OcHelpdeskReport ocHelpdeskReport) {
        HelpdeskReportVO.HelpdeskReport helpdeskReport = BeanCopierUtils.copyProperties(ocHelpdeskReport, HelpdeskReportVO.HelpdeskReport.class);
        helpdeskReport.setWeeks(TimeUtils.whatWeek(ocHelpdeskReport.getHelpdeskTime()));
        return helpdeskReport;
    }

    public static List<HelpdeskReportVO.HelpdeskReport> toVOList(List<OcHelpdeskReport> ocHelpdeskReportList) {
        List<HelpdeskReportVO.HelpdeskReport> helpdeskReportList = Lists.newArrayListWithCapacity(ocHelpdeskReportList.size());
        ocHelpdeskReportList.forEach(ocHelpdeskReport -> helpdeskReportList.add(toVO(ocHelpdeskReport)));
        return helpdeskReportList;
    }

}
