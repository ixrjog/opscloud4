package com.baiyi.opscloud.service.ram;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamPolicy;
import com.baiyi.opscloud.domain.param.cloud.AliyunRAMPolicyParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/10 10:50 上午
 * @Version 1.0
 */
public interface OcAliyunRamPolicyService {

    DataTable<OcAliyunRamPolicy> queryOcAliyunRamPolicyByParam(AliyunRAMPolicyParam.PageQuery pageQuery);

    List<OcAliyunRamPolicy> queryOcAliyunRamPolicyByAccountUid(String accountUid);

    OcAliyunRamPolicy queryOcAliyunRamPolicyByUniqueKey(String accountUid, String policyName);

    OcAliyunRamPolicy queryOcAliyunRamPolicyById(int id);

    void addOcAliyunRamPolicy(OcAliyunRamPolicy ocAliyunRamPolicy);

    void updateOcAliyunRamPolicy(OcAliyunRamPolicy ocAliyunRamPolicy);

    void deleteOcAliyunRamPolicyById(int id);

    List<OcAliyunRamPolicy> queryOcAliyunRamPolicyByUserPermission(String accountUid, Integer ramUserId);

    List<OcAliyunRamPolicy> queryUserTicketOcRamPolicyByParam(AliyunRAMPolicyParam.UserTicketOcRamPolicyQuery queryParam);

}
