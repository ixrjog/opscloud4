package com.baiyi.opscloud.decorator.aliyun.slb;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAcl;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAclListener;
import com.baiyi.opscloud.domain.vo.cloud.AliyunSLBVO;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbAclListenerService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 5:32 下午
 * @Since 1.0
 */

@Component
public class AliyunSLBAccessControlDecorator {

    @Resource
    private OcAliyunSlbAclListenerService ocAliyunSlbAclListenerService;

    public AliyunSLBVO.AccessControl decoratorVO(OcAliyunSlbAcl ocAliyunSlbAcl) {
        AliyunSLBVO.AccessControl accessControl = BeanCopierUtils.copyProperties(ocAliyunSlbAcl, AliyunSLBVO.AccessControl.class);
        List<OcAliyunSlbAclListener> aclListenerList = ocAliyunSlbAclListenerService.queryOcAliyunSlbAclListenerBySlbAclId(ocAliyunSlbAcl.getSlbAclId());
        accessControl.setAclListenerList(aclListenerList);
        return accessControl;
    }

    public List<AliyunSLBVO.AccessControl> decoratorVOList(List<OcAliyunSlbAcl> ocAliyunSlbAclList) {
        List<AliyunSLBVO.AccessControl> aclList = Lists.newArrayListWithCapacity(ocAliyunSlbAclList.size());
        ocAliyunSlbAclList.forEach(acl -> aclList.add(decoratorVO(acl)));
        return aclList;
    }
}
