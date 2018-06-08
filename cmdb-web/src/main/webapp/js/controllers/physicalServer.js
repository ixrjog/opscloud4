/**
 * Created by liangjian on 2017/8/29.
 */

app.controller('physicalServerCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.serverUseType = staticModel.serverUseType;

    $scope.psStatistics = function () {
        var url = "/server/psStatistics";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.vmData = body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.psStatistics();

    $scope.queryName = "";
    $scope.useType = -1;

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.useType = -1;
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
        var url = "/server/psPage?"
            + "&serverName=" + $scope.queryName
            + "&useType=" + $scope.useType
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


    ///////////////////////////////////////////////////////
    /**
     * Esxi物理机的虚拟服务器列表
     * @param esxiHost
     */
    $scope.vmServerList = function(esxiHost) {
        var modalInstance = $uibModal.open({
            templateUrl: 'vmServerList',
            controller: 'vmServerListInstanceCtrl',
            size : 'lg',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                esxiHost : function() {
                    return esxiHost;
                }
            }
        });
    }

    ///////////////////////////////////////////////////////
    /**
     * Esxi物理机的数据存储列表
     * @param esxiHost
     */
    $scope.vmDatastoresList = function(esxiHost) {
        var modalInstance = $uibModal.open({
            templateUrl: 'vmDatastoresList',
            controller: 'vmDatastoresListInstanceCtrl',
            size : 'lg',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                esxiHost : function() {
                    return esxiHost;
                }
            }
        });
    }

});

app.controller('vmServerListInstanceCtrl', function ($scope, $uibModalInstance, httpService, esxiHost) {
    $scope.esxiHost = esxiHost;

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.vmSize = 0;

    // $scope.pageChanged = function(currentPage) {
    //     $scope.currentPage = currentPage;
    //     $scope.doQuery();
    // };

    /////////////////////////////////////////////////

    $scope.doQuery = function () {
        var url = "/server/ps/esxiVms?serverName=" + $scope.esxiHost.vmName;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.vmSize = body.size;
                $scope.pageData = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQuery();

});

app.controller('vmDatastoresListInstanceCtrl', function ($scope, $uibModalInstance, httpService, esxiHost) {
    $scope.esxiHost = esxiHost;

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.dsSize = 0;



    /////////////////////////////////////////////////

    $scope.doQuery = function () {
        var url = "/server/ps/esxiDatastores?serverName=" + $scope.esxiHost.vmName;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.dsSize = body.size;
                $scope.pageData = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQuery();

});