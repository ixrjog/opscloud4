package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "auth_group")
public class AuthGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "group_name")
    private String groupName;

    /**
     * 基本路径
     */
    @Column(name = "base_path")
    private String basePath;

    private String comment;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}