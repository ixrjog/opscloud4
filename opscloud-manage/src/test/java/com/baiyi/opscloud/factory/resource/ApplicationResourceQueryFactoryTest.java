package com.baiyi.opscloud.factory.resource;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.application.ApplicationResourceParam;
import com.baiyi.opscloud.domain.types.ApplicationResTypeEnum;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
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
        IApplicationResourceQuery iApplicationResourceQuery
                = ApplicationResourceQueryFactory.getIApplicationResourceQuery(ApplicationResTypeEnum.SERVERGROUP.name(), BusinessTypeEnum.SERVERGROUP.getType());
        if (iApplicationResourceQuery != null) {
            System.err.print(iApplicationResourceQuery.getApplicationResType());
            ApplicationResourceParam.ResourcePageQuery pageQuery = ApplicationResourceParam.ResourcePageQuery.builder()
                    .applicationId(22)
                    .queryName("user")
                    .build();
            pageQuery.setPage(1);
            pageQuery.setLength(10);
            DataTable<ApplicationResourceVO.Resource> table = iApplicationResourceQuery.queryResourcePage(pageQuery);
            System.err.print(JSON.toJSONString(table));
        }
    }

}