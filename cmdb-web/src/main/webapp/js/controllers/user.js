'use strict';

app.controller('userCtrl', function($scope, $uibModal, toaster, httpService) {

    $scope.roleList = [];
    $scope.username = "";
    $scope.role = {};

    $scope.queryRole = function(roleName) {
        var url = "/role/query?"
            + "resourceId=" + 0
            + "&roleName=" + roleName
            + "&page=" + 0
            + "&length=" + 10;

        httpService.doGet(url).then(function(data) {
            var body = data.body;

            $scope.roleList = body.data;
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    /////////////////////////////////////////////////

    $scope.resetToken = function() {
        var url = "/user/resetToken";

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
    
    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function() {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    $scope.reSet = function() {
        $scope.username = "";
        $scope.role = {};
    }

    $scope.doQuery = function() {
        var url = "/user/role?"
            + "roleId=" + ($scope.role.selected == null ? 0 : $scope.role.selected.id)
            + "&username=" + $scope.username
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

    $scope.doQuery();


    $scope.addItem = function() {
        var body = {
            username : "",
            roleId : ""
        }

        doSaveItem(body);
    }

    var doSaveItem = function(item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'user',
            controller: 'userInstanceCtrl',
            size : 'sm',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                user : function() {
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

    $scope.deleteItem = function(item) {
        var url = "/user/role/unbind";

        var body = {
            roleId : item.roleDO.id,
            username : item.username
        }

        httpService.doPostWithForm(url, body).then(function(data) {
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

app.controller('userInstanceCtrl', function ($scope, $uibModalInstance, httpService, user) {
    $scope.user = user;

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

    $scope.roleList = [];
    $scope.role = {};

    $scope.queryRole = function(roleName) {
        var url = "/role/query?"
            + "resourceId=" + 0
            + "&roleName=" + roleName
            + "&page=" + 0
            + "&length=" + 10;

        httpService.doGet(url).then(function(data) {
            var body = data.body;

            $scope.roleList = body.data;
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    ////////////////////////////////////////////////////////

    $scope.saveData = function() {
        if($scope.user.username == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "用户名称不能为空!";

            return;
        }

        if($scope.role.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "角色不能为空!";

            return;
        }
        $scope.user.roleId = $scope.role.selected.id;

        var url = "/user/role/bind";
        httpService.doPostWithForm(url, $scope.user).then(function(data) {
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