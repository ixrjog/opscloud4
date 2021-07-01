package com.baiyi.opscloud.domain.vo.tag;

import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/22 1:17 下午
 * @Version 1.0
 */
public class TagVO {

    public interface ITags {
        void setTags(List<Tag> tags);
        int getBusinessType();
        int getBusinessId();
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Tag extends BaseVO implements Serializable {

        private static final long serialVersionUID = 1445359231777384339L;

        @ApiModelProperty(value = "主键", example = "1")
        private Integer id;

        @ApiModelProperty(value = "业务类型", example = "0")
        private Integer BusinessType;

        @ApiModelProperty(value = "标签key")
        private String tagKey;

        @ApiModelProperty(value = "颜色值")
        private String color;

        @ApiModelProperty(value = "描述")
        private String comment;

        private BusinessTypeEnum businessTypeEnum;
    }


}
