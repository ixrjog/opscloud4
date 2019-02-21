'use strict';


// Ldap组管理
app.controller('ldapGroupCtrl', function ($scope, $state, $uibModal, toaster, httpService) {
    $scope.queryName = "";

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 20;

    $scope.pageChanged = function () {
        init();
    };

    /////////////////////////////////////////////////

    $scope.doQuery = function () {
        var url = "/ldap/group/query?"
            + "cn=" + $scope.queryName
            + "&groupType=-1"
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

    /**
     * 删除server 信息
     * @param serverId
     */
    $scope.delLdapGroup = function (id) {
        var url = "/ldap/group/del?id=" + id;

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

    /////////////////////////////////////////////////////////////

    $scope.createGroup = function () {
        var ldapGroup = {
            id: 0,
            cn: "",
            content: "",
            groupType: 0
        }

        var modalInstance = $uibModal.open({
            templateUrl: 'ldapGroupModal',
            controller: 'ldapGroupInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                ldapGroup: function () {
                    return ldapGroup;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }


    /////////////////////////////////////////////////////////////
    $scope.userList = function (ldapGroup) {
        var modalInstance = $uibModal.open({
            templateUrl: 'ldapGroupUsersModal',
            controller: 'ldapGroupUsersInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                ldapGroup: function () {
                    return ldapGroup;
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

// 新建Ldap组
app.controller('ldapGroupInstanceCtrl', function ($scope, $uibModalInstance, httpService, ldapGroup) {
    $scope.nowUser = {};
    $scope.ldapGroup = ldapGroup;

    $scope.createGroup = function () {

        if ($scope.ldapGroup.cn == null || $scope.ldapGroup.cn == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定Ldap组名称";
            return;
        }

        var url = "/ldap/group/create?"
            + "cn=" + $scope.ldapGroup.cn
            + "&content=" + $scope.ldapGroup.content
            + "&groupType=" + $scope.ldapGroup.groupType;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type="success";
                $scope.alert.msg="新建Ldap组成功!"
            } else {
                $scope.alert.type="warning";
                $scope.alert.msg=data.msg;
            }
        }, function (err) {
            $scope.alert.type="error";
            $scope.alert.msg=err;
        });
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

});


// 服务器组-用户管理
app.controller('ldapGroupUsersInstanceCtrl', function ($scope, $uibModalInstance, httpService, ldapGroup) {
    $scope.nowUser = {};
    $scope.ldapGroup = ldapGroup;

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


    var getLdapGroup = function () {
        var url = "/ldap/group/get?cn=" + $scope.ldapGroup.cn;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.ldapGroup = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    //////////////////////////////////////////////////////

    $scope.addItem = function () {
        if ($scope.nowUser.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择用户才能添加!";
        } else {
            $scope.alert.type = '';
        }

        var url = "/ldap/group/addUser?groupname=" + $scope.ldapGroup.cn + "&username=" +$scope.nowUser.selected.username;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "添加成功!";
                getLdapGroup();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    $scope.delItem = function (username) {
        var url = "/ldap/group/delUser?"
            + "groupname=" + $scope.ldapGroup.cn
            + "&username=" + username;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "删除成功!";
                getLdapGroup();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    /////////////////////////////////////////////////




});





