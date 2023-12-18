package com.baiyi.opscloud.domain.vo.datasource.aliyun;

import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.base.ReadableTime;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/9/16 5:01 下午
 * @Version 1.0
 */
public class AliyunLogMemberVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class LogMember extends BaseVO implements ReadableTime.IAgo, EnvVO.IEnv {

        private AliyunLogVO.Log log;

        private ServerGroup serverGroup;

        @Schema(description = "IP列表")
        private ArrayList<String> machineList;

        private EnvVO.Env env;

        private String ago;

        private Integer id;

        @NotNull(message = "必须指定日志服务ID")
        private Integer aliyunLogId;

        @NotNull(message = "必须指定服务器组ID")
        private Integer serverGroupId;

        private String serverGroupName;

        private String topic;

        @NotNull(message = "必须指定环境")
        private Integer envType;

        private String comment;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date lastPushTime;

        @Override
        public Date getAgoTime() {
            return lastPushTime;
        }

    }

}
