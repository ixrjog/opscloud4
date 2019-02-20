package com.sdg.cmdb.domain.jumpserver;


import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
public class UsersUsergroupDO implements Serializable {
    private static final long serialVersionUID = 6340029318510551641L;

    private String id;
    private String name;
    private String comment = "";
    private String date_created;
    private String created_by = "opscloud";
    private String org_id = "";

    public UsersUsergroupDO() {

    }

    public UsersUsergroupDO(String id,String name, String comment, String date_created) {
        this.id = id;
        this.name = name;
        if (!StringUtils.isEmpty(comment))
            this.comment = comment;
        this.date_created = date_created;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
