'use strict';

app.controller('logcleanupCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.envType = staticModel.envType;

    $scope.enabledType = staticModel.enabled;

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
    $scope.nowEnabled = -1;
    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.nowEnabled = -1;
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
        var url = "/task/logcleanup/page?"
            + "serverGroupId=" + ($scope.nowServerGroup.selected == null ? -1 : $scope.nowServerGroup.selected.id)
            + "&serverName=" + $scope.queryName
            + "&enabled=" + ($scope.nowEnabled == null ? -1 : $scope.nowEnabled)
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
                $scope.refreshViewHistory();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQuery();

    $scope.refreshViewHistory = function () {
        for (var i = 0; i < $scope.pageData.length; i++) {
            var item = $scope.pageData[i];
            // "<b style='color: red'>I can</b> have <div class='label label-success'>HTML "

            item.viewHistory = Math.round(item.history);
        }
    }


    ///////////////////////////////////////////////////////////

    /**
     * 更新采集数据
     * @param item
     */
    $scope.update = function (item) {
        var url = "/task/logcleanup/update?serverId=" + item.serverId;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "更新采集数据成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    /**
     * 同步数据
     * @param item
     */
    $scope.sync = function () {
        var url = "/task/logcleanup/refresh"
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "同步成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }


    /**
     * 增加history
     * @param item
     */
    $scope.refreshDiskRate = function (item) {
        var url = "/task/logcleanup/refreshDiskRate?"
            + "serverId=" + item.serverId;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "设置成功");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    ///////////////////////////////////////////////////////////

    $scope.edit = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'logcleanupInfo',
            controller: 'logcleanupInfoCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                logcleanup: function () {
                    return item;
                }
            }
        });
        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }

    $scope.task = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'logcleanupTask',
            controller: 'logcleanupTaskCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                logcleanup: function () {
                    return item;
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

/**
 * 配置
 */
app.controller('logcleanupInfoCtrl', function ($scope, $uibModalInstance, staticModel, httpService, logcleanup) {
    $scope.userType = staticModel.userType;
    $scope.envType = staticModel.envType;
    $scope.serverType = staticModel.serverType;
    $scope.item = logcleanup;
    $scope.nowScript = {};
    $scope.scriptList = [];


    var queryLogcleanupStriptList = function () {
        var url = "/task/script/page?scriptName=logcleanup&sysScript=1&page=0&length=15";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.scriptList = data.body.data;
                setScript();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    var setScript = function () {
        if ($scope.item.scriptId != 0) {
            for (var i = 0; i < $scope.scriptList.length; i++) {
                if ($scope.scriptList[i].id == $scope.item.scriptId) {
                    $scope.nowScript.selected = $scope.scriptList[i];
                    break;
                }
            }
        } else {
            $scope.nowScript.selected = $scope.scriptList[0];
        }

    }

    var init = function () {
        queryLogcleanupStriptList();
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

    /**
     * 修改配置
     * @param item
     */
    $scope.saveItem = function (item) {
        if (item.enabled) {
            if ($scope.nowScript.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "必须清理脚本";
                return;
            } else {
                item.scriptId = $scope.nowScript.selected.id; // 设置脚本id
            }
        }
        var url = "/task/logcleanup/save";

        httpService.doPostWithJSON(url, item).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";

                $scope.nowServerGroup = {};
                $scope.nowServer = {};
                $scope.propertyGroup = {};
                $scope.groupProperties = [];
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }


});

/**
 * 任务详情
 */
app.controller('logcleanupTaskCtrl', function ($scope, $uibModalInstance, staticModel, httpService, logcleanup) {

    $scope.item = logcleanup;


    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 5;


    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    $scope.doQuery = function () {
        var url = "/task/logcleanup/log/page?"
            + "id=" +  $scope.item.id
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

    /**
     * 执行清理任务
     */
    $scope.doTask = function () {
        var url = "/task/logcleanup/doTask?id=" +  $scope.item.id ;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.doQuery();
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


});