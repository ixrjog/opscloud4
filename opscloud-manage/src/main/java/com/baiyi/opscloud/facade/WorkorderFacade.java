package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.workorder.WorkorderGroupParam;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderGroupVO;

/**
 * @Author baiyi
 * @Date 2020/4/26 1:13 下午
 * @Version 1.0
 */
public interface WorkorderFacade {

    DataTable<OcWorkorderGroupVO.WorkorderGroup> queryWorkorderGroupPage(WorkorderGroupParam.PageQuery pageQuery);
}
