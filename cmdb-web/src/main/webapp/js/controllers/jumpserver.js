'use strict';

// Jumpserver管理
app.controller('jumpserverCtrl', function ($scope, $state, $uibModal, toaster, httpService) {

    $scope.btnSyncAssets = false;
    $scope.btnSyncUsers = false;

    $scope.jumpserver = {};

    $scope.systemuserList = [];
    $scope.nowSystemuser = {};

    $scope.adminuserList = [];
    $scope.nowAdminuser = {};


    $scope.nowUser = {};
    $scope.userList = [];

    $scope.queryUser = function (queryParam) {
        var url = "/users?username=" + queryParam + "&page=0&length=10";
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


    // 授权用户
    $scope.authAdmin = function () {
        if ($scope.nowUser == null || $scope.nowUser.selected == null)
            return;
        var url = "/jumpserver/authAdmin?userId=" + $scope.nowUser.selected.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "授权完成！");
                getJumpserver();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.getJumpserver = function () {
        var url = "/jumpserver/get"

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.jumpserver = data.body;
                if ($scope.jumpserver == null) return;
                if ($scope.jumpserver.assetsAdminuserDO != null) {
                    $scope.nowAdminuser.selected = $scope.jumpserver.assetsAdminuserDO;
                }
                if ($scope.jumpserver.assetsSystemuserDO != null) {
                    $scope.nowSystemuser.selected = $scope.jumpserver.assetsSystemuserDO;
                }
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.getJumpserver();

    $scope.querySystemuser = function (queryName) {
        var url = "/jumpserver/assetsSystemuser/query?name=" + (queryName == null ? "" : queryName);

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.systemuserList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryAdminuser = function (queryName) {
        var url = "/jumpserver/assetsAdminuser/query?name=" + (queryName == null ? "" : queryName);

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.adminuserList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.saveAdminuser = function () {
        if ($scope.nowAdminuser.selected == null) return;
        var url = "/jumpserver/assetsAdminuser/save?id=" + $scope.nowAdminuser.selected.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "保存管理账户成功！");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.saveSystemuser = function () {
        if ($scope.nowSystemuser.selected == null) return;
        var url = "/jumpserver/assetsSystemuser/save?id=" + $scope.nowSystemuser.selected.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "保存系统账户成功！");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.syncAssets = function () {
        var url = "/jumpserver/assets/sync";
        $scope.btnSyncAssets = true;


        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "资产同步成功！");
                $scope.btnSyncAssets = false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.btnSyncAssets = false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.btnSyncAssets = false;
        });
    }

    $scope.syncUsers = function () {
        var url = "/jumpserver/users/sync";
        $scope.btnSyncUsers = true;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "用户同步成功！");
                $scope.btnSyncUsers = false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.btnSyncUsers = false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.btnSyncUsers = false;
        });
    }

});

app.controller('jumpserverCheckCtrl', function ($scope, $state, $uibModal, toaster, httpService) {

    $scope.btnCheckAssets = false;
    $scope.btnCheckUsers = false;

    $scope.jumpserver = {};



    $scope.getJumpserver = function () {
        var url = "/jumpserver/get"

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.jumpserver = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.getJumpserver();

    $scope.checkUsers = function () {
        $scope.btnCheckUsers = true;
        var url = "/jumpserver/users/check";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "用户数据校验完成");
                $scope.getJumpserver();
                $scope.btnCheckUsers = false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.btnCheckUsers = false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.btnCheckUsers = false;
        });
    }

    $scope.checkAssets = function () {
        $scope.btnCheckAssets = true;
        var url = "/jumpserver/assets/check";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "资产数据校验完成");
                $scope.getJumpserver();
                $scope.btnCheckAssets = false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.btnCheckAssets = false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.btnCheckAssets = false;
        });
    }

    $scope.delUsers = function (id) {
        var url = "/jumpserver/users/del?id=" + id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除成功!");
                $scope.getJumpserver();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    $scope.delAssets = function (id) {
        var url = "/jumpserver/assets/del?id=" + id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除成功!");
                $scope.getJumpserver();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }




});



