package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.domain.dashboard.DashboardVO;
import com.sdg.cmdb.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ServerGroupService serverGroupService;

    @Autowired
    private ServerService serverService;

    @Autowired
    private CiService ciService;

    @Autowired
    private WorkflowService workflowService;

    @Override
    public DashboardVO getDashboard() {
        int myGroupSize = serverGroupService.getMyGroupSize();
        int myServerSize = serverService.getMyServerSize();
        int myAppSize = ciService.getMyAppSize();
        // TODO workflow
        int pendingApprovalSize = workflowService.getMyTodoSize(-1); // 审批中
        int completedSize = workflowService.getMyTodoSize(5);  // 完成
        DashboardVO dashboardVO = new DashboardVO(myGroupSize, myServerSize, myAppSize, pendingApprovalSize, completedSize);
        return dashboardVO;
    }


}
