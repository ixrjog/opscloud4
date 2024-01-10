package com.baiyi.opscloud.sshcore.task.audit.output;

import com.baiyi.opscloud.common.util.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/7/23 3:41 下午
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutputMessage {

    private String instanceId;
    private String output;

    @Override
    public String toString(){
        return JSONUtil.writeValueAsString(this);
    }

}