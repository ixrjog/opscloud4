package com.baiyi.caesar.domain.vo.jenkins;

import com.baiyi.caesar.domain.vo.base.AgoVO;
import com.baiyi.caesar.domain.vo.build.BuildExecutorVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

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
    @ApiModel
    public static class Pipeline implements AgoVO.IAgo, BuildExecutorVO.IBuildExecutors, UserVO.IUser, Serializable {

        private static final long serialVersionUID = -1020196514240621058L;

        @Override
        public Integer getBuildId() {
            return id;
        }

        private int buildType;
        private List<BuildExecutorVO.BuildExecutor> executors;

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
        private UserVO.User user;

        @Override
        public Date getAgoTime() {
            return startTime;
        }

        private String ago;

    }


    @Data
    @Builder
    @ApiModel
    public static class Node implements Serializable {

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
