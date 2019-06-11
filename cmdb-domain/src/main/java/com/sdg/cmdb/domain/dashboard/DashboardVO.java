package com.sdg.cmdb.domain.dashboard;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class DashboardVO implements Serializable {
    private static final long serialVersionUID = -3727072599194417783L;

    private int myProblemCnt;
    private int myServerGroupCnt;
    private int myServerCnt;
    private int myAppCnt;
    private int myTodoPendingApprovalCnt;
    private int myTodoCompletedCnt;

    public DashboardVO() {
    }

    public DashboardVO(int myServerGroupCnt, int myServerCnt) {
        this.myServerGroupCnt = myServerGroupCnt;
        this.myServerCnt = myServerCnt;
    }

    public DashboardVO(int myServerGroupCnt, int myServerCnt, int myAppCnt, int myTodoPendingApprovalCnt, int myTodoCompletedCnt) {
        this.myServerGroupCnt = myServerGroupCnt;
        this.myServerCnt = myServerCnt;
        this.myAppCnt = myAppCnt;
        this.myTodoPendingApprovalCnt = myTodoPendingApprovalCnt;
        this.myTodoCompletedCnt = myTodoCompletedCnt;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
