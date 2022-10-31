package com.baiyi.opscloud.factory.resource;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.ApplicationResTypeEnum;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.param.application.ApplicationResourceParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import org.junit.jupiter.api.Test;


/**
 * @Author baiyi
 * @Date 2021/9/8 5:00 下午
 * @Version 1.0
 */
class ApplicationResourceQueryFactoryTest extends BaseUnit {

    @Test
    void applicationResourceQueryFactoryTest() {
        IAppResQuery iApplicationResourceQuery
                = AppResQueryFactory.getAppResQuery(ApplicationResTypeEnum.SERVERGROUP.name(), BusinessTypeEnum.SERVERGROUP.getType());
        if (iApplicationResourceQuery != null) {
            System.err.print(iApplicationResourceQuery.getAppResType());
            ApplicationResourceParam.ResourcePageQuery pageQuery = ApplicationResourceParam.ResourcePageQuery.builder()
                    .applicationId(22)
                    .queryName("user")
                    .page(1)
                    .length(10)
                    .build();
            DataTable<ApplicationResourceVO.Resource> table = iApplicationResourceQuery.queryResourcePage(pageQuery);
        }
    }

}