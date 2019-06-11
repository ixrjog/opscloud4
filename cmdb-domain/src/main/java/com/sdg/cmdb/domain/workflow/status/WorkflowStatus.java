package com.sdg.cmdb.domain.workflow.status;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class WorkflowStatus implements Serializable{

    private static final long serialVersionUID = -273888346930743785L;

    private List<String> wfTodoCat;
    private List<Integer> wfTodoData;

    private List<String> wfTodoMonthCat;
    private List<Integer> wfTodoMonthData;


    public WorkflowStatus(){}

    public WorkflowStatus(List<WorkflowTodoStatus> data,List<WorkflowTodoMonthStatus> monthData){
        wfTodoCat = new ArrayList<>();
        wfTodoData = new ArrayList<>();
        for(WorkflowTodoStatus d:data){
            wfTodoCat.add(d.getTitle());
            wfTodoData.add(d.getCnt());
        }
        wfTodoMonthCat = new ArrayList<>();
        wfTodoMonthData = new ArrayList<>();
        for(WorkflowTodoMonthStatus d:monthData){
            wfTodoMonthCat.add(d.getDateCat());
            wfTodoMonthData.add(d.getCnt());
        }
    }


}
