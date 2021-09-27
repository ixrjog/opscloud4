package com.baiyi.opscloud.ansible.play;

import com.alibaba.fastjson.JSON;
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

    private String instanceId; // serverName
    private Integer serverTaskId;
    private Integer serverTaskMemberId;
    private String output;
    private String error;

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }
}
