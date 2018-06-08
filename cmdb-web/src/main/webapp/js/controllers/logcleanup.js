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
            + "&enabled=" + $scope.nowEnabled
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
     * 清理数据
     * @param item
     */
    $scope.cleanup = function (item) {
        var url = "/task/logcleanup/cleanup?"
            + "serverId=" + item.serverId;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "清理成功", data.body);
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
     * 设置是否启用任务
     * @param item
     */
    $scope.setEnabled = function (item) {
        var url = "/task/logcleanup/setEnabled?"
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

    /**
     * 减少history
     * @param item
     */
    $scope.subtractHistory = function (item) {
        var url = "/task/logcleanup/subtractHistory?"
            + "serverId=" + item.serverId;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.doQuery();
            }
        });
    }

    /**
     * 增加history
     * @param item
     */
    $scope.addHistory = function (item) {
        var url = "/task/logcleanup/addHistory?"
            + "serverId=" + item.serverId;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "设置成功");
                $scope.doQuery();
            }
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
            controller: 'logcleanupInstanceCtrl',
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


app.controller('logcleanupInstanceCtrl', function ($scope, $uibModalInstance, staticModel, httpService, logcleanup) {
    $scope.userType = staticModel.userType;
    $scope.envType = staticModel.envType;
    $scope.serverType = staticModel.serverType;
    $scope.item = logcleanup;

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
     * 修改日志保留天数
     * @param item
     */
    $scope.saveItem = function (item) {
        var url = "/task/logcleanup/save?"
            + "id=" + item.serverId
            + "&history=" + item.history;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功!";
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