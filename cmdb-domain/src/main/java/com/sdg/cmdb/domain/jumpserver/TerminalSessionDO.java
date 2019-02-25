package com.sdg.cmdb.domain.jumpserver;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class TerminalSessionDO implements Serializable {
    private static final long serialVersionUID = -8697442363082208053L;

    private String id;
    private String user;
    private String asset;
    private String system_user;
    private boolean is_finished;
    private String date_start;
    private String terminal_id;
    private String remote_addr;
    private String protocol;

    /**
     * terminal表 查询获得
     */
    private String terminal_name;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
