package com.baiyi.opscloud.sshserver.command.event.base;

import com.baiyi.opscloud.domain.vo.server.ServerVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/10/13 4:14 下午
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventContext {

    private ServerVO.Server serverVO;

    private Integer id;

    private String instanceUuid;

    private String eventName;

    private String eventId;

    private String eventIdDesc;

    private Integer priority;

    //@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastchangeTime;

    private Boolean isActive;

    private Date expiredTime;

    private Date createTime;

    private Date updateTime;

    private String eventMessage;

}
