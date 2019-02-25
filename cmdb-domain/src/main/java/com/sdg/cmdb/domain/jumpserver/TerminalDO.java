package com.sdg.cmdb.domain.jumpserver;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class TerminalDO implements Serializable {
    private static final long serialVersionUID = 7662139091443909638L;

    private String id;
    private String name;

    private String remote_addr;
    private String ssh_port;
    private String http_port;

    private boolean is_accepted;
    private boolean is_deleted;

    private String date_created;
    private String comment;
    private String command_storage;
    private String replay_storage;

    /**
     * terminal_session表 查询获得
     */
    private int session;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
