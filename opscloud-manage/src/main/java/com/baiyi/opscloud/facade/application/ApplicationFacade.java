package com.baiyi.opscloud.facade.application;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.param.application.ApplicationResourceParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;

/**
 * @Author baiyi
 * @Date 2021/7/12 12:58 下午
 * @Version 1.0
 */
public interface ApplicationFacade {

    DataTable<ApplicationVO.Application> queryApplicationPage(ApplicationParam.ApplicationPageQuery pageQuery);

    DataTable<ApplicationVO.Application> queryMyApplicationPage(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery);

    DataTable<ApplicationResourceVO.Resource> previewApplicationResourcePage(ApplicationResourceParam.ResourcePageQuery pageQuery);

    ApplicationVO.Application getApplicationById(Integer id);

    void addApplication(ApplicationParam.AddApplication addApplication);

    void updateApplication(ApplicationParam.UpdateApplication updateApplication);

    void deleteApplication(Integer id);

    void bindApplicationResource(ApplicationResourceParam.Resource resource);

    void unbindApplicationResource(Integer id);

}
