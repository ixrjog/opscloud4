package com.sdg.cmdb.domain.workflow.status;

import lombok.Data;

import java.io.Serializable;

@Data
public class WorkflowTodoMonthStatus implements Serializable {
    private static final long serialVersionUID = 5937906362971477050L;

    private String dateCat;
    private int cnt;

}
