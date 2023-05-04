package com.baiyi.opscloud.facade.leo;

import com.baiyi.opscloud.domain.vo.leo.LeoReportVO;

/**
 * @Author baiyi
 * @Date 2023/1/11 18:25
 * @Version 1.0
 */
public interface LeoReportFacade {

    LeoReportVO.LeoReport statLeoReport();

    LeoReportVO.LeoProdReport statLeoProdReport();

}
