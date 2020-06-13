package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/13 2:28 下午
 * @Version 1.0
 */
@Data
@Builder
public class UserDocumentBO {

    private Integer id;
    private Integer userId;
    private String username;
    private String docTitle;
    @Builder.Default
    private Integer docType = 1;
    private String comment;
    private String docContent;
}
