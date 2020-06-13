package com.baiyi.opscloud.aliyun.log.handler;

import com.aliyun.openservices.log.common.MachineGroup;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.GetMachineGroupRequest;
import com.baiyi.opscloud.aliyun.log.base.BaseAliyunLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/6/13 11:47 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class AliyunLogMachineGroupHandler extends BaseAliyunLog {

    public MachineGroup getMachineGroup(String project, String groupName) {
        GetMachineGroupRequest req = new GetMachineGroupRequest(project, groupName);
        try {
            return client.GetMachineGroup(req).GetMachineGroup();
        } catch (LogException lg) {
            log.error("阿里云日志服务查询MachineGroup错误! , {}", lg.GetErrorMessage());
        }
        return null;
    }

}
