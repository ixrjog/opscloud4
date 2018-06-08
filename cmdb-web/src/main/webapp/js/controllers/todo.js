/**
 * Created by liangjian on 2017/9/5.
 */
'use strict';

app.controller('todoCtrl', function ($scope, $uibModal, $state, $sce, $interval, $timeout, toaster, httpService) {
        $scope.authPoint = $state.current.data.authPoint;
        $scope.todoGroupList = [];
        $scope.todoDetailList = [];
        $scope.todoDetailCompleteList = [];
        $scope.myJobStatusOpen = true;

        $scope.queryMyJob = function () {
            var url = "/todo/queryMyJob";
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.todoDetailList = data.body;
                    $scope.refreshInitiatorUserInfo();
                }
            });
        }

        $scope.queryCompleteJob = function () {
            var url = "/todo/queryCompleteJob";
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.todoDetailCompleteList = data.body;
                    $scope.refreshCompleteInitiatorUserInfo();
                    $scope.refreshCompleteAssigneeUserInfo();
                }
            });
        }

        // 生成发起人信息
        $scope.refreshCompleteInitiatorUserInfo = function () {

            if ($scope.todoDetailCompleteList.length == 0) return;

            for (var i = 0; i < $scope.todoDetailCompleteList.length; i++) {
                var info = '<b style="color: #286090">申请人</b>';
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

        $scope.queryMyJob();
        $scope.queryCompleteJob();

        // 60秒刷新1次待办工单
        var timer1 = $interval(function () {
            $scope.queryMyJob();
        }, 30000);

        // 60秒刷新1次待办工单
        var timer2 = $interval(function () {
            $scope.queryCompleteJob();
        }, 60000);

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

        $scope.queryTodoGroup = function () {
            var url = "/todo/group/query";
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    var body = data.body;
                    $scope.todoGroupList = body;
                } else {
                    toaster.pop("warning", data.msg);
                }
            }, function (err) {
                toaster.pop("error", err);
            });
        }

        $scope.queryTodoGroup();

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

        $scope.viewTodoKeyBox = function (todoDetail) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoKeyBoxModal',
                controller: 'todoKeyBoxInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    todoItem: function () {
                        return todoDetail.todoDO;
                    },
                    todoDetail: function () {
                        return todoDetail;
                    }
                }
            });
        }

        $scope.viewTodoCiUserGroup = function (todoDetail) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoCiUserGroupModal',
                controller: 'todoCiUserGroupInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    todoItem: function () {
                        return todoDetail.todoDO;
                    },
                    todoDetail: function () {
                        return todoDetail;
                    }
                }
            });
        }

        $scope.viewTodoVpn = function (todoDetail) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoVpnModal',
                controller: 'todoVpnInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    todoItem: function () {
                        return todoDetail.todoDO;
                    },
                    todoDetail: function () {
                        return todoDetail;
                    }
                }
            });
        }

        //// TODO 查看工单 平台权限申请
        $scope.viewSystemAuth = function (todoDetail) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoSystemAuthModal',
                controller: 'todoSystemAuthInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    todoItem: function () {
                        return todoDetail.todoDO;
                    },
                    todoDetail: function () {
                        return todoDetail;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.queryMyJob();
            }, function () {
                $scope.queryMyJob();
            });
        }

        //// TODO 查看工单 新项目申请
        $scope.viewTodoNewProject = function (todoDetail) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoNewProjectModal',
                controller: 'todoNewProjectInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    authPoint: function () {
                        return $scope.authPoint;
                    },
                    todoItem: function () {
                        return todoDetail.todoDO;
                    },
                    todoDetail: function () {
                        return todoDetail;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.queryMyJob();
            }, function () {
                $scope.queryMyJob();
            });
        }

        $scope.viewTodoCmdbRole = function (todoDetail) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoCmdbRoleModal',
                controller: 'todoCmdbRoleInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    authPoint: function () {
                        return $scope.authPoint;
                    },
                    todoItem: function () {
                        return todoDetail.todoDO;
                    },
                    todoDetail: function () {
                        return todoDetail;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.queryMyJob();
            }, function () {
                $scope.queryMyJob();
            });
        }

        //// TODO 查看工单 SCM权限申请
        $scope.viewTodoScm = function (todoDetail) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoScmModal',
                controller: 'todoScmInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    authPoint: function () {
                        return $scope.authPoint;
                    },
                    todoItem: function () {
                        return todoDetail.todoDO;
                    },
                    todoDetail: function () {
                        return todoDetail;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.queryMyJob();
            }, function () {
                $scope.queryMyJob();
            });
        }


        //// TODO 查看工单 SCM权限申请
        $scope.viewTodoTomcatVersion = function (todoDetail) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoTomcatVersionModal',
                controller: 'todoTomcatVersionInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    authPoint: function () {
                        return $scope.authPoint;
                    },
                    todoItem: function () {
                        return todoDetail.todoDO;
                    },
                    todoDetail: function () {
                        return todoDetail;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.queryMyJob();
            }, function () {
                $scope.queryMyJob();
            });
        }


        //////////////////////////////////////////////////////

        //// TODO 提交工单 堡垒机权限申请
        $scope.submitTodoKeyBox = function (todoItem) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoKeyBoxModal',
                controller: 'todoKeyBoxInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    todoItem: function () {
                        return todoItem;
                    },
                    todoDetail: function () {
                        return null;
                    }
                }
            });
            // 关闭查询
            modalInstance.result.then(function () {
                $scope.queryMyJob();
            }, function () {
                $scope.queryMyJob();
            });
        }

        //// TODO 提交工单 持续集成权限申请
        $scope.submitTodoCiUserGroup = function (todoItem) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoCiUserGroupModal',
                controller: 'todoCiUserGroupInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    todoItem: function () {
                        return todoItem;
                    },
                    todoDetail: function () {
                        return null;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.queryMyJob();
            }, function () {
                $scope.queryMyJob();
            });
        }

        //// TODO 提交工单 平台权限申请
        $scope.submitSystemAuth = function (todoItem) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoSystemAuthModal',
                controller: 'todoSystemAuthInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    todoItem: function () {
                        return todoItem;
                    },
                    todoDetail: function () {
                        return null;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.queryMyJob();
            }, function () {
                $scope.queryMyJob();
            });
        }

        //// TODO 提交工单 VPN权限申请
        $scope.submitTodoVpn = function (todoItem) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoVpnModal',
                controller: 'todoVpnInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    todoItem: function () {
                        return todoItem;
                    },
                    todoDetail: function () {
                        return null;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.queryMyJob();
            }, function () {
                $scope.queryMyJob();
            });
        }

        //// TODO 提交工单 新项目申请
        $scope.submitTodoNewProject = function (todoItem) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoNewProjectModal',
                controller: 'todoNewProjectInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    authPoint: function () {
                        return $scope.authPoint;
                    },
                    todoItem: function () {
                        return todoItem;
                    },
                    todoDetail: function () {
                        return null;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.queryMyJob();
            }, function () {
                $scope.queryMyJob();
            });
        }

        //// TODO 提交工单 持续集成权限申请
        $scope.submitTodoCmdbRole = function (todoItem) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoCmdbRoleModal',
                controller: 'todoCmdbRoleInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    authPoint: function () {
                        return $scope.authPoint;
                    },
                    todoItem: function () {
                        return todoItem;
                    },
                    todoDetail: function () {
                        return null;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.queryMyJob();
            }, function () {
                $scope.queryMyJob();
            });
        }

        //// TODO 提交工单 SCM权限申请
        $scope.submitTodoScm = function (todoItem) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoScmModal',
                controller: 'todoScmInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    authPoint: function () {
                        return $scope.authPoint;
                    },
                    todoItem: function () {
                        return todoItem;
                    },
                    todoDetail: function () {
                        return null;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.queryMyJob();
            }, function () {
                $scope.queryMyJob();
            });
        }

        //// TODO 提交工单 Tomcat(JDK)版本变更
        $scope.submitTodoTomcatVersion = function (todoItem) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoTomcatVersionModal',
                controller: 'todoTomcatVersionInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    authPoint: function () {
                        return $scope.authPoint;
                    },
                    todoItem: function () {
                        return todoItem;
                    },
                    todoDetail: function () {
                        return null;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.queryMyJob();
            }, function () {
                $scope.queryMyJob();
            });
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

app.controller('todoKeyBoxInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, todoItem, todoDetail) {
    $scope.todoItem = todoItem;
    $scope.todoDetail = todoDetail;
    //$scope.todoDetail = {};
    $scope.initiatorUsername = "";
    $scope.assigneeUsersInfo = "";
    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

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
        if (todoDetail != null) {
            $scope.refreshAssigneeUsersInfo();
            return;
        }

        var url = "/todo/establish?todoId=" + $scope.todoItem.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.todoDetail = data.body;
                $scope.initiatorUsername = $scope.todoDetail.initiatorUsername;
                $scope.refreshAssigneeUsersInfo();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });

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

        var url = "/todo/todoDetail/addTodoKeybox";

        var requestBody = {
            todoDetailId: $scope.todoDetail.id,
            serverGroupId: $scope.nowServerGroup.selected.id,
            ciAuth: choose
        }
        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功!";
                $scope.doQuery();
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
        var url = "/todo/todoDetail/delTodoKeybox?id=" + id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "删除成功!";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    $scope.submitTodo = function () {

        if ($scope.todoDetail.todoKeyboxDetailList == null || $scope.todoDetail.todoKeyboxDetailList.length == 0) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "工单未指定服务器组!";
            return;
        }

        var url = "/todo/todoDetail/submit?id=" + $scope.todoDetail.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "提交成功!";
                $scope.doQuery();
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

app.controller('todoCiUserGroupInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, todoItem, todoDetail) {
    $scope.todoItem = todoItem;
    $scope.todoDetail = todoDetail;
    //$scope.todoDetail = {};
    $scope.initiatorUsername = "";
    $scope.assigneeUsersInfo = "";
    $scope.nowCigroup = {};
    $scope.cigroupList = [];

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
        if (todoDetail != null) {
            $scope.refreshAssigneeUsersInfo();
            return;
        }

        var url = "/todo/establish?todoId=" + $scope.todoItem.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.todoDetail = data.body;
                $scope.initiatorUsername = $scope.todoDetail.initiatorUsername;
                $scope.refreshAssigneeUsersInfo();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    // 创建工单（复用）
    init();

    $scope.queryCiGroup = function (queryParam) {
        var url = "/ci/usergroup/page?page=0&length=10&envType=-1&groupName=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.cigroupList = body.data;
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
    $scope.addItem = function () {
        if ($scope.nowCigroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择权限组才能添加!";
        } else {
            $scope.alert.type = '';
        }

        var url = "/todo/todoDetail/addTodoCiUserGroup";

        var requestBody = {
            todoDetailId: $scope.todoDetail.id,
            ciUserGroupId: $scope.nowCigroup.selected.id
        }
        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功!";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    /////////////////////////////////////////////////

    $scope.delItem = function (id) {
        var url = "/todo/todoDetail/delTodoCiUserGroup?id=" + id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "删除成功!";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    /////////////////////////////////////////////////

    $scope.submitTodo = function () {

        if ($scope.todoDetail.todoCiUserGroupDetailList == null || $scope.todoDetail.todoCiUserGroupDetailList.length == 0) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "工单未指定持续集成权限组!";
            return;
        }

        var url = "/todo/todoDetail/submit?id=" + $scope.todoDetail.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "提交成功!";
                $scope.doQuery();
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

});

app.controller('todoSystemAuthInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, todoItem, todoDetail) {
    $scope.todoItem = todoItem;
    $scope.todoDetail = todoDetail;
    //$scope.todoDetail = {};
    $scope.initiatorUsername = "";
    $scope.assigneeUsersInfo = "";
    $scope.nowCigroup = {};
    $scope.cigroupList = [];

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
        if (todoDetail != null) {
            $scope.refreshAssigneeUsersInfo();
            return;
        }

        var url = "/todo/establish?todoId=" + $scope.todoItem.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.todoDetail = data.body;
                $scope.initiatorUsername = $scope.todoDetail.initiatorUsername;
                $scope.refreshAssigneeUsersInfo();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    // 创建工单（复用）
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

    /////////////////////////////////////////////////

    $scope.setItem = function (id) {
        var url = "/todo/todoDetail/setTodoSystemAuth?id=" + id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "设置成功!";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    /////////////////////////////////////////////////

    $scope.submitTodo = function () {

        var check = false;

        if ($scope.todoDetail.todoSystemAuthGetway.need) {
            check = true;
        } else {
            for (var i = 0; i < $scope.todoDetail.todoSystemAuthDetailList.length; i++) {
                var item = $scope.todoDetail.todoSystemAuthDetailList[i];
                if (item.need == true) {
                    check = true;
                    break;
                }
            }
        }

        if (!check) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "工单未配置!";
            return;
        }


        var url = "/todo/todoDetail/submit?id=" + $scope.todoDetail.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "提交成功!";
                $scope.doQuery();
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

});

app.controller('todoVpnInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, todoItem, todoDetail) {
    $scope.todoItem = todoItem;
    $scope.todoDetail = todoDetail;
    //$scope.todoDetail = {};
    $scope.initiatorUsername = "";
    $scope.assigneeUsersInfo = "";
    $scope.nowCigroup = {};
    $scope.cigroupList = [];

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
        if (todoDetail != null) {
            $scope.refreshAssigneeUsersInfo();
            return;
        }

        var url = "/todo/establish?todoId=" + $scope.todoItem.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.todoDetail = data.body;
                $scope.initiatorUsername = $scope.todoDetail.initiatorUsername;
                $scope.refreshAssigneeUsersInfo();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    // 创建工单（复用）
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

    /////////////////////////////////////////////////

    $scope.setItem = function (id) {
        var url = "/todo/todoDetail/setTodoVpn?id=" + id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "设置成功!";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    /////////////////////////////////////////////////

    $scope.submitTodo = function () {

        var check = false;

        if ($scope.todoDetail.todoVpn.need) {
            check = true;
        }

        if (!check) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "工单未配置!";
            return;
        }


        var url = "/todo/todoDetail/submit?id=" + $scope.todoDetail.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "提交成功!";
                $scope.doQuery();
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

});

app.controller('todoNewProjectInstanceCtrl', function ($scope, $uibModalInstance, $uibModal, $sce, staticModel, toaster, httpService, authPoint, todoItem, todoDetail) {
    //$scope.localAuthPoint = authPoint;

    $scope.envType = staticModel.envType;
    $scope.buildTools = staticModel.buildTools;
    $scope.instanceType = staticModel.instanceType;

    $scope.jdks = staticModel.jdks;
    $scope.tomcats = staticModel.tomcats;
    $scope.todoItem = todoItem;
    $scope.todoDetail = todoDetail;
    $scope.nowLeaderUser = {};

    // branches
    $scope.refs = [];

    $scope.checkAuth = function (item) {
        var list = authPoint;
        if (list == null || list == undefined) {
            return false;
        }
        var i = list.length - 1;
        while (i >= 0) {
            if (list[i].resourceName === item) {
                return true;
            } else {
                i--;
            }
        }
        return false;
    }

    $scope.builderPlan = {
        todoDetailId: 0,
        envType: -1,
        branch: "",
        buildTool: 0,
        needEnvParams: true,
        envParams: "",
        content: ""
    };

    $scope.newServerList = [];
    $scope.newServer = {
        todoDetailId: 0,
        envType: -1,
        instanceType: -1,
        zoneType: -1,
        allocateIp: false,
        dataDiskSize: 100,
        serverCnt: 1,
        content: ""
    };

    $scope.nowUser = {};

    $scope.userList = [];
    $scope.nowTool = {};
    $scope.nowStashProject = {};
    $scope.nowStashRepository = {};
    $scope.stashProjectList = [];
    $scope.stashRepositoryList = [];
    $scope.todoHelp = {};
    $scope.nowBranch = {};

    $scope.builderPlanList = [];
    //$scope.nowBuilderPlan = {};

    // 生成构建计划中的环境名称
    $scope.refreshBuilderPlans = function () {

        if ($scope.builderPlanList.length == 0) return;

        for (var i = 0; i < $scope.builderPlanList.length; i++) {
            var item = $scope.builderPlanList[i];
            for (var j = 0; j < $scope.envType.length; j++) {
                if ($scope.envType[j].code == item.envType) {
                    $scope.builderPlanList[i].envName = $scope.envType[j].name;
                }
            }
        }
    }

    $scope.checkProjectName = function (projectName) {


        if (projectName == null || projectName == '') {
            projectName = $scope.todoDetail.newProjectMap.projectName;
        }

        if (projectName == null || projectName == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "项目名未指定!";
            return;
        }

        var url = "/todo/todoNewProject/checkProjectName?projectName=" + projectName;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = projectName + "项目名可使用!";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    $scope.initRefs = function (refs) {
        if (refs == null) return;
        $scope.refs = [];
        if (refs.branches.length != 0) {
            for (var i = 0; i < refs.branches.length; i++) {
                var ref = {
                    type: "branches",
                    name: refs.branches[i]
                }
                $scope.refs.push(ref);
            }
        }
        if (refs.tags.length != 0) {
            for (var i = 0; i < refs.tags.length; i++) {
                var ref = {
                    type: "tags",
                    name: refs.tags[i]
                }
                $scope.refs.push(ref);
            }
        }
    }

    $scope.queryUsers = function (queryParam) {
        var url = "/safe/users?page=0&length=10&username=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.userList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    $scope.queryStashProject = function (queryParam) {
        var url = "/todo/todoNewProject/stash/project/query?page=0&length=10&name=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.stashProjectList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    $scope.changeStashRepository = function () {
        $scope.getStashProject();
        $scope.queryRefs();
    }

    $scope.getStashProject = function () {

        if ($scope.nowStashRepository.selected == null) return;

        if ($scope.nowStashProject.selected != null) return;

        var url = "/todo/todoNewProject/stash/project/get?id=" + $scope.nowStashRepository.selected.project_id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.nowStashProject.selected = body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });

    }

    $scope.changeStashProject = function () {
        $scope.queryStashRepository("");
    }

    $scope.queryStashRepository = function (queryParam) {

        // ($scope.nowServerGroup.selected == null ? -1 : $scope.nowServerGroup.selected.id)
        var url = "/todo/todoNewProject/stash/repository/query?page=0&length=10" +
            "&id=" + ($scope.nowStashProject.selected == null ? -1 : $scope.nowStashProject.selected.id) +
            "&name=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.stashRepositoryList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.addUser = function () {
        if ($scope.nowUser.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择用户才能添加!";
        } else {
            $scope.alert.type = '';
        }

        if ($scope.todoDetail.newProjectMap.users != 0) {
            for (var i = 0; i < $scope.todoDetail.newProjectMap.users.length; i++) {
                var username = $scope.todoDetail.newProjectMap.users[i].username;
                if (username === $scope.nowUser.selected.username) {
                    $scope.alert.type = 'warning';
                    $scope.alert.msg = "重复添加!";
                    return;
                }
            }
        }

        $scope.todoDetail.newProjectMap.users.push($scope.nowUser.selected);

    }

    $scope.delUser = function (user) {

        if ($scope.todoDetail.newProjectMap.users != 0) {
            for (var i = 0; i < $scope.todoDetail.newProjectMap.users.length; i++) {
                var username = $scope.todoDetail.newProjectMap.users[i].username;
                if (username === user.username) {
                    $scope.todoDetail.newProjectMap.users.splice(i, 1);
                    return;
                }
            }
        }

    }


    // 渲染帮助项
    $scope.trustAsHtmlTodoHelps = function () {

        var buildArgs = '<div style="width: 400px;"><b style="color: #286090">构建参数</b>';
        buildArgs += '<hr style="margin-bottom: 2px; margin-top: 2px" />' +
            '<b style="color: #777;">Maven : </b>clean package -Dmaven.test.skip=true -e -U<br/>' +
            '<b style="color: #777">Gradle : </b>clean war -refresh-dependencies<br/></div>'

        $scope.todoHelp.buildParams = $sce.trustAsHtml(
            buildArgs
        );

        var projectName = '<div style="width: 400px;"><b style="color: #286090">项目名称</b>';
        projectName += '<hr style="margin-bottom: 2px; margin-top: 2px" />' +
            '<b style="color: #777;">* </b>使用小写英文<br/>' +
            '<b style="color: #777;">* </b>单词链接使用\"-\"<br/>' +
            '<b style="color: #777;">* </b>名称中不要使用带环境的关键字,例如"trade<b style="color: darkred;">-daily</b>"<br/>' +
            '<b style="color: #777;">* </b>环境关键字：<br/>' +
            '<b style="color: #777;"> > </b>开发环境关键字\"<b style="color: #5bc0de;">-dev</b>\"<br/>' +
            '<b style="color: #777;"> > </b>日常环境关键字\"<b style="color: #449d44;">-daily</b>\"<br/>' +
            '<b style="color: #777;"> > </b>预发环境关键字\"<b style="color: #ec971f;">-gray</b>\"<br/>' +
            '<b style="color: #777;"> > </b>生产环境关键字\"<b style="color: #d9534f;">-prod</b>\"<br/>' +
            '<b style="color: #777;"> > </b>测试环境关键字\"<b style="color: #5e5e5e;">-test</b>\"<br/>' +
            '<b style="color: #777;"> > </b>后台环境关键字\"<b style="color: #286090;">-back</b>\"<br/>' +
            '<b style="color: #777;">* </b>正确的列子：trade,tradeback,ka-groupon<br/></div>'

        $scope.todoHelp.projectName = $sce.trustAsHtml(
            projectName
        );

        //todoHelp.environmentVariables
        var environmentVariables = '<div style="width: 400px;"><b style="color: #286090">环境参数</b>';
        environmentVariables += '<hr style="margin-bottom: 2px; margin-top: 2px" />' +
            '<b style="color: #777;">Extra environment variables.</b><br/>' +
            '<b style="color: #777;">e.g. MAVEN_OPTS="</b>' +
            '<b style="color: darkred;">-Xmx256m -Xms128m</b>' +
            '<b style="color: #777;">".</b><br/>' +
            '<b style="color: #777;">You can add multiple parameters separated by a space.</b><br/></div>'

        $scope.todoHelp.environmentVariables = $sce.trustAsHtml(
            environmentVariables
        );

        var branch = '<div style="width: 400px;"><b style="color: #286090">代码分支</b>';
        branch += '<hr style="margin-bottom: 2px; margin-top: 2px" />' +
            '<b style="color: #777;">*</b>日常环境发布分支:\"<b style="color: #449d44;">develop</b>\"<br/>' +
            '<b style="color: #777;">*</b>灰度环境发布分支:\"<b style="color: #ec971f;">gray</b>\"<br/>' +
            '<b style="color: #777;">*</b>生产环境发布分支:\"<b style="color: #d9534f;">master</b>\"<br/></div>'

        $scope.todoHelp.branch = $sce.trustAsHtml(
            branch
        );

        var serverEnv = '<div style="width: 400px;"><b style="color: #286090">服务器环境</b>';
        serverEnv += '<hr style="margin-bottom: 2px; margin-top: 2px" />' +
            '<b style="color: #777;">*</b>开发环境"<b style="color: #5bc0de;">dev</b>\"<br/>' +
            '<b style="color: #777;">*</b>日常环境\"<b style="color: #449d44;">daily</b>\"<br/>' +
            '<b style="color: #777;">*</b>预发环境\"<b style="color: #ec971f;">gray</b>\"<br/>' +
            '<b style="color: #777;">*</b>生产环境\"<b style="color: #d9534f;">prod</b>\"<br/>' +
            '<b style="color: #777;">*</b>测试环境\"<b style="color: #5e5e5e;">test</b>\"<br/>' +
            '<b style="color: #777;">*</b>后台环境\"<b style="color: #286090;">back</b>\"<br/></div>';

        $scope.todoHelp.serverEnv = $sce.trustAsHtml(
            serverEnv
        );

        var envParams = '<div style="width: 400px;"><b style="color: #286090">环境参数</b>';
        envParams += '<hr style="margin-bottom: 2px; margin-top: 2px" />' +
            '<b style="color: #777;">Maven : </b>clean package -Dmaven.test.skip=true -e -U<br/>' +
            '<b style="color: #777;">*</b>日常环境构建参数:<b style="color: #449d44;">-Pdaily</b><br/>' +
            '<b style="color: #777;">*</b>灰度环境构建参数:<b style="color: #ec971f;">-Pgray</b><br/>' +
            '<b style="color: #777;">*</b>日常环境构建参数:<b style="color: #d9534f;">-Ponline</b><br/>' +
            '<b style="color: #777">Gradle : </b>clean war -refresh-dependencies<br/>' +
            '<b style="color: #777;">*</b>日常环境构建参数:<b style="color: #449d44;">-Denv=daily</b><br/>' +
            '<b style="color: #777;">*</b>灰度环境构建参数:<b style="color: #ec971f;">-Denv=gray</b><br/>' +
            '<b style="color: #777;">*</b>日常环境构建参数:<b style="color: #d9534f;">-Denv=online</b><br/></div>';

        $scope.todoHelp.envParams = $sce.trustAsHtml(
            envParams
        );

        var zoneType = '<div style="width: 500px;"><b style="color: #286090">区域</b>';
        zoneType += '<hr style="margin-bottom: 2px; margin-top: 2px" />' +
            '<b style="color: #777;">内部虚拟机 : </b>办公网的私有云服务器，通常给内部系统和daily环境使用.<br/>' +
            '<b style="color: #777">阿里云 : </b>阿里云ECS服务器，prod和gray环境使用.<br/></div>'

        $scope.todoHelp.zoneType = $sce.trustAsHtml(
            zoneType
        );

        // allocateIp
        var allocateIp = '<div style="width: 250px;"><b style="color: #286090">阿里云公网IP</b>';
        allocateIp += '<hr style="margin-bottom: 2px; margin-top: 2px" />' +
            '<b style="color: #777;">访问外部平台需要开通公网IP，没有公网IP不影响内部系统调用(通过nginx访问也不需要公网IP).</b></div>'

        $scope.todoHelp.allocateIp = $sce.trustAsHtml(
            allocateIp
        );

        // tomcatAppName
        var tomcatAppName = '<div style="width: 250px;"><b style="color: #286090">Tomcat名称</b>';
        tomcatAppName += '<hr style="margin-bottom: 2px; margin-top: 2px" />' +
            '<b style="color: #777;">可以留空，默认为项目名称（按运维规范也必须为项目名）</b></div>'
        $scope.todoHelp.tomcatAppName = $sce.trustAsHtml(
            tomcatAppName
        );

        // tomcatWebAppsPath
        var tomcatWebAppsPath = '<div style="width: 500px;"><b style="color: #286090">Tomcat项目路径配置</b>';
        tomcatWebAppsPath += '<hr style="margin-bottom: 2px; margin-top: 2px" />' +
            '<b style="color: #777;">* 可以留空，默认为项目名称（访问路径http://localhost:8080/${项目名称}/</b><br/>' +
            '<b style="color: #777;">* 需要根路径访问则配置\"<b style="color: darkred;">ROOT</b>\"（访问路径http://localhost:8080/）</b></div>'
        $scope.todoHelp.tomcatWebAppsPath = $sce.trustAsHtml(
            tomcatWebAppsPath
        );

        // httpStatus
        var httpStatus = '<div style="width: 500px;"><b style="color: #286090">check页面配置</b>';
        httpStatus += '<hr style="margin-bottom: 2px; margin-top: 2px" />' +
            '<b style="color: #777;">* 可以留空，默认检测页面:http://localhost:8080/${项目名称}/webStauts</b><br/>' +
            '<b style="color: #777;">* 若Tomcat项目路径配置="<b style="color: darkred;">ROOT</b>"则检测页面:http://localhost:8080/webStatus</b></div>'
        // webStatus
        $scope.todoHelp.httpStatus = $sce.trustAsHtml(
            httpStatus
        );
    }

    ////////
    $scope.initiatorUsername = "";
    $scope.assigneeUsersInfo = "";
    $scope.nowCigroup = {};
    $scope.cigroupList = [];
    //////////
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
        // 新建工单
        if ($scope.todoDetail == null) {
            var url = "/todo/establish?todoId=" + $scope.todoItem.id;
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.todoDetail = data.body;
                    $scope.initiatorUsername = $scope.todoDetail.initiatorUsername;
                    $scope.nowLeaderUser.selected = $scope.todoDetail.newProjectMap.leaderUser;
                    $scope.refreshAssigneeUsersInfo();
                    // 查询构建计划
                    $scope.doQueryBuilderPlans();
                    // 查询新建服务器
                    $scope.doQueryNewServers();
                    initData();
                } else {
                    toaster.pop("warning", data.msg);
                }
            }, function (err) {
                toaster.pop("error", err);
            });
        } else {
            $scope.refreshAssigneeUsersInfo();
            // 查看工单
            //var url = "/todo/todoDetail/query?todoId=" + $scope.todoItem.id;
            $scope.initiatorUsername = $scope.todoDetail.initiatorUsername;
            $scope.nowLeaderUser.selected = $scope.todoDetail.newProjectMap.leaderUser;
            $scope.refreshAssigneeUsersInfo();
            $scope.builderPlanList = $scope.todoDetail.todoBuilderPlanDetailList;
            // 生成构建计划中的环境名称
            $scope.refreshBuilderPlans();
            $scope.newServerList = $scope.todoDetail.todoNewServerDetailList;
            initData();
        }

        $scope.trustAsHtmlTodoHelps();

    }

    var initData = function () {

        if ($scope.todoDetail.newProjectMap.stashProject != '') {
            var stashProject = {
                id: 0,
                name: $scope.todoDetail.newProjectMap.stashProject
            }
            $scope.nowStashProject.selected = stashProject;
        }

        if ($scope.todoDetail.newProjectMap.stashRepository != '') {
            var stashRepository = {
                id: 0,
                name: $scope.todoDetail.newProjectMap.stashRepository
            }
            $scope.nowStashRepository.selected = stashRepository;
        }
        //$scope.queryRefs();
    }

    // 初始化数据
    init();

    // 查询分支 数据库缓存
    $scope.queryRefs = function () {

        if ($scope.nowStashProject.selected == null) return;
        if ($scope.nowStashRepository.selected == null) return;
        $scope.doQueryRefs = true;

        var url = "/git/refs/query?"
            + "project=" + $scope.nowStashProject.selected.name + "&"
            + "repo=" + $scope.nowStashRepository.selected.name;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.initRefs(data.body);
                //$scope.refs = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
            $scope.doQueryRefs = false;
        }, function (err) {
            toaster.pop("error", err);
            $scope.doQueryRefs = false;
        });
    }

    // 查询分支
    $scope.getRefs = function () {

        if ($scope.nowStashProject.selected == null) return;
        if ($scope.nowStashRepository.selected == null) return;
        $scope.doQueryRefs = true;

        var url = "/git/refs/get?"
            + "project=" + $scope.nowStashProject.selected.name + "&"
            + "repo=" + $scope.nowStashRepository.selected.name;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.initRefs(data.body);
            } else {
                toaster.pop("warning", data.msg);
            }
            $scope.doQueryRefs = false;
        }, function (err) {
            toaster.pop("error", err);
            $scope.doQueryRefs = false;
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

    /////////////////////////////////////////////////

    // $scope.setItem = function (id) {
    //     var url = "/todo/todoDetail/setTodoVpn?id=" + id;
    //
    //     httpService.doGet(url).then(function (data) {
    //         if (data.success) {
    //             $scope.alert.type = 'success';
    //             $scope.alert.msg = "设置成功!";
    //             $scope.doQuery();
    //         } else {
    //             $scope.alert.type = 'warning';
    //             $scope.alert.msg = data.msg;
    //         }
    //     }, function (err) {
    //         $scope.alert.type = 'warning';
    //         $scope.alert.msg = err;
    //     });
    // }

    /////////////////////////////////////////////////
    // 保存工单
    $scope.saveTodo = function () {

        var url = "/todo/todoDetail/save"

        if ($scope.nowStashProject.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定Stash项目名";
            return;
        } else {
            $scope.todoDetail.newProjectMap.stashProject = $scope.nowStashProject.selected.name;
        }

        if ($scope.nowStashRepository.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定Stash仓库名";
            return;
        } else {
            $scope.todoDetail.newProjectMap.stashRepository = $scope.nowStashRepository.selected.name;
        }

        var requestBody = $scope.todoDetail;

        //requestBody.newProjectMap.stashProject

        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";
                $scope.todoDetail = data.body;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }


    // 提交工单
    $scope.submitTodo = function () {

        $scope.saveTodo();

        if ($scope.alert.type != 'success') {
            $scope.alert.type = 'warning';
            $scope.alert.msg += '不能提交工单：工单保存失败！';
            return;
        }

        var url = "/todo/todoDetail/submit?id=" + $scope.todoDetail.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "提交成功!";
                $scope.doQuery();
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

    $scope.doQueryBuilderPlans = function () {
        var url = "/todo/todoNewProject/builderPlan/query?todoDetailId=" + $scope.todoDetail.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                //$scope.totalItems = body.size;
                $scope.builderPlanList = body.data;
                // 生成构建计划中的环境名称
                $scope.refreshBuilderPlans();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.saveBuilderPlan = function () {

        if ($scope.nowBranch.selected == null || $scope.nowBranch.selected == "") {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定分支";
            return;
        } else {
            $scope.builderPlan.branch = $scope.nowBranch.selected.name;
        }

        if ($scope.builderPlan.envType == null || $scope.builderPlan.envType == -1) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定服务器环境";
            return;
        }

        $scope.builderPlan.buildTool = $scope.todoDetail.newProjectMap.buildTool;
        $scope.builderPlan.todoDetailId = $scope.todoDetail.id;
        var url = "/todo/todoNewProject/builderPlan/save";

        httpService.doPostWithJSON(url, $scope.builderPlan).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功!";
                //$scope.builderPlan = data.body;
                $scope.doQueryBuilderPlans();
                $scope.resetBuilderPlan();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.delBuilderPlan = function (id) {
        var url = "/todo/todoNewProject/builderPlan/del?id=" + id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                //$scope.builderPlanList = data.body;
                $scope.doQueryBuilderPlans();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.resetBuilderPlan = function () {
        $scope.builderPlan = {
            todoDetailId: 0,
            envType: -1,
            branch: "",
            buildTool: -1,
            needEnvParams: true,
            envParams: "",
            content: ""
        };

        $scope.nowBranch = {};
    }


    $scope.editBuilderPlan = function (builderPlan) {
        $scope.builderPlan = builderPlan;
    }

    /////////////////////////////////////////////////
    $scope.doQueryNewServers = function () {
        var url = "/todo/todoNewProject/newServer/query?todoDetailId=" + $scope.todoDetail.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                //$scope.totalItems = body.size;
                $scope.newServerList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.saveNewServer = function () {

        if ($scope.newServer.envType == null || $scope.newServer.envType == -1) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定服务器环境";
            var x = 1;
            return;
        }

        if ($scope.newServer.zoneType == null || $scope.newServer.zoneType == -1) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定服务器区域";
            return;
        }

        if ($scope.newServer.instanceType == null || $scope.newServer.instanceType == -1) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定虚拟机规格";
            return;
        }

        $scope.newServer.todoDetailId = $scope.todoDetail.id;

        var url = "/todo/todoNewProject/newServer/save";

        httpService.doPostWithJSON(url, $scope.newServer).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功!";
                //$scope.newServer = data.body;
                $scope.doQueryNewServers();
                $scope.resetNewServer();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.delNewServer = function (id) {
        var url = "/todo/todoNewProject/newServer/del?id=" + id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.doQueryNewServers();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.resetNewServer = function () {
        $scope.newServer = {
            todoDetailId: 0,
            envType: -1,
            instanceType: -1,
            zoneType: -1,
            allocateIp: false,
            dataDiskSize: 100,
            serverCnt: 1,
            content: ""
        };
    }

    $scope.editNewServer = function (newServer) {
        $scope.newServer = newServer;
    }

    $scope.resetTodoOtherCfg = function () {
        $scope.todoDetail.newProjectMap.tomcatAppName = "";
        $scope.todoDetail.newProjectMap.tomcatWebAppsPath = "";
        $scope.todoDetail.newProjectMap.httpStatus = "";
        $scope.todoDetail.newProjectMap.webWww = false;
        $scope.todoDetail.newProjectMap.webManage = false;
        $scope.todoDetail.newProjectMap.webKa = false;
    }

    // 执行工单
    $scope.invokeNewProjectTode = function (type) {
        var url = "/todo/todoNewProject/invoke?todoDetailId=" + $scope.todoDetail.id +
            "&type=" + type;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "执行成功!");
                if (type == 0) {
                    $scope.todoDetail.newProjectMap.procServerGroup = true;
                }
            } else {
                toaster.pop("warning", "执行失败!");
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

});

app.controller('todoCmdbRoleInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, todoItem, todoDetail) {
    $scope.todoItem = todoItem;
    $scope.todoDetail = todoDetail;
    //$scope.todoDetail = {};
    $scope.initiatorUsername = "";
    $scope.assigneeUsersInfo = "";
    $scope.nowCigroup = {};
    $scope.cigroupList = [];

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
        if (todoDetail != null) {
            $scope.refreshAssigneeUsersInfo();
            return;
        }

        var url = "/todo/establish?todoId=" + $scope.todoItem.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.todoDetail = data.body;
                $scope.initiatorUsername = $scope.todoDetail.initiatorUsername;
                $scope.refreshAssigneeUsersInfo();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    // 创建工单（复用）
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

    /////////////////////////////////////////////////

    $scope.setItem = function (roleName) {
        if (roleName == 'devFt') {
            $scope.todoDetail.cmdbRoleMap.devFt = !$scope.todoDetail.cmdbRoleMap.devFt;
        }

        if (roleName == 'devAndroid') {
            $scope.todoDetail.cmdbRoleMap.devAndroid = !$scope.todoDetail.cmdbRoleMap.devAndroid;
        }

        if (roleName == 'devIos') {
            $scope.todoDetail.cmdbRoleMap.devIos = !$scope.todoDetail.cmdbRoleMap.devIos;
        }

        if (roleName == 'qa') {
            $scope.todoDetail.cmdbRoleMap.qa = !$scope.todoDetail.cmdbRoleMap.qa;
        }

        var requestBody = $scope.todoDetail;
        var url = "/todo/todoDetail/save";
        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";
                $scope.todoDetail = data.body;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    /////////////////////////////////////////////////

    $scope.submitTodo = function () {

        var check = false;

        if ($scope.todoDetail.cmdbRoleMap.devFt == true) {
            check = true;
        }

        if ($scope.todoDetail.cmdbRoleMap.devAndroid == true) {
            check = true;
        }

        if ($scope.todoDetail.cmdbRoleMap.devIos == true) {
            check = true;
        }

        if ($scope.todoDetail.cmdbRoleMap.qa == true) {
            check = true;
        }

        if (!check) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "工单未配置!";
            return;
        }


        var url = "/todo/todoDetail/submit?id=" + $scope.todoDetail.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "提交成功!";
                $scope.doQuery();
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

});

app.controller('todoScmInstanceCtrl', function ($scope, $uibModalInstance, $uibModal, $sce, staticModel, toaster, httpService, authPoint, todoItem, todoDetail) {
    //$scope.localAuthPoint = authPoint;

    $scope.todoItem = todoItem;
    $scope.todoDetail = todoDetail;

    // branches
    $scope.refs = [];

    $scope.checkAuth = function (item) {
        var list = authPoint;
        if (list == null || list == undefined) {
            return false;
        }
        var i = list.length - 1;
        while (i >= 0) {
            if (list[i].resourceName === item) {
                return true;
            } else {
                i--;
            }
        }
        return false;
    }

    $scope.nowStashProject = {};
    $scope.nowStashRepository = {};
    $scope.stashProjectList = [];
    $scope.stashRepositoryList = [];
    $scope.todoHelp = {};
    $scope.nowBranch = {};

    $scope.initRefs = function (refs) {
        if (refs == null) return;
        $scope.refs = [];
        if (refs.branches.length != 0) {
            for (var i = 0; i < refs.branches.length; i++) {
                var ref = {
                    type: "branches",
                    name: refs.branches[i]
                }
                $scope.refs.push(ref);
            }
        }
        if (refs.tags.length != 0) {
            for (var i = 0; i < refs.tags.length; i++) {
                var ref = {
                    type: "tags",
                    name: refs.tags[i]
                }
                $scope.refs.push(ref);
            }
        }
    }

    $scope.queryStashProject = function (queryParam) {
        var url = "/todo/todoNewProject/stash/project/query?page=0&length=10&name=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.stashProjectList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.changeStashRepository = function () {
        $scope.getStashProject();
        $scope.queryRefs();
    }

    $scope.getStashProject = function () {
        if ($scope.nowStashRepository.selected == null) return;
        if ($scope.nowStashProject.selected != null) return;
        var url = "/todo/todoNewProject/stash/project/get?id=" + $scope.nowStashRepository.selected.project_id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.nowStashProject.selected = body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    // 选中scmProject则查询SCM组配置
    $scope.getScmPermissions = function () {
        if ($scope.nowStashProject.selected == null) return;
        var url = "/scm/permissions/get?scmProject=" + $scope.nowStashProject.selected.name;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.todoDetail.scmMap.ciUserGroupId = body.ciUserGroupId;
                $scope.todoDetail.scmMap.groupName = body.groupName;
                $scope.todoDetail.scmMap.scmDesc = body.scmDescription;
                $scope.todoDetail.scmMap.scmProject = body.scmProject;
                //  $scope.nowStashProject.selected = body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.changeStashProject = function () {
        $scope.queryStashRepository("");
        $scope.getScmPermissions();
    }

    $scope.queryStashRepository = function (queryParam) {

        // ($scope.nowServerGroup.selected == null ? -1 : $scope.nowServerGroup.selected.id)
        var url = "/todo/todoNewProject/stash/repository/query?page=0&length=10" +
            "&id=" + ($scope.nowStashProject.selected == null ? -1 : $scope.nowStashProject.selected.id) +
            "&name=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.stashRepositoryList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    ////////
    $scope.initiatorUsername = "";
    $scope.assigneeUsersInfo = "";
    $scope.nowCigroup = {};
    $scope.cigroupList = [];
    //////////
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
        // 新建工单
        if ($scope.todoDetail == null) {
            var url = "/todo/establish?todoId=" + $scope.todoItem.id;
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.todoDetail = data.body;
                    if ($scope.todoDetail.scmMap.scmProjectDO != null) {
                        $scope.nowStashProject.selected = $scope.todoDetail.scmMap.scmProjectDO;
                    }
                    $scope.initiatorUsername = $scope.todoDetail.initiatorUsername;
                    //$scope.nowLeaderUser.selected = $scope.todoDetail.newProjectMap.leaderUser;
                    $scope.refreshAssigneeUsersInfo();
                } else {
                    toaster.pop("warning", data.msg);
                }
            }, function (err) {
                toaster.pop("error", err);
            });
        } else {
            $scope.refreshAssigneeUsersInfo();
            // 查看工单
            //var url = "/todo/todoDetail/query?todoId=" + $scope.todoItem.id;
            $scope.initiatorUsername = $scope.todoDetail.initiatorUsername;
            $scope.refreshAssigneeUsersInfo();
            if ($scope.todoDetail.scmMap.scmProjectDO != null) {
                $scope.nowStashProject.selected = $scope.todoDetail.scmMap.scmProjectDO;
            }
        }

    }

    // 初始化数据
    init();

    // 查询分支 数据库缓存
    $scope.queryRefs = function () {

        if ($scope.nowStashProject.selected == null) return;
        if ($scope.nowStashRepository.selected == null) return;
        $scope.doQueryRefs = true;

        var url = "/git/refs/query?"
            + "project=" + $scope.nowStashProject.selected.name + "&"
            + "repo=" + $scope.nowStashRepository.selected.name;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.initRefs(data.body);
                //$scope.refs = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
            $scope.doQueryRefs = false;
        }, function (err) {
            toaster.pop("error", err);
            $scope.doQueryRefs = false;
        });
    }

    // 查询分支
    $scope.getRefs = function () {

        if ($scope.nowStashProject.selected == null) return;
        if ($scope.nowStashRepository.selected == null) return;
        $scope.doQueryRefs = true;

        var url = "/git/refs/get?"
            + "project=" + $scope.nowStashProject.selected.name + "&"
            + "repo=" + $scope.nowStashRepository.selected.name;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.initRefs(data.body);
            } else {
                toaster.pop("warning", data.msg);
            }
            $scope.doQueryRefs = false;
        }, function (err) {
            toaster.pop("error", err);
            $scope.doQueryRefs = false;
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


    /////////////////////////////////////////////////
    // 保存工单
    $scope.saveTodo = function () {

        var url = "/todo/todoDetail/save"
        if ($scope.nowStashProject.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定Stash项目名";
            return;
        }

        if ($scope.todoDetail.scmMap.scmProject == null || $scope.todoDetail.scmMap.scmProject == "") {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定SCM项目名";
            return;
        }

        var requestBody = $scope.todoDetail;
        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";
                $scope.todoDetail = data.body;
                $scope.submitTodo();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
                return;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
            return;
        });

    }


    // 提交工单
    $scope.submitTodo = function () {
        var url = "/todo/todoDetail/submit?id=" + $scope.todoDetail.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "提交成功!";
                $scope.doQuery();
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

    $scope.resetTodo = function () {
        $scope.todoDetail.scmMap.ciUserGroupId = 0;
        $scope.todoDetail.scmMap.groupName = "";
        $scope.todoDetail.scmMap.scmDesc = "";
        $scope.todoDetail.scmMap.scmProject = "";
        $scope.nowStashProject = {};
        $scope.nowStashRepository = {};
    }

    // 执行工单
    $scope.invokeScmTode = function (type) {
        var url = "/todo/todoNewProject/invoke?todoDetailId=" + $scope.todoDetail.id +
            "&type=" + type;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "执行成功!");
                if (type == 0) {
                    $scope.todoDetail.newProjectMap.procServerGroup = true;
                }
            } else {
                toaster.pop("warning", "执行失败!");
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

});

app.controller('todoTomcatVersionInstanceCtrl', function ($scope, $uibModalInstance, $uibModal, $sce, staticModel, toaster, httpService, authPoint, todoItem, todoDetail) {
    //$scope.localAuthPoint = authPoint;

    $scope.todoItem = todoItem;
    $scope.todoDetail = todoDetail;

    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    $scope.tomcatVersion = {};

    $scope.tomcatNewVersion = {};

    $scope.jdkNewVersion = {};

    $scope.jdks = staticModel.jdks;
    $scope.tomcats = staticModel.tomcats;

    $scope.taskMsg = "";

    $scope.pageData = [];

    $scope.butTaskUpdateTomcatDisabled = false;

    var butTaskUpdateTomcatRunning = function (isRun) {
        $scope.butTaskUpdateTomcatDisabled = isRun;
        if (isRun) {
            $scope.taskMsg = "请勿关闭窗口，等待任务执行！！！";
        }
    }


    $scope.checkAuth = function (item) {
        var list = authPoint;
        if (list == null || list == undefined) {
            return false;
        }
        var i = list.length - 1;
        while (i >= 0) {
            if (list[i].resourceName === item) {
                return true;
            } else {
                i--;
            }
        }
        return false;
    }

    $scope.todoHelp = {};

    $scope.getTomcatVersion = function () {
        var url = "/todo/todoTomcatVersion/tomcatVersion/query?"
            + "serverGroupId=" + $scope.nowServerGroup.selected.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.tomcatVersion = data.body;
                $scope.todoDetail.tomcatVersionMap.tomcatInstallVersion = $scope.tomcatVersion.tomcatInstallVersion;
                $scope.todoDetail.tomcatVersionMap.jdkInstallVersion = $scope.tomcatVersion.jdkInstallVersion;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    $scope.queryServerGroup = function (queryParam) {
        // web-service
        //var useType = 2;

        var url = "/servergroup/query/page?page=0&length=10&name=" + queryParam + "&useType=2";

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

    $scope.changeServerGroup = function () {
        if ($scope.nowServerGroup.selected != null) {
            $scope.getTomcatVersion();
        }
    }


    $scope.queryZabbixServer = function () {
        if ($scope.nowServerGroup.selected == null) return;

        var url = "/zabbixserver/page?"
            + "serverGroupId=" + $scope.nowServerGroup.selected.id
            + "&serverName="
            + "&useType=2"
            + "&envType=-1"
            + "&queryIp="
            + "&zabbixStatus=-1"
            + "&zabbixMonitor=-1"
            + "&tomcatVersion="
            + "&page=0"
            + "&length=20";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                //$scope.totalItems = body.size;
                $scope.pageData = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    $scope.taskUpdateTomcat = function (serverId) {
        if ($scope.nowServerGroup.selected == null) return;

        butTaskUpdateTomcatRunning(true);

        var url = "/todo/todoTomcatVersion/task/updateTomcat?"
            + "todoDetailId=" + $scope.todoDetail.id
            + "&serverId=" + serverId;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                //$scope.alert.type = 'success';
                //$scope.alert.msg = body.msg;
                $scope.taskMsg = body.msg;
                butTaskUpdateTomcatRunning(false);
            } else {
                //$scope.alert.type = 'warning';
                // $scope.alert.msg = data.msg;
                $scope.taskMsg = data.msg;
                butTaskUpdateTomcatRunning(false);
            }
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
            $scope.taskMsg = "";
            butTaskUpdateTomcatRunning(false);
        });

    }

    $scope.taskRollbackTomcat = function (serverId) {
        if ($scope.nowServerGroup.selected == null) return;

        butTaskUpdateTomcatRunning(true);

        var url = "/todo/todoTomcatVersion/task/rollbackTomcat?"
            + "todoDetailId=" + $scope.todoDetail.id
            + "&serverId=" + serverId;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.taskMsg = body.msg;
                butTaskUpdateTomcatRunning(false);
            } else {
                $scope.taskMsg = data.msg;
                butTaskUpdateTomcatRunning(false);
            }
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
            $scope.taskMsg = "";
            butTaskUpdateTomcatRunning(false);
        });

    }


    ////////
    $scope.initiatorUsername = "";
    $scope.assigneeUsersInfo = "";

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
        // 新建工单
        if ($scope.todoDetail == null) {
            var url = "/todo/establish?todoId=" + $scope.todoItem.id;
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.todoDetail = data.body;
                    $scope.initiatorUsername = $scope.todoDetail.initiatorUsername;
                    //$scope.nowLeaderUser.selected = $scope.todoDetail.newProjectMap.leaderUser;
                    $scope.refreshAssigneeUsersInfo();
                } else {
                    toaster.pop("warning", data.msg);
                }
            }, function (err) {
                toaster.pop("error", err);
            });
        } else {
            $scope.refreshAssigneeUsersInfo();
            // 查看工单
            //var url = "/todo/todoDetail/query?todoId=" + $scope.todoItem.id;
            $scope.initiatorUsername = $scope.todoDetail.initiatorUsername;
            $scope.refreshAssigneeUsersInfo();
            //
            $scope.nowServerGroup.selected = $scope.todoDetail.tomcatVersionMap.serverGroupDO;

            for (var i = 0; i < $scope.tomcats.length; i++) {
                var item = $scope.tomcats[i];
                if (item.name == $scope.todoDetail.tomcatVersionMap.tomcatNewVersion) {
                    $scope.tomcatNewVersion.selected = item.code;
                    break;
                }
            }

            for (var i = 0; i < $scope.jdks.length; i++) {
                var item = $scope.jdks[i];
                if (item.name == $scope.todoDetail.tomcatVersionMap.jdkNewVersion) {
                    $scope.jdkNewVersion.selected = item.code;
                    break;
                }
            }

            //$scope.tomcatNewVersion.selected = $scope.todoDetail.tomcatVersionMap.tomcatNewVersion;
            //$scope.jdkNewVersion.selected = $scope.todoDetail.tomcatVersionMap.jdkNewVersion;
        }

    }

    // 初始化数据
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


    /////////////////////////////////////////////////
    // 保存工单
    $scope.saveTodo = function () {

        var url = "/todo/todoDetail/save"
        if ($scope.nowServerGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定服务器组";
            return;
        }

        if ($scope.tomcatNewVersion.selected == null || $scope.tomcatNewVersion.selected < 0) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定Tomcat版本";
            return;
        } else {
            for (var i = 0; i < $scope.tomcats.length; i++) {
                var item = $scope.tomcats[i];
                if (item.code == $scope.tomcatNewVersion.selected) {
                    $scope.todoDetail.tomcatVersionMap.tomcatNewVersion = item.name;
                    break;
                }
            }
        }

        if ($scope.jdkNewVersion.selected == null || $scope.jdkNewVersion.selected < 0 ) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定JDK版本";
            return;
        } else {
            for (var i = 0; i < $scope.jdks.length; i++) {
                var item = $scope.jdks[i];
                if (item.code == $scope.jdkNewVersion.selected) {
                    $scope.todoDetail.tomcatVersionMap.jdkNewVersion = item.name;
                    break;
                }
            }
        }

        $scope.todoDetail.tomcatVersionMap.serverGroupId = $scope.nowServerGroup.selected.id;
        $scope.todoDetail.tomcatVersionMap.groupName = $scope.nowServerGroup.selected.name;

        var requestBody = $scope.todoDetail;
        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";
                $scope.todoDetail = data.body;
                $scope.submitTodo();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
                return;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
            return;
        });

    }


    // 提交工单
    $scope.submitTodo = function () {
        var url = "/todo/todoDetail/submit?id=" + $scope.todoDetail.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "提交成功!";
                //$scope.doQuery();
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

    $scope.resetTodo = function () {
        $scope.todoDetail.scmMap.ciUserGroupId = 0;
        $scope.todoDetail.scmMap.groupName = "";
        $scope.todoDetail.scmMap.scmDesc = "";
        $scope.todoDetail.scmMap.scmProject = "";
        $scope.nowStashProject = {};
        $scope.nowStashRepository = {};
    }

    // 执行工单
    // $scope.invokeScmTode = function (type) {
    //     var url = "/todo/todoNewProject/invoke?todoDetailId=" + $scope.todoDetail.id +
    //         "&type=" + type;
    //     httpService.doGet(url).then(function (data) {
    //         if (data.success) {
    //             toaster.pop("success", "执行成功!");
    //             if (type == 0) {
    //                 $scope.todoDetail.newProjectMap.procServerGroup = true;
    //             }
    //         } else {
    //             toaster.pop("warning", "执行失败!");
    //         }
    //     }, function (err) {
    //         $scope.alert.type = 'warning';
    //         $scope.alert.msg = err;
    //     });
    // }

});