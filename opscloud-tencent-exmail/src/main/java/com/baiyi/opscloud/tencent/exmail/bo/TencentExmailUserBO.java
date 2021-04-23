package com.baiyi.opscloud.tencent.exmail.bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/29 2:47 下午
 * @Since 1.0
 */

@Data
@NoArgsConstructor
public class TencentExmailUserBO {

    @JSONField(name = "userid")
    private String userId;
    private String name;
    private List<Long> department;
    private String position;
    private String mobile;
    private String tel;
    private List<String> slaves;
}
