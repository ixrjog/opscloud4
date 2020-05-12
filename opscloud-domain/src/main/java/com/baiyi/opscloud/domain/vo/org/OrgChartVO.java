package com.baiyi.opscloud.domain.vo.org;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/8 4:56 下午
 * @Version 1.0
 */
public class OrgChartVO {

    @Data
    @Builder
    @ApiModel
    public static class OrgChart {
        //  parentId;
        private Integer id;
        //  用户名
        private String name;
        //  部门名
        private String title;

        private List<OrgChartVO.Children> children;
    }

    @Data
    @Builder
    @ApiModel
    public static class Children {
        private Integer id;
        private String name;
        private String title;
        private List<OrgChartVO.Children> children;
    }


}
