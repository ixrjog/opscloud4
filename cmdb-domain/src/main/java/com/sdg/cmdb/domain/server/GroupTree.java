package com.sdg.cmdb.domain.server;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GroupTree implements Serializable {

    private String label;
    private List<ServerGroupHostPattern> children;

    public GroupTree() {
    }

    public GroupTree(String label, List<ServerGroupHostPattern> children) {
        this.label = label;
        this.children = children;
    }


}
