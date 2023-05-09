package com.baiyi.opscloud.domain.generator.opscloud;

import com.baiyi.opscloud.domain.base.BaseBusiness;
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
@Table(name = "business_tag")
public class BusinessTag implements BaseBusiness.IBusiness {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 业务id
     */
    @Column(name = "business_id")
    private Integer businessId;

    /**
     * 标签id
     */
    @Column(name = "tag_id")
    private Integer tagId;

    /**
     * 业务类型
     */
    @Column(name = "business_type")
    private Integer businessType;

    @Column(name = "tag_value")
    private String tagValue;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

}