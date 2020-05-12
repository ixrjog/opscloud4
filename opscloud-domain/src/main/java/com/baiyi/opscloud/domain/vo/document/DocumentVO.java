package com.baiyi.opscloud.domain.vo.document;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/5/12 5:50 下午
 * @Version 1.0
 */
public class DocumentVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Doc {

        private Integer id;
        private String docKey;
        private Integer docType;
        private String comment;
        private String docContent;
        private String previewDoc;

    }
}
