package com.baiyi.opscloud.domain.vo.datasource.aliyun;

import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.base.ShowTime;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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
    @ApiModel
    public static class LogMember extends BaseVO implements ShowTime.IAgo, EnvVO.IEnv {

        private AliyunLogVO.Log log;

        private ServerGroup serverGroup;

        private ArrayList<String> machineList; // IP列表

        private EnvVO.Env env;

        private String ago;

        private Integer id;

        @NotNull(message = "必须指定日志服务id")
        private Integer aliyunLogId;

        @NotNull(message = "必须指定服务器组id")
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
