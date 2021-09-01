package com.baiyi.opscloud.domain.generator.opscloud;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "ansible_playbook")
public class AnsiblePlaybook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 剧本uuid
     */
    @Column(name = "playbook_uuid")
    private String playbookUuid;

    /**
     * 名称
     */
    private String name;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 剧本内容
     */
    private String playbook;

    /**
     * 标签配置
     */
    private String tags;

    /**
     * 外部变量
     */
    private String vars;

    /**
     * 描述
     */
    private String comment;
}