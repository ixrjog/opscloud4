package com.baiyi.opscloud.packer.application;

import com.baiyi.opscloud.common.type.DsAssetTypeEnum;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.kubernetes.provider.KubernetesPodProvider;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.ApplicationResource;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/12 1:23 下午
 * @Version 1.0
 */
@Component
public class ApplicationPacker {

    @Resource
    private KubernetesPodProvider kubernetesPodProvider;

    @Resource
    private ApplicationResourceService applicationResourceService;

    public List<ApplicationVO.Application> wrapVOList(List<Application> data) {
        return BeanCopierUtil.copyListProperties(data, ApplicationVO.Application.class);
    }

    public List<ApplicationVO.Application> wrapVOList(List<Application> data, IExtend iExtend) {
        return BeanCopierUtil.copyListProperties(data, ApplicationVO.Application.class);
    }

    /**
     * Kubernetes专业
     *
     * @param data
     * @return
     */
    public List<ApplicationVO.Application> wrapVOListByKubernetes(List<Application> data) {
        List<ApplicationVO.Application> voList = toVOList(data);
        voList.forEach(e -> {
            List<ApplicationResource> resources = applicationResourceService.queryByApplication(e.getId(), DsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
            //resources.forEach(r-> wrapPodByDeployment());


        });
        return voList;
    }


    private void wrapPodByDeployment(ApplicationVO.Application applicationm, ApplicationResource resource) {
      //  kubernetesPodProvider.queryAssets()
    }


    private List<ApplicationVO.Application> toVOList(List<Application> data) {
        return BeanCopierUtil.copyListProperties(data, ApplicationVO.Application.class);
    }

}
