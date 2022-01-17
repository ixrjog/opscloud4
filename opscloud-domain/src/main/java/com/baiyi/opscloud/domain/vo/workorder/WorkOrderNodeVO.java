package com.baiyi.opscloud.domain.vo.workorder;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/17 4:49 PM
 * @Version 1.0
 */
public class WorkOrderNodeVO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @ApiModel
    public static class NodeView implements Serializable {

        private static final long serialVersionUID = 466008285525003198L;
        private List<Stage> stages;

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @ApiModel
    public static class Stage implements Serializable {

        private static final long serialVersionUID = 1606626459378798989L;
        private String name;

        private String state; // "SUCCESS","PAUSED","not_built","FINISHED"
        @Builder.Default
        private int completePercent = 50;

        private int id;
        private String type; // STAGE,PARALLEL
        private PopInfo popInfo;

        private List<Stage> children;

    }

    @Builder
    @Data
    @ApiModel
    public static class PopInfo implements Serializable {
        private static final long serialVersionUID = 4493746212394051044L;

        private String title;
        private String msg;
        private String width; // "101"
        private String height; // "120"

    }

}
