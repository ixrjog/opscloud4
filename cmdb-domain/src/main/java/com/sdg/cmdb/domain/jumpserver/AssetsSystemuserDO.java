package com.sdg.cmdb.domain.jumpserver;

import lombok.Data;

import java.io.Serializable;

@Data
public class AssetsSystemuserDO  implements Serializable{
    private static final long serialVersionUID = -7728837504497781781L;

    private String id;
    private String name;
    private String username;

}
