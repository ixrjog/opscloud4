package com.baiyi.opscloud.jumpserver.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/9 10:53 上午
 * @Version 1.0
 */
@Data
@Builder
public class AssetsNodeBO {

    private String id;
    private String key;
    private String value;
    @Builder.Default
    private Integer childMark = 0;
    @Builder.Default
    private Date dateCreate = new Date();
    @Builder.Default
    private String orgId ="";

}
