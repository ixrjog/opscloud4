package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.vo.leo.LeoReportVO;
import com.baiyi.opscloud.facade.leo.LeoReportFacade;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2023/1/11 18:47
 * @Version 1.0
 */
class LeoReportFacadeImplTest extends BaseUnit {

    @Resource
    private LeoReportFacade leoReportFacade;

    @Test
    void reportTest() {
        LeoReportVO.LeoReport report = leoReportFacade.statLeoReport();
        print(report);
    }

}