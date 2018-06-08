'use strict';

app.controller('ipGroupCtrl', function($scope, $state, $uibModal, toaster, httpService) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.ipType = [
        {
            code : 0,
            name : "公网"
        },
        {
            code : 1,
            name : "内网"
        }
    ];

    $scope.nowIpType = -1;

    $scope.queryServerGroup = function(queryParam) {
        var url = "/servergroup/query/page?page=0&length=10&name=" + queryParam + "&useType=0";

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.serverGroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }


    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    $scope.ipNetwork = "";

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function() {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    var queryIPGroup = function() {
        var url = "/ipgroup/query?" + "page=" + $scope.currentPage + "&length=" + $scope.pageLength;

        var queryBody = {
            ipNetwork : $scope.ipNetwork,
            serverGroupId : $scope.nowServerGroup.selected == null ? 0 : $scope.nowServerGroup.selected.id,
            ipType : $scope.nowIpType
        }
        httpService.doPostWithJSON(url, queryBody).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.pageData = body.data;
                $scope.totalItems = body.size;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    queryIPGroup();

    $scope.doQuery = function() {
        queryIPGroup();
    }

    $scope.reSet = function() {
        $scope.nowIpType = -1;
        $scope.ipNetwork = "";
        $scope.nowServerGroup = {};
    }

    $scope.addIPGroup = function() {
        var ipGroup = {
            id : 0,
            serverGroupId : 0,
            ipNetwork : "",
            gateWay : "",
            dnsOne : "",
            dnsTwo : "",
            ipType : -1,
            content : "",
            produceMark : ""
        }

        saveItem(ipGroup);
    }

    /**
     * 编辑ip组
     * @param item
     */
    $scope.editIPGroup = function(item) {
        var ipGroup = {
            id : item.id,
            serverGroupId : item.serverGroupId,
            ipNetwork : item.ipNetwork,
            gateWay : item.gateWay,
            dnsOne : item.dnsOne,
            dnsTwo : item.dnsTwo,
            ipType : item.ipType,
            content : item.content,
            produceMark : item.produceMark
        }

        saveItem(ipGroup);
    }

    var saveItem = function(ipGroup) {
        var modalInstance = $uibModal.open({
            templateUrl: 'ipGroup',
            controller: 'ipGroupInstanceCtrl',
            size : 'sm',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                ipType : function() {
                    return $scope.ipType;
                },
                ipGroup : function() {
                    return ipGroup;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function() {
            $scope.doQuery();
        });
    }

    /**
     * 删除ip组
     * @param ipGroupId
     */
    $scope.delIPGroup = function(ipGroupId) {
        var url = "/ipgroup/del?ipGroupId=" + ipGroupId;
        httpService.doPost(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "删除成功！");
                queryIPGroup();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    /**
     * 基于指定ip组,生成一批ip
     * @param ipGroupId
     */
    $scope.createIPs = function(ipGroupId) {
        var url = "/ipgroup/create?ipGroupId=" + ipGroupId;

        httpService.doPost(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "生成成功！生成IP数:" + data.body);
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("warning", data.msg);
        });
    }
});


app.controller('ipGroupInstanceCtrl', function ($scope, $uibModalInstance, httpService, ipType, ipGroup) {
    $scope.ipGroup = ipGroup;
    $scope.ipType = ipType;

    $scope.alert = null;

    $scope.closeAlert = function() {
        $scope.alert = null;
    }

    $scope.saveData = function() {
        var alertInfo = {
            type : "",
            msg : ""
        }
        $scope.alert = alertInfo;
        var url = "/ipgroup/save";
        httpService.doPostWithJSON(url, $scope.ipGroup).then(function(data) {
            if(data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    $scope.closeModal = function() {
        $uibModalInstance.dismiss('cancel');
    }
});