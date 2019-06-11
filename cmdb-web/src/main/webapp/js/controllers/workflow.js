'use strict';


/**
 * 工作流主界面
 */
app.controller('workflowMainCtrl', function ($scope, $rootScope, $uibModal, $state, $sce, $interval, $timeout, toaster, staticModel, httpService) {
        $scope.authPoint = $state.current.data.authPoint;
        $scope.workflowGroupList = [];

        $scope.queryPhaseType = staticModel.queryPhaseType;

        //  创建
        $scope.btnCreateTodo = false;
        //  按钮点击
        $scope.btnTodoClick = false;
        $scope.workflowOpen = true;

        $scope.queryTopics = "";

        $scope.queryUsername = {};
        $scope.nowPhase = {};
        $scope.pageData = [];
        $scope.totalItems = 0;

        $scope.pageLength = 10;

        $scope.workflowList = staticModel.workflowList;

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
         * 创建工作流
         */
        $scope.createTodo = function (workflow) {
            $scope.btnCreateTodo = true;
            var url = "/workflow/todo/create?wfKey=" + workflow.wfKey;

            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    var todo = data.body;
                    var wf = $scope.getByKey($scope.workflowList, todo.workflowDO.wfKey);
                    // 触发事件
                    $rootScope.$emit("callWorkflowTodo", wf.templateUrl, wf.controller, "lg", todo, 0);
                }
                $scope.btnCreateTodo = false;
            })
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

        $scope.reSet = function () {
            $scope.queryUsername = "";
            $scope.nowPhase = {};
        }

        $scope.pageChanged = function (currentTodoPage) {
            $scope.doQuery(currentTodoPage);
        };

        $scope.doQuery = function (currentTodoPage) {
            var url = "/workflow/todo/page?"
                + "queryName=" + ($scope.queryUsername.selected == null ? "" : $scope.queryUsername.selected)
                + "&queryPhase=" + ($scope.nowPhase.selected == null ? -1 : $scope.nowPhase.selected.code)
                + "&page=" + (currentTodoPage == null ? 0 : currentTodoPage - 1)
                + "&length=" + $scope.pageLength;

            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    var body = data.body;
                    $scope.totalItems = body.size;
                    $scope.pageData = body.data;
                } else {
                    toaster.pop("warning", data.msg);
                }
            }, function (err) {
                toaster.pop("error", err);
            });
        }
    }
);

/**
 * 我的工作流
 */
app.controller('myTodoCtrl', function ($scope, $rootScope, $uibModal, $state, $sce, $interval, $timeout, toaster, staticModel, httpService) {
        $scope.authPoint = $state.current.data.authPoint;
        $scope.workflowGroupList = [];

        $scope.queryPhaseType = staticModel.queryPhaseType;

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
        $scope.myCompleteTodoList = [];

        $scope.workflowList = staticModel.workflowList;


        // 校验当前用户是否具有审批&&审核状态（服务端会对工作流进行权限校验，篡改数据无效）
        var initTodo = function () {
            if ($scope.myTodoList.length != 0) {
                var username = $scope.app.settings.user.username;
                for (var i = 0; i < $scope.myTodoList.length; i++) {
                    var todo = $scope.myTodoList[i];
                    todo.isApproval = false;
                    todo.isAudit = false;
                    switch (todo.todoPhase) {
                        case 1: // CMO
                            if (todo.todoUserList.cmo != null && todo.todoUserList.cmo.username == username)
                                todo.isApproval = true;
                            break;
                        case 2: // TL
                            if (todo.todoUserList.teamleader != null && todo.todoUserList.teamleader.username == username)
                                todo.isApproval = true;
                            break;
                        case 3: // DL
                            if (todo.todoUserList.deptLeader != null && todo.todoUserList.deptLeader.username == username)
                                todo.isApproval = true;
                            break;
                        case 4: // OPS
                            if (todo.todoUserList.ops != null && todo.todoUserList.ops.username == username)
                                todo.isAudit = true;
                            break;
                        default:
                    }
                }
            }
        }

        $rootScope.$on("callQueryMyTodo", function () {
            $scope.queryMyTodo();
        });

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


        $rootScope.$on("callWorkflowTodo", function (event, templateUrl, controller, size, todo, type) {
            workflowTodo(templateUrl, controller, size, todo, type);
        });

        var workflowTodo = function (templateUrl, controller, size, todo, type) {
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
                    todo: function () {
                        return todo;
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

        $scope.editTodo = function (todo) {
            var wf = $scope.getByKey($scope.workflowList, todo.workflowDO.wfKey);
            workflowTodo(wf.templateUrl, wf.controller, "lg", todo, 0);
        }

        $scope.viewTodo = function (todo) {
            var wf = $scope.getByKey($scope.workflowList, todo.workflowDO.wfKey);
            workflowTodo(wf.templateUrl, wf.controller, "lg", todo, 1);
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

        // 审批审核工单（批准）
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

        // 审批审核工单（不批准）
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
 * 工作流统计
 */
app.controller('workflowStatusCtrl', function ($scope, $rootScope, $uibModal, $state, $sce, $interval, $timeout, toaster, staticModel, httpService) {
        $scope.workflowStatus = {};

        var initChart = function () {
            $scope.wfStatus = Highcharts.chart('wfStatus', {
                title: {
                    text: '工作流分类汇总(累计审批)'
                },
                subtitle: {
                    text: '柱形图'
                },
                xAxis: {
                    categories: $scope.workflowStatus.wfTodoCat
                },
                series: [{
                    type: 'column',
                    colorByPoint: true,
                    data: $scope.workflowStatus.wfTodoData,
                    showInLegend: false
                }]
            });

            $scope.wfMonthStatus = Highcharts.chart('wfMonthStatus', {
                title: {
                    text: '工作流按月汇总(累计审批)'
                },
                subtitle: {
                    text: '柱形图'
                },
                xAxis: {
                    categories: $scope.workflowStatus.wfTodoMonthCat
                },
                series: [{
                    type: 'column',
                    colorByPoint: true,
                    data: $scope.workflowStatus.wfTodoMonthData,
                    showInLegend: false
                }]
            });
        }

        $scope.getWorkflowStatus = function () {
            var url = "/workflow//status";
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.workflowStatus = data.body;
                    initChart();
                }
            });
        }

        $scope.getWorkflowStatus();


        // 给 wrapper 添加点击事件
        Highcharts.addEvent(document.getElementById('wrapper-wfStatus'), 'click', function (e) {
            var target = e.target,
                button = null;
            if (target.tagName === 'BUTTON') { // 判断点的是否是 button
                button = target.id;
                switch (button) {
                    case 'wfStatusPlain':
                        $scope.wfStatus.update({
                            chart: {
                                inverted: false,
                                polar: false
                            },
                            subtitle: {
                                text: '柱形图'
                            }
                        });
                        break;
                    case 'wfStatusInverted':
                        $scope.wfStatus.update({
                            chart: {
                                inverted: true,
                                polar: false
                            },
                            subtitle: {
                                text: '条形图'
                            }
                        });
                        break;
                    case 'wfStatusPolar':
                        $scope.wfStatus.update({
                            chart: {
                                inverted: false,
                                polar: true
                            },
                            subtitle: {
                                text: '极地图'
                            }
                        });
                        break;
                }
            }
        });

        Highcharts.addEvent(document.getElementById('wrapper-wfMonthStatus'), 'click', function (e) {
            var target = e.target,
                button = null;
            if (target.tagName === 'BUTTON') { // 判断点的是否是 button
                button = target.id;
                switch (button) {
                    case 'wfMonthStatusPlain':
                        $scope.wfMonthStatus.update({
                            chart: {
                                inverted: false,
                                polar: false
                            },
                            subtitle: {
                                text: '柱形图'
                            }
                        });
                        break;
                    case 'wfMonthStatusInverted':
                        $scope.wfMonthStatus.update({
                            chart: {
                                inverted: true,
                                polar: false
                            },
                            subtitle: {
                                text: '条形图'
                            }
                        });
                        break;
                    case 'wfMonthStatusPolar':
                        $scope.wfMonthStatus.update({
                            chart: {
                                inverted: false,
                                polar: true
                            },
                            subtitle: {
                                text: '极地图'
                            }
                        });
                        break;
                }
            }
        });


    }
);

/**
 * 管理界面
 */
app.controller('workflowAdminCtrl', function ($scope, $rootScope, $uibModal, $state, $sce, $interval, $timeout, toaster, staticModel, httpService) {
        $scope.authPoint = $state.current.data.authPoint;
        $scope.workflowGroupList = [];

        $scope.queryPhaseType = staticModel.queryPhaseType;


        $scope.queryUsername = {};
        $scope.nowPhase = {};
        $scope.pageData = [];
        $scope.totalItems = 0;

        $scope.pageLength = 10;
        $scope.workflowList = staticModel.workflowList;


        $scope.viewTodo = function (todo) {
            var wf = $scope.getByKey($scope.workflowList, todo.workflowDO.wfKey);
            // 触发事件
            $rootScope.$emit("callWorkflowTodo", wf.templateUrl, wf.controller, "lg", todo, 1);
        }

        $scope.reSet = function () {
            $scope.queryUsername = "";
            $scope.nowPhase = {
                selected: 1
            };
        }

        $scope.reSet();

        $scope.pageChanged = function (currentTodoPage) {
            $scope.doQuery(currentTodoPage);
        };

        $scope.doQuery = function (currentTodoPage) {
            var url = "/workflow/todo/page?"
                + "queryName=" + ($scope.queryUsername.selected == null ? "" : $scope.queryUsername.selected)
                + "&queryPhase=" + ($scope.nowPhase.selected == null ? -1 : $scope.nowPhase.selected)
                + "&page=" + (currentTodoPage == null ? 0 : currentTodoPage - 1)
                + "&length=" + $scope.pageLength;

            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    var body = data.body;
                    $scope.totalItems = body.size;
                    $scope.pageData = body.data;
                } else {
                    toaster.pop("warning", data.msg);
                }
            }, function (err) {
                toaster.pop("error", err);
            });
        }

    }
);

/**
 * Keybox（申请页面）
 */
app.controller('todoKeyboxInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, user, todo, type) {
    //$scope.workflow = workflow;
    // type 0 编辑 / 1 查看/审批
    $scope.type = type;

    $scope.btnSaveing = false;

    // TODO 可以取到当前用户信息
    $scope.user = user;

    $scope.workflowTodo = todo;
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

    $scope.md = {};
    $scope.mdShow = true;
    var initMD = function () {

        marked.setOptions({
            gfm: true,
            tables: true,
            breaks: false,
            pedantic: false,
            sanitize: false,
            smartLists: true,
            smartypants: false,
            highlight: function (code) {
                return hljs.highlightAuto(code).value;
            }
        });
        var mdKey = "KEYBOX";
        var url = "/readmeMD/get?mdKey=" + mdKey;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.md = data.body;
                $scope.md.preview = $sce.trustAsHtml(marked($scope.md.preview));
            }
        }, function (err) {
            return {};
        });
    }
    initMD();

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
 * AliyunRAM (申请页面）
 */
app.controller('todoAliyunRamInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, user, todo, type) {
    //$scope.workflow = workflow;
    // type 0 编辑 / 1 查看/审批
    $scope.type = type;

    $scope.btnSaveing = false;
    // TODO 可以取到当前用户信息
    $scope.user = user;

    $scope.workflowTodo = todo;
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
 * LdapGroup (外部平台权限 申请页面）
 */
app.controller('todoLdapGroupInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, user, todo, type) {
    //$scope.workflow = workflow;
    // type 0 编辑 / 1 查看/审批
    $scope.type = type;

    $scope.btnSaveing = false;

    // TODO 可以取到当前用户信息
    $scope.user = user;

    $scope.workflowTodo = todo;
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

    $scope.md = {};
    $scope.mdShow = true;
    var initMD = function () {

        marked.setOptions({
            gfm: true,
            tables: true,
            breaks: false,
            pedantic: false,
            sanitize: false,
            smartLists: true,
            smartypants: false,
            highlight: function (code) {
                return hljs.highlightAuto(code).value;
            }
        });
        var mdKey = "LDAP_GROUP";
        var url = "/readmeMD/get?mdKey=" + mdKey;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.md = data.body;
                $scope.md.preview = $sce.trustAsHtml(marked($scope.md.preview));
            }
        }, function (err) {
            return {};
        });
    }
    initMD();


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
 * AliyunRAMPolicy (申请页面）
 */
app.controller('todoAliyunRamPolicyInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, user, todo, type) {
    //$scope.workflow = workflow;
    // type 0 编辑 / 1 查看/审批
    $scope.type = type;

    $scope.btnSaveing = false;
    // TODO 可以取到当前用户信息
    $scope.user = user;
    $scope.ramUser = {};

    $scope.workflowTodo = todo;
    $scope.nowDeptLeader = {};
    $scope.nowOps = {};

    $scope.md = {};
    $scope.mdShow = true;
    var initMD = function () {

        marked.setOptions({
            gfm: true,
            tables: true,
            breaks: false,
            pedantic: false,
            sanitize: false,
            smartLists: true,
            smartypants: false,
            highlight: function (code) {
                return hljs.highlightAuto(code).value;
            }
        });
        var mdKey = "ALIYUN_RAM_POLICY";
        var url = "/readmeMD/get?mdKey=" + mdKey;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.md = data.body;
                $scope.md.preview = $sce.trustAsHtml(marked($scope.md.preview));
            }
        }, function (err) {
            return {};
        });
    }
    initMD();


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

        $scope.contains()
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
        if ($scope.ramUser == null || $scope.ramUser.ramUserName == '') return;
        var url = "/aliyun/ram/user/save";
        httpService.doPostWithJSON(url, $scope.ramUser).then(function (data) {
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

/**
 * Role (平台权限 申请页面）
 */
app.controller('todoRoleInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, user, todo, type) {
    //$scope.workflow = workflow;
    // type 0 编辑 / 1 查看/审批
    $scope.type = type;

    $scope.btnSaveing = false;

    // TODO 可以取到当前用户信息
    $scope.user = user;

    $scope.workflowTodo = todo;
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

    $scope.md = {};
    $scope.mdShow = true;
    var initMD = function () {

        marked.setOptions({
            gfm: true,
            tables: true,
            breaks: false,
            pedantic: false,
            sanitize: false,
            smartLists: true,
            smartypants: false,
            highlight: function (code) {
                return hljs.highlightAuto(code).value;
            }
        });
        var mdKey = "ROLE";
        var url = "/readmeMD/get?mdKey=" + mdKey;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.md = data.body;
                $scope.md.preview = $sce.trustAsHtml(marked($scope.md.preview));
            }
        }, function (err) {
            return {};
        });
    }
    initMD();

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
 * Gitlab Group （Gitlab 群组权限申请）
 */
app.controller('todoGitlabGroupInstanceCtrl', function ($scope, staticModel, $uibModalInstance, $sce, toaster, httpService, user, todo, type) {
    //$scope.workflow = workflow;
    // type 0 编辑 / 1 查看/审批
    $scope.type = type;

    $scope.accessLevelType = staticModel.gitlabAccessLevel;

    // 默认 "Developer"
    $scope.nowAccessLevel = {};

    /**
     *     Guest(10),
     Reporter(20),
     Developer(30),
     Master(40),
     Owner(50);
     * @type {boolean}
     */

    $scope.btnSaveing = false;

    // TODO 可以取到当前用户信息
    $scope.user = user;

    $scope.workflowTodo = todo;
    $scope.nowDeptLeader = {};
    $scope.nowOps = {};

    $scope.initiatorUsername = "";
    $scope.assigneeUsersInfo = "";
    $scope.nowCMO = {};
    $scope.gitlabGroupList = [];
    $scope.nowGitlabGroup = {};

    /**
     * 查询群组列表
     * @param queryParam
     */
    var getGitlabGroup = function (queryParam, name) {
        var url = "/gitlab/group/query?groupName=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.gitlabGroupList = data.body;
                if (name != "") {
                    for (var i = 0; i < $scope.gitlabGroupList.length; i++) {
                        var item = $scope.gitlabGroupList[i];
                        if (name == item.name) {
                            $scope.nowGitlabGroup.selected = item;
                            break;
                        }
                    }
                }
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryGitlabGroup = function (queryParam) {
        getGitlabGroup(queryParam, "");
    }

    // 初始化审批选中状态
    var initApproval = function () {
        // 设置cmo选中
        if ($scope.workflowTodo.todoUserList.cmo != null) {
            $scope.workflowTodo.cmoUserList.push($scope.workflowTodo.todoUserList.cmo);
            $scope.nowCMO.selected = $scope.workflowTodo.cmoUserList[0];
        }
    }

    var initTodoDetail = function () {
        if ($scope.workflowTodo.todoDetails != null && $scope.workflowTodo.todoDetails.length >= 1) {
            // 设置GitlabGroup
            if ($scope.workflowTodo.todoDetails[0].name != '') {
                var name = $scope.workflowTodo.todoDetails[0].name;
                getGitlabGroup(name, name);
            }
        }


        // 设置角色,默认为开发人员
        var accessLevelCode = "Developer";
        if ($scope.workflowTodo.todoDetails != null && $scope.workflowTodo.todoDetails.length >= 1) {
            accessLevelCode = $scope.workflowTodo.todoDetails[0].detail.accessLevel;
        }
        for (var i = 0; i < $scope.accessLevelType.length; i++) {
            var item = $scope.accessLevelType[i];
            if (item.code == accessLevelCode) {
                $scope.nowAccessLevel.selected = item;
                break;
            }
        }
    }

    var init = function () {
        initApproval();
        initTodoDetail();
    }

    init();

    $scope.md = {};
    $scope.mdShow = true;
    var initMD = function () {

        marked.setOptions({
            gfm: true,
            tables: true,
            breaks: false,
            pedantic: false,
            sanitize: false,
            smartLists: true,
            smartypants: false,
            highlight: function (code) {
                return hljs.highlightAuto(code).value;
            }
        });
        var mdKey = "GITLAB_GROUP";
        var url = "/readmeMD/get?mdKey=" + mdKey;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.md = data.body;
                $scope.md.preview = $sce.trustAsHtml(marked($scope.md.preview));
            }
        }, function (err) {
            return {};
        });
    }
    initMD();


    /////////////////////////////////////

    $scope.setCmoUserList = function () {
        if ($scope.nowGitlabGroup.selected == null || $scope.nowGitlabGroup.selected.memberList.length == 0) {
            $scope.workflowTodo.cmoUserList = [];
            return;
        }
        var userList = [];
        // 转换用户
        for (var i = 0; i < $scope.nowGitlabGroup.selected.memberList.length; i++) {
            var item = $scope.nowGitlabGroup.selected.memberList[i];
            var user = {
                username: item.username,
                displayName: item.name
            }
            userList.push(user);
        }
        $scope.workflowTodo.cmoUserList = userList;
        $scope.nowCMO.selected = $scope.workflowTodo.cmoUserList[0];
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

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }

    /////////////////////////////////////////////////
    $scope.saveTodo = function () {
        doSave();
    }

    var doSave = function () {
        if ($scope.workflowTodo.workflowDO.cmoApproval) {
            if ($scope.nowCMO.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "必须指定CMO配置管理员人选";
                return;
            } else {
                $scope.workflowTodo.todoUserList.cmo = $scope.nowCMO.selected;
            }
        }
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

        var todoGitlabGroup = {
            groupId: $scope.nowGitlabGroup.selected.groupId,
            name: $scope.nowGitlabGroup.selected.name,
            accessLevel: $scope.nowAccessLevel.selected.code,
            userId: 0
        }

        var workflowTodoDetailVO = {
            id: 0,
            todoId: $scope.workflowTodo.id,
            detailKey: "TodoGitlabGroup",
            detail: todoGitlabGroup,
            name: ""
        }

        $scope.btnSaveing = true;

        var url = "/workflow/todo/save";

        var requestBody = $scope.workflowTodo;

        requestBody.todoDetails = [];
        requestBody.todoDetails.push(workflowTodoDetailVO);

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
 * Gitlab Project （Gitlab 项目权限申请）
 */
app.controller('todoGitlabProjectInstanceCtrl', function ($scope, staticModel, $uibModalInstance, $sce, toaster, httpService, user, todo, type) {
    //$scope.workflow = workflow;
    // type 0 编辑 / 1 查看/审批
    $scope.type = type;

    $scope.accessLevelType = staticModel.gitlabProjectAccessLevel;

    // 默认 "Developer"
    $scope.nowAccessLevel = {};

    /**
     *
     Guest(10),
     Reporter(20),
     Developer(30),
     Master(40),
     Owner(50);
     * @type {boolean}
     */

    $scope.btnSaveing = false;

    // TODO 可以取到当前用户信息
    $scope.user = user;

    $scope.workflowTodo = todo;
    $scope.nowDeptLeader = {};
    $scope.nowOps = {};

    $scope.initiatorUsername = "";
    $scope.assigneeUsersInfo = "";


    $scope.nowCMO = {};
    $scope.gitlabProjectList = [];
    $scope.nowGitlabProject = {};

    /**
     * 查询群组列表
     * @param queryParam
     */
    var getGitlabProject = function (queryParam, name) {
        var url = "/gitlab/project/query?projectName=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.gitlabProjectList = data.body;
                if (name != "") {
                    for (var i = 0; i < $scope.gitlabProjectList.length; i++) {
                        var item = $scope.gitlabProjectList[i];
                        if (name == item.name) {
                            $scope.nowGitlabProject.selected = item;
                            break;
                        }
                    }
                }
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryGitlabProject = function (queryParam) {
        getGitlabProject(queryParam, "");
    }

    // 初始化审批选中状态
    var initApproval = function () {
        // 设置cmo选中
        if ($scope.workflowTodo.todoUserList.cmo != null) {
            $scope.workflowTodo.cmoUserList.push($scope.workflowTodo.todoUserList.cmo);
            $scope.nowCMO.selected = $scope.workflowTodo.cmoUserList[0];
        }
    }

    var initTodoDetail = function () {
        if ($scope.workflowTodo.todoDetails != null && $scope.workflowTodo.todoDetails.length >= 1) {
            // 设置GitlabGroup
            if ($scope.workflowTodo.todoDetails[0].name != '') {
                var name = $scope.workflowTodo.todoDetails[0].name;
                getGitlabProject(name, name);
            }
        }

        // 设置角色,默认为开发人员
        var accessLevelCode = "Developer";
        if ($scope.workflowTodo.todoDetails != null && $scope.workflowTodo.todoDetails.length >= 1) {
            accessLevelCode = $scope.workflowTodo.todoDetails[0].detail.accessLevel;
        }
        for (var i = 0; i < $scope.accessLevelType.length; i++) {
            var item = $scope.accessLevelType[i];
            if (item.code == accessLevelCode) {
                $scope.nowAccessLevel.selected = item;
                break;
            }
        }
    }

    var init = function () {
        initApproval();
        initTodoDetail();
    }

    init();

    $scope.md = {};
    $scope.mdShow = true;
    var initMD = function () {

        marked.setOptions({
            gfm: true,
            tables: true,
            breaks: false,
            pedantic: false,
            sanitize: false,
            smartLists: true,
            smartypants: false,
            highlight: function (code) {
                return hljs.highlightAuto(code).value;
            }
        });
        var mdKey = "GITLAB_PROJECT";
        var url = "/readmeMD/get?mdKey=" + mdKey;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.md = data.body;
                $scope.md.preview = $sce.trustAsHtml(marked($scope.md.preview));
            }
        }, function (err) {
            return {};
        });
    }
    initMD();

    /////////////////////////////////////

    $scope.setCmoUserList = function () {
        if ($scope.nowGitlabProject.selected == null || $scope.nowGitlabProject.selected.memberList.length == 0) {
            $scope.workflowTodo.cmoUserList = [];
            return;
        }
        var userList = [];
        // 转换用户
        for (var i = 0; i < $scope.nowGitlabProject.selected.memberList.length; i++) {
            var item = $scope.nowGitlabProject.selected.memberList[i];
            var user = {
                username: item.username,
                displayName: item.name
            }
            userList.push(user);
        }
        $scope.workflowTodo.cmoUserList = userList;
        $scope.nowCMO.selected = $scope.workflowTodo.cmoUserList[0];
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

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }

    /////////////////////////////////////////////////
    $scope.saveTodo = function () {
        doSave();
    }

    var doSave = function () {
        if ($scope.workflowTodo.workflowDO.cmoApproval) {
            if ($scope.nowCMO.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "必须指定CMO配置管理员人选";
                return;
            } else {
                $scope.workflowTodo.todoUserList.cmo = $scope.nowCMO.selected;
            }
        }
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

        var todoGitlabProject = {
            projectId: $scope.nowGitlabProject.selected.projectId,
            name: $scope.nowGitlabProject.selected.name,
            accessLevel: $scope.nowAccessLevel.selected.code,
            userId: 0
        }

        var workflowTodoDetailVO = {
            id: 0,
            todoId: $scope.workflowTodo.id,
            detailKey: "TodoGitlabProject",
            detail: todoGitlabProject,
            name: ""
        }

        $scope.btnSaveing = true;

        var url = "/workflow/todo/save";

        var requestBody = $scope.workflowTodo;

        requestBody.todoDetails = [];
        requestBody.todoDetails.push(workflowTodoDetailVO);

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
 * 持续集成应用授权(Android)
 */
app.controller('todoCIAndroidAuthInstanceCtrl', function ($scope, staticModel, $uibModalInstance, $sce, toaster, httpService, user, todo, type) {
    //$scope.workflow = workflow;
    // type 0 编辑 / 1 查看/审批
    $scope.type = type;

    $scope.btnSaveing = false;

    $scope.CIAppType = 3; // Android

    $scope.user = user; // 可以取到当前用户信息

    $scope.ciAppList = [];
    $scope.nowCiApp = {};

    $scope.workflowTodo = todo;
    $scope.nowDeptLeader = {};
    $scope.nowOps = {};

    $scope.initiatorUsername = "";
    $scope.assigneeUsersInfo = "";

    $scope.nowCMO = {};

    /**
     * 查询群组列表
     * @param queryParam
     */
    var getCIApp = function (queryParam) {
        var url = "/ci/app/unauth/queryByType?queryName=" + (queryParam == null ? "" : queryParam)
            + "&appType=" + $scope.CIAppType;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.ciAppList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    var getCIAppByName = function (appName) {
        var url = "/ci/app/queryByName?appName=" + appName;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.nowCiApp.selected  = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryCIApp = function (queryParam) {
        getCIApp(queryParam);
    }

    // 初始化审批选中状态
    var initApproval = function () {
        // 设置cmo选中
        if ($scope.workflowTodo.todoUserList.cmo != null) {
            $scope.workflowTodo.cmoUserList.push($scope.workflowTodo.todoUserList.cmo);
            $scope.nowCMO.selected = $scope.workflowTodo.cmoUserList[0];
        }
    }

    var initTodoDetail = function () {
        if ($scope.workflowTodo.todoDetails != null && $scope.workflowTodo.todoDetails.length >= 1) {
            if ($scope.workflowTodo.todoDetails[0].name != '') {
                var appName = $scope.workflowTodo.todoDetails[0].name;
                getCIAppByName(appName);
            }
        }
    }

    var init = function () {
        initApproval();
        initTodoDetail();
    }

    init();

    $scope.md = {};
    $scope.mdShow = true;
    var initMD = function () {

        marked.setOptions({
            gfm: true,
            tables: true,
            breaks: false,
            pedantic: false,
            sanitize: false,
            smartLists: true,
            smartypants: false,
            highlight: function (code) {
                return hljs.highlightAuto(code).value;
            }
        });
        var mdKey = "CI_ANDROID_AUTH";
        var url = "/readmeMD/get?mdKey=" + mdKey;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.md = data.body;
                $scope.md.preview = $sce.trustAsHtml(marked($scope.md.preview));
            }
        }, function (err) {
            return {};
        });
    }
    initMD();

    /////////////////////////////////////

    $scope.setCmoUserList = function () {
        if ($scope.nowCiApp.selected == null || $scope.nowCiApp.selected.authUserList.length == 0) {
            $scope.workflowTodo.cmoUserList = [];
            return;
        }
        $scope.workflowTodo.cmoUserList = $scope.nowCiApp.selected.authUserList;
        $scope.nowCMO.selected = $scope.workflowTodo.cmoUserList[0];
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

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }

    /////////////////////////////////////////////////
    $scope.saveTodo = function () {
        doSave();
    }

    var doSave = function () {
        if ($scope.workflowTodo.workflowDO.cmoApproval) {
            if ($scope.nowCMO.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "必须指定CMO配置管理员人选";
                return;
            } else {
                $scope.workflowTodo.todoUserList.cmo = $scope.nowCMO.selected;
            }
        }
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


        var todoCiAndroidAuth = {
            appId: $scope.nowCiApp.selected.id,
            appName: $scope.nowCiApp.selected.appName,
            projectName: $scope.nowCiApp.selected.projectName
        }

        var workflowTodoDetailVO = {
            id: 0,
            todoId: $scope.workflowTodo.id,
            detailKey: "TodoDetailCIAndroidAuth",
            detail: todoCiAndroidAuth,
            name: ""
        }

        $scope.btnSaveing = true;

        var url = "/workflow/todo/save";

        var requestBody = $scope.workflowTodo;

        requestBody.todoDetails = [];
        requestBody.todoDetails.push(workflowTodoDetailVO);

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
 * 持续集成应用授权(Test)
 */
app.controller('todoCITestAuthInstanceCtrl', function ($scope, staticModel, $uibModalInstance, $sce, toaster, httpService, user, todo, type) {
    //$scope.workflow = workflow;
    // type 0 编辑 / 1 查看/审批
    $scope.type = type;

    $scope.btnSaveing = false;

    $scope.CIAppType = 4; // Android

    $scope.user = user; // 可以取到当前用户信息

    $scope.ciAppList = [];
    $scope.nowCiApp = {};

    $scope.workflowTodo = todo;
    $scope.nowDeptLeader = {};
    $scope.nowOps = {};

    $scope.initiatorUsername = "";
    $scope.assigneeUsersInfo = "";

    $scope.nowCMO = {};

    /**
     * 查询群组列表
     * @param queryParam
     */
    var getCIApp = function (queryParam) {
        var url = "/ci/app/unauth/queryByType?queryName=" + (queryParam == null ? "" : queryParam)
            + "&appType=" + $scope.CIAppType;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.ciAppList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    var getCIAppByName = function (appName) {
        var url = "/ci/app/queryByName?appName=" + appName;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.nowCiApp.selected  = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryCIApp = function (queryParam) {
        getCIApp(queryParam);
    }

    // 初始化审批选中状态
    var initApproval = function () {
        // 设置cmo选中
        if ($scope.workflowTodo.todoUserList.cmo != null) {
            $scope.workflowTodo.cmoUserList.push($scope.workflowTodo.todoUserList.cmo);
            $scope.nowCMO.selected = $scope.workflowTodo.cmoUserList[0];
        }
    }

    var initTodoDetail = function () {
        if ($scope.workflowTodo.todoDetails != null && $scope.workflowTodo.todoDetails.length >= 1) {
            if ($scope.workflowTodo.todoDetails[0].name != '') {
                var appName = $scope.workflowTodo.todoDetails[0].name;
                getCIAppByName(appName);
            }
        }
    }

    var init = function () {
        initApproval();
        initTodoDetail();
    }

    init();

    $scope.md = {};
    $scope.mdShow = true;
    var initMD = function () {

        marked.setOptions({
            gfm: true,
            tables: true,
            breaks: false,
            pedantic: false,
            sanitize: false,
            smartLists: true,
            smartypants: false,
            highlight: function (code) {
                return hljs.highlightAuto(code).value;
            }
        });
        var mdKey = "CI_TEST_AUTH";
        var url = "/readmeMD/get?mdKey=" + mdKey;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.md = data.body;
                $scope.md.preview = $sce.trustAsHtml(marked($scope.md.preview));
            }
        }, function (err) {
            return {};
        });
    }
    initMD();

    /////////////////////////////////////

    $scope.setCmoUserList = function () {
        if ($scope.nowCiApp.selected == null || $scope.nowCiApp.selected.authUserList.length == 0) {
            $scope.workflowTodo.cmoUserList = [];
            return;
        }
        $scope.workflowTodo.cmoUserList = $scope.nowCiApp.selected.authUserList;
        $scope.nowCMO.selected = $scope.workflowTodo.cmoUserList[0];
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

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }

    /////////////////////////////////////////////////
    $scope.saveTodo = function () {
        doSave();
    }

    var doSave = function () {
        if ($scope.workflowTodo.workflowDO.cmoApproval) {
            if ($scope.nowCMO.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "必须指定CMO配置管理员人选";
                return;
            } else {
                $scope.workflowTodo.todoUserList.cmo = $scope.nowCMO.selected;
            }
        }
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


        var todoCiAndroidAuth = {
            appId: $scope.nowCiApp.selected.id,
            appName: $scope.nowCiApp.selected.appName,
            projectName: $scope.nowCiApp.selected.projectName
        }

        var workflowTodoDetailVO = {
            id: 0,
            todoId: $scope.workflowTodo.id,
            detailKey: "TodoDetailCIAndroidAuth",
            detail: todoCiAndroidAuth,
            name: ""
        }

        $scope.btnSaveing = true;

        var url = "/workflow/todo/save";

        var requestBody = $scope.workflowTodo;

        requestBody.todoDetails = [];
        requestBody.todoDetails.push(workflowTodoDetailVO);

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