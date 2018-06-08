'use strict';

app.controller('javaLogServiceManageCtrl', function ($scope, $state, $uibModal, $timeout, staticModel, httpService, toaster) {

    $scope.userType = staticModel.userType;

    $scope.nowType = 0;

    $scope.queryName = "";

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
        if ($scope.nowType == null) {
            $scope.nowType = 0;
        }

        var url = "/logService/servergroup/query?"
            + "name=" + $scope.queryName + "&"
            + "isUsername=false" + "&"
            + "useType=" + $scope.nowType + "&"
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

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.nowType = 0;
        $scope.doQuery();
    }

    ///////////////////////////////////////////////////////


    /**
     * 保存
     * @param item
     */
    $scope.editMachineGroup = function (logServiceServerGroupCfgItem) {

        var modalInstance = $uibModal.open({
            templateUrl: 'machineGroupInfo',
            controller: 'machineGroupInfoInstanceCtrl',
            size: 'lg',
            resolve: {
                logServiceServerGroupCfgItem: function () {
                    return logServiceServerGroupCfgItem;
                }
            }
        });

        // modalInstance.result.then(function (serverGroupItem) {
        //     var url = "/servergroup/update";
        //     httpService.doPostWithJSON(url, serverGroupItem).then(function (data) {
        //         if (data.success) {
        //             toaster.pop("success", "更新成功！");
        //             $scope.doQuery();
        //         } else {
        //             toaster.pop("warning", data.msg);
        //         }
        //     }, function (err) {
        //         toaster.pop("error", err);
        //     });
        // });
    }


    ///////////////////////////////////////////////////////////////////
});

app.controller('machineGroupInfoInstanceCtrl', function ($scope, $uibModalInstance, httpService, toaster, logServiceServerGroupCfgItem) {
    $scope.logServiceServerGroupCfgItem = logServiceServerGroupCfgItem;
    $scope.projectList = [];
    $scope.nowProject = {};
    $scope.logstoreList = [];
    $scope.nowLogstore = {};
    $scope.machineGroup = {};


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

    var init = function () {
        var item = $scope.logServiceServerGroupCfgItem;
        if (item.project != null && item.project != '') {
            $scope.nowProject.selected = item.project;
        }

        if (item.logstore != null && item.logstore != '') {
            $scope.nowLogstore.selected = item.logstore;
        }
        $scope.queryProject();
        $scope.queryLogstore();
        $scope.queryMachineGroup();
    }

    $scope.queryMachineGroup = function () {
        var item = $scope.logServiceServerGroupCfgItem;
        if (item.project == null || item.project == '') return;

        var url = "/logService/machineGroup/query?" +
            "project=" + item.project +
            "&groupName=" + item.serverGroupDO.name;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.machineGroup = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        });
    }

    $scope.queryProject = function () {
        var url = "/logService/project/query";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.projectList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        });
    }

    $scope.queryLogstore = function () {

        var url = "/logService/logstore/query?"
            + "project=" + ($scope.nowProject.selected != null ? $scope.nowProject.selected : $scope.logServiceServerGroupCfgItem.project );

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.logstoreList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        });
    }


    $scope.saveCfg = function () {

        if ($scope.nowProject.selected == null) {
            toaster.pop("warning", "未选择project");
            return;
        }

        if ($scope.nowLogstore.selected == null) {
            toaster.pop("warning", "未选择logstore");
            return;
        }


        var cfgItem = $scope.logServiceServerGroupCfgItem;
        cfgItem.project = $scope.nowProject.selected;
        cfgItem.logstore = $scope.nowLogstore.selected;

        var url = "/logService/serverGroupCfg/save";


        httpService.doPostWithJSON(url, cfgItem).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功!";
                init();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        });
    }


    init();

    // $scope.saveEdit = function () {
    //     $uibModalInstance.close($scope.showItem);
    // };
});

