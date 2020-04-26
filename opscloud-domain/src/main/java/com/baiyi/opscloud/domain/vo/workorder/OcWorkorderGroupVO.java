package com.baiyi.opscloud.domain.vo.workorder;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/4/26 1:27 下午
 * @Version 1.0
 */
public class OcWorkorderGroupVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class WorkorderGroup {

        private Integer id;
        private String name;
        private Integer workorderType;
        private String comment;
        private Date createTime;
        private Date updateTime;

    }

}