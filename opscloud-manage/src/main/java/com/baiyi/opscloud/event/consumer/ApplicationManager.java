package com.baiyi.opscloud.event.consumer;

import com.baiyi.opscloud.datasource.manager.base.IManager;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.facade.application.ApplicationAlertFacade;
import com.baiyi.opscloud.service.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/9/5 18:35
 * @Version 1.0
 */

@Component
@RequiredArgsConstructor
public class ApplicationManager implements IManager<BaseBusiness.IBusiness> {

    private final ApplicationAlertFacade applicationAlertFacade;

    private final ApplicationService applicationService;

    @Override
    public void create(BaseBusiness.IBusiness businessResource) {
        handle(businessResource);
    }

    @Override
    public void update(BaseBusiness.IBusiness businessResource) {
        handle(businessResource);
    }

    @Override
    public void delete(BaseBusiness.IBusiness businessResource) {
        handle(businessResource);
    }

    @Override
    public void grant(User user, BaseBusiness.IBusiness businessResource) {
    }

    @Override
    public void revoke(User user, BaseBusiness.IBusiness businessResource) {
    }

    private void handle(BaseBusiness.IBusiness businessResource){
        Application application = applicationService.getById(businessResource.getBusinessId());
        if (application == null) {
            return;
        }
        applicationAlertFacade.refreshCache(application.getName());
    }

}