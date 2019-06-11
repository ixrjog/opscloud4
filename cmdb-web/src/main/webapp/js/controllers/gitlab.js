'use strict';

app.controller('gitlabVersionCtrl', function ($scope, $state, $uibModal, $parse, $sce, $interval, $localStorage, httpService) {
    $scope.version = {};


    var version = function () {
        var url = "/gitlab/version";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.version = data.body;
            }
        });
    }
    version();
});


app.controller('projectCtrl', function ($scope, $state, $uibModal, $parse, $sce, $interval, $localStorage, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.queryName = "";

    $scope.queryUsername = "";
    // 执行命令后的返回
    //$scope.taskResult = {};
    // 查询任务的结果详情
    //$scope.taskVO = {};

    // 按钮状态
    //$scope.btnDoCmd = false;

    $scope.sysScriptType = staticModel.defSelect;

    $scope.scriptType = staticModel.scriptType;

    $scope.btnUpdateing = false;


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

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;
    $scope.queryScriptName = "";
    $scope.nowSysScript = -1;

    $scope.reSet = function () {
        $scope.queryScriptName = "";
        $scope.nowSysScript = -1;
    }

    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    $scope.doQuery = function () {
        var url = "/gitlab/project/page?"
            + "name=" + $scope.queryName
            + "&username=" + $scope.queryUsername
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

    $scope.updateProjects = function () {
        var url = "/gitlab/project/update";
        $scope.btnUpdateing = true;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.doQuery();
                $scope.btnUpdateing = false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.btnUpdateing = false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.btnUpdateing = false;
        });
    }

    /////////////////////////////////////////////////

    $scope.addScript = function () {
        var scriptItem = {
            id: 0,
            scriptName: "",
            content: "",
            userId: 0,
            username: "",
            script: "",
            scriptType: 0,
            sysScript: 0
        }

        $scope.editScript(scriptItem);
    }

    $scope.viewScript = function (scriptItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'scriptInfoModal',
            controller: 'scriptInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                sysScriptType: function () {
                    return $scope.sysScriptType;
                },
                scriptType: function () {
                    return $scope.scriptType;
                },
                isView: function () {
                    return true;
                },
                scriptItem: function () {
                    return scriptItem;
                }
            }
        });
    }


    $scope.editScript = function (scriptItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'scriptInfoModal',
            controller: 'scriptInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                sysScriptType: function () {
                    return $scope.sysScriptType;
                },
                scriptType: function () {
                    return $scope.scriptType;
                },
                isView: function () {
                    return false;
                },
                scriptItem: function () {
                    return scriptItem;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }


});


app.controller('webHooksCtrl', function ($scope, $state, $uibModal, $parse, $sce, $interval, $localStorage, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;
    $scope.triggerBuildType = staticModel.triggerBuildType;

    $scope.queryProjectName = "";
    $scope.queryRef = "";
    $scope.queryTriggerBuild = -1;

    $scope.reSet = function () {
        $scope.queryProjectName = "";
        $scope.queryRef = "";
        $scope.queryTriggerBuild = -1;
    }

    $scope.btnUpdateing = false;

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

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    $scope.doQuery = function () {
        var url = "/gitlab/webHooks/page?"
            + "projectName=" + $scope.queryProjectName
            + "&ref=" + $scope.queryRef
            + "&triggerBuild=" + $scope.queryTriggerBuild
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

    $scope.updateProjects = function () {
        var url = "/gitlab/project/update";
        $scope.btnUpdateing = true;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.doQuery();
                $scope.btnUpdateing = false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.btnUpdateing = false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.btnUpdateing = false;
        });
    }

    /////////////////////////////////////////////////

    $scope.addScript = function () {
        var scriptItem = {
            id: 0,
            scriptName: "",
            content: "",
            userId: 0,
            username: "",
            script: "",
            scriptType: 0,
            sysScript: 0
        }

        $scope.editScript(scriptItem);
    }

    $scope.viewScript = function (scriptItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'scriptInfoModal',
            controller: 'scriptInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                sysScriptType: function () {
                    return $scope.sysScriptType;
                },
                scriptType: function () {
                    return $scope.scriptType;
                },
                isView: function () {
                    return true;
                },
                scriptItem: function () {
                    return scriptItem;
                }
            }
        });
    }


    $scope.editScript = function (scriptItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'scriptInfoModal',
            controller: 'scriptInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                sysScriptType: function () {
                    return $scope.sysScriptType;
                },
                scriptType: function () {
                    return $scope.scriptType;
                },
                isView: function () {
                    return false;
                },
                scriptItem: function () {
                    return scriptItem;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }


});