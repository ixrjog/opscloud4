package com.baiyi.opscloud.domain.vo.gitlab;

import com.baiyi.opscloud.domain.vo.tag.TagVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/20 2:15 下午
 * @Version 1.0
 */
public class GitlabProjectVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Project {

        private List<TagVO.Tag> tags;

        private GitlabInstanceVO.Instance instance;

        private Integer id;
        private Integer instanceId;
        private Integer projectId;
        private String name;
        private String path;
        private String projectVisibility;
        private Integer namespaceId;
        private String namespaceName;
        private String namespacePath;
        private String namespaceKind;
        private String namespaceFullPath;
        private String sshUrl;
        private String webUrl;
        private String httpUrl;
        private String defaultBranch;
        private Date createTime;
        private Date updateTime;
        private String description;
    }

}
