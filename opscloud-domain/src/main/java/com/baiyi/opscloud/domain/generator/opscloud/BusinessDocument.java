package com.baiyi.opscloud.domain.generator.opscloud;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "business_document")
public class BusinessDocument implements BaseBusiness.IBusiness {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 业务类型
     */
    @Column(name = "business_type")
    private Integer businessType;

    /**
     * 业务ID
     */
    @Column(name = "business_id")
    private Integer businessId;

    /**
     * 文档类型
     */
    @Column(name = "document_type")
    private Integer documentType;

    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 文档内容
     */
    private String content;
}