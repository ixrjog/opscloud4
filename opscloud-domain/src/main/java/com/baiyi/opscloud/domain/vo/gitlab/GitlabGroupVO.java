package com.baiyi.opscloud.domain.vo.gitlab;

import com.baiyi.opscloud.domain.vo.tag.TagVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/21 5:15 下午
 * @Version 1.0
 */
public class GitlabGroupVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Group{

        private List<TagVO.Tag> tags;
        private GitlabInstanceVO.Instance instance;
        private Integer id;
        private Integer instanceId;
        private Integer groupId;
        private String name;
        private String path;
        private String groupVisibility;
        private String fullName;
        private String fullPath;
        private String webUrl;
        private String applicationKey;
        private String description;
    }
}
