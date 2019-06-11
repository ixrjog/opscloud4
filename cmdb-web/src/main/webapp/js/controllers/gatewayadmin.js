'use strict';

app.controller('gatewayadminCtrl', function ($scope, $state, $uibModal, $timeout, staticModel, httpService, toaster) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.queryGroupName = "";
    $scope.queryAppName = "";

    $scope.butSync = false;
    $scope.butBatchSet = false;

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.reSet = function () {
        $scope.queryGroupName = "";
        $scope.queryAppName = "";
        $scope.doQuery();
    }

    $scope.pageChanged = function () {
        $scope.doQuery();
    };


    $scope.doQuery = function () {
        var url = "/gatewayadmin/page?"
            + "serverGroupName=" + $scope.queryGroupName + "&"
            + "appName=" + $scope.queryAppName + "&"
            + "page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1) + "&"
            + "length=" + $scope.pageLength;

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

    ////////////////////////////////////////////////////////


    $scope.sync = function () {
        $scope.butSync = true;
        var url = "/gatewayadmin/sync";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "扫描配置完成！");
                $scope.butSync = false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.butSync = false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.butSync = false;
        });
    }

    $scope.batchSet = function () {
        $scope.butBatchSet = true;
        var url = "/gatewayadmin/batchSet";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "批量配置完成！");
                $scope.butBatchSet = false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.butBatchSet = false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.butBatchSet = false;
        });
    }

    $scope.preview = function (id, envType) {
        var url = "/gatewayadmin/preview?"
            + "id=" + id + "&"
            + "envType=" + envType;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.previewAppSet(data.body);
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.previewAppSet = function (appSetItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'appSetInfoModal',
            controller: 'appSetInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                appSetItem: function () {
                    return appSetItem;
                }
            }
        });
    }


    $scope.appServerList = function (id, envType) {
        var url = "/gatewayadmin/appServerList?"
            + "id=" + id + "&"
            + "envType=" + envType;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.listAppServer(data.body);
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    $scope.listAppServer = function (appServerListItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'appServerListInfoModal',
            controller: 'appServerListInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                appServerListItem: function () {
                    return appServerListItem;
                }
            }
        });
    }


    $scope.appSet = function (id, envType) {
        var url = "/gatewayadmin/appSet?"
            + "id=" + id + "&"
            + "envType=" + envType;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                if (data.body) {
                    toaster.pop("success", "配置成功！");
                } else {
                    toaster.pop("warning", "配置错误！");
                }
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.appServerSet = function (id, envType) {
        var url = "/gatewayadmin/appServerSet?"
            + "id=" + id + "&"
            + "envType=" + envType;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                if (data.body) {
                    toaster.pop("success", "配置成功！");
                } else {
                    toaster.pop("warning", "配置错误！");
                }
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    ///////////////////////////////////////////////////////

    /**
     * 删除
     * @param item
     */
    $scope.delGatewayadmin = function (id) {
        var url = "/gatewayadmin/del?id=" + id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }



});


app.controller('appSetInstanceCtrl', function ($scope, $uibModalInstance, $uibModal, $sce, staticModel, toaster, httpService, appSetItem) {

    $scope.appSetItem = appSetItem;

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

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }

});


app.controller('appServerListInstanceCtrl', function ($scope, $uibModalInstance, $uibModal, $sce, staticModel, toaster, httpService, appServerListItem) {

    $scope.appServerListItem = appServerListItem;

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

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }


});