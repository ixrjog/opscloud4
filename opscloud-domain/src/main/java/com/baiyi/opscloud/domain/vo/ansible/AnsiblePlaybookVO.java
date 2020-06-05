package com.baiyi.opscloud.domain.vo.ansible;

import com.baiyi.opscloud.domain.vo.ansible.playbook.PlaybookTask;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/4/13 4:48 下午
 * @Version 1.0
 */
public class AnsiblePlaybookVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AnsiblePlaybook {
        private String path;
        private List<PlaybookTask> tasks;
        private Set<String> selectedTags;

        // 格式错误
        private Boolean formatError ;

        private Integer id;
        private String playbookUuid;
        private String name;
        private String comment;
        private Integer userId;
        private Integer useType;
        private Date createTime;
        private Date updateTime;
        private String playbook;
        private String tags;
        private String extraVars;
        private String userDetail;
    }
}
