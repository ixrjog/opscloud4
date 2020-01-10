package com.baiyi.opscloud.jumpserver.model;

import lombok.Data;

@Data
public class AssetsNodes {
    private String id;

    private String key;

    private String value;

    private String parent;

    private String assets_amount;

    private String[] assets;

    private String[] nodes;


}
