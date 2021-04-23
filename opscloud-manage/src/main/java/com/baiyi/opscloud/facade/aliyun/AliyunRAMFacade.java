package com.baiyi.opscloud.facade.aliyun;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamUser;
import com.baiyi.opscloud.domain.param.cloud.AliyunRAMPolicyParam;
import com.baiyi.opscloud.domain.param.cloud.AliyunRAMUserParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunRAMVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/9 5:35 下午
 * @Version 1.0
 */
public interface AliyunRAMFacade {

    DataTable<AliyunRAMVO.RAMUser> queryRAMUserPage(AliyunRAMUserParam.RamUserPageQuery pageQuery);

    DataTable<AliyunRAMVO.RAMPolicy> queryRAMPolicyPage(AliyunRAMPolicyParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> syncRAMUser();

    BusinessWrapper<Boolean> syncRAMPolicy();

    BusinessWrapper<Boolean> setRAMPolicyWorkorderById(int id);

    void delRAMUserByUsername(String username);

    List<AliyunRAMVO.RAMUser> queryRamUsersByUsername(int userId);

    BusinessWrapper<Boolean> attachPolicyToUser(OcAliyunRamUser ocAliyunRamUser, AliyunRAMVO.RAMPolicy ramPolicy);
}
