'use strict';

app.controller('systemCtrl', function($scope, $uibModal, $state, toaster, httpService) {

    $scope.goBack = function() {
        $state.go('app.dashboard');
    }

    $scope.systemName = "";

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 8;

    $scope.pageChanged = function() {
        $scope.querySystem();
    };

    /////////////////////////////////////////////////

    /**
     * 查询系统
     */
    $scope.querySystem = function() {
        var url = "/system/query?"
            + "systemName=" + $scope.systemName
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;

        httpService.doGet(url).then(function(data) {
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

    $scope.querySystem();

    /**
     * 新增系统
     */
    $scope.addSystem = function() {
        var system = {
            id : 0,
            systemName : "",
            systemUrl : "",
            imgUrl : "",
            systemDesc : "",
            owner : ""
        }

        var modalInstance = $uibModal.open({
            templateUrl: 'systemModal',
            controller: 'systemInstanceCtrl',
            size : 'md',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                system : function() {
                    return system;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.querySystem();
        }, function() {
            $scope.querySystem();
        });
    }

    /**
     * 更改系统
     */
    $scope.editSystem = function(item) {
        var system = {
            id : item.id,
            systemName : item.systemName,
            systemUrl : item.systemUrl,
            imgUrl : item.imgUrl,
            systemDesc : item.systemDesc,
            owner : item.owner
        }

        var modalInstance = $uibModal.open({
            templateUrl: 'systemModal',
            controller: 'systemInstanceCtrl',
            size : 'md',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                system : function() {
                    return system;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.querySystem();
        }, function() {
            $scope.querySystem();
        });
    }

    /**
     * 删除指定的系统
     * @param systemId
     */
    $scope.delSystem = function(systemId) {
        var url = "/system/del?systemId=" + systemId;

        httpService.doDelete(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "删除成功!");
                $scope.querySystem();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }
});

app.controller('systemInstanceCtrl', function ($scope, $uibModalInstance, httpService, system) {
    $scope.system = system;

    $scope.alert = {
        type : "",
        msg : ""
    };

    $scope.closeAlert = function() {
        $scope.alert = {
            type : "",
            msg : ""
        };
    }


    $scope.saveData = function() {
        var url = "/system/save";

        if($scope.system.systemName == null || $scope.system.systemName == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定系统名称";
            return;
        }

        if($scope.system.systemUrl == null || $scope.system.systemUrl == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定系统URL";
            return;
        }

        if($scope.system.owner == null || $scope.system.owner == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定系统负责人";
            return;
        }

        httpService.doPostWithJSON(url, $scope.system).then(function(data) {
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