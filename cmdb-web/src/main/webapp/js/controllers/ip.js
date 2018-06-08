'use strict';

app.controller('ipCtrl', function($scope, $state, $uibModal, toaster, httpService) {
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

    $scope.useType = [
        {
            code : 0,
            name : "已使用"
        },
        {
            code : 1,
            name : "未使用"
        }
    ];

    $scope.nowIpType = -1;
    $scope.nowUseType = -1;

    $scope.ipGroupList = [];
    $scope.nowIPGroup = {};

    $scope.queryIPGroup = function(queryParam) {
        var url = "/ipgroup/query?page=0&length=10";
        var body = {
            ipNetwork : queryParam,
            ipType : -1
        }

        httpService.doPostWithJSON(url, body).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.ipGroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    $scope.ip = "";

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function() {
        $scope.doQuery($scope.currentPage - 1);
    };

    /////////////////////////////////////////////////

    $scope.doQuery = function(page) {
        if(page == undefined || page == null) {
            page = 0;
        }
        var url = "/ip/query?" + "page=" + page + "&length=" + $scope.pageLength;

        var body = {
            ip : $scope.ip,
            ipNetworkId : $scope.nowIPGroup.selected == null ? 0 : $scope.nowIPGroup.selected.id,
            ipType : $scope.nowIpType,
            useType : $scope.nowUseType
        }

        httpService.doPostWithJSON(url, body).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    $scope.reSet = function() {
        $scope.ip = "";
        $scope.nowIPGroup = {};
        $scope.nowIpType = -1;
        $scope.nowUseType = -1;
    }

    $scope.doQuery(0);

    //////////////////////////////////////////////////////////

    /**
     * 新增ip信息
     */
    $scope.addIP = function() {
        var ip = {
            id : 0,
            ipNetwork : null,
            ip : "",
            content : "",
            serverId : 0,
            ipType : -1
        }

        saveItem(ip);
    }

    /**
     * 编辑指定的ip信息
     * @param item
     */
    $scope.editIP = function(item) {
        var ip = {
            id : item.id,
            ipNetwork : item.ipNetworkDO,
            ip : item.ip,
            content : item.content,
            serverId : item.serverId,
            ipType : item.ipType
        }

        saveItem(ip);
    }

    var saveItem = function(ip) {
        var modalInstance = $uibModal.open({
            templateUrl: 'ipModal',
            controller: 'ipInstanceCtrl',
            size : 'sm',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                ipType : function() {
                    return $scope.ipType;
                },
                ip : function() {
                    return ip;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery(0);
        }, function() {
            $scope.doQuery(0);
        });
    }

    /**
     * 删除指定的ip信息
     * @param id
     */
    $scope.delIP = function(id) {
        var url = "/ip/del?ipId=" + id;
        httpService.doPost(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "删除成功!");
                $scope.doQuery(0);
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("warning", err);
        });
    }
});


app.controller('ipInstanceCtrl', function ($scope, $uibModalInstance, httpService, ipType, ip) {
    $scope.ipType = ipType;
    $scope.ip = ip;

    $scope.alert = null;

    $scope.closeAlert = function() {
        $scope.alert = null;
    }

    ////////////////////////////////////////////////////////

    $scope.ipGroupList = [];
    $scope.nowIPGroup = {};
    $scope.nowIPGroup.selected = ip.ipNetwork;

    $scope.queryIPGroup = function(queryParam) {
        var url = "/ipgroup/query?page=0&length=10";
        var body = {
            ipNetwork : queryParam,
            ipType : -1
        }

        httpService.doPostWithJSON(url, body).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.ipGroupList = body.data;

            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryIPGroup($scope.ip.ipNetwork == null ? "" : $scope.ip.ipNetwork.ipNetwork);
    if($scope.ip.ipNetwork != null) {
        $scope.nowIPGroup.selected = $scope.ip.ipNetwork;
    }

    ////////////////////////////////////////////////////////

    $scope.saveData = function() {
        var body = {
            id : $scope.ip.id,
            ipNetworkId : $scope.nowIPGroup.selected == null ? 0 : $scope.nowIPGroup.selected.id,
            ip : $scope.ip.ip,
            content : $scope.ip.content,
            serverId : $scope.ip.serverId,
            ipType : $scope.ip.ipType
        }

        var alertInfo = {
            type : "",
            msg : ""
        }
        $scope.alert = alertInfo;
        var url = "/ip/save";
        httpService.doPostWithJSON(url, body).then(function(data) {
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