/**
 * Created by liangjian on 2017/8/18.
 */

// 用户
app.controller('cigroupsCtrl', function ($scope, $state, $uibModal, toaster, httpService, staticModel) {


    $scope.envType = staticModel.groupEnvType;
    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 20;
    $scope.queryName = "";
    $scope.nowEnv = -1;

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.nowEnv = -1;
    }


    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    $scope.doQuery = function () {
        var url = "/ci/usergroup/page?"
            + "groupName=" + $scope.queryName
            + "&envType=" + $scope.nowEnv
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

    /////////////////////////////////////////////////////////////

    $scope.doRefresh = function () {
        var url = "/ci/usergroup/refresh";

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

    $scope.delCigroup = function (item) {
        var url = "/ci/usergroup/del?id="+item.id;

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

    $scope.editCigroup = function (item) {
        var cigroupItem = {
            id: item.id,
            content: item.content,
            groupName: item.groupName,
            envType: item.envType,
            serverGroupId: item.serverGroupId,
            serverGroupDO: item.serverGroupDO
        }
        saveItem(cigroupItem);
    }

    var saveItem = function (cigroupItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'cigroupsInfo',
            controller: 'cigroupsInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                cigroupItem: function () {
                    return cigroupItem;
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

app.controller('cigroupsInstanceCtrl', function ($scope, $uibModalInstance, httpService, staticModel, cigroupItem) {

    $scope.groupEnvType = staticModel.groupEnvType;

    $scope.cigroupItem = cigroupItem;

    $scope.nowServerGroup = {};
    
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
     * 初始化环境
     */
    var initEnv = function () {
        if ($scope.cigroupItem.serverGroupDO != null) {
            $scope.nowServerGroup = {
                selected: $scope.cigroupItem.serverGroupDO
            }
        } else {
            $scope.nowServerGroup = {};
        }

    }

    initEnv();
    
    /**
     * 保存server item信息
     */
    $scope.saveCigroupItem = function () {
        var url = "/ci/usergroup/save";

        $scope.cigroupItem.serverGroupDO = $scope.nowServerGroup.selected ;
        //$scope.cigroupItem.envType = $scope.groupEnvType.selected ;
   
        httpService.doPostWithJSON(url, $scope.cigroupItem).then(function (data) {
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

    //////////////////////////////////////////////////////////

    $scope.serverGroupList = [];

    $scope.queryServerGroup = function (queryParam) {
        var url = "/servergroup/query/page?page=0&length=10&name=" + queryParam + "&useType=0";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.serverGroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    //////////////////////////////////////////////////////////
    
});