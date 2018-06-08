/**
 * Created by liangjian on 2017/8/21.
 */
app.controller('ciusersCtrl', function ($scope, $state, $uibModal, toaster, httpService) {
    $scope.username = "";

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 5;

    $scope.pageChanged = function () {
        init();
    };

    /////////////////////////////////////////////////

    var init = function () {
        var url = "/cmdb/ci/users?"
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

    $scope.doRefresh = function () {
        var url = "/cmdb/ci/users/refresh";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "数据更新成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    $scope.ciUsergroupList = function(user) {
        var modalInstance = $uibModal.open({
            templateUrl: 'ciusersModal',
            controller: 'ciusersInstanceCtrl',
            size : 'lg',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                user : function() {
                    return user;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function() {
            $scope.doQuery();
        });
    }

});


app.controller('ciusersInstanceCtrl', function ($scope, $uibModalInstance, httpService,toaster, user) {
    $scope.user = user ;
    $scope.nowCigroup = {};
    $scope.cigroupList = [];

    $scope.pageData = $scope.user.ciUsers;
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

    $scope.doQuery = function () {
        var url = "/cmdb/ci/user?"
            + "username=" + user.username;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
               // var body = data.body;
               // $scope.totalItems = body.size;
                $scope.user = data.body;
                $scope.pageData = $scope.user.ciUsers;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }
    
    $scope.queryCiGroup = function(queryParam) {
        var url = "/ci/usergroup/page?page=0&length=10&envType=-1&groupName=" + queryParam ;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.cigroupList = body.data;
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
        if($scope.nowCigroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择权限组才能添加!";
        } else {
            $scope.alert.type = '';
        }

        var url = "/cmdb/ci/users/addGroup?"
            + "userId=" + $scope.user.id
            + "&usergroupId=" + $scope.nowCigroup.selected.id;

        httpService.doGet(url).then(function(data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功!";
                $scope.doQuery();
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
    
    $scope.delCiuser = function(ciuserId) {
        var url = "/cmdb/ci/users/delGroup?"
            + "ciuserId=" + ciuserId;
        
        httpService.doDelete(url).then(function(data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "删除成功!";
                $scope.doQuery();
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
    
    
});