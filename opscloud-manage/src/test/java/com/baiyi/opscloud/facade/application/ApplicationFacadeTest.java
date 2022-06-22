package com.baiyi.opscloud.facade.application;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/6/22 10:51
 * @Version 1.0
 */
class ApplicationFacadeTest extends BaseUnit {

    @Resource
    private ApplicationFacade applicationFacade;

    @Test
    void queryPageTest() {
        SessionUtil.setUsername("baiyi");
        UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery =UserBusinessPermissionParam.UserBusinessPermissionPageQuery.builder()
                .queryName("trade")
                .extend(false)
                .page(1)
                .length(5)
                .build();
        DataTable<ApplicationVO.Application> dataTable = applicationFacade.queryApplicationKubernetesPage(pageQuery);
        print(dataTable);
    }

}