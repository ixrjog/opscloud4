package com.baiyi.opscloud.facade.aliyun;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunSLBVO;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 5:30 下午
 * @Since 1.0
 */

public interface AliyunSLBACLFacade {

    BusinessWrapper<Boolean> syncSLBACL();

    DataTable<AliyunSLBVO.AccessControl> queryAccessControlPage(AliyunSLBParam.ACLPageQuery pageQuery);

    List<AliyunSLBVO.AccessControlEntry> queryAccessControlEntry(String slbAclId);
}
