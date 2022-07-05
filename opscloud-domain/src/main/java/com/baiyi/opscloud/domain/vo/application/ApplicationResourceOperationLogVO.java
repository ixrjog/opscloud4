package com.baiyi.opscloud.domain.vo.application;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.base.ShowTime;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/7/5 16:21
 * @Version 1.0
 */
public class ApplicationResourceOperationLogVO {

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class OperationLog extends BaseVO implements ShowTime.IAgo, UserVO.IUser, Serializable {

        private static final long serialVersionUID = 4028480531533162951L;

        private String ago;

        private UserVO.User user;

        private Integer id;

        private Integer resourceId;

        private String resourceType;

        private Integer envType;

        private String username;

        private Boolean isAdmin;

        private String operationType;

        private Date operationTime;

        private String result;

        private String comment;

        @Override
        public Date getAgoTime() {
            return operationTime;
        }

    }

}
