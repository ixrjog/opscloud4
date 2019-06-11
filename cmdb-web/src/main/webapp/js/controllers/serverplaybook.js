'use strict';

app.controller('ansibleVersionCtrl', function ($scope, $state, $uibModal, $parse, $sce, $interval, $localStorage, httpService, toaster) {
    $scope.version = {};


    $scope.ansibleVersion = function () {
        if ($scope.version == null || $scope.version.version == null) {
            var url = "/task/ansible/version";
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.version = data.body;
                    $scope.viewVersion();
                }
            });
        } else {
            $scope.viewVersion();
        }
    }

    $scope.viewVersion = function () {

        var modalInstance = $uibModal.open({
            templateUrl: 'versionInfoModal',
            controller: 'versionInstanceCtrl',
            size: 'lg',
            resolve: {
                versionItem: function () {
                    return $scope.version;
                }
            }
        });
    }

});

app.controller('serverplaybookCtrl', function ($scope, $sce, $uibModal, $interval, $timeout, toaster, httpService) {

    $scope.my_data = [];
    $scope.queryName = "";
    $scope.hostPatternSelected = {};
    $scope.playbookGroupList = [];

    $scope.taskPlaybook = {};
    // 按钮状态
    $scope.btnDoScript = false;

    $scope.extraVars = "";
    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];
    $scope.task = {};

    $scope.taskScriptList = [];
    $scope.nowTaskScript = {};

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

    ///////////////////////

    // 3秒刷新1次待办工单
    var timer1 = $interval(function () {

        if ($scope.taskPlaybook == null || $scope.taskPlaybook.id == null) return;

        if ($scope.taskPlaybook.complete == false || $scope.taskPlaybook.taskHostList == null) {
            $scope.getPlaybookTask();
        }
    }, 3000);

    $scope.getPlaybookTask = function () {
        var url = "/task/playbook/get?id=" + $scope.taskPlaybook.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.taskPlaybook = data.body;
                $scope.btnDoScript = false;
            }
        });
    }

    /**
     * 关闭任务详情
     * @param serverId
     */
    $scope.closeTaskHost = function (id) {

        if ($scope.taskPlaybook.taskHostList == null || $scope.taskPlaybook.taskHostList.length == 0) return;
        for (var i = 0; i < $scope.taskPlaybook.taskHostList.length; i++) {
            if (id == $scope.taskPlaybook.taskHostList[i].id) {
                $scope.taskPlaybook.taskHostList[i].closed = true;
                break;
            }
        }
    }

    $scope.doPlaybook = function () {

        if ($scope.nowTaskScript.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定Playbook!";
            return;
        } else {
            $scope.alert.type = '';
        }

        if ($scope.playbookGroupList.length == 0) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "执行列表为红!";
            return;
        } else {
            $scope.alert.type = '';
        }

        $scope.btnDoScript = true;

        var url = "/task/playbook/do";

        var doPlaybook = {
            taskScriptId: $scope.nowTaskScript.selected.id,
            extraVars: $scope.extraVars,
            playbookGroupList: $scope.playbookGroupList
        }
        httpService.doPostWithJSON(url, doPlaybook).then(function (data) {
            if (data.success) {
                $scope.taskPlaybook = data.body;
            }
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
        });
    }

    $scope.queryTaskScript = function (queryParam) {
        var url = "/task/script/queryPlaybook?playbookName=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.taskScriptList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.viewScript = function () {
        if ($scope.nowTaskScript.selected == null) return;

        var modalInstance = $uibModal.open({
            templateUrl: 'scriptInfoModal',
            controller: 'scriptInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                sysScriptType: function () {
                    return null;
                },
                scriptType: function () {
                    return null;
                },
                isView: function () {
                    return true;
                },
                scriptItem: function () {
                    return $scope.nowTaskScript.selected;
                }
            }
        });
    }

    ///////////////////////

    $scope.queryGroupTree = function () {
        var url = "/servergroup/tree/query?name=" + $scope.queryName;
        $scope.my_data = [];
        $scope.doing_async = true;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.my_data = data.body;
                $scope.doing_async = false;
            }
        });
    }

    $scope.my_tree_handler = function (branch) {
        //var _ref;
        if (branch.label.indexOf("group_", 0) != -1)
            return;
        var type = (branch.children != null && branch.children.length > 0 ? 0 : 1  );
        $scope.hostPatternSelected = {
            hostPattern: branch.label,
            type: type
        }

        if (type == 0) {
            var info = '<b style="color: #286090">服务器列表</b>';
            info += '<hr style="margin-bottom: 2px; margin-top: 2px" />';
            for (var i = 0; i < branch.children.length; i++) {
                var server = branch.children[i].label;
                info += server + "<br/>";
            }
            $scope.hostPatternSelected.info = $sce.trustAsHtml(info);
        }
    };

    $scope.addGroup = function () {
        for (var i = 0; i < $scope.playbookGroupList.length; i++) {
            if ($scope.playbookGroupList[i].hostPattern == $scope.hostPatternSelected.hostPattern) {
                toaster.pop("warning", "重复添加！");
                return;
            }
        }

        $scope.playbookGroupList.push($scope.hostPatternSelected);
    }

    // 渲染分组中服务器列表信息
    $scope.invokeServerInfo = function () {
        if ($scope.hostPatternSelected.children != null && $scope.hostPatternSelected.children.length == 0) return;
        var info = '<b style="color: #286090">服务器列表</b>';
        info += '<hr style="margin-bottom: 2px; margin-top: 2px" />';
        for (var i = 0; i < $scope.hostPatternSelected.children.length; i++) {
            var server = $scope.hostPatternSelected.children[i];
            info += '<b style="color: #777"><' + server + "></b><br/>";
        }
        $scope.hostPatternSelected.info = $sce.trustAsHtml(info);
    }


    $scope.delPg = function (pg) {
        for (var i = 0; i < $scope.playbookGroupList.length; i++) {
            if ($scope.playbookGroupList[i].hostPattern == pg.hostPattern) {
                $scope.playbookGroupList.splice(i,1);
                return;
            }
        }

    }


});

app.controller('taskPlaybookCtrl', function ($scope, $state, $uibModal, $parse, $sce, $interval, $localStorage, httpService, toaster) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.taskPlaybook = {};

    // 按钮状态
    $scope.btnDoScript = false;


    $scope.extraVars = "";
    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];
    $scope.task = {};

    $scope.taskScriptList = [];
    $scope.nowTaskScript = {};

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

    // 60秒刷新1次待办工单
    var timer1 = $interval(function () {

        if ($scope.taskPlaybook == null || $scope.taskPlaybook.id == null) return;

        if ($scope.taskPlaybook.complete == false || $scope.taskPlaybook.taskHostList == null) {
            $scope.getPlaybookTask();
        }
    }, 3000);

    $scope.getPlaybookTask = function () {
        var url = "/task/playbook/get?id=" + $scope.taskPlaybook.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.taskPlaybook = data.body;
                $scope.btnDoScript = false;
            }
        });
    }

    /**
     * 关闭任务详情
     * @param serverId
     */
    $scope.closeTaskHost = function (id) {

        if ($scope.taskPlaybook.taskHostList == null || $scope.taskPlaybook.taskHostList.length == 0) return;
        for (var i = 0; i < $scope.taskPlaybook.taskHostList.length; i++) {
            if (id == $scope.taskPlaybook.taskHostList[i].id) {
                $scope.taskPlaybook.taskHostList[i].closed = true;
                break;
            }
        }
    }

    ////////////////////////////////////////////////////////////////////


    $scope.playbookServerGroupList = [];

    /**
     * 添加服务器组
     */
    $scope.addServerGroup = function () {
        if ($scope.nowServerGroup.selected == null) return;
        for (var i = 0; i < $scope.playbookServerGroupList.length; i++) {
            var item = $scope.playbookServerGroupList[i];
            if (item.name == $scope.nowServerGroup.selected.name) {
                toaster.pop("warning", "重复添加服务器组");
                return;
            }
        }

        var serverGroup = $scope.nowServerGroup.selected;
        var url = "/servergroup/hostPattern/get?serverGroupId=" + serverGroup.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                serverGroup.choose = true;
                serverGroup.hostPattern = data.body;
                $scope.playbookServerGroupList.push(serverGroup);
                toaster.pop("success", "添加成功！");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });


    }

    /////////////////////////////////////////////////


    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    /////////////////////////////////////////////////////////////////////

    $scope.doPlaybook = function () {

        if ($scope.nowTaskScript.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定Playbook!";
            return;
        } else {
            $scope.alert.type = '';
        }

        // 选中的服务器组
        var playbookServerGroupList = [];
        for (var i = 0; i < $scope.playbookServerGroupList.length; i++) {
            var item = $scope.playbookServerGroupList[i];
            if (item.choose == true && item.hostPatternSelected != null) {
                var playbookHostPattern = {
                    choose: item.choose,
                    hostPatternSelected: item.hostPatternSelected.hostPattern
                }
                playbookServerGroupList.push(playbookHostPattern);
            }
        }

        $scope.btnDoScript = true;

        var url = "/task/cmd/doPlaybook";

        var doPlaybook = {
            taskScriptId: $scope.nowTaskScript.selected.id,
            extraVars: $scope.extraVars,
            playbookServerGroupList: playbookServerGroupList
        }
        httpService.doPostWithJSON(url, doPlaybook).then(function (data) {
            if (data.success) {
                $scope.taskPlaybook = data.body;
            }
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
        });
    }


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

    /////////////////////////////////////////////////

    $scope.queryTaskScript = function (queryParam) {
        var url = "/task/script/queryPlaybook?playbookName=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.taskScriptList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.viewScript = function () {
        if ($scope.nowTaskScript.selected == null) return;

        var modalInstance = $uibModal.open({
            templateUrl: 'scriptInfoModal',
            controller: 'scriptInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                sysScriptType: function () {
                    return null;
                },
                scriptType: function () {
                    return null;
                },
                isView: function () {
                    return true;
                },
                scriptItem: function () {
                    return $scope.nowTaskScript.selected;
                }
            }
        });
    }


});

// 任务查询
app.controller('serverTaskCtrl', function ($scope, $state, $uibModal, $parse, $sce, $interval, $localStorage, httpService, toaster) {
    $scope.authPoint = $state.current.data.authPoint;
    $scope.queryCmd = "";
    $scope.taskVO = {};


    $scope.resultChoose = {
        all: true,
        success: false,
        failed: false
    };

    // $scope.task = {};

    // $scope.taskScriptList = [];
    // $scope.nowTaskScript = {};

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


    $scope.queryTask = function (item) {
        var url = "/task/cmd/query?taskId=" + item.ansibleTaskDO.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.taskVO = body.body;
            }
        });
    }


    $scope.viewScript = function (item) {

        var modalInstance = $uibModal.open({
            templateUrl: 'scriptInfoModal',
            controller: 'scriptInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                sysScriptType: function () {
                    return null;
                },
                scriptType: function () {
                    return null;
                },
                isView: function () {
                    return true;
                },
                scriptItem: function () {
                    return item.taskScriptDO;
                }
            }
        });
    }

    /**
     * 关闭任务详情
     * @param serverId
     */
    $scope.closeTaskServer = function (serverId) {
        // $scope.taskVO.taskServerList.remove(0);
        if ($scope.taskVO.taskServerList == null || $scope.taskVO.taskServerList.length == 0) return;
        for (var i = 0; i < $scope.taskVO.taskServerList.length; i++) {
            var id = $scope.taskVO.taskServerList[i].serverId;
            if (serverId === id) {
                $scope.taskVO.taskServerList.splice(i, 1);
                return;
            }
        }
    }

    ////////////////////////////////////////////////////////////////////
    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 5;

    $scope.doQuery = function () {
        var url = "/task/ansibleTask/page?"
            + "cmd=" + $scope.queryCmd
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
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

    $scope.doQuery();

    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////


    $scope.checkResultShow = function (taskServer) {
        if ($scope.resultChoose.all == true) return true;

        var result = taskServer.result == 'SUCCESS';

        if (result) {
            if ($scope.resultChoose.success) {
                return true;
            } else {
                return false;
            }
        } else {
            if ($scope.resultChoose.failed) {
                return true;
            } else {
                return false;
            }
        }
    }

});

app.controller('scriptInstanceCtrl', function ($scope, $uibModalInstance, $uibModal, $sce, staticModel, toaster, httpService, sysScriptType, scriptType, isView, scriptItem) {
    //$scope.localAuthPoint = authPoint;
    $scope.sysScriptType = sysScriptType;
    $scope.scriptType = scriptType;
    $scope.isView = isView;
    $scope.scriptItem = scriptItem;

    $scope.modes = ['sh', 'yaml', 'python', 'php', 'lua', 'scheme'];
    $scope.aceOption = {};

    var init = function () {
        // 设置文本模式
        if ($scope.scriptItem.modeType === null || $scope.scriptItem.modeType === '') {
            $scope.scriptItem.modeType = $scope.modes[0];
        }

        $scope.aceOption = {
            useWrapMode: true,
            mode: $scope.scriptItem.modeType
        };
    }

    init();


    $scope.modeChanged = function () {
        $scope.aceOption.mode = $scope.scriptItem.modeType;
    };

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

    $scope.saveScript = function () {

        var url = "/task/script/save"
        if ($scope.scriptItem.scriptName == "" || $scope.scriptItem.scriptName == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定Script名称";
            return;
        }

        var requestBody = $scope.scriptItem;
        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";
                $scope.scriptItem = data.body;
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

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }


});

app.controller('versionInstanceCtrl', function ($scope, $uibModalInstance, $uibModal, toaster, versionItem) {
    //$scope.localAuthPoint = authPoint;
    $scope.versionItem = versionItem;

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }

});