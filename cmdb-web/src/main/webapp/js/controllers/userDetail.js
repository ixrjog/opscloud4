'use strict';

app.controller('userCtrl', function ($scope, $uibModal, $localStorage, $state, toaster, httpService) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.userDetail = {};

    $scope.queryName = "";

    $scope.editingSshKey = false;

    // 管理员查询用户信息
    $scope.nowUser = {};
    $scope.userList = [];

    $scope.queryUser = function (queryParam) {
        var url = "/cmdb/users?username=" + queryParam + "&page=0&length=10";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.userList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    var init = function (username) {
        var url = "/user/query?username=" + username;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.userDetail = data.body;
                $scope.userDetail.vPwd = "******************";
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    init($localStorage.settings.user.username);

    $scope.queryUserInfo = function () {
        if ($scope.nowUser.selected == null) {
            toaster.pop("warning", "查询用户名不为空!");
            return;
        }
        init($scope.nowUser.selected.username);
    }


    $scope.viewPwd = function () {
        $scope.userDetail.vPwd = $scope.userDetail.pwd;
    }


    $scope.addUser = function () {
        var userItem = {
            id: 0,
            username: "",
            pwd: "",
            mail: "",
            mobile: "",
            displayName: "",
        }

        saveItem(userItem);
    }

    $scope.editUserInfo = function () {
        var userItem = {
            username: $scope.userDetail.username,
            mobile: $scope.userDetail.mobile,
            mail: $scope.userDetail.mail,
            displayName: $scope.userDetail.displayName
        }

        var modalInstance = $uibModal.open({
            templateUrl: 'editUserInfo',
            controller: 'editUserInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                userItem: function () {
                    return userItem;
                }
            }
        });

        modalInstance.result.then(function () {
            init($localStorage.settings.user.username);
        }, function () {
            init($localStorage.settings.user.username);
        });
    }

    $scope.editSshKey = function () {
        $scope.editingSshKey = true;
    }

    $scope.saveUserInfo = function () {

        var requestBody = {
            username: $scope.userDetail.username,
            mobile: $scope.userDetail.mobile,
            pwd: $scope.userDetail.pwd,
            rsaKey: $scope.userDetail.sshKey.key
        }

        var url = "/user/save";

        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                toaster.pop("success", "更新成功!");
                init($localStorage.settings.user.username);
                $scope.editingSshKey = false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.editingSshKey = false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.editingSshKey = false;
        });
    }
});

// 更新个人信息
app.controller('editUserInstanceCtrl', function ($scope, $uibModalInstance, httpService, userItem) {

    $scope.userItem = userItem;

    $scope.newUserItem = {
        username: $scope.userItem.username,
        mobile: "",
        mai: "",
        displayName: ""
    }

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
    $scope.saveUserDetail = function () {
        var url = "/box/user/saveDetail";

        httpService.doPostWithJSON(url, $scope.newUserItem).then(function (data) {
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