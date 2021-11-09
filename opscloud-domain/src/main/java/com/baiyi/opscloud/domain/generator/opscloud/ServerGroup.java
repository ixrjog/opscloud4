package com.baiyi.opscloud.domain.generator.opscloud;

import com.baiyi.opscloud.domain.base.IAllowOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "server_group")
public class ServerGroup implements IAllowOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 服务器组名称
     */
    private String name;

    /**
     * 服务器组类型
     */
    @Column(name = "server_group_type_id")
    private Integer serverGroupTypeId;

    /**
     * 允许工单申请
     */
    @Column(name = "allow_order")
    private Boolean allowOrder;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    private String comment;
}