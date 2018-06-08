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

app.controller('serverTaskCmdCtrl', function ($scope, $state, $uibModal, $parse, $sce, $interval, $localStorage, httpService, toaster) {
    $scope.authPoint = $state.current.data.authPoint;
    // 执行命令后的返回
    //$scope.taskResult = {};
    // 查询任务的结果详情
    $scope.taskVO = {};

    // 按钮状态
    $scope.btnDoCmd = false;

    $scope.resultChoose = {
        all: true,
        success: false,
        failed: false
    };

    $scope.hostGroup = "";
    $scope.cmd = "";
    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];
    $scope.task = {};

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

        if ($scope.taskVO.ansibleTaskDO == null) return;

        if ($scope.taskVO.ansibleTaskDO.finalized == false) {
            $scope.queryTask();
        }
    }, 3000);

    $scope.queryTask = function () {
        var url = "/task/cmd/query?taskId=" + $scope.taskVO.ansibleTaskDO.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.taskVO = body.body;
                $scope.btnDoCmd = false;
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
    $scope.pageLength = 20;

    $scope.pageChanged = function () {
        $scope.doQueryServers();
    };

    /////////////////////////////////////////////////

    $scope.changeServerGroup = function () {
        $scope.doQueryServers();
    };

    var init = function () {
        $scope.envChoose = {
            prod: false,
            back: false,
            gray: false,
            daily: false
        };

        $scope.resultChoose = {
            all: true,
            success: false,
            failed: false
        };
    }

    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    /////////////////////////////////////////////////////////////////////

    $scope.allChoose = false;
    $scope.chooseAllItem = function () {
        if ($scope.allChoose) {
            for (var i = 0; i < $scope.pageData.length; i++) {
                $scope.pageData[i].choose = true;
            }
        } else {
            for (var i = 0; i < $scope.pageData.length; i++) {
                $scope.pageData[i].choose = false;
            }
        }
    }

    $scope.envChoose = {
        prod: false,
        back: false,
        gray: false,
        daily: false
    };

    $scope.chooseAllResult = function () {
        $scope.resultChoose = {
            all: true,
            success: false,
            failed: false
        };
    }

    $scope.chooseResult = function (code) {
        var success = false;
        var failed = false;
        if (code == 0) {
            success = true;
        }
        if (code == 1) {
            failed = true;
        }
        $scope.resultChoose = {
            all: false,
            success: success,
            failed: failed
        };
    }

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

    $scope.chooseEnv = function (envCode) {

        for (var i = 0; i < $scope.pageData.length; i++) {
            //var item = $scope.pageData[i];
            if ($scope.pageData[i].envType == envCode) {
                switch (envCode) {
                    case 4:
                        $scope.pageData[i].choose = $scope.envChoose.prod;
                        break;
                    case 6:
                        $scope.pageData[i].choose = $scope.envChoose.back;
                        break;
                    case 3:
                        $scope.pageData[i].choose = $scope.envChoose.gray;
                        break;
                    case 2:
                        $scope.pageData[i].choose = $scope.envChoose.daily;
                        break;
                    default:
                        return;
                }
            }

        }
    }

    $scope.doQueryServers = function () {
        var url = "/box/server/query?"
            + "serverGroupId=" + ($scope.nowServerGroup.selected == null ? -1 : $scope.nowServerGroup.selected.id)
            + "&envType=-1"
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
                //$scope.refreshServerInfo();
                init();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQueryServers();


    $scope.doCmd = function () {

        if ($scope.cmd == "") {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定指令!";
            return;
        } else {
            $scope.alert.type = '';
        }

        // 选中的服务器组
        var serverList = [];
        for (var i = 0; i < $scope.pageData.length; i++) {
            var item = $scope.pageData[i];
            if (item.choose == true) {
                serverList.push(item);
            }
        }

        if (serverList.length == 0) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定服务器!";
            return;
        } else {
            $scope.alert.type = '';
        }

        $scope.btnDoCmd = true;

        var url = "/task/cmd/doCmd";

        var requestBody = {
            hostGroup: $scope.hostGroup,
            cmd: $scope.cmd,
            serverList: serverList
        }
        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.taskVO.ansibleTaskDO = body.body;
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


});

app.controller('serverTaskScriptCtrl', function ($scope, $state, $uibModal, $parse, $sce, $interval, $localStorage, httpService, toaster) {
    $scope.authPoint = $state.current.data.authPoint;
    // 执行命令后的返回
    //$scope.taskResult = {};
    // 查询任务的结果详情
    $scope.taskVO = {};

    // 按钮状态
    $scope.btnDoScript = false;

    $scope.resultChoose = {
        all: true,
        success: false,
        failed: false
    };

    $scope.hostGroup = "";
    $scope.params = "";
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

        if ($scope.taskVO.ansibleTaskDO == null) return;

        if ($scope.taskVO.ansibleTaskDO.finalized == false) {
            $scope.queryTask();
        }
    }, 3000);

    $scope.queryTask = function () {
        var url = "/task/cmd/query?taskId=" + $scope.taskVO.ansibleTaskDO.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.taskVO = body.body;
                $scope.btnDoScript = false;
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
    $scope.pageLength = 20;

    $scope.pageChanged = function () {
        $scope.doQueryServers();
    };

    /////////////////////////////////////////////////

    $scope.changeServerGroup = function () {
        $scope.doQueryServers();
    };

    var init = function () {
        $scope.envChoose = {
            prod: false,
            back: false,
            gray: false,
            daily: false
        };

        $scope.resultChoose = {
            all: true,
            success: false,
            failed: false
        };
    }

    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    /////////////////////////////////////////////////////////////////////

    $scope.allChoose = false;
    $scope.chooseAllItem = function () {
        if ($scope.allChoose) {
            for (var i = 0; i < $scope.pageData.length; i++) {
                $scope.pageData[i].choose = true;
            }
        } else {
            for (var i = 0; i < $scope.pageData.length; i++) {
                $scope.pageData[i].choose = false;
            }
        }
    }

    $scope.envChoose = {
        prod: false,
        back: false,
        gray: false,
        daily: false
    };

    $scope.chooseAllResult = function () {
        $scope.resultChoose = {
            all: true,
            success: false,
            failed: false
        };
    }

    $scope.chooseResult = function (code) {
        var success = false;
        var failed = false;
        if (code == 0) {
            success = true;
        }
        if (code == 1) {
            failed = true;
        }
        $scope.resultChoose = {
            all: false,
            success: success,
            failed: failed
        };
    }

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

    $scope.chooseEnv = function (envCode) {

        for (var i = 0; i < $scope.pageData.length; i++) {
            //var item = $scope.pageData[i];
            if ($scope.pageData[i].envType == envCode) {
                switch (envCode) {
                    case 4:
                        $scope.pageData[i].choose = $scope.envChoose.prod;
                        break;
                    case 6:
                        $scope.pageData[i].choose = $scope.envChoose.back;
                        break;
                    case 3:
                        $scope.pageData[i].choose = $scope.envChoose.gray;
                        break;
                    case 2:
                        $scope.pageData[i].choose = $scope.envChoose.daily;
                        break;
                    default:
                        return;
                }
            }

        }
    }

    $scope.doQueryServers = function () {
        var url = "/box/server/query?"
            + "serverGroupId=" + ($scope.nowServerGroup.selected == null ? -1 : $scope.nowServerGroup.selected.id)
            + "&envType=-1"
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
                //$scope.refreshServerInfo();
                init();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQueryServers();


    $scope.doScript = function () {

        if ($scope.nowTaskScript.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定Script!";
            return;
        } else {
            $scope.alert.type = '';
        }

        // 选中的服务器组
        var serverList = [];
        for (var i = 0; i < $scope.pageData.length; i++) {
            var item = $scope.pageData[i];
            if (item.choose == true) {
                serverList.push(item);
            }
        }

        if (serverList.length == 0) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定服务器!";
            return;
        } else {
            $scope.alert.type = '';
        }

        $scope.btnDoScript = true;

        var url = "/task/cmd/doScript";

        var requestBody = {
            hostGroup: $scope.hostGroup,
            taskScriptId: $scope.nowTaskScript.selected.id,
            params: $scope.params,
            serverList: serverList
        }
        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.taskVO.ansibleTaskDO = body.body;
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
        var url = "/task/script/page?"
            + "scriptName=" + queryParam
            + "&sysScript=-1"
            + "&page=0"
            + "&length=10";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.taskScriptList = body.data;
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

app.controller('serverTaskOldCtrl', function ($scope, $state, $uibModal, $localStorage, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.userType = staticModel.userType;

    $scope.envType = staticModel.envType;

    //登录类型
    $scope.logType = staticModel.logType;

    //服务器类型
    $scope.serverType = staticModel.serverType;

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

    $scope.queryName = "";
    $scope.queryIp = "";
    $scope.nowType = 0;
    $scope.nowEnv = -1;
    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.queryIp = "";
        $scope.nowType = 0;
        $scope.nowEnv = -1;
        $scope.nowServerGroup = {};
        $scope.serverGroupList = [];
    }

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    $scope.doQuery = function () {
        var url = "/server/page?"
            + "serverGroupId=" + ($scope.nowServerGroup.selected == null ? -1 : $scope.nowServerGroup.selected.id)
            + "&serverName=" + $scope.queryName
            + "&useType=" + $scope.nowType
            + "&envType=" + $scope.nowEnv
            + "&queryIp=" + $scope.queryIp
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

    ///////////////////////////////////////////////////////////


    /**
     * 初始化服务器
     * @param serverId
     */
    $scope.initializationSystem = function (item) {
        var url = "/task/servertask/initializationSystem?serverId=" + item.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "执行成功", data.body);
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    //////////////////////////////////////////////////////////////////////////

    /**
     * 更新表格行列
     * @param idx
     * @param item
     */
    var rowsUpdate = function (idx, item, serverList) {
        if ($scope.nowDetailIdx == -1) {
            $scope.nowDetailIdx = idx + 1;
        } else if ($scope.nowDetailIdx == (idx + 1)) {
            return;
        } else {
            $scope.pageData.splice($scope.nowDetailIdx, 1);

            if ($scope.nowDetailIdx <= idx) {
                $scope.nowDetailIdx = idx == 0 ? 1 : idx;
            } else {
                $scope.nowDetailIdx = idx == 0 ? 1 : (idx + 1);
            }
        }

        var detail = {
            detail: true,
            serverGroup: item,
            serverList: serverList
        }
        $scope.pageData.splice($scope.nowDetailIdx, 0, detail);
    }

    $scope.nowDetailIdx = -1;

    $scope.lookDetail = function (idx, item) {
        rowsUpdate(idx, item);

        initServer();
        login(item.id);
    }

    //////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////// ws管道 /////////////////////////////////////
    var server = null;

    var login = function (body) {
        var subContext = {
            id: $localStorage.settings.user.token,
            token: $localStorage.settings.user.token,
            requestType: "taskChain",
            body: body
        };

        server.send(JSON.stringify(subContext));
    }

    /**
     * 初始化server ws
     */
    var initServer = function () {
        if (server != null) {
            return;
        }

        server = httpService.wsInstance();

        server.onMessage(function (data) {
            //回调数据渲染前端
        });

        server.onError(function (data) {
            toaster.pop("error", "WS异常连接关闭");
            server = null;
        });

        server.onClose(function (data) {
            toaster.pop("warning", "WS连接关闭");
            server = null;
        });
    }
    ///////////////////////////////////// ws管道 /////////////////////////////////////
});

app.controller('scriptInstanceCtrl', function ($scope, $uibModalInstance, $uibModal, $sce, staticModel, toaster, httpService, sysScriptType, scriptType, isView, scriptItem) {
    //$scope.localAuthPoint = authPoint;
    $scope.sysScriptType = sysScriptType;
    $scope.scriptType = scriptType;
    $scope.isView = isView;
    $scope.scriptItem = scriptItem;


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