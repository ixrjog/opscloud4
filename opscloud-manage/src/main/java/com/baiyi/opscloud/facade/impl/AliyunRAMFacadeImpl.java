package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import com.baiyi.opscloud.aliyun.ram.handler.AliyunRAMUserPolicyHandler;
import com.baiyi.opscloud.cloud.ram.AliyunRAMPolicyCenter;
import com.baiyi.opscloud.cloud.ram.AliyunRAMUserCenter;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.aliyun.AliyunRAMDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamPolicy;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamUser;
import com.baiyi.opscloud.domain.param.cloud.AliyunRAMPolicyParam;
import com.baiyi.opscloud.domain.param.cloud.AliyunRAMUserParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunRAMVO;
import com.baiyi.opscloud.facade.AliyunRAMFacade;
import com.baiyi.opscloud.service.ram.OcAliyunRamPolicyService;
import com.baiyi.opscloud.service.ram.OcAliyunRamUserService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/6/9 5:36 下午
 * @Version 1.0
 */
@Service
public class AliyunRAMFacadeImpl implements AliyunRAMFacade {

    @Resource
    private AliyunCore aliyunCore;

    @Resource
    private OcAliyunRamUserService ocAliyunRamUserService;

    @Resource
    private OcAliyunRamPolicyService ocAliyunRamPolicyService;

    @Resource
    private AliyunRAMUserCenter aliyunRAMUserCenter;

    @Resource
    private AliyunRAMPolicyCenter aliyunRAMPolicyCenter;

    @Resource
    private AliyunRAMDecorator aliyunRAMDecorator;

    @Resource
    private AliyunRAMUserPolicyHandler aliyunRAMUserPolicyHandler;

    @Override
    public DataTable<AliyunRAMVO.RAMUser> queryRAMUserPage(AliyunRAMUserParam.RamUserPageQuery pageQuery) {
        DataTable<OcAliyunRamUser> table = ocAliyunRamUserService.queryOcAliyunRamUserByParam(pageQuery);
        List<AliyunRAMVO.RAMUser> page = BeanCopierUtils.copyListProperties(table.getData(), AliyunRAMVO.RAMUser.class)
                .stream().map(e -> aliyunRAMDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList());
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> syncRAMUser() {
        return aliyunRAMUserCenter.syncUsers();
    }

    @Override
    public BusinessWrapper<Boolean> syncRAMPolicy() {
        return aliyunRAMPolicyCenter.syncPolicies();
    }

    @Override
    public BusinessWrapper<Boolean> setRAMPolicyWorkorderById(int id) {
        OcAliyunRamPolicy ocAliyunRamPolicy = ocAliyunRamPolicyService.queryOcAliyunRamPolicyById(id);
        ocAliyunRamPolicy.setInWorkorder(ocAliyunRamPolicy.getInWorkorder() == 0 ? 1 : 0);
        ocAliyunRamPolicyService.updateOcAliyunRamPolicy(ocAliyunRamPolicy);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<AliyunRAMVO.RAMPolicy> queryRAMPolicyPage(AliyunRAMPolicyParam.PageQuery pageQuery) {
        DataTable<OcAliyunRamPolicy> table = ocAliyunRamPolicyService.queryOcAliyunRamPolicyByParam(pageQuery);
        List<AliyunRAMVO.RAMPolicy> page = BeanCopierUtils.copyListProperties(table.getData(), AliyunRAMVO.RAMPolicy.class);
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public List<AliyunRAMVO.RAMUser> queryRamUsersByUsername(int userId) {
        List<OcAliyunRamUser> ramUserList = ocAliyunRamUserService.queryUserPermissionRamUserByUserId(userId);
        if (ramUserList == null) return Lists.newArrayList();
        return BeanCopierUtils.copyListProperties(ramUserList, AliyunRAMVO.RAMUser.class).stream().map(e -> aliyunRAMDecorator.decorator(e, 1)).collect(Collectors.toList());
    }

    @Override
    public BusinessWrapper<Boolean> attachPolicyToUser(OcAliyunRamUser ocAliyunRamUser, AliyunRAMVO.RAMPolicy ramPolicy) {
        AliyunCoreConfig.AliyunAccount aliyunAccount = aliyunCore.getAliyunAccountByUid(ramPolicy.getAccountUid());
        BusinessWrapper<Boolean> wrapper = aliyunRAMUserPolicyHandler.attachPolicyToUser(aliyunAccount, ocAliyunRamUser, ramPolicy);
        if (!wrapper.isSuccess())
            return wrapper;
        return aliyunRAMUserCenter.syncUser(ocAliyunRamUser);
    }
}
