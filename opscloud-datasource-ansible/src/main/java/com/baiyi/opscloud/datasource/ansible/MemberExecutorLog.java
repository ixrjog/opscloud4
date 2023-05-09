package com.baiyi.opscloud.datasource.ansible;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2021/9/22 5:48 下午
 * @Version 1.0
 */
@Builder
@Data
public class MemberExecutorLog implements Serializable {

    @Serial
    private static final long serialVersionUID = -7074695649624711405L;

    private int memberId;

    private String outputMsg;

    private String errorMsg;

}
