package com.baiyi.opscloud.domain.vo.business;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/21 4:33 下午
 * @Since 1.0
 */
public class BusinessPropertyVO {

    public interface IBusinessProperty extends BaseBusiness.IBusiness {
        void setBusinessProperty(BusinessPropertyVO.Property BusinessProperty);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Property extends BaseVO implements Serializable {

        private static final long serialVersionUID = -1685813744181450467L;
        private Integer id;
        private Integer businessType;
        private Integer businessId;
        private String comment;
        private String property;
    }
}
