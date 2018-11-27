'use strict';


/**
 * 工作流主界面
 */
app.controller('workflowCtrl', function ($scope, $uibModal, $state, $sce, $interval, $timeout, toaster, httpService) {
        $scope.authPoint = $state.current.data.authPoint;
        $scope.workflowGroupList = [];
        //$scope.todoDetailList = [];
        //$scope.todoDetailCompleteList = [];
        //$scope.myJobStatusOpen = true;

        // $scope.queryMyJob = function () {
        //     var url = "/todo/queryMyJob";
        //     httpService.doGet(url).then(function (data) {
        //         if (data.success) {
        //             $scope.todoDetailList = data.body;
        //             $scope.refreshInitiatorUserInfo();
        //         }
        //     });
        // }
        //
        // $scope.queryCompleteJob = function () {
        //     var url = "/todo/queryCompleteJob";
        //     httpService.doGet(url).then(function (data) {
        //         if (data.success) {
        //             $scope.todoDetailCompleteList = data.body;
        //             $scope.refreshCompleteInitiatorUserInfo();
        //             $scope.refreshCompleteAssigneeUserInfo();
        //         }
        //     });
        // }


        /**
         * 创建Todo
         */
        $scope.createTodo = function (workflow) {
            switch (workflow.wfKey) {
                case "KEYBOX":
                    ////TODO 堡垒机权限申请
                    $scope.viewTodoKeybox(workflow, 0);
                    break;
                default:
                //
            }
        }

        $scope.viewTodoKeybox = function (workflow, type) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoKeyboxModal',
                controller: 'todoKeyboxInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    workflow: function () {
                        return workflow;
                    },
                    type: function () {
                        return type;
                    }
                }
            });
        }


        // 生成发起人信息
        $scope.refreshCompleteInitiatorUserInfo = function () {
            if ($scope.todoDetailCompleteList.length == 0) return;
            for (var i = 0; i < $scope.todoDetailCompleteList.length; i++) {
                var info = '<b style="color: #286090">发起人</b>';
                var item = $scope.todoDetailCompleteList[i].initiatorUserDO;
                // "<b style='color: red'>I can</b> have <div class='label label-success'>HTML "
                var mobile = "";
                if (item.mobile != null && item.mobile != '') {
                    mobile = "<br/>" + item.mobile;
                }
                info += '<hr style="margin-bottom: 2px; margin-top: 2px" />' + item.username +
                    '<b style="color: #777"><' + item.displayName + "></b><br/>"
                    + '<b style="color: #286090">' + item.mail + "</b>"
                    + mobile
                item.initiatorUserInfo = $sce.trustAsHtml(
                    info
                );
            }
        }

        // 生成负责人信息
        $scope.refreshCompleteAssigneeUserInfo = function () {

            if ($scope.todoDetailCompleteList.length == 0) return;

            for (var i = 0; i < $scope.todoDetailCompleteList.length; i++) {
                var info = '<b style="color: #286090">负责人</b>';
                var item = $scope.todoDetailCompleteList[i].assigneeUserDO;
                // "<b style='color: red'>I can</b> have <div class='label label-success'>HTML "
                var mobile = "";
                if (item.mobile != null && item.mobile != '') {
                    mobile = "<br/>" + item.mobile;
                }
                info += '<hr style="margin-bottom: 2px; margin-top: 2px" />' + item.username +
                    '<b style="color: #777"><' + item.displayName + "></b><br/>"
                    + '<b style="color: #286090">' + item.mail + "</b>"
                    + mobile
                item.assigneeUserInfo = $sce.trustAsHtml(
                    info
                );
            }
        }

        // $scope.queryMyJob();
        // $scope.queryCompleteJob();

        // 60秒刷新1次待办工单
        // var timer1 = $interval(function () {
        //     $scope.queryMyJob();
        // }, 30000);

        // 60秒刷新1次待办工单
        // var timer2 = $interval(function () {
        //     $scope.queryCompleteJob();
        // }, 60000);

        // 生成发起人信息
        $scope.refreshInitiatorUserInfo = function () {

            if ($scope.todoDetailList.length == 0) return;

            for (var i = 0; i < $scope.todoDetailList.length; i++) {
                var info = '<b style="color: #286090">申请人</b>';
                var item = $scope.todoDetailList[i].initiatorUserDO;
                // "<b style='color: red'>I can</b> have <div class='label label-success'>HTML "

                var mobile = "";
                if (item.mobile != null && item.mobile != '') {
                    mobile = "<br/>" + item.mobile;
                }

                info += '<hr style="margin-bottom: 2px; margin-top: 2px" />' + item.username +
                    '<b style="color: #777"><' + item.displayName + "></b><br/>"
                    + '<b style="color: #286090">' + item.mail + "</b>"
                    + mobile

                item.initiatorUserInfo = $sce.trustAsHtml(
                    info
                );
            }

        }

        $scope.queryWorkflowGroup = function (topics) {
            var url = "/workflow/queryGroup?topics=" + (topics == null ? "" : topics);
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    var body = data.body;
                    $scope.workflowGroupList = body;
                } else {
                    toaster.pop("warning", data.msg);
                }
            }, function (err) {
                toaster.pop("error", err);
            });
        }

        $scope.queryWorkflowGroup();

        $scope.submitTodo = function (todoItem) {
            switch (todoItem.id) {
                case 1:
                    ////TODO 堡垒机权限申请
                    $scope.submitTodoKeyBox(todoItem);
                    break;
                case 2:
                    ////TODO 持续集成权限申请
                    $scope.submitTodoCiUserGroup(todoItem);
                    break;
                case 3:
                    ////TODO 平台权限申请
                    $scope.submitSystemAuth(todoItem);
                    break;
                case 4:
                    ////TODO VPN权限申请
                    $scope.submitTodoVpn(todoItem);
                    break;
                case 5:
                    ////TODO 新项目申请
                    $scope.submitTodoNewProject(todoItem);
                    break;
                case 6:
                    ////TODO 持续集成权限申请(前端)
                    $scope.submitTodoCmdbRole(todoItem);
                    break;
                case 7:
                    ////TODO SCM权限申请
                    $scope.submitTodoScm(todoItem);
                    break;
                case 8:
                    ////TODO Tomcat(JDK)版本变更
                    $scope.submitTodoTomcatVersion(todoItem);
                    break;
                default:
                //

            }
        }

        $scope.viewTodo = function (todoDetail) {
            switch (todoDetail.todoDO.id) {
                case 1:
                    ////TODO 查看-堡垒机权限申请
                    $scope.viewTodoKeyBox(todoDetail);
                    break;
                case 2:
                    ////TODO 查看-持续集成权限申请
                    $scope.viewTodoCiUserGroup(todoDetail);
                    break;
                case 3:
                    ////TODO 查看-平台权限申请
                    $scope.viewSystemAuth(todoDetail);
                    break;
                case 4:
                    ////TODO 查看-VPN权限申请
                    $scope.viewTodoVpn(todoDetail);
                    break;
                case 5:
                    ////TODO 查看-新项目申请
                    $scope.viewTodoNewProject(todoDetail);
                    break;
                case 6:
                    ////TODO 持续集成权限申请(前端)
                    $scope.viewTodoCmdbRole(todoDetail);
                    break;
                case 7:
                    ////TODO SCM权限申请
                    $scope.viewTodoScm(todoDetail);
                    break;
                case 8:
                    ////TODO Tomcat(JDK)版本变更
                    $scope.viewTodoTomcatVersion(todoDetail);
                    break;
                default:
                //
            }
        }


        //////////////////////////////////////////////////////

        // 撤销工单
        $scope.revokeTodoDetail = function (id) {
            var url = "/todo/revokeTodoDetail?id=" + id;
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    toaster.pop("success", "撤销成功!");
                    $scope.queryMyJob();
                } else {
                    toaster.pop("warning", "撤销失败!");
                }
            }, function (err) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = err;
            });
        }

        // 执行工单
        $scope.invokeTodoDetail = function (id) {
            var url = "/todo/invokeTodoDetail?id=" + id;
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    toaster.pop("success", "执行成功!");
                    $scope.queryMyJob();
                } else {
                    toaster.pop("warning", "执行失败!");
                }
            }, function (err) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = err;
            });
        }
    }
);


/**
 * Todo keybox
 */
app.controller('todoKeyboxInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, workflow, type) {
    $scope.workflow = workflow;
    $scope.type = type;

    $scope.workflowTodo = {};


    $scope.initiatorUsername = "";
    $scope.assigneeUsersInfo = "";
    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];


    $scope.doCreate = function () {
        var url = "/workflow/todo/create?wfKey=" + $scope.workflow.wfKey;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.workflowTodo = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    // 生成负责人信息
    $scope.refreshAssigneeUsersInfo = function () {

        var info = '<b style="color: #286090">负责人</b>';

        switch ($scope.todoItem.todoType) {
            case (0) :
                info += '<b class="pull-right" style="color: #777">DevOps</b>'
                break;
            case (1) :
                info += '<b class="pull-right" style="color: #777">DBA</b>'
                break;
            default:
        }

        for (var i = 0; i < $scope.todoDetail.assigneeUsers.length; i++) {
            var item = $scope.todoDetail.assigneeUsers[i];
            // "<b style='color: red'>I can</b> have <div class='label label-success'>HTML "

            var mobile = "";
            if (item.mobile != null && item.mobile != '') {
                mobile = "<br/>" + item.mobile;
            }

            info += '<hr style="margin-bottom: 2px; margin-top: 2px" />' + item.username +
                '<b style="color: #777"><' + item.displayName + "></b><br/>"
                + '<b style="color: #286090">' + item.mail + "</b>"
                + mobile
        }

        $scope.assigneeUsersInfo = $sce.trustAsHtml(
            info
        );
    }

    var init = function () {
        if ($scope.type == 0) $scope.doCreate();
        // if (todoDetail != null) {
        //     $scope.refreshAssigneeUsersInfo();
        //     return;
        // }
        //
        // var url = "/todo/establish?todoId=" + $scope.todoItem.id;
        // httpService.doGet(url).then(function (data) {
        //     if (data.success) {
        //         $scope.todoDetail = data.body;
        //         $scope.initiatorUsername = $scope.todoDetail.initiatorUsername;
        //         $scope.refreshAssigneeUsersInfo();
        //     } else {
        //         toaster.pop("warning", data.msg);
        //     }
        // }, function (err) {
        //     toaster.pop("error", err);
        // });

    }

    // 创建工单（复用）
    init();

    $scope.queryServerGroup = function (queryParam) {
        var url = "/servergroup/query/page?page=0&length=10&name=" + queryParam + "&useType=0";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.serverGroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.alert = {
        type: "",
        msg: ""
    };

    $scope.closeAlert = function () {
        $scope.alert = {
            type: "",
            msg: ""
        };
    }

    //////////////////////////////////////////////////////

    $scope.addItem = function (choose) {
        if ($scope.nowServerGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择服务器组才能添加!";
        } else {
            $scope.alert.type = '';
        }

        var todoKeybox = {
            serverGroupDO: $scope.nowServerGroup.selected,
            ciUserGroupDO: {},
            ciAuth: (choose == null ? false : choose),
            status: 0
        }

        var workflowTodoDetailVO = {
            id: 0,
            todoId: $scope.workflowTodo.id,
            detailKey: "TodoKeybox",
            detail: todoKeybox,
            name: ""
        }

        var url = "/workflow/todo/save";

        var requestBody = $scope.workflowTodo;
        requestBody.todoDetails.push(workflowTodoDetailVO);

        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                $scope.workflowTodo = data.body;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    $scope.delItem = function (id) {
        var url = "/workflow/todo/detail/del?" +
            "todoId=" + $scope.workflowTodo.id +
            "&detailId=" + id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.workflowTodo = data.body;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    /**
     * 申请todo
     */
    $scope.applyTodo = function () {
        if ($scope.workflowTodo.todoDetails == null || $scope.workflowTodo.todoDetails.length == 0) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "工作流未填写内容!";
            return;
        }

        var url = "/workflow/todo/apply?todoId=" + $scope.workflowTodo.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "提交成功!";
                $scope.workflowTodo = data.body;
                // 工单状态（工单状态 0:发起人状态  1:团队领导状态  2:部分领导状态  3:审核操作状态  4:执行完成 ）
                $scope.type = $scope.workflowTodo.todoPhase;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });

    }

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }


    /////////////////////////////////////////////////

    $scope.doQuery = function () {
        var url = "/todo/todoDetail/query?id=" + $scope.todoDetail.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.todoDetail = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

});