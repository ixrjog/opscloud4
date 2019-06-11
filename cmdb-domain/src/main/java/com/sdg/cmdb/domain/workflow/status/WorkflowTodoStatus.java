package com.sdg.cmdb.domain.workflow.status;

import lombok.Data;

import java.io.Serializable;

@Data
public class WorkflowTodoStatus implements Serializable {
    private static final long serialVersionUID = -7286329308227735073L;

    private String title;
    private int cnt;

}
