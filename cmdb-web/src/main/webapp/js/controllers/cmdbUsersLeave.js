/**
 * Created by liangjian on 2017/8/21.
 */


app.controller('usersLeaveCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {

    $scope.userType = staticModel.userType;

    $scope.queryName = "";

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
        var url = "/usersLeave/page?"
            + "username=" + $scope.queryName
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

    // 解除用户的组绑定
    $scope.delLdapGroup = function (username) {
        var url = "/cmdb/ldapGroup/remove?username=" + username;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "解除绑定成功!", data.body);
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    // 解除用户绑定
    $scope.delLdap = function (username) {
        var url = "/cmdb/ldap/remove?username=" + username;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "解除绑定成功!", data.body);
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.delZabbix = function (username) {
        var url = "/cmdb/zabbix/remove?username=" + username;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "解除绑定成功!", data.body);
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    // 关闭邮箱
    $scope.closeMail = function (username) {
        var url = "/cmdb/mailLdap/close?username=" + username;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "关闭成功!", data.body);
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    $scope.delUserLeave = function (id) {
        var url = "/usersLeave/del?id=" + id;
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

});