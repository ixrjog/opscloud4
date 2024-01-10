package com.baiyi.opscloud.leo.handler.build.helper;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/11/14 11:36
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LeoJenkinsInstanceHelper {

    private final InstanceHelper instanceHelper;

    private final BusinessTagService bizTagService;

    private final TagService tagService;

    /**
     * 过滤数据源实例类型
     *
     * @return
     */
    private DsTypeEnum[] getFilterInstanceTypes() {
        return new DsTypeEnum[]{DsTypeEnum.JENKINS};
    }

    /**
     * Leo实例专属标签
     *
     * @return
     */
    private String getInstanceTag() {
        return TagConstants.LEO.getTag();
    }

    /**
     * 查询实例
     *
     * @return
     */
    private List<DatasourceInstance> listInstance() {
        return instanceHelper.listInstance(getFilterInstanceTypes(), getInstanceTag());
    }

    /**
     * 按标签查询可用实例
     *
     * @param tags LeoJob标签
     * @return
     */
    public List<DatasourceInstance> queryAvailableInstancesWithTags(List<String> tags) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            throw new LeoBuildException("无可用的Jenkins实例: 实例无效或没有 {} 标签！", getInstanceTag());
        }
        // tags为空，返回所有实例
        if (CollectionUtils.isEmpty(tags)) {
            return instances;
        }
        return instances.stream().filter(instance -> {
            List<String> instanceTags = getInstanceTags(instance.getId());
            return new HashSet<>(instanceTags).containsAll(tags);
        }).collect(Collectors.toList());
    }

    /**
     * 查询实例所有标签
     *
     * @param instanceId
     * @return
     */
    private List<String> getInstanceTags(int instanceId) {
        SimpleBusiness simpleBusiness = SimpleBusiness.builder()
                .businessId(instanceId)
                .businessType(BusinessTypeEnum.DATASOURCE_INSTANCE.getType())
                .build();
        return bizTagService.queryByBusiness(simpleBusiness).stream()
                .map(e -> tagService.getById(e.getTagId()).getTagKey())
                .collect(Collectors.toList());
    }

}