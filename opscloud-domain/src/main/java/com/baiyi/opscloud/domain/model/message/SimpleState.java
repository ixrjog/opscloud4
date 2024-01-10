package com.baiyi.opscloud.domain.model.message;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2022/1/14 9:00 PM
 * @Version 1.0
 */
@Data
public class SimpleState implements IState {

    private String state;

}