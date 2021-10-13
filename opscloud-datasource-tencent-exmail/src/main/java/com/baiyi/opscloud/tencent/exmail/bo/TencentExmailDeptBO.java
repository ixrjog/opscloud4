package com.baiyi.opscloud.tencent.exmail.bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/13 10:32 上午
 * @Since 1.0
 */
public class TencentExmailDeptBO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class deptList {
        private List<dept> department;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class dept {
        private Long id;
        private String name;
        @JSONField(name = "parentid")
        private String parentId;
        private Long order;
        private String path;
    }
}
