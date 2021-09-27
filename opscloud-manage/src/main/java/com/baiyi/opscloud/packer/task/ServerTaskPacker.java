package com.baiyi.opscloud.packer.task;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.ServerTask;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.task.ServerTaskVO;
import com.baiyi.opscloud.packer.base.IPacker;
import com.baiyi.opscloud.packer.user.UserPacker;
import com.baiyi.opscloud.util.time.AgoUtil;
import com.baiyi.opscloud.util.time.DurationUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/9/24 1:51 下午
 * @Version 1.0
 */
@Component
public class ServerTaskPacker implements IPacker<ServerTaskVO.ServerTask, ServerTask> {

    @Resource
    private ServerTaskMemberPacker serverTaskMemberPacker;

    @Resource
    private AnsiblePlaybookPacker ansiblePlaybookPacker;

    @Resource
    private UserPacker userPacker;

    @Override
    public ServerTaskVO.ServerTask toVO(ServerTask serverTask) {
        return BeanCopierUtil.copyProperties(serverTask, ServerTaskVO.ServerTask.class);
    }

    public ServerTaskVO.ServerTask wrapToVO(ServerTask serverTask, IExtend iExtend) {
        ServerTaskVO.ServerTask vo = toVO(serverTask);
        if (iExtend.getExtend()) {
            serverTaskMemberPacker.wrap(vo);
            AgoUtil.wrap(vo); // 以前
            DurationUtil.wrap(vo); // 时长
            ansiblePlaybookPacker.wrap(vo); // playbook
            userPacker.wrap(vo); // user
        }
        return vo;
    }

}
