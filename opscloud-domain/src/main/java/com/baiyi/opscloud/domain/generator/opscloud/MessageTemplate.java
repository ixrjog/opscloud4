package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "message_template")
public class MessageTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 消息关键字
     */
    @Column(name = "msg_key")
    private String msgKey;

    /**
     * 消息类型
     */
    @Column(name = "msg_type")
    private String msgType;

    /**
     * 消费者
     */
    private String consumer;

    /**
     * 标题
     */
    private String title;

    /**
     * 创建时间
     */
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 通知模板
     */
    @Column(name = "msg_template")
    private String msgTemplate;

    /**
     * 描述
     */
    private String comment;
}