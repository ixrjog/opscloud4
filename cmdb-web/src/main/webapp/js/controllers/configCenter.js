/**
 * Created by liangjian on 2017/6/1.
 */
'use strict';

app.controller('configCenterCtrl', function($scope, $state, $uibModal, toaster, httpService, staticModel) {
    $scope.envType = staticModel.serverEnv;
    $scope.itemGroupType = staticModel.configCenterItemGroup;
    
    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 15;
    
    $scope.queryItem = "";
    $scope.queryItemGroup = "";
    $scope.queryEnv = "";

    $scope.reSet = function () {
        $scope.queryItem = "";
        $scope.queryItemGroup = "";
        $scope.queryEnv = "";
    }
    
    
    $scope.pageChanged = function() {
        $scope.doQuery();
    };
    
    /////////////////////////////////////////////////

    $scope.doQuery = function() {
        var url = "/config/center/query?"
            + "item=" + $scope.queryItem
            + "&itemGroup=" + $scope.queryItemGroup
            + "&env=" + $scope.queryEnv
            + "&page=" + ($scope.currentPage <= 0 ? 0 : ($scope.currentPage - 1))
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

    /////////////////////////////////////////////////

    $scope.refreshCache = function (item) {
        var url = "/config/center/refreshCache?"
            + "itemGroup=" + item.itemGroup
            + "&env=" + item.env;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "更新成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    /////////////////////////////////////////////////
    
    /**
     * 删除信息
     * @param id
     */
    $scope.delItem = function (id) {
        var url = "/config/center/del?id=" + id;

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

    /////////////////////////////////////////////////

    $scope.addItem = function () {
        var configCenterItem = {
            id: 0,
            itemGroup: "",
            env: "",
            item: "",
            value: "",
            content: ""
        }
        saveItem(configCenterItem);
    }

    $scope.editItem = function (item) {
        var configCenterItem = {
            id: item.id,
            itemGroup: item.itemGroup,
            env: item.env,
            item: item.item,
            value: item.value,
            content: item.content
        }

        saveItem(configCenterItem);
    }

    var saveItem = function (configCenterItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'configCenterInfo',
            controller: 'configCenterInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                envType: function () {
                    return $scope.envType;
                },
                itemGroupType: function () {
                    return $scope.itemGroupType;
                },
                configCenterItem: function () {
                    return configCenterItem;
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

app.controller('configCenterInstanceCtrl', function ($scope, $uibModalInstance, httpService, envType, itemGroupType, configCenterItem) {
    $scope.envType = envType;
    $scope.itemGroupType = itemGroupType;

    $scope.configCenterItem = configCenterItem;

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
     * 重置
     */
    $scope.resetConfigCenterItem = function () {
        $scope.configCenterItem = {
            id: 0,
            itemGroup: "",
            env: "",
            item: "",
            value: "",
            content: ""
        }

    }

    /**
     * 保存configCenter item信息
     */
    $scope.saveConfigCenterItem = function () {
        var url = "/config/center/save";
        
        httpService.doPostWithJSON(url, $scope.configCenterItem).then(function (data) {
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
