package com.baiyi.opscloud.domain.vo.cloud;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/12 9:51 上午
 * @Version 1.0
 */
public class AliyunAccountVO {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AliyunAccount {
        private String uid;
        private Boolean master;
        private String name;
        private String regionId;
        private List<String> regionIds;
    }

}
