/**
 * Created by liangjian on 2017/7/11.
 */
'use strict';

app.controller('dnsmasqOfficeCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.dnsmasqItemType = staticModel.dnsmasqItemType;


    $scope.nowDnsmasqItemType = -1;
    $scope.queryItemValue = "";


    $scope.reSet = function () {
        $scope.nowDnsmasqItemType = -1;
        $scope.queryItemValue = "";
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
            + "dnsGroupId=1"
            + "&itemType=" + $scope.nowDnsmasqItemType
            + "&queryItemValue=" + $scope.queryItemValue
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

    ///////////////////////////////////////////////////////////
    
    $scope.delDnsmasq = function (id) {
        var url = "/dns/dnsmasq/del?"
            + "id=" +id;
        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.doQuery();
                // var body = data.body;
                // $scope.totalItems = body.size;
                // $scope.pageData = body.data;
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
            dnsGroupId: 1,
            itemType: -1,
            item: "",
            itemValue: "",
            content: ""
        }
        saveItem(dnsmasqItem);
    }
    
    $scope.editDnsmasq = function (item) {
        var dnsmasqItem = {
            id: item.id,
            dnsGroupId: item.dnsGroupId,
            itemType: item.itemType,
            item: item.item,
            itemValue: item.itemValue,
            content: item.content
        }
        saveItem(dnsmasqItem);
    }

    var saveItem = function (dnsmasqItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'dnsmasqInfo',
            controller: 'dnsmasqInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                dnsmasqItem: function () {
                    return dnsmasqItem;
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

app.controller('dnsmasqAliyunCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.dnsmasqItemType = staticModel.dnsmasqItemType;


    $scope.nowDnsmasqItemType = -1;
    $scope.queryItemValue = "";


    $scope.reSet = function () {
        $scope.nowDnsmasqItemType = -1;
        $scope.queryItemValue = "";
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
            + "dnsGroupId=2"
            + "&itemType=" + $scope.nowDnsmasqItemType
            + "&queryItemValue=" + $scope.queryItemValue
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

    ///////////////////////////////////////////////////////////

    $scope.delDnsmasq = function (id) {
        var url = "/dns/dnsmasq/del?"
            + "id=" +id;
        httpService.doDelete(url).then(function (data) {
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
    
    ///////////////////////////////////////////////////////////

    $scope.addDnsmasq = function () {
        var dnsmasqItem = {
            id: 0,
            dnsGroupId: 2,
            itemType: -1,
            item: "",
            itemValue: "",
            content: ""
        }
        saveItem(dnsmasqItem);
    }

    $scope.editDnsmasq = function (item) {
        var dnsmasqItem = {
            id: item.id,
            dnsGroupId: item.dnsGroupId,
            itemType: item.itemType,
            item: item.item,
            itemValue: item.itemValue,
            content: item.content
        }
        saveItem(dnsmasqItem);
    }

    var saveItem = function (dnsmasqItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'dnsmasqInfo',
            controller: 'dnsmasqInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                dnsmasqItem: function () {
                    return dnsmasqItem;
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

app.controller('dnsmasqInstanceCtrl', function ($scope, $uibModalInstance, staticModel, httpService, dnsmasqItem) {
    $scope.dnsmasqItemType = staticModel.dnsmasqItemType;

    $scope.dnsmasqItem = dnsmasqItem;

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
     * 保存server item信息
     */
    $scope.saveDnsmasqItem = function () {
        var url = "/dns/dnsmasq/save";

        httpService.doPostWithJSON(url, $scope.dnsmasqItem).then(function (data) {
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

    //////////////////////////////////////////////////////////

   

});