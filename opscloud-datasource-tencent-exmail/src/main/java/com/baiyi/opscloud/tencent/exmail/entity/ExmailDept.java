package com.baiyi.opscloud.tencent.exmail.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 修远
 * @Date 2021/10/13 10:32 上午
 * @Since 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExmailDept {

    private Long id;
    private String name;
    @JsonProperty("parentid")
    private String parentId;
    private Long order;
    private String path;

}