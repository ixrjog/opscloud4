package com.baiyi.opscloud.domain.generator.opscloud;

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
@Table(name = "terminal_session_instance_command")
public class TerminalSessionInstanceCommand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 实例表id
     */
    @Column(name = "terminal_session_instance_id")
    private Integer terminalSessionInstanceId;

    /**
     * 提示符
     */
    private String prompt;

    /**
     * 是否格式化
     */
    @Column(name = "is_formatted")
    private Boolean isFormatted;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 输入
     */
    private String input;

    /**
     * 输入格式化
     */
    @Column(name = "input_formatted")
    private String inputFormatted;

    /**
     * 输出
     */
    private String output;

}