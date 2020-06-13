package com.baiyi.opscloud.ansible.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2020/4/17 6:01 下午
 * @Version 1.0
 */
@Data
public class MemberExecutorLogBO implements Serializable {
    private static final long serialVersionUID = -8859340053712119052L;

    private int memberId;
    private String outputMsg;
    private String errorMsg;

}
