package com.baiyi.opscloud.domain.vo.workorder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
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
    @Schema
    public static class NodeView implements Serializable {

        @Serial
        private static final long serialVersionUID = 466008285525003198L;
        private List<Stage> stages;

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema
    public static class Stage implements Serializable {

        @Serial
        private static final long serialVersionUID = 1606626459378798989L;
        private String name;

        @Schema(description = "SUCCESS,PAUSED,not_built,FINISHED")
        private String state;

        @Builder.Default
        private int completePercent = 50;

        private int id;

        @Schema(description = "STAGE,PARALLEL")
        private String type;

        private PopInfo popInfo;

        private List<Stage> children;

    }

    @Builder
    @Data
    @Schema
    public static class PopInfo implements Serializable {
        @Serial
        private static final long serialVersionUID = 4493746212394051044L;

        private String title;
        private List<String> msg;
        @Builder.Default
        private String width = "100"; // "101"
        @Builder.Default
        private String height = "102"; // "120"

    }

}