'use strict';


/**
 * 工作流主界面
 */
app.controller('workflowCtrl', function ($scope, $uibModal, $state, $sce, $interval, $timeout, toaster, httpService) {
        $scope.authPoint = $state.current.data.authPoint;
        $scope.workflowGroupList = [];

        $scope.workflowOpen = true;
        $scope.myJobStatusOpen = true;

        //  创建
        $scope.btnCreateTodo = false;
        //  按钮点击
        $scope.btnTodoClick = false;


        $scope.queryTopics = "";

        /**
         * 待办工作流
         * @type {Array}
         */
        $scope.myTodoList = [];

        /**
         * 已完成的工作流
         * @type {Array}
         */
        $scope.myCompleteTodoList = []


        // TODO 校验当前用户是否具有审批&&审核状态（服务端会对工作流进行权限校验，篡改数据无效）
        var initTodo = function () {
            if ($scope.myTodoList.length != 0) {
                for (var i = 0; i < $scope.myTodoList.length; i++) {
                    var todo = $scope.myTodoList[i];
                    todo.isApproval = false;
                    todo.isAudit = false;
                    // TODO 判断当前用户是否为 teamleader
                    if (todo.todoUserList.teamleader != null) {
                        if (todo.todoUserList.teamleader.username == $scope.app.settings.user.username && todo.todoPhase == 2) {
                            todo.isApproval = true;
                            continue;
                        }
                    }
                    if (todo.todoUserList.deptLeader != null) {
                        if (todo.todoUserList.deptLeader.username == $scope.app.settings.user.username && todo.todoPhase == 3) {
                            todo.isApproval = true;
                            continue;
                        }
                    }
                    if (todo.todoUserList.ops) {
                        if (todo.todoUserList.ops.username == $scope.app.settings.user.username && todo.todoPhase == 4) {
                            todo.isAudit = true;
                            continue;
                        }
                    }
                }
            }
        }

        $scope.queryMyTodo = function () {
            var url = "/workflow/todo/query";
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.myTodoList = data.body;
                    initTodo();
                }
            });
        }

        $scope.queryMyTodo();

        $scope.queryMyCompleteTodo = function () {
            var url = "/workflow/todo/queryComplete";
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.myCompleteTodoList = data.body;
                    initTodo();
                }
            });
        }

        $scope.queryMyCompleteTodo();

        /**
         * 30秒刷新1次待办工作流
         */
        var timer1 = $interval(function () {
            $scope.queryMyTodo();
        }, 30000);

        /**
         * 60秒刷新1次已完成工作流
         */
        var timer2 = $interval(function () {
            $scope.queryMyCompleteTodo();
        }, 60000);

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
        $scope.doCreate = function (workflow) {
            var url = "/workflow/todo/create?wfKey=" + workflow.wfKey;
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

        /**
         * 创建Todo
         */
        $scope.createTodo = function (workflow) {
            $scope.btnCreateTodo = true;
            var url = "/workflow/todo/create?wfKey=" + workflow.wfKey;

            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    // $scope.btnCreateTodo = false;
                    var workflowTodo = data.body;
                    switch (workflow.wfKey) {
                        case "KEYBOX":
                            ////TODO 堡垒机权限申请
                            $scope.viewTodoKeybox(workflowTodo, 0);
                            break;
                        case "RAM":
                            ////TODO 阿里云RAM权限申请
                            $scope.viewTodoAliyunRAM(workflowTodo, 0);
                            break;
                        case "LDAPGROUP":
                            ////TODO 外部平台权限申请
                            $scope.viewTodoLdapGroup(workflowTodo, 0);
                            break;
                        case "RAMPolicy":
                            ////TODO 阿里云RAMPolicy权限申请
                            $scope.viewTodoAliyunRAMPolicy(workflowTodo, 0);
                            break;
                        default:
                        //
                    }
                }
                $scope.btnCreateTodo = false;
            })

        }

        $scope.editTodo = function (todo) {
            switch (todo.workflowDO.wfKey) {
                case "KEYBOX":
                    ////TODO 堡垒机权限申请
                    $scope.viewTodoKeybox(todo, 0);
                    break;
                case "RAM":
                    ////TODO 阿里云RAM权限申请
                    $scope.viewTodoAliyunRAM(todo, 0);
                    break;
                case "LDAPGROUP":
                    ////TODO 外部平台权限申请
                    $scope.viewTodoLdapGroup(todo, 0);
                    break;
                case "RAMPolicy":
                    ////TODO 阿里云RAMPolicy权限申请
                    $scope.viewTodoAliyunRAMPolicy(todo, 0);
                    break;
                default:
                //
            }
        }


        $scope.viewTodo = function (todo) {
            switch (todo.workflowDO.wfKey) {
                case "KEYBOX":
                    ////TODO 堡垒机权限申请
                    $scope.viewTodoKeybox(todo, 1);
                    break;
                case "RAM":
                    ////TODO 阿里云RAM权限申请
                    $scope.viewTodoAliyunRAM(todo, 1);
                    break;
                case "LDAPGROUP":
                    ////TODO 外部平台权限申请
                    $scope.viewTodoLdapGroup(todo, 1);
                    break;
                case "RAMPolicy":
                    ////TODO 阿里云RAMPolicy权限申请
                    $scope.viewTodoAliyunRAMPolicy(todo, 1);
                    break;
                default:
                //
            }
        }


        $scope.viewTodoKeybox = function (workflowTodo, type) {
            viewTodo("todoKeyboxModal", "todoKeyboxInstanceCtrl", "lg", workflowTodo, type);
        }

        $scope.viewTodoAliyunRAM = function (workflowTodo, type) {
            viewTodo("todoAliyunRamModal", "todoAliyunRamInstanceCtrl", "lg", workflowTodo, type);
        }

        $scope.viewTodoLdapGroup = function (workflowTodo, type) {
            viewTodo("todoLdapGroupModal", "todoLdapGroupInstanceCtrl", "lg", workflowTodo, type);
        }

        $scope.viewTodoAliyunRAMPolicy = function (workflowTodo, type) {
            viewTodo("todoAliyunRamPolicyModal", "todoAliyunRamPolicyInstanceCtrl", "lg", workflowTodo, type);
        }


        var viewTodo = function (templateUrl, controller, size, workflowTodo, type) {
            var modalInstance = $uibModal.open({
                templateUrl: templateUrl,
                controller: controller,
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    user: function () {
                        return $scope.app.settings.user;
                    },
                    workflowTodo: function () {
                        return workflowTodo;
                    },
                    type: function () {
                        return type;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.queryMyTodo();
            }, function () {
                $scope.queryMyTodo();
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

        $scope.queryWorkflowGroup = function () {
            // var url = "/workflow/queryGroup?topics=" + ($scope.queryTopics == null ? "" : $scope.queryTopics);
            var url = "/workflow/queryGroup?topics=" + $scope.queryTopics;
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

        /**
         * 申请todo
         */
        $scope.applyTodo = function (todo) {
            $scope.btnTodoClick = true;
            if (todo.todoDetails == null || todo.todoDetails.length == 0) {
                toaster.pop("warning", "工作流未填写内容!");
                return;
            }
            var url = "/workflow/todo/apply?todoId=" + todo.id;
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    toaster.pop("success", "申请成功!");
                    $scope.queryMyTodo();
                    $scope.btnTodoClick = false;
                } else {
                    toaster.pop("warning", data.msg);
                    $scope.btnTodoClick = false;
                }
            }, function (err) {
                toaster.pop("warning", err);
                $scope.btnTodoClick = false;
            });

        }

        //////////////////////////////////////////////////////

        // 撤销工单
        $scope.revokeTodo = function (id) {
            $scope.btnTodoClick = true;
            var url = "/workflow/todo/revoke?id=" + id;
            httpService.doDelete(url).then(function (data) {
                if (data.success) {
                    toaster.pop("success", "撤销成功!");
                    $scope.queryMyTodo();
                    $scope.btnTodoClick = false;
                } else {
                    toaster.pop("warning", "撤销失败!");
                    $scope.btnTodoClick = false;
                }
            }, function (err) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = err;
                $scope.btnTodoClick = false;
            });
        }

        // TODO 审批审核工单（批准）
        $scope.approvalTodo = function (id) {
            $scope.btnTodoClick = true;
            var url = "/workflow/todo/approval?id=" + id;
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    toaster.pop("success", "审批/审核成功!");
                    $scope.queryMyTodo();
                    $scope.btnTodoClick = false;
                } else {
                    toaster.pop("warning", "审批/审核失败!");
                    $scope.btnTodoClick = false;
                }
            }, function (err) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = err;
                $scope.btnTodoClick = false;
            });
        }

        // TODO 审批审核工单（不批准）
        $scope.disapproveTodo = function (id) {
            $scope.btnTodoClick = true;
            var url = "/workflow/todo/disapprove?id=" + id;
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    toaster.pop("success", "审批/审核（不批准）成功!");
                    $scope.queryMyTodo();
                    $scope.btnTodoClick = false;
                } else {
                    toaster.pop("warning", "审批/审核（不批准）失败!");
                    $scope.btnTodoClick = false;
                }
            }, function (err) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = err;
                $scope.btnTodoClick = false;
            });
        }
    }
);


/**
 * TODO keybox（申请页面）
 */
app.controller('todoKeyboxInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, user, workflowTodo, type) {
    //$scope.workflow = workflow;
    // type 0 编辑 / 1 查看/审批
    $scope.type = type;

    $scope.btnSaveing = false;

    // TODO 可以取到当前用户信息
    $scope.user = user;

    $scope.workflowTodo = workflowTodo;
    $scope.nowDeptLeader = {};
    $scope.nowOps = {};

    $scope.initiatorUsername = "";
    $scope.assigneeUsersInfo = "";
    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    // 初始化审批选中状态
    var init = function () {
        // 设置deptLeader选中
        if ($scope.workflowTodo.todoUserList.deptLeader == null) {
            if ($scope.workflowTodo.dlUserList.length != 0)
                $scope.nowDeptLeader.selected = $scope.workflowTodo.dlUserList[0];
        } else {
            for (var i = 0; i < $scope.workflowTodo.dlUserList.length; i++) {
                if ($scope.workflowTodo.dlUserList[i].id == $scope.workflowTodo.todoUserList.deptLeader.userId) {
                    $scope.nowDeptLeader.selected = $scope.workflowTodo.dlUserList[i];
                    break;
                }
            }
        }

        // 设置ops选中
        if ($scope.workflowTodo.todoUserList.ops == null) {
            if ($scope.workflowTodo.opsUserList.length != 0)
                $scope.nowOps.selected = $scope.workflowTodo.opsUserList[0];
        } else {
            for (var i = 0; i < $scope.workflowTodo.opsUserList.length; i++) {
                if ($scope.workflowTodo.opsUserList[i].id == $scope.workflowTodo.todoUserList.ops.userId) {
                    $scope.nowOps.selected = $scope.workflowTodo.opsUserList[i];
                    break;
                }
            }
        }
    }

    init();

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

    /**
     * 未授权的
     * @param queryParam
     */
    $scope.queryServerGroup = function (queryParam) {
        var url = "/servergroup/query/unauthPage?page=0&length=10&name=" + queryParam + "&useType=0";

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
                init();
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

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }


    /////////////////////////////////////////////////
    $scope.saveTodo = function () {
        doSave();
    }

    var doSave = function () {
        if ($scope.workflowTodo.workflowDO.dlApproval) {
            if ($scope.nowDeptLeader.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "必须指定DeptLeader人选";
                return;
            } else {
                var deptLeader = {userId: $scope.nowDeptLeader.selected.id}
                $scope.workflowTodo.todoUserList.deptLeader = deptLeader;
            }
        }

        if ($scope.workflowTodo.workflowDO.opsAudit) {
            if ($scope.nowOps.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "必须指定Ops人选";
                return;
            } else {
                var ops = {userId: $scope.nowOps.selected.id}
                $scope.workflowTodo.todoUserList.ops = ops;
            }
        }
        $scope.btnSaveing = true;
        var url = "/workflow/todo/save";

        var requestBody = $scope.workflowTodo;

        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                $scope.workflowTodo = data.body;
                init();
                $scope.btnSaveing = false;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
                $scope.btnSaveing = false;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
            $scope.btnSaveing = false;
        });
    }


});


/**
 * TODO AliyunRAM (申请页面）
 */
app.controller('todoAliyunRamInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, user, workflowTodo, type) {
    //$scope.workflow = workflow;
    // type 0 编辑 / 1 查看/审批
    $scope.type = type;

    $scope.btnSaveing = false;
    // TODO 可以取到当前用户信息
    $scope.user = user;

    $scope.workflowTodo = workflowTodo;
    $scope.nowDeptLeader = {};
    $scope.nowOps = {};

    // 初始化审批选中状态
    var init = function () {
        // 设置deptLeader选中
        if ($scope.workflowTodo.todoUserList.deptLeader == null) {
            if ($scope.workflowTodo.dlUserList.length != 0)
                $scope.nowDeptLeader.selected = $scope.workflowTodo.dlUserList[0];
        } else {
            // if ($scope.workflowTodo.dlUserList.length == 0)
            //     $scope.workflowTodo.dlUserList.push($scope.workflowTodo.todoUserList.deptLeader);
            for (var i = 0; i < $scope.workflowTodo.dlUserList.length; i++) {
                if ($scope.workflowTodo.dlUserList[i].id == $scope.workflowTodo.todoUserList.deptLeader.userId) {
                    $scope.nowDeptLeader.selected = $scope.workflowTodo.dlUserList[i];
                    break;
                }
            }

            // $scope.nowDeptLeader.selected = $scope.workflowTodo.todoUserList.deptLeader
        }

        // 设置ops选中
        if ($scope.workflowTodo.todoUserList.ops == null) {
            if ($scope.workflowTodo.opsUserList.length != 0)
                $scope.nowOps.selected = $scope.workflowTodo.opsUserList[0];
        } else {
            // if ($scope.workflowTodo.opsUserList.length == 0)
            //     $scope.workflowTodo.opsUserList.push($scope.workflowTodo.todoUserList.ops)
            // $scope.nowOps.selected = $scope.workflowTodo.todoUserList.ops

            for (var i = 0; i < $scope.workflowTodo.opsUserList.length; i++) {
                if ($scope.workflowTodo.opsUserList[i].id == $scope.workflowTodo.todoUserList.ops.userId) {
                    $scope.nowOps.selected = $scope.workflowTodo.opsUserList[i];
                    break;
                }
            }
        }
    }

    init();


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
    $scope.setItem = function (todoDetail) {
        for (var i = 0; i < $scope.workflowTodo.todoDetails.length; i++) {
            if ($scope.workflowTodo.todoDetails[i].name == todoDetail.name) {
                $scope.workflowTodo.todoDetails[i].detail.apply = !$scope.workflowTodo.todoDetails[i].detail.apply;
            }
        }
    }


    /////////////////////////////////////////////////
    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }


    /////////////////////////////////////////////////
    $scope.saveTodo = function () {
        doSave();
    }

    var doSave = function () {
        if ($scope.workflowTodo.workflowDO.dlApproval) {
            if ($scope.nowDeptLeader.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "必须指定DeptLeader人选";
                return;
            } else {
                var deptLeader = {userId: $scope.nowDeptLeader.selected.id}
                $scope.workflowTodo.todoUserList.deptLeader = deptLeader;
            }
        }

        if ($scope.workflowTodo.workflowDO.opsAudit) {
            if ($scope.nowOps.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "必须指定Ops人选";
                return;
            } else {
                var ops = {userId: $scope.nowOps.selected.id}
                $scope.workflowTodo.todoUserList.ops = ops;
            }
        }

        $scope.btnSaveing = true;

        var url = "/workflow/todo/save";

        var requestBody = $scope.workflowTodo;

        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                $scope.workflowTodo = data.body;
                init();
                $scope.btnSaveing = false;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
                $scope.btnSaveing = false;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
            $scope.btnSaveing = false;
        });
    }

});

/**
 * TODO LdapGroup (外部平台权限 申请页面）
 */
app.controller('todoLdapGroupInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, user, workflowTodo, type) {
    //$scope.workflow = workflow;
    // type 0 编辑 / 1 查看/审批
    $scope.type = type;

    $scope.btnSaveing = false;

    // TODO 可以取到当前用户信息
    $scope.user = user;

    $scope.workflowTodo = workflowTodo;
    $scope.nowDeptLeader = {};
    $scope.nowOps = {};

    // 初始化审批选中状态
    var init = function () {
        // 设置deptLeader选中
        if ($scope.workflowTodo.todoUserList.deptLeader == null ) {
            if ($scope.workflowTodo.dlUserList.length != 0)
                $scope.nowDeptLeader.selected = $scope.workflowTodo.dlUserList[0];
        } else {
            // if ($scope.workflowTodo.dlUserList.length == 0)
            //     $scope.workflowTodo.dlUserList.push($scope.workflowTodo.todoUserList.deptLeader);
            for (var i = 0; i < $scope.workflowTodo.dlUserList.length; i++) {
                if ($scope.workflowTodo.dlUserList[i].id == $scope.workflowTodo.todoUserList.deptLeader.userId) {
                    $scope.nowDeptLeader.selected = $scope.workflowTodo.dlUserList[i];
                    break;
                }
            }

            // $scope.nowDeptLeader.selected = $scope.workflowTodo.todoUserList.deptLeader
        }

        // 设置ops选中
        if ($scope.workflowTodo.todoUserList.ops == null) {
            if ($scope.workflowTodo.opsUserList.length != 0)
                $scope.nowOps.selected = $scope.workflowTodo.opsUserList[0];
        } else {
            // if ($scope.workflowTodo.opsUserList.length == 0)
            //     $scope.workflowTodo.opsUserList.push($scope.workflowTodo.todoUserList.ops)
            // $scope.nowOps.selected = $scope.workflowTodo.todoUserList.ops

            for (var i = 0; i < $scope.workflowTodo.opsUserList.length; i++) {
                if ($scope.workflowTodo.opsUserList[i].id == $scope.workflowTodo.todoUserList.ops.userId) {
                    $scope.nowOps.selected = $scope.workflowTodo.opsUserList[i];
                    break;
                }
            }
        }
    }

    init();


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
    $scope.setItem = function (todoDetail) {
        for (var i = 0; i < $scope.workflowTodo.todoDetails.length; i++) {
            if ($scope.workflowTodo.todoDetails[i].name == todoDetail.name) {
                $scope.workflowTodo.todoDetails[i].detail.apply = !$scope.workflowTodo.todoDetails[i].detail.apply;
            }
        }
    }


    /////////////////////////////////////////////////
    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }


    /////////////////////////////////////////////////
    $scope.saveTodo = function () {
        doSave();
    }

    var doSave = function () {
        if ($scope.workflowTodo.workflowDO.dlApproval) {
            if ($scope.nowDeptLeader.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "必须指定DeptLeader人选";
                return;
            } else {
                var deptLeader = {userId: $scope.nowDeptLeader.selected.id}
                $scope.workflowTodo.todoUserList.deptLeader = deptLeader;
            }
        }

        if ($scope.workflowTodo.workflowDO.opsAudit) {
            if ($scope.nowOps.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "必须指定Ops人选";
                return;
            } else {
                var ops = {userId: $scope.nowOps.selected.id}
                $scope.workflowTodo.todoUserList.ops = ops;
            }
        }

        $scope.btnSaveing = true;
        var url = "/workflow/todo/save";

        var requestBody = $scope.workflowTodo;

        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                $scope.workflowTodo = data.body;
                init();
                $scope.btnSaveing = false;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
                $scope.btnSaveing = false;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
            $scope.btnSaveing = false;
        });
    }

});


/**
 * TODO AliyunRAMPolicy (申请页面）
 */
app.controller('todoAliyunRamPolicyInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, user, workflowTodo, type) {
    //$scope.workflow = workflow;
    // type 0 编辑 / 1 查看/审批
    $scope.type = type;

    $scope.btnSaveing = false;
    // TODO 可以取到当前用户信息
    $scope.user = user;
    $scope.ramUser = {};

    $scope.workflowTodo = workflowTodo;
    $scope.nowDeptLeader = {};
    $scope.nowOps = {};

    $scope.addItem = function () {
        if ($scope.nowRamPolicy.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择阿里云RAM策略才能添加!";
        } else {
            $scope.alert.type = '';
        }

        for (var i = 0; i < $scope.workflowTodo.todoDetails.length; i++) {
            if ($scope.workflowTodo.todoDetails[i].name == $scope.nowRamPolicy.selected.policyName) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "重复添加策略!";
                return;
            }
        }

        var workflowTodoDetailVO = {
            id: 0,
            todoId: $scope.workflowTodo.id,
            detailKey: "RAMPolicy",
            detail: $scope.nowRamPolicy.selected,
            name: $scope.nowRamPolicy.selected.policyName
        }

        //var requestBody = $scope.workflowTodo;
        $scope.workflowTodo.todoDetails.push(workflowTodoDetailVO);
    }

    $scope.delItem = function (todoDetail) {
        for (var i = 0; i < $scope.workflowTodo.todoDetails.length; i++) {
            if ($scope.workflowTodo.todoDetails[i].name == todoDetail.name) {
                $scope.workflowTodo.todoDetails.splice(i, 1);
                return;
            }
        }
        $scope.alert.type = 'warning';
        $scope.alert.msg = "移除失败!";
    }

    // 初始化审批选中状态
    var init = function () {
        // 设置deptLeader选中
        if ($scope.workflowTodo.todoUserList.deptLeader == null) {
            if ($scope.workflowTodo.dlUserList.length != 0)
                $scope.nowDeptLeader.selected = $scope.workflowTodo.dlUserList[0];
        } else {
            for (var i = 0; i < $scope.workflowTodo.dlUserList.length; i++) {
                if ($scope.workflowTodo.dlUserList[i].id == $scope.workflowTodo.todoUserList.deptLeader.userId) {
                    $scope.nowDeptLeader.selected = $scope.workflowTodo.dlUserList[i];
                    break;
                }
            }
        }

        // 设置ops选中
        if ($scope.workflowTodo.todoUserList.ops == null) {
            if ($scope.workflowTodo.opsUserList.length != 0)
                $scope.nowOps.selected = $scope.workflowTodo.opsUserList[0];
        } else {
            for (var i = 0; i < $scope.workflowTodo.opsUserList.length; i++) {
                if ($scope.workflowTodo.opsUserList[i].id == $scope.workflowTodo.todoUserList.ops.userId) {
                    $scope.nowOps.selected = $scope.workflowTodo.opsUserList[i];
                    break;
                }
            }
        }
    }

    init();

    $scope.nowRamPolicy = {};
    $scope.ramPolicyList = [];

    $scope.queryRamPolicy = function (queryName) {
        var url = "/aliyun/ram/policy/query?queryName=" + queryName;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.ramPolicyList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    //    $scope.user = user;
    //    $scope.ramUser = {};

    $scope.getRamUser = function () {
        // $scope.user.username
        var url = "/aliyun/ram/user/get?userId=" + $scope.workflowTodo.applyUserId;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.ramUser = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.getRamUser();

    $scope.saveRamUser = function () {
        if($scope.ramUser == null || $scope.ramUser.ramUserName == '') return ;
        var url = "/aliyun/ram/user/save";
        httpService.doPostWithJSON(url,$scope.ramUser).then(function (data) {
            if (data.success) {
                $scope.getRamUser();
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

    ////////////////////////////////////////////////////// todoDetail.detail.detachPolicy
    $scope.setDetachPolicy = function (todoDetail) {
        for (var i = 0; i < $scope.workflowTodo.todoDetails.length; i++) {
            if ($scope.workflowTodo.todoDetails[i].name == todoDetail.name) {
                $scope.workflowTodo.todoDetails[i].detail.detachPolicy = !$scope.workflowTodo.todoDetails[i].detail.detachPolicy;
            }
        }
    }


    /////////////////////////////////////////////////
    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }


    /////////////////////////////////////////////////
    $scope.saveTodo = function () {
        doSave();
    }

    var doSave = function () {
        if ($scope.workflowTodo.workflowDO.dlApproval) {
            if ($scope.nowDeptLeader.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "必须指定DeptLeader人选";
                return;
            } else {
                var deptLeader = {userId: $scope.nowDeptLeader.selected.id}
                $scope.workflowTodo.todoUserList.deptLeader = deptLeader;
            }
        }

        if ($scope.workflowTodo.workflowDO.opsAudit) {
            if ($scope.nowOps.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "必须指定Ops人选";
                return;
            } else {
                var ops = {userId: $scope.nowOps.selected.id}
                $scope.workflowTodo.todoUserList.ops = ops;
            }
        }

        $scope.btnSaveing = true;

        var url = "/workflow/todo/save";

        var requestBody = $scope.workflowTodo;

        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                $scope.workflowTodo = data.body;
                init();
                $scope.btnSaveing = false;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
                $scope.btnSaveing = false;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
            $scope.btnSaveing = false;
        });
    }

});