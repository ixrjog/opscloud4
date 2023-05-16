package com.baiyi.opscloud.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2023/5/11 15:11
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrderLeoDeployToken implements Serializable {

    @Serial
    private static final long serialVersionUID = -1L;

    private Integer buildId;

    private Integer applicationId;


}
