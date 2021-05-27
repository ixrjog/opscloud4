package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "server_group_type")
public class ServerGroupType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String color;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private String comment;
}