/**
 * Created by liangjian on 2017/8/18.
 */

// 用户
app.controller('scmgroupsCtrl', function ($scope, $state, $uibModal, toaster, httpService, staticModel) {


    $scope.envType = staticModel.groupEnvType;

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 20;
    $scope.queryGroupName = "";
    $scope.queryScmProject = "";


    $scope.reSet = function () {
        $scope.queryGroupName = "";
        $scope.queryScmProject = "";
    }


    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    $scope.doQuery = function () {
        var url = "/scm/permissions/page?"
            + "groupName=" + $scope.queryGroupName
            + "&scmProject=" + $scope.queryScmProject
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
        var url = "/scm/permissions/refresh";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "SCM数据更新成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    $scope.delCigroup = function (item) {
        var url = "/ci/usergroup/del?id=" + item.id;

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

    $scope.editScmgroup = function (item) {
        var scmgroupItem = {
            id: item.id,
            ciUserGroupId: item.ciUserGroupId,
            scmProject: item.scmProject,
            groupName: item.groupName,
            scmDescription: item.scmDescription
        }
        saveItem(scmgroupItem);
    }

    var saveItem = function (scmgroupItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'scmgroupsInfo',
            controller: 'scmgroupsInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                scmgroupItem: function () {
                    return scmgroupItem;
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

app.controller('scmgroupsInstanceCtrl', function ($scope, $uibModalInstance, httpService, staticModel, scmgroupItem) {

    //  $scope.groupEnvType = staticModel.groupEnvType;
    $scope.scmgroupItem = scmgroupItem;
    $scope.nowCiUserGroup = {};
    $scope.ciUserGroupList = [];


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
        if ($scope.scmgroupItem.ciUserGroupDO != null) {
            $scope.nowCiUserGroup = {
                selected: $scope.scmgroupItem.ciUserGroupDO
            }
        } else {
            $scope.nowCiUserGroup = {};
        }

    }

    initEnv();

    /**
     * 保存scmgroup信息
     */
    $scope.saveScmgroupItem = function () {
        var url = "/scm/permissions/save";

        if ($scope.nowCiUserGroup == null || $scope.nowCiUserGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定持续集成权限组";
            return;
        } else {
            $scope.scmgroupItem.ciUserGroupDO = $scope.nowCiUserGroup.selected;
        }

        httpService.doPostWithJSON(url, $scope.scmgroupItem).then(function (data) {
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

    /**
     * 查询
     * @param queryParam
     */
    $scope.queryCiUserGroup = function (queryParam) {
        if (queryParam == '') {
            queryParam = $scope.scmgroupItem.scmProject;
        }

        var url = "/ci/usergroup/page?"
            + "groupName=" + queryParam
            + "&envType=-1"
            + "&page=0"
            + "&length=10";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.ciUserGroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


});