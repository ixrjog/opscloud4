package com.baiyi.opscloud.domain.vo.tag;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/22 1:17 下午
 * @Version 1.0
 */
public class TagVO {

    public interface ITags extends BaseBusiness.IBusiness {
        void setTags(List<Tag> tags);
        List<Tag> getTags();
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class Tag extends BaseVO implements Serializable {

        @Serial
        private static final long serialVersionUID = 1445359231777384339L;

        private Integer quantityUsed;

        @Schema(description = "主键", example = "1")
        private Integer id;

        @Schema(description = "业务类型", example = "0")
        private Integer businessType;

        @Schema(description = "标签key")
        private String tagKey;

        @Schema(description = "颜色值")
        private String color;

        @Schema(description = "描述")
        private String comment;

        private BusinessTypeEnum businessTypeEnum;
    }

}