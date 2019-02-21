/**
 * Created by liangjian on 2017/7/11.
 */
'use strict';

app.controller('dnsmasqCtrl', function ($scope, $state, $uibModal, httpService, toaster) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.queryDnsItem = "";

    $scope.reSet = function () {
        $scope.queryDnsItem = "";
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
        var url = "/dns/dnsmasq/page?"
            + "dnsItem=" + $scope.queryDnsItem
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

    $scope.buildDnsmasq = function () {
        var url = "/dns/dnsmasq/build";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "保存成功!");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    ///////////////////////////////////////////////////////////

    $scope.delDnsmasq = function (id) {
        var url = "/dns/dnsmasq/del?"
            + "id=" + id;
        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    ///////////////////////////////////////////////////////////

    $scope.addDnsmasq = function () {
        var dnsmasqItem = {
            id: 0,
            dnsItem: "",
            content: ""
        }
        saveItem(dnsmasqItem);
    }

    $scope.editDnsmasq = function (item) {
        saveItem(item);
    }

    var saveItem = function (dnsItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'dnsmasqInfo',
            controller: 'dnsmasqInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                dnsItem: function () {
                    return dnsItem;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }


    $scope.viewDnsmasq = function () {
        var modalInstance = $uibModal.open({
            templateUrl: 'dnsmasqConfInfoModal',
            controller: 'dnsmasqConfInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                }
            }
        });
    }

});

app.controller('dnsmasqInstanceCtrl', function ($scope, $uibModalInstance, httpService, dnsItem) {

    $scope.dnsItem = dnsItem;

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

    $scope.saveItem = function () {
        var url = "/dns/dnsmasq/save";

        httpService.doPostWithJSON(url, $scope.dnsItem).then(function (data) {
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

app.controller('dnsmasqConfInstanceCtrl', function ($scope, $uibModalInstance, $uibModal, $sce, toaster, httpService) {

    $scope.dnsmasqConf = "";


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

    $scope.getDnsmasqConf = function () {
        var url = "/dns/dnsmasq/getConf";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.dnsmasqConf  = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.getDnsmasqConf();

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }


});

