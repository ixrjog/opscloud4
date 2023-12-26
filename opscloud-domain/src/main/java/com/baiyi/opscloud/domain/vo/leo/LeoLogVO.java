package com.baiyi.opscloud.domain.vo.leo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2023/10/10 11:24
 * @Version 1.0
 */
public class LeoLogVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Log implements Serializable {

        @Serial
        private static final long serialVersionUID = -7021932501615063517L;

        private Integer id;

        private String level;

        private String log;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

    }

}