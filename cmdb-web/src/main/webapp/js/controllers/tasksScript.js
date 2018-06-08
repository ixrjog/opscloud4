'use strict';

app.controller('taskScriptCtrl', function ($scope, $state, $uibModal, $parse, $sce, $interval, $localStorage, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;
    // 执行命令后的返回
    //$scope.taskResult = {};
    // 查询任务的结果详情
    //$scope.taskVO = {};

    // 按钮状态
    //$scope.btnDoCmd = false;

    $scope.sysScriptType = staticModel.defSelect;

    $scope.scriptType = staticModel.scriptType;


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
        var url = "/task/script/page?"
            + "scriptName=" + $scope.queryScriptName
            + "&sysScript=" + ($scope.nowSysScript == null ? -1 : $scope.nowSysScript)
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

    /////////////////////////////////////////////////

    $scope.addScript = function () {
        var scriptItem = {
            id:0,
            scriptName:"",
            content:"",
            userId:0,
            username:"",
            script:"",
            scriptType:0,
            sysScript:0
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


