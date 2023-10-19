package com.baiyi.opscloud.facade.application;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/6/22 10:51
 * @Version 1.0
 */
class ApplicationFacadeTest extends BaseUnit {

    @Resource
    private ApplicationKubernetesFacade kubernetesFacade;

    @Test
    void queryPageTest() {
        SessionHolder.setUsername("baiyi");
        UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery =UserBusinessPermissionParam.UserBusinessPermissionPageQuery.builder()
                .queryName("trade")
                .extend(false)
                .page(1)
                .length(5)
                .build();
      //  DataTable<ApplicationVO.Application> dataTable = kubernetesFacade.queryKubernetesPage(pageQuery);
     //   print(dataTable);
    }

}