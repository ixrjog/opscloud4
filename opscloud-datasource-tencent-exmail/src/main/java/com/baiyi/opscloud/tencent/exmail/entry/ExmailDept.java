package com.baiyi.opscloud.tencent.exmail.entry;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/13 10:32 上午
 * @Since 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExmailDept {

    private Long id;
    private String name;
    @JSONField(name = "parentid")
    private String parentId;
    private Long order;
    private String path;

}
