package com.baiyi.opscloud.facade.application.impl;

import com.baiyi.opscloud.common.base.AccessLevel;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.ApplicationResource;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.param.application.ApplicationResourceParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.facade.application.ApplicationFacade;
import com.baiyi.opscloud.factory.resource.ApplicationResourceQueryFactory;
import com.baiyi.opscloud.factory.resource.IApplicationResourceQuery;
import com.baiyi.opscloud.packer.application.ApplicationPacker;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.auth.AuthRoleService;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * @Author baiyi
 * @Date 2021/7/12 12:58 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class ApplicationFacadeImpl implements ApplicationFacade {

    private final ApplicationService applicationService;

    private final ApplicationResourceService applicationResourceService;

    private final ApplicationPacker applicationPacker;

    private final UserService userService;

    private final AuthRoleService authRoleService;

    @Override
    public DataTable<ApplicationVO.Application> queryApplicationPage(ApplicationParam.ApplicationPageQuery pageQuery) {
        DataTable<Application> table = applicationService.queryPageByParam(pageQuery);
        return new DataTable<>(applicationPacker.wrapVOList(table.getData(), pageQuery), table.getTotalNum());
    }

    @Override
    public DataTable<ApplicationVO.Application> queryApplicationPageByWebTerminal(ApplicationParam.UserPermissionApplicationPageQuery pageQuery) {
        pageQuery.setUserId(isAdmin(SessionUtil.getUsername()) ? null : userService.getByUsername(SessionUtil.getUsername()).getId());
        DataTable<Application> table = applicationService.queryPageByParam(pageQuery);
        return new DataTable<>(applicationPacker.wrapVOListByKubernetes(table.getData()), table.getTotalNum());
    }

    /**
     * OPS角色以上即认定为系统管理员
     *
     * @return
     */
    private boolean isAdmin(String username) {
        int accessLevel = authRoleService.getRoleAccessLevelByUsername(username);
        return accessLevel >= AccessLevel.OPS.getLevel();
    }

    @Override
    public DataTable<ApplicationResourceVO.Resource> previewApplicationResourcePage(ApplicationResourceParam.ResourcePageQuery pageQuery) {
        IApplicationResourceQuery iApplicationResourceQuery
                = ApplicationResourceQueryFactory.getIApplicationResourceQuery(pageQuery.getApplicationResType(), pageQuery.getBusinessType());
        if (iApplicationResourceQuery == null)
            throw new CommonRuntimeException("无法预览应用资源，未找到对应的方法！");
        return iApplicationResourceQuery.queryResourcePage(pageQuery);
    }

    @Override
    public ApplicationVO.Application getApplicationById(Integer id) {
        Application application = applicationService.getById(id);
        if (application == null)
            throw new CommonRuntimeException(ErrorEnum.APPLICATION_NOT_EXIST);
        return applicationPacker.wrapVO(application, SimpleExtend.EXTEND);
    }

    @Override
    public void addApplication(ApplicationVO.Application application) {
        if (applicationService.getByKey(application.getApplicationKey()) != null)
            throw new CommonRuntimeException(ErrorEnum.APPLICATION_ALREADY_EXIST);
        Application app = BeanCopierUtil.copyProperties(application, Application.class);
        applicationService.add(app);
    }

    @Override
    public void updateApplication(ApplicationVO.Application application) {
        Application app = applicationService.getByKey(application.getApplicationKey());
        if (app == null)
            throw new CommonRuntimeException(ErrorEnum.APPLICATION_ALREADY_EXIST);
        app.setComment(application.getComment());
        app.setName(application.getName());
        applicationService.update(app);
    }

    @Override
    public void deleteApplication(Integer id) {
        if (!CollectionUtils.isEmpty(applicationResourceService.queryByApplication(id)))
            throw new CommonRuntimeException(ErrorEnum.APPLICATION_RES_IS_NOT_EMPTY);
        applicationService.deleteById(id);
    }

    @Override
    public void bindApplicationResource(ApplicationResourceVO.Resource resource) {
        if (applicationResourceService.getByUniqueKey(resource.getApplicationId(), resource.getBusinessType(), resource.getBusinessId()) != null)
            throw new CommonRuntimeException(ErrorEnum.APPLICATION_RES_ALREADY_EXIST);
        ApplicationResource res = BeanCopierUtil.copyProperties(resource, ApplicationResource.class);
        applicationResourceService.add(res);
    }

    @Override
    public void unbindApplicationResource(Integer id) {
        applicationResourceService.delete(id);
    }
}
