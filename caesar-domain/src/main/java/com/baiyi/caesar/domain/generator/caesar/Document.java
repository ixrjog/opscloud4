package com.baiyi.caesar.domain.generator.caesar;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "sys_document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "document_key")
    private String documentKey;

    /**
     * 文档类型
     */
    @Column(name = "document_type")
    private Integer documentType;

    private String comment;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 文档内容
     */
    private String content;
}