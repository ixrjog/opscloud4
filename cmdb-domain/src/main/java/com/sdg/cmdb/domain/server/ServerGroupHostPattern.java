package com.sdg.cmdb.domain.server;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ServerGroupHostPattern implements Serializable {
    private static final long serialVersionUID = -4617525926194333887L;

    private String label;
    private List<String> children;

    public ServerGroupHostPattern() {
    }

    public ServerGroupHostPattern(String label, List<String> children) {
        this.label = label;
        this.children = children;
    }

}
