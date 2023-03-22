package com.baiyi.opscloud.datasource.ansible.play;

import com.baiyi.opscloud.common.util.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/9/26 5:39 下午
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayOutputMessage {

    /**
     * serverName
     */
    private String instanceId;
    private Integer serverTaskId;
    private Integer serverTaskMemberId;
    private String output;
    private String error;

    @Override
    public String toString(){
        return JSONUtil.writeValueAsString(this);
    }
}
