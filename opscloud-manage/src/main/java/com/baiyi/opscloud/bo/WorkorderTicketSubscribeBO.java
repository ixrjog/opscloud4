package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/6 2:15 下午
 * @Version 1.0
 */
@Data
@Builder
public class WorkorderTicketSubscribeBO {

    private Integer id;
    private Integer ticketId;
    private Integer userId;
    private String username;
    private Integer subscribeType ;
    @Builder.Default
    private Boolean subscribeActive = true;
    private String comment;

}
