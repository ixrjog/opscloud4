package com.baiyi.opscloud.decorator.aliyun.log;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeAgoUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcEnv;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.vo.cloud.AliyunLogMemberVO;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/6/15 10:22 上午
 * @Version 1.0
 */
@Component
public class AliyunLogMemberDecorator {

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcEnvService ocEnvService;

    public AliyunLogMemberVO.LogMember decorator(AliyunLogMemberVO.LogMember logMember) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(logMember.getServerGroupId());
        if (ocServerGroup != null)
            logMember.setServerGroup(ocServerGroup);
        if (logMember.getLastPushTime() != null)
            logMember.setAgo(TimeAgoUtils.format(logMember.getLastPushTime()));
        if (logMember.getEnvType() != null) {
            OcEnv ocEnv = ocEnvService.queryOcEnvByType(logMember.getEnvType());
            if (ocEnv != null) {
                logMember.setEnv(BeanCopierUtils.copyProperties(ocEnv, EnvVO.Env.class));
            }
        }
        return logMember;
    }
}
