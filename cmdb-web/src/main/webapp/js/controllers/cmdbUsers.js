/**
 * Created by liangjian on 2017/6/21.
 */

// 用户
app.controller('usersCtrl', function ($scope, $state, $uibModal, toaster, httpService) {
    $scope.username = "";

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function () {
        init();
    };

    /////////////////////////////////////////////////

    var init = function () {
        var url = "/cmdb/users?"
            + "username=" + $scope.username
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

    init();

    $scope.doQuery = function () {
        init();
    }

    /////////////////////////////////////////////////////////////

    $scope.createGlobalFile = function () {
        var url = "/box/user/group/global/create";

        httpService.doPost(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "创建成功!");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.createAllUserGroupConfigFile = function () {
        var url = "/box/user/group/createAll";

        httpService.doPost(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "创建成功!");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    /////////////////////////////////////////////////////////////


    // 解除用户的组绑定
    $scope.userLeave = function (username) {
        var url = "/cmdb/user/leave?username=" + username;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "执行成功!", data.body);
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    ///////////////////////////////////////////////////////////

    $scope.serverList = function (username) {
        var modalInstance = $uibModal.open({
            templateUrl: 'keyBoxServerModal',
            controller: 'keyBoxServerInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                username: function () {
                    return username;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }

    ///////////////////////////////////////////////////////////

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

    var saveItem = function (userItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'userInfo',
            controller: 'userInstanceCtrl',
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
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }

    ///////////////////////////////////////////////////////
    /**
     * 编辑用户权限
     * @param user
     */
    $scope.userLdapInfo = function (userItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'userLdapInfo',
            controller: 'userLdapInstanceCtrl',
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
    }

    ///////////////////////////////////////////////////////

    // 填充用户手机号
    $scope.addUsersMobile = function () {
        var url = "/cmdb/users/addUsersMobile" ;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "执行成功!", data.body);
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.addUserMobile = function (id) {
        var url = "/cmdb/user/addUsersMobile?userId=" + id ;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "执行成功!", data.body);
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


});


app.controller('userLdapInstanceCtrl', function ($scope, $uibModalInstance, toaster, httpService, userItem) {
    $scope.userItem = userItem;

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.dsSize = 0;


    /////////////////////////////////////////////////
    //$scope.userChanged = function () {
    //    doQuery();
    //};

    $scope.doQuery = function () {
        var url = "/cmdb/user?"
            + "username=" + $scope.userItem.username;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.userItem = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        });
    }
    /////////////////////////////////////////////////////////////

    $scope.auth = function (username) {
        var url = "/box/auth/add?username=" + username;

        httpService.doPost(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "执行成功!", data.body);
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        });
        
    }

    $scope.unauth = function (username) {
        var url = "/box/auth/del?username=" + username;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "执行成功!", data.body);
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
        
    }

    // 解除用户的组绑定
    $scope.unbindGroup = function (username) {
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
    $scope.unbind = function (username) {
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

    // 激活邮箱
    $scope.activeMail = function (username) {
        var url = "/cmdb/mailLdap/active?username=" + username;

        httpService.doGet(url).then(function (data) {
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

    // 加入Ldap用户组
    $scope.addUserGroup = function (username, groupname) {
        var url = "/cmdb/ldapGroup/add?username=" + username
            + "&groupname=" + groupname;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "添加成功!", data.body);
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        });
    }

    // 移除Ldap用户组
    $scope.delUserGroup = function (username, groupname) {
        var url = "/cmdb/ldapGroup/del?username=" + username
            + "&groupname=" + groupname;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "移除成功!", data.body);
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        });
        
    }


    ///////////////////////////////////////////////////////////

});

// 用户-服务器组管理
app.controller('keyBoxServerInstanceCtrl', function ($scope, $uibModalInstance, httpService, username) {

    $scope.username = username ;
    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    $scope.queryServerGroup = function(queryParam) {
        var url = "/servergroup/query/page?page=0&length=10&name=" + queryParam + "&useType=0";

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.serverGroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

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

    //////////////////////////////////////////////////////

    $scope.addItem = function() {
        if($scope.nowServerGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择服务器组才能添加!";
        } else {
            $scope.alert.type = '';
        }

        var url = "/box/user/group/save";

        var requestBody = {
            username : username,
            serverGroupId : $scope.nowServerGroup.selected.id
        }
        httpService.doPostWithJSON(url, requestBody).then(function(data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功!";
                init();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    $scope.delItem = function(groupId) {
        var url = "/box/user/group/del?"
            + "groupId=" + groupId
            + "&username=" + username;

        httpService.doDelete(url).then(function(data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "删除成功!";
                init();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function(currentPage) {
        $scope.currentPage = currentPage;
        init();
    };

    /////////////////////////////////////////////////

    var init = function() {
        var url = "/box/user/group?"
            + "page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;

        var queryItem = {
            username : username
        }
        httpService.doPostWithJSON(url, queryItem).then(function(data) {
            if (data.success) {
                var body = data.body;
                $scope.pageData = body.data;
                $scope.totalItems = body.size;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    init();

    $scope.createFile = function() {
        var url = "/box/user/group/create?username=" + username;

        httpService.doPost(url).then(function(data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "创建成功!";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }
});

// 新增用户
app.controller('userInstanceCtrl', function ($scope, $uibModalInstance, httpService, userItem) {

    $scope.userItem = userItem;

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
    $scope.saveUser = function () {
        var url = "/box/user/save";

        httpService.doPostWithJSON(url, $scope.userItem).then(function (data) {
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