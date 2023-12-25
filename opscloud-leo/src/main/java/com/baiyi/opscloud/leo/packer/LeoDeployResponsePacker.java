package com.baiyi.opscloud.leo.packer;

import com.baiyi.opscloud.common.annotation.AgoWrapper;
import com.baiyi.opscloud.common.annotation.RuntimeWrapper;
import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeployLog;
import com.baiyi.opscloud.domain.vo.leo.LeoDeployVO;
import com.baiyi.opscloud.domain.vo.leo.LeoLogVO;
import com.baiyi.opscloud.leo.constants.HeartbeatTypeConstants;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.holder.LeoHeartbeatHolder;
import com.baiyi.opscloud.service.leo.LeoDeployLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/12/6 20:02
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeoDeployResponsePacker {

    private final LeoDeployLogService leoDeployLogService;

    private final LeoHeartbeatHolder leoHeartbeatHolder;

    @AgoWrapper(extend = true)
    @RuntimeWrapper(extend = true)
    @TagsWrapper(extend = true)
    public void wrap(LeoDeployVO.Deploy deploy) {
        LeoDeployModel.DeployConfig deployConfig = LeoDeployModel.load(deploy.getDeployConfig());
        deploy.setDeployDetails(deployConfig);
        wrapLogs(deploy);
        deploy.setIsLive(leoHeartbeatHolder.isLive(HeartbeatTypeConstants.DEPLOY, deploy.getId()));
    }

    private void wrapLogs(LeoDeployVO.Deploy deploy) {
        deploy.setDeployLogs(leoDeployLogService.queryByDeployId(deploy.getId()).stream()
                .map(this::toLog)
                .collect(Collectors.toList())
        );
    }

    private LeoLogVO.Log toLog(LeoDeployLog leoDeployLog) {
        return LeoLogVO.Log.builder()
                .id(leoDeployLog.getId())
                .level(leoDeployLog.getLogLevel())
                .log(leoDeployLog.getLog())
                .createTime(leoDeployLog.getCreateTime())
                .build();
    }

}