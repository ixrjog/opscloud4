'use strict';

app.controller('serviceCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;
    $scope.servicePortType = staticModel.servicePortType;

    $scope.btnSyncDisabled = false;

    $scope.queryName = "";

    $scope.reSet = function () {
        $scope.queryName = "";
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
    $scope.nowNamespace = {};
    $scope.namespaceList = [];

    // 同步
    $scope.sync = function () {
        $scope.btnSyncDisabled = true;
        var url = "/kubernetes/sync";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "数据同步完成！");
                $scope.btnSyncDisabled = false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.btnSyncDisabled = false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.btnSyncDisabled = false;
        });
    }

    $scope.queryNamespace = function () {
        var url = "/kubernetes/namespace/query";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.namespaceList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryNamespace();
    $scope.nowServicePort = {};

    $scope.doQuery = function () {
        var url = "/kubernetes/service/query?"
            + "namespaceId=" + ($scope.nowNamespace.selected == null ? -1 : $scope.nowNamespace.selected.id)
            + "&name=" + $scope.queryName
            + "&portName=" + ($scope.nowServicePort.selected == null ? "" : $scope.nowServicePort.selected)
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

    $scope.delService = function (id) {
        var url = "/kubernetes/service/del?id=" + id;
        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    ///////////////////////////////////////////////////////////


    $scope.editService = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'serviceInfo',
            controller: 'serviceInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                serviceItem: function () {
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


app.controller('serviceInstanceCtrl', function ($scope, $uibModalInstance, httpService, serviceItem) {
    $scope.service = serviceItem;
    // 保存按钮
    $scope.btnSaveing = false;

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

    //////////////////////////////////////////////////////////

    $scope.serverGroupList = [];
    $scope.nowServerGroup = {};

    $scope.queryServerGroup = function (queryParam) {
        var url = "/servergroup/query/page?page=0&length=10&name=" + queryParam + "&useType=0";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.serverGroupList = body.data;
                if($scope.service.serverGroupName != "" && $scope.service.serverGroupName == queryParam){
                    setServerGroup();
                }
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    //////////////////////////////////////////////////////////

    var setServerGroup = function () {
        for (var i = 0; i < $scope.serverGroupList.length; i++) {
            var item = $scope.serverGroupList[i];
            if ($scope.service.serverGroupName == item.name) {
                $scope.nowServerGroup.selected = $scope.serverGroupList[i];
                break;
            }
        }
    }

    var init= function () {
        if($scope.service.serverGroupName != "")
            $scope.queryServerGroup($scope.service.serverGroupName);
    }

    init();

    $scope.saveService = function () {
        var url = "/kubernetes/service/save";

        if ($scope.nowServerGroup.selected != null) {
            $scope.service.serverGroupId = $scope.nowServerGroup.selected.id;
            $scope.service.serverGroupName = $scope.nowServerGroup.selected.name;
        } else {
            $scope.service.serverGroupId = 0;
            $scope.service.serverGroupName = "";
        }

        httpService.doPostWithJSON(url, $scope.service).then(function (data) {
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

    $scope.removeServerGroup = function () {
        $scope.nowServerGroup = {};
    }



});
