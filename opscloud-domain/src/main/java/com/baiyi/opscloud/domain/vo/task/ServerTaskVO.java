package com.baiyi.opscloud.domain.vo.task;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Member;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/18 3:43 下午
 * @Version 1.0
 */
public class ServerTaskVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @ApiModel
    public static class ServerTask extends BaseVO {

        private List<Member> members; // 任务成员

        private Integer id;

        private String taskUuid;

        private String username;

        private Integer memberSize;

        private Integer ansiblePlaybookId;

        private String taskType;

        private Boolean finalized;

        private Integer stopType;

        private String taskStatus;

        private Date startTime;

        private Date endTime;

        private String vars;

        private String tags;
    }
}
