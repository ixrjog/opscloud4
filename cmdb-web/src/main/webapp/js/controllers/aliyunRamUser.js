'use strict';

app.controller('aliyunRamUserCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.queryUsername = "";
    $scope.queryUserTag = "";
    $scope.butUpdateUsers = false;
    $scope.butImportPolicy = false;

    $scope.reSet = function () {
        $scope.queryUsername = "";
    }

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;


    $scope.queryUser = function () {
        var url = "/aliyun/ram/user/page?username=" + ($scope.queryUsername == null ? "" : $scope.queryUsername)
            + "&userTag=" + $scope.queryUserTag
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

    $scope.setAllows = function (id) {
        var url = "/aliyun/ram/policy/set?id=" + id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "设置成功！");
                $scope.queryPolicy();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.updateUsers = function () {
        $scope.butUpdateUsers = true;
        var url = "/aliyun/ram/user/update";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "同步成功！");
                $scope.queryUser();
                $scope.butUpdateUsers = false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.butUpdateUsers = false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.butUpdateUsers = false;
        });
    }

    /**
     * 导入策略
     */
    $scope.importPolicy = function (id) {
        $scope.butImportPolicy = true;
        var url = "/aliyun/ram/user/import?id=" + id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "导入成功！");
                $scope.queryUser();
                $scope.butImportPolicy = false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.butImportPolicy = false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.butImportPolicy = false;
        });
    }

    $scope.queryUser();

    $scope.pageChanged = function () {
        $scope.queryUser();
    }

    $scope.editRamUserInfo = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'editRamUserInfo',
            controller: 'editRamUserInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                ramUser: function () {
                    return item;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.queryUser();
        }, function () {
            $scope.queryUser();
        });
    }


});


// 更新个人信息
app.controller('editRamUserInstanceCtrl', function ($scope, $uibModalInstance, httpService, ramUser) {

    $scope.ramUser = ramUser;


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
     * 保存user信息
     */
    $scope.saveRamUser = function () {
        var url = "/aliyun/ram/user/save";

        httpService.doPostWithJSON(url, $scope.ramUser).then(function (data) {
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
