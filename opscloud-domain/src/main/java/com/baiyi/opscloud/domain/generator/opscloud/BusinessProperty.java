package com.baiyi.opscloud.domain.generator.opscloud;

import com.baiyi.opscloud.domain.vo.business.BaseBusiness;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "business_property")
public class BusinessProperty implements BaseBusiness.IBusiness {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 业务类型
     */
    @Column(name = "business_type")
    private Integer businessType;

    /**
     * 业务id
     */
    @Column(name = "business_id")
    private Integer businessId;

    /**
     * 说明
     */
    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 属性
     */
    private String property;
}