package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@Table(name = "sys_env")
@AllArgsConstructor
@NoArgsConstructor
public class Env implements Serializable {
    @Serial
    private static final long serialVersionUID = 1037808244060772085L;

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

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * 顺序
     */
    private Integer seq;

    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}