package com.baiyi.opscloud.packer.application;

import com.baiyi.opscloud.common.annotation.BizDocWrapper;
import com.baiyi.opscloud.common.annotation.BizUserWrapper;
import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.ApplicationResource;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/7/12 1:23 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ApplicationPacker implements IWrapper<ApplicationVO.Application> {

    private final ApplicationResourceService applicationResourceService;

    private final ApplicationResourcePacker resourcePacker;

    private final ApplicationResourceDsInstancePacker applicationResourceInstancePacker;

    private final LeoJobService leoJobService;

    @Override
    @TagsWrapper
    @BizDocWrapper
    @BizUserWrapper
    public void wrap(ApplicationVO.Application application, IExtend iExtend) {
        if (!iExtend.getExtend()) {
            return;
        }
        List<ApplicationResource> applicationResourceList = applicationResourceService.queryByApplication(application.getId());
        List<ApplicationResourceVO.Resource> resources = BeanCopierUtil.copyListProperties(applicationResourceList, ApplicationResourceVO.Resource.class).stream()
                .peek(resourcePacker::wrapProperties)
                .toList();
        resources.forEach(applicationResourceInstancePacker::wrap);
        Map<String, List<ApplicationResourceVO.Resource>> resourcesMap = resources.stream()
                .collect(Collectors.groupingBy(ApplicationResourceVO.Resource::getResourceType));
        application.setResourceMap(resourcesMap);
        // LeoJob
        List<LeoJob> jobs = leoJobService.queryJobWithApplicationId(application.getId());
        application.setLeoJobs(BeanCopierUtil.copyListProperties(jobs, LeoJobVO.Job.class));
    }

    /**
     * 包装Kubernetes
     *
     * @param application
     */
    @Override
    @TagsWrapper(extend = true)
    @BizDocWrapper(extend = true)
    public void wrap(ApplicationVO.Application application) {
        List<ApplicationResource> resources = applicationResourceService.queryByApplication(application.getId(), DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name());
        List<ApplicationResourceVO.Resource> data = BeanCopierUtil.copyListProperties(resources, ApplicationResourceVO.Resource.class).stream()
                .peek(resourcePacker::wrap).collect(Collectors.toList());
        application.setResources(data);
    }

}
