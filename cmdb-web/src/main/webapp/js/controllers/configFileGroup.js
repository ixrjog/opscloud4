'use strict';

app.controller('configFileGroupCtrl', function($scope, $state, $uibModal, toaster, httpService) {

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 15;

    $scope.pageChanged = function() {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    $scope.groupName = "";

    $scope.doQuery = function() {
        var url = "/config/fileGroup/query?"
            + "groupName=" + $scope.groupName
            + "&page=" + ($scope.currentPage <= 0 ? 0 : ($scope.currentPage - 1))
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

    $scope.doQuery();

    $scope.addItem = function() {
        var body = {
            id : 0,
            groupName : "",
            groupDesc : ""
        }

        doSaveItem(body);
    }

    $scope.editItem = function(item) {
        var body = {
            id : item.id,
            groupName : item.groupName,
            groupDesc : item.groupDesc
        }

        doSaveItem(body);
    }

    var doSaveItem = function(item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'configFileGroup',
            controller: 'configFileGroupInstanceCtrl',
            size : 'md',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                item : function() {
                    return item;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function() {
            $scope.doQuery();
        });
    }

    $scope.delItem = function(id) {
        var url = "/config/fileGroup/del?id=" + id;
        httpService.doDelete(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "删除成功!");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }
});


app.controller('configFileGroupInstanceCtrl', function ($scope, $uibModalInstance, httpService, item) {
    $scope.item = item;

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
        var url = "/config/fileGroup/save";

        if($scope.item.groupName == null || $scope.item.groupName == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定文件组名";
            return;
        }

        httpService.doPostWithJSON(url, $scope.item).then(function(data) {
            if(data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";

                $scope.item = {};
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