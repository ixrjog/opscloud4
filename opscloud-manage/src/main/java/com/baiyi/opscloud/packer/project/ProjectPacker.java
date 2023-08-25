package com.baiyi.opscloud.packer.project;

import com.baiyi.opscloud.common.annotation.BizDocWrapper;
import com.baiyi.opscloud.common.annotation.BizUserWrapper;
import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.FunctionUtil;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.ProjectResource;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.IRelation;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.project.ProjectResourceVO;
import com.baiyi.opscloud.domain.vo.project.ProjectVO;
import com.baiyi.opscloud.packer.IWrapperRelation;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import com.baiyi.opscloud.service.project.ProjectResourceService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author 修远
 * @Date 2023/5/18 5:22 PM
 * @Since 1.0
 */

@Component
@RequiredArgsConstructor
public class ProjectPacker implements IWrapperRelation<ProjectVO.Project> {

    private final ProjectResourcePacker resourcePacker;

    private final ProjectResourceService projectResourceService;

    private final ApplicationService applicationService;

    private final LeoDeployService leoDeployService;

    @Override
    @TagsWrapper
    @BizDocWrapper
    @BizUserWrapper
    public void wrap(ProjectVO.Project project, IExtend iExtend, IRelation iRelation) {
        if (!iExtend.getExtend()) {
            return;
        }
        List<ProjectResourceVO.Resource> assetList = Lists.newArrayList();
        List<ProjectResourceVO.Resource> applicationList = Lists.newArrayList();
        List<ProjectResource> projectResourceList = projectResourceService.listByProjectId(project.getId());
        projectResourceList.forEach(res ->
                FunctionUtil.isTureOrFalse(res.getBusinessType() == BusinessTypeEnum.ASSET.getType())
                        .withBoolean(
                                () -> {
                                    ProjectResourceVO.Resource resource = BeanCopierUtil.copyProperties(res, ProjectResourceVO.Resource.class);
                                    resourcePacker.wrap(resource, iExtend, iRelation);
                                    assetList.add(resource);
                                },
                                () -> {
                                    Application application = applicationService.getById(res.getBusinessId());
                                    ProjectResourceVO.Resource resource = BeanCopierUtil.copyProperties(res, ProjectResourceVO.Resource.class);
                                    resource.setApplication(application);
                                    applicationList.add(resource);
                                }
                        )
        );
        Map<String, List<ProjectResourceVO.Resource>> resourcesMap = assetList.stream()
                .collect(Collectors.groupingBy(ProjectResourceVO.Resource::getResourceType));
        project.setResourceMap(resourcesMap);
        project.setApplicationList(applicationList);
        // 部署次数
        project.setDeployCount(leoDeployService.countByProjectId(project.getId()));
        Map<String, Integer> envDeployCount = Maps.newHashMap();
        envDeployCount.put(Global.ENV_PROD,leoDeployService.countByEnvProjectId(project.getId(),4));
        project.setEnvDeployCount( envDeployCount);
    }

    @Override
    public void wrap(DsAssetVO.Asset asset, IExtend iExtend) {
    }

}
