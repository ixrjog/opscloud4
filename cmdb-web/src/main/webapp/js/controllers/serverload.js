'use strict';

app.controller('serverloadCtrl', function ($scope, $state, $uibModal, $sce, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.userType = staticModel.userType;

    $scope.envType = staticModel.envType;

    //登录类型
    $scope.logType = staticModel.logType;

    //服务器类型
    $scope.serverType = staticModel.serverType;

    //监控状态
    $scope.zabbixStatus = staticModel.zabbixStatus;

    //主机监控
    $scope.zabbixMonitor = staticModel.zabbixMonitor;


    $scope.task = {};


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
    $scope.pageLength = 20;

    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    $scope.doQuery = function () {
        var url = "/statistics/serverperf/page?"
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

                $scope.refreshDiskInfo();
                // 统计信息
                $scope.serverPerfStatistics();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    $scope.taskGet = function () {
        var url = "/statistics/serverperf/task/get" ;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                // var body = data.body;

                $scope.task = data.body;
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.taskGet();

    $scope.taskReset = function () {
        var url = "/statistics/serverperf/task/reset" ;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "复位成功!");
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.refreshDiskInfo = function () {
        for (var i = 0; i < $scope.pageData.length; i++) {
            var item = $scope.pageData[i];
            // "<b style='color: red'>I can</b> have <div class='label label-success'>HTML "

            item.diskSysInfo = $sce.trustAsHtml(
                "流量Bps:  " + item.diskSysBps + "<br/>"
                + "IOPS:   " + item.diskSysIops + "<br/>"
                + "读响应(ms):    " + item.diskSysReadMs + "<br/>"
                + "写响应(ms):    " + item.diskSysWriteMs
            );
            item.diskDataInfo = $sce.trustAsHtml(
                "流量Bps:  " + item.diskDataBps + "<br/>"
                + "IOPS:   " + item.diskDataIops + "<br/>"
                + "读响应(ms):    " + item.diskDataReadMs + "<br/>"
                + "写响应(ms):    " + item.diskDataWriteMs
            );
        }
    }

    //$scope.doQuery();


    $scope.serverPerfStatistics = function () {
        var url = "/statistics/serverperf/statistics";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.serverPerfData = body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    ///////////////////////////////////////////////////////////

});