package com.baiyi.caesar.domain.generator.caesar;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "sys_env")
public class Env {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "env_name")
    private String envName;

    @Column(name = "env_type")
    private Integer envType;

    private String color;

    @Column(name = "prompt_color")
    private Integer promptColor;

    private String comment;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}