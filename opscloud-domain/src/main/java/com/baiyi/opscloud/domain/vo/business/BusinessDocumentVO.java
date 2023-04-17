package com.baiyi.opscloud.domain.vo.business;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2022/5/15 18:25
 * @Version 1.0
 */
public class BusinessDocumentVO {

    public interface IBusinessDocument extends BaseBusiness.IBusiness {

        void setDocument(BusinessDocumentVO.Document document);

    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Document implements BaseBusiness.IBusiness, Serializable {

        @Serial
        private static final long serialVersionUID = 6050583884005972339L;

        @Schema(description = "业务对象显示名称")
        private String displayName;

        private Integer id;

        @Schema(description = "业务类型")
        private Integer businessType;

        @Schema(description = "业务ID")
        private Integer businessId;

        @Schema(description = "文档类型")
        private Integer documentType;

        private String comment;

        @Schema(description = "文档")
        private String doc;

        @Schema(description = "文档原内容")
        private String content;

    }

}