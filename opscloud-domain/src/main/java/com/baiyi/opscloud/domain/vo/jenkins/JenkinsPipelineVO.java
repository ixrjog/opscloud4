package com.baiyi.opscloud.domain.vo.jenkins;

import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/3/30 6:04 下午
 * @Version 1.0
 */
public class JenkinsPipelineVO {

    @Data
    @Builder
    @Schema
    public static class Pipeline implements  Serializable {

        @Serial
        private static final long serialVersionUID = -1020196514240621058L;

        @Builder.Default
        private String chartHeight = "120px";

        private List<Node> nodes;
        private String jobName;

        private String jobUrl;
        private String buildUrl;

        private Integer jobBuildNumber;
        private Integer id;
        private Boolean isRunning;
        private Date startTime;

        // UserVO.IUser
        private String username;


        private String ago;

    }

    @Data
    @Builder
    @Schema
    public static class Node implements Serializable {

        @Serial
        private static final long serialVersionUID = -1465972308441846486L;
        private String firstParent;
        private String name;
        private String state;
        @Builder.Default
        private Integer completePercent = 100;
        private String id;
        @Builder.Default
        private String type = "STAGE";
        @Builder.Default
        private List<Node> children = Lists.newArrayList();

    }

}