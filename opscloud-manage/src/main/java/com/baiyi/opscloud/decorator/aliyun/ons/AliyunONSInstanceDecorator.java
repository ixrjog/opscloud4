package com.baiyi.opscloud.decorator.aliyun.ons;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsInstance;
import com.baiyi.opscloud.domain.vo.cloud.AliyunONSVO;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsGroupService;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsTopicService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/5 3:48 下午
 * @Since 1.0
 */

@Component("AliyunONSInstanceDecorator")
public class AliyunONSInstanceDecorator {

    @Resource
    private OcAliyunOnsTopicService ocAliyunOnsTopicService;

    @Resource
    private OcAliyunOnsGroupService ocAliyunOnsGroupService;

    public AliyunONSVO.Instance decoratorVO(OcAliyunOnsInstance ocAliyunOnsInstance) {
        AliyunONSVO.Instance instance = BeanCopierUtils.copyProperties(ocAliyunOnsInstance, AliyunONSVO.Instance.class);
        instance.setGroupIdTotal(ocAliyunOnsGroupService.countOcAliyunOnsGroupByInstanceId(ocAliyunOnsInstance.getInstanceId()));
        instance.setTopicTotal(ocAliyunOnsTopicService.countOcAliyunOnsTopicByInstanceId(ocAliyunOnsInstance.getInstanceId()));
        return instance;
    }

    public List<AliyunONSVO.Instance> decoratorVOList(List<OcAliyunOnsInstance> ocAliyunOnsInstance) {
        List<AliyunONSVO.Instance> instanceList = Lists.newArrayListWithCapacity(ocAliyunOnsInstance.size());
        ocAliyunOnsInstance.forEach(instance -> instanceList.add(decoratorVO(instance)));
        return instanceList;
    }
}
