package com.baiyi.opscloud.domain.vo.ansible.playbook;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/4/14 6:48 下午
 * @Version 1.0
 */
@Data
public class PlaybookTask {

    private String name;
    private String tags;
    private Boolean choose;
}
