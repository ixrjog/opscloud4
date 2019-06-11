'use strict';

app.controller('clusterCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;
    $scope.envType = staticModel.envType;


    $scope.btnSyncClusterLabel =false;

    $scope.reSet = function () {
        $scope.queryName = "";
    }

    /////////////////////////////////////////////////

    $scope.pageData = [];

    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    $scope.syncClusterLabel = function (item) {
        $scope.btnSyncClusterLabel =true;
        var url = "/kubernetes/cluster/syncLabel?clusterName=" + item.name;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "集群标签同步完成!");
                $scope.btnSyncClusterLabel =false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.btnSyncClusterLabel =false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.btnSyncClusterLabel =false;
        });
    }

    $scope.doQuery = function () {
        var url = "/kubernetes/cluster/query";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.pageData = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQuery();

    var saveCluster = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'clusterInfo',
            controller: 'clusterInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                envType: function () {
                    return $scope.envType;
                },
                clusterItem: function () {
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

    $scope.editCluster = function (item) {
        saveCluster(item);
    }

    $scope.addCluster = function () {

    }

    $scope.addCluster = function () {
        var item = {
            id:0,
            name:"",
            descriptionL:"",
            env:-1,
            kubeconfigFile:"",
            masterUrl:"",
            namespace:"",
            serverGroupId:0,
            serverGroupName:""
        }
        saveCluster(item);
    }


});


app.controller('clusterInstanceCtrl', function ($scope, $uibModalInstance, httpService, envType, clusterItem) {
    $scope.envType = envType;
    $scope.cluster = clusterItem;
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
                if ($scope.cluster.serverGroupName != "" && $scope.cluster.serverGroupName == queryParam) {
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
            if ($scope.cluster.serverGroupName == item.name) {
                $scope.nowServerGroup.selected = $scope.serverGroupList[i];
                break;
            }
        }
    }

    var init = function () {
        if ($scope.cluster.serverGroupName != "")
            $scope.queryServerGroup($scope.cluster.serverGroupName);
    }

    init();

    $scope.saveCluster = function () {
        var url = "/kubernetes/cluster/save";

        if ($scope.nowServerGroup.selected != null) {
            $scope.cluster.serverGroupId = $scope.nowServerGroup.selected.id;
            $scope.cluster.serverGroupName = $scope.nowServerGroup.selected.name;
        } else {
            $scope.cluster.serverGroupId = 0;
            $scope.cluster.serverGroupName = "";
        }

        httpService.doPostWithJSON(url, $scope.cluster).then(function (data) {
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
