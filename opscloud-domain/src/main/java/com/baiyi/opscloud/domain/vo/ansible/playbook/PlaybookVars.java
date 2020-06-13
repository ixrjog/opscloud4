package com.baiyi.opscloud.domain.vo.ansible.playbook;

import lombok.Data;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/14 6:45 下午
 * @Version 1.0
 */
@Data
public class PlaybookVars {

    private Map<String,String> vars;
}
